package com.fatih.chatapplicationjava.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fatih.chatapplicationjava.Adapter.MessageAdapter;
import com.fatih.chatapplicationjava.Model.Message;
import com.fatih.chatapplicationjava.R;
import com.fatih.chatapplicationjava.databinding.ActivityMessageBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class MessageActivity extends AppCompatActivity {
        private ActivityMessageBinding binding;
        private FirebaseFirestore firestore;
        private FirebaseAuth auth;
        private FirebaseUser user;
        private static final String ONESIGNAL_APP_ID="f5d5daca-43db-4c68-8ebb-6b3a1d730e73";
        private ArrayList<Message> messageArrayList;
        private MessageAdapter messageAdapter;
        private String recieverUid,recieverRoom,senderRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMessageBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageArrayList=new ArrayList<>();
        messageAdapter=new MessageAdapter(messageArrayList, this);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        Objects.requireNonNull(getSupportActionBar()).hide();
        Intent intent=getIntent();
        binding.userName.setText(intent.getStringExtra("username"));
        Picasso.get().load(intent.getStringExtra("image")).placeholder(R.drawable.avatar).into(binding.profileImage);
        recieverUid=intent.getStringExtra("uid");
        System.out.println(recieverUid);
        recieverRoom=recieverUid+user.getUid();
        senderRoom=user.getUid()+recieverUid;
        getMessages();
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

    }
    public void sent(View view){
        if(!binding.editText.getText().toString().equals("")) {
           String message= binding.editText.getText().toString();
            String uid=user.getUid();
            Long longDate= Timestamp.now().toDate().getTime();
            UUID uuid= UUID.randomUUID();
            Message senderMessage=new Message(message, uid, longDate);

           firestore.collection("Chat").document(senderRoom).collection("Messages").document(uuid.toString()).set(senderMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void unused) {
                   firestore.collection("Chat").document(recieverRoom).collection("Messages").document(uuid.toString()).set(senderMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {

                           binding.editText.setText("");
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(MessageActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(MessageActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
               }
           });

        }
    }
    public void back(View view){
        Intent intent=new Intent(MessageActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void getMessages(){
        firestore.collection("Chat").document(senderRoom).collection("Messages").orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Toast.makeText(MessageActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        messageArrayList.clear();
                        if(value!=null&&!value.isEmpty()){
                            for(DocumentSnapshot snapshot:value.getDocuments()){
                                Message message=snapshot.toObject(Message.class);
                                messageArrayList.add(message);
                                binding.recyclerView.setAdapter(messageAdapter);
                            }
                        }
                    }
            }
        });
    }
}