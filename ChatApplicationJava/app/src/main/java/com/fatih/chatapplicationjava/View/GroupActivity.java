package com.fatih.chatapplicationjava.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.fatih.chatapplicationjava.Adapter.MessageAdapter;
import com.fatih.chatapplicationjava.Model.Message;
import com.fatih.chatapplicationjava.databinding.ActivityGroupBinding;
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

import java.util.ArrayList;
import java.util.UUID;

public class GroupActivity extends AppCompatActivity {
    private ActivityGroupBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageArrayList;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroupBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageArrayList=new ArrayList<>();
        messageAdapter=new MessageAdapter(messageArrayList, this);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        getUserName();
        getData();

    }
    public void sent(View view){
        if(!binding.editText.getText().toString().equals("")) {
            String message= binding.editText.getText().toString();
            String uid=user.getUid();
            Long longDate= Timestamp.now().toDate().getTime();
            UUID uuid= UUID.randomUUID();
            Message senderMessage=new Message(message, uid, longDate, userName);
            firestore.collection("Group").document(uuid.toString()).set(senderMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    binding.editText.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GroupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public  void getData(){
        firestore.collection("Group").orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Toast.makeText(GroupActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        messageArrayList.clear();
                        if(value!=null&& !value.isEmpty()){
                            for(DocumentSnapshot document:value.getDocuments()){
                                Message message=document.toObject(Message.class);
                                messageArrayList.add(message);
                                binding.recyclerView.setAdapter(messageAdapter);
                            }
                        }
                    }
            }
        });
    }
    public void back(View view){
        Intent intent=new Intent(GroupActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void getUserName(){
        firestore.collection("User").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot!=null){
                         userName= documentSnapshot.getString("userName");
                    }
            }
        });
    }
}