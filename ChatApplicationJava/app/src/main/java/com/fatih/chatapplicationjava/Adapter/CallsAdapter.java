package com.fatih.chatapplicationjava.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fatih.chatapplicationjava.Model.Contacts;
import com.fatih.chatapplicationjava.databinding.CallRowBinding;

import java.util.ArrayList;
import java.util.List;

public class CallsAdapter extends RecyclerView.Adapter<CallsAdapter.CallsViewHolder> {
    List<Contacts> phoneList;

    public CallsAdapter(List<Contacts> phoneList) {
        this.phoneList = phoneList;
    }

    @NonNull
    @Override
    public CallsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CallRowBinding binding=CallRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CallsViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CallsViewHolder holder, int position) {
            holder.binding.userName2.setText(phoneList.get(position).getContactName());
            holder.binding.phoneNumber.setText(phoneList.get(position).getContactNumber());


    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

    public class CallsViewHolder extends RecyclerView.ViewHolder{
            private CallRowBinding binding;
        public CallsViewHolder(@NonNull CallRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Contacts> arrayList){
        phoneList.clear();
        phoneList.addAll(arrayList);
        Contacts contacts=new Contacts();
        contacts.setContactName("Fatih");
        contacts.setContactNumber("05524663313");
        phoneList.add(contacts);
        notifyDataSetChanged();
        System.out.println("Burdayım2");
        System.out.println("--------------------------------PHONELIST-----------------------"+phoneList);
        System.out.println("YAZDIRDIM"+phoneList.get(1).getContactName());
    }
}
