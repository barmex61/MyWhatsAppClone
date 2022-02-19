package com.fatih.chatapplicationjava.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.fatih.chatapplicationjava.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivityViewModel extends ViewModel {

    public  MutableLiveData<Boolean> progressDialog=new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess=new MutableLiveData<>();
    public  MutableLiveData<Boolean> isSuccess1=new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess2=new MutableLiveData<>();
    public  MutableLiveData<Boolean> isSuccess3=new MutableLiveData<>();


    public void signUp(String email, String userName, String password, FirebaseAuth auth){
            progressDialog.setValue(true);
            isSuccess1.setValue(false);
            isSuccess2.setValue(false);
            isSuccess3.setValue(false);
        if(!email.equals("") &&!password.equals("")&&!userName.equals("")){
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    User currentUser=new User(userName, password, email,auth.getCurrentUser().getUid());
                    FirebaseFirestore.getInstance().collection("User").document(auth.getCurrentUser().getUid()).set(currentUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.setValue(false);
                            isSuccess.setValue(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            isSuccess1.setValue(true);                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                            isSuccess2.setValue(true);}
            });
        } else{
            isSuccess3.setValue(true);}
    }
}
