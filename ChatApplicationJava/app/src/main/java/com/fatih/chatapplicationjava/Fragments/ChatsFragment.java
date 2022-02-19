package com.fatih.chatapplicationjava.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fatih.chatapplicationjava.Adapter.UserAdapter;
import com.fatih.chatapplicationjava.Model.User;
import com.fatih.chatapplicationjava.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class ChatsFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private ArrayList<User> userArrayList=new ArrayList<>();
    private UserAdapter userAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Chat");
        auth=FirebaseAuth.getInstance();
        RecyclerView recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        firestore.collection("User").whereNotEqualTo("uid",auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                userArrayList.clear();
                if(error!=null){
                    Toast.makeText(requireContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    if(value!=null&& !value.isEmpty()){
                        for(DocumentSnapshot documentSnapshot:value.getDocuments()){
                            User user=new User();
                            user=documentSnapshot.toObject(User.class);
                            if(user!=null){
                            userArrayList.add(user);
                            userAdapter=new UserAdapter(userArrayList,requireContext());
                            recyclerView.setAdapter(userAdapter);

                            }
                        }
                    }
                }
            }
        });

    }
}