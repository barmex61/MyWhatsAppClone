package com.fatih.chatapplicationjava.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fatih.chatapplicationjava.Adapter.CallsAdapter;
import com.fatih.chatapplicationjava.Adapter.UserAdapter;
import com.fatih.chatapplicationjava.Model.Contacts;
import com.fatih.chatapplicationjava.R;
import com.fatih.chatapplicationjava.ViewModel.CallsFragmentViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class CallsFragment extends Fragment {
    private CallsAdapter callsAdapter=new CallsAdapter(new ArrayList<>());
    private ActivityResultLauncher<String> permissionLauncher;
    private CallsFragmentViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLauncher();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_calls, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=new ViewModelProvider(this).get(CallsFragmentViewModel.class);
        observeLiveData();
        checkPermission(view);
        RecyclerView recyclerView=view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(callsAdapter);

    }
    private void checkPermission(View view){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_CONTACTS)) {
                Snackbar.make(view, "Need Permission", BaseTransientBottomBar.LENGTH_LONG).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //permission
                        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_CONTACTS)) {
                            permissionLauncher.launch(Manifest.permission.READ_CONTACTS);
                        }
                    }
                }).show();
            }else{
                //permission
                permissionLauncher.launch(Manifest.permission.READ_CONTACTS);
            }
        }else{
                viewModel.getPhoneData();
        }

    }
    private void registerLauncher(){
        permissionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                    if(result){
                        viewModel.getPhoneData();
                    }else{
                        Toast.makeText(requireActivity(), "Need Permission...", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
    private void observeLiveData(){
        viewModel.contactList.observe(getViewLifecycleOwner(), new Observer<List<Contacts>>() {
            @Override
            public void onChanged(List<Contacts> contacts) {
                System.out.println("DEĞİŞTİ BAŞKAN");
                callsAdapter.updateList((ArrayList<Contacts>) contacts);

            }
        });
    }
}