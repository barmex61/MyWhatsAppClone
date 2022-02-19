package com.fatih.chatapplicationjava.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fatih.chatapplicationjava.R;
import com.fatih.chatapplicationjava.ViewModel.SettingsActivityViewModel;
import com.fatih.chatapplicationjava.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private ActivityResultLauncher<String> permissionLauncher;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Uri selectedUri;
    private StorageReference reference;
    private ProgressDialog progressDialog;
    private SettingsActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        viewModel=new ViewModelProvider(this).get(SettingsActivityViewModel.class);
        observeLiveData();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        reference = storage.getReference();
        progressDialog  = new ProgressDialog(this);
        progressDialog.setTitle("Image is loading ");
        progressDialog.setMessage("Please wait ...");
        Objects.requireNonNull(getSupportActionBar()).hide();
        registerLauncher();
        getData();

    }
    public void goBack(View view){
        Intent intent=new Intent(SettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void selectImage(View view){
        viewModel.selectImage(view,SettingsActivity.this,this,permissionLauncher,activityResultLauncher);

    }
    private void registerLauncher(){
        permissionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                        if(result){
                            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            activityResultLauncher.launch(intent);
                        }else{
                            Toast.makeText(SettingsActivity.this, "Need Permission ...", Toast.LENGTH_SHORT).show();
                        }
             }
        });

        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        Intent intentFromResult=result.getData();
                        if(intentFromResult!=null){
                                selectedUri=intentFromResult.getData();
                                binding.profileImage.setImageURI(selectedUri);
                        }
                    }
            }
        });
    }
    public  void save(View view){
        String userName=binding.usernametext.getText().toString();
        String about=binding.aboutText.getText().toString();
        viewModel.save(selectedUri,reference,userName,about);
    }
    public void getData(){
        viewModel.getData(SettingsActivity.this);

    }
    public void observeLiveData(){
        viewModel.progressDialog.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    progressDialog.show();
                }else{
                    progressDialog.dismiss();
                }
            }
        });
        viewModel.isSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Intent intent=new Intent(SettingsActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        viewModel.isSuccess1.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(SettingsActivity.this,"Error1!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.isSuccess3.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(SettingsActivity.this,"Select an Image",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
        viewModel.myMap.observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> stringObjectMap) {
                binding.aboutText.setText((String) stringObjectMap.get("status"));
                binding.usernametext.setText((String)stringObjectMap.get("userName"));
                Picasso.get().load((String) stringObjectMap.get("photo")).placeholder(R.drawable.avatar).into(binding.profileImage);
            }
        });

    }
}