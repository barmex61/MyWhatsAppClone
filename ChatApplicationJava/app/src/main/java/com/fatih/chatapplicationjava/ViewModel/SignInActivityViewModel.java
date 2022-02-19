package com.fatih.chatapplicationjava.ViewModel;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivityViewModel extends ViewModel {

    public MutableLiveData<Boolean> progressDialog=new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess=new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess1=new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess2=new MutableLiveData<>();

    public void signIn(String email, String password){
            isSuccess.setValue(false);
            isSuccess1.setValue(false);
            isSuccess2.setValue(false);
        if(!email.equals("")&&!password.equals("")){
            progressDialog.setValue(true);
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressDialog.setValue(false);
                    isSuccess.setValue(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.setValue(false);
                    isSuccess1.setValue(true);                       }
            });

        }else{
            isSuccess2.setValue(true);
        }
    }
}
