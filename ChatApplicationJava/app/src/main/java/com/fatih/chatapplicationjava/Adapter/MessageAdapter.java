package com.fatih.chatapplicationjava.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fatih.chatapplicationjava.Model.Message;
import com.fatih.chatapplicationjava.View.GroupActivity;
import com.fatih.chatapplicationjava.View.MessageActivity;
import com.fatih.chatapplicationjava.databinding.RecieverMessageBinding;
import com.fatih.chatapplicationjava.databinding.SenderMessageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Message> messageArrayList=new ArrayList<>();

    public MessageAdapter(ArrayList<Message> messageArrayList, Context context) {
        this.messageArrayList = messageArrayList;
        this.context = context;
    }

    Context context;
    Integer SENT_ITEM=1;
    Integer RECIEVE_ITEM=2;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENT_ITEM){
            SenderMessageBinding senderMessageBinding=SenderMessageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new SenderHolder(senderMessageBinding);
        }else{
            RecieverMessageBinding recieverMessageBinding=RecieverMessageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new RecieverHolder(recieverMessageBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(context.getClass()== MessageActivity.class){
            if(holder.getClass()==SenderHolder.class){
                ((SenderHolder) holder).binding.senderMessage.setText(messageArrayList.get(position).message);
                Date date=new Date(messageArrayList.get(position).date);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                String strDate= simpleDateFormat.format(date);
                ((SenderHolder) holder).binding.timeText2.setText(strDate);

            }else if(holder.getClass()==RecieverHolder.class) {
                ((RecieverHolder) holder).binding.userName2.setVisibility(View.GONE);
                ((RecieverHolder) holder).binding.recieverMessage.setText(messageArrayList.get(position).message);
                Date date=new Date(messageArrayList.get(position).date);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                String strDate= simpleDateFormat.format(date);
                ((RecieverHolder) holder).binding.timeText2.setText(strDate);
            }
        }
        else if(context.getClass()==GroupActivity.class){
            if(holder.getClass()==SenderHolder.class){
                ((SenderHolder) holder).binding.senderMessage.setText(messageArrayList.get(position).message);
                Date date=new Date(messageArrayList.get(position).date);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                String strDate= simpleDateFormat.format(date);
                ((SenderHolder) holder).binding.timeText2.setText(strDate);

            }else if(holder.getClass()==RecieverHolder.class) {
                ((RecieverHolder) holder).binding.userName2.setVisibility(View.VISIBLE);
                ((RecieverHolder) holder).binding.userName2.setText(messageArrayList.get(position).userName);
                ((RecieverHolder) holder).binding.recieverMessage.setText(messageArrayList.get(position).message);
                Date date=new Date(messageArrayList.get(position).date);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                String strDate= simpleDateFormat.format(date);
                ((RecieverHolder) holder).binding.timeText2.setText(strDate);
            }
        }

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public class SenderHolder extends RecyclerView.ViewHolder{
        private SenderMessageBinding binding;
        public SenderHolder(@NonNull SenderMessageBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
    public class RecieverHolder extends RecyclerView.ViewHolder{
        private RecieverMessageBinding binding;
        public RecieverHolder(@NonNull RecieverMessageBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messageArrayList.get(position).uid.equals(FirebaseAuth.getInstance().getUid())){
            return SENT_ITEM;
        }else {
            return RECIEVE_ITEM;
        }
    }
}
