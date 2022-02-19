package com.fatih.chatapplicationjava.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.fatih.chatapplicationjava.Model.Contacts;
import com.fatih.chatapplicationjava.Model.User;
import com.fatih.chatapplicationjava.R;
import com.fatih.chatapplicationjava.View.MessageActivity;
import com.fatih.chatapplicationjava.databinding.RecyclerRowBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    ArrayList<User> userArrayList=new ArrayList<>();
    ArrayList<Contacts> phoneArrayList=new ArrayList<>();
    Context context;
    private int viewGroup;


    public UserAdapter(ArrayList<User> userArrayList,Context context) {
        this.userArrayList = userArrayList;
        this.context=context;
        this.viewGroup=1;
    }


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerRowBinding binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new UserHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
                    FirebaseFirestore.getInstance().collection("Chat").document( FirebaseAuth.getInstance().getUid()+userArrayList.get(position).uid).collection("Messages").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error!=null){

                            }else{
                                if(value!=null&& !value.isEmpty()){
                                    DocumentSnapshot documentSnapshot=value.getDocuments().get(0);
                                    String message=documentSnapshot.getString("message");
                                    ((UserHolder) holder).binding.message.setText(message);
                                }
                            }
                        }
                    });

                    String username=userArrayList.get(position).userName.substring(0, 1).toUpperCase()+userArrayList.get(position).userName.substring(1);
                    ((UserHolder) holder).binding.userName.setText(username);
                    ((UserHolder) holder).binding.message.setText(userArrayList.get(position).lastMessage);
                    if(userArrayList.get(position).photo!=null&&!userArrayList.get(position).photo.isEmpty())
                        Picasso.get().load(userArrayList.get(position).photo).placeholder(R.drawable.avatar).into(((UserHolder) holder).binding.profileImage);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, MessageActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("image", userArrayList.get(position).photo);
                            intent.putExtra("uid", userArrayList.get(position).uid);
                            holder.itemView.getContext().startActivity(intent);
                        }
                    });


    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder{
        private RecyclerRowBinding binding;
        public UserHolder(@NonNull RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
