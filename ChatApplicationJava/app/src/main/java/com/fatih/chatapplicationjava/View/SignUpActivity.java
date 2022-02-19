package com.fatih.chatapplicationjava.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.fatih.chatapplicationjava.ViewModel.SignUpActivityViewModel;
import com.fatih.chatapplicationjava.databinding.ActivitySignUpBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivitySignUpBinding binding;
    private ProgressDialog progressDialog;
    private SignUpActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        Objects.requireNonNull(getSupportActionBar()).hide();
        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");
        viewModel= new ViewModelProvider(this).get(SignUpActivityViewModel.class);
        observeLiveData();

        binding.textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void signUp(View view){

        String email = binding.emailText.getText().toString();
        String password = binding.passwordText.getText().toString();
        String userName = binding.userNameText.getText().toString();
            viewModel.signUp(email, userName, password,auth);
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
                        Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
            }

        });
        viewModel.isSuccess1.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(SignUpActivity.this, "Error!", Toast.LENGTH_LONG).show();
                }
            }
        });
        viewModel.isSuccess2.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(SignUpActivity.this,"Error!", Toast.LENGTH_LONG).show();
                }
            }
        });
        viewModel.isSuccess3.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(SignUpActivity.this,"Enter all Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}