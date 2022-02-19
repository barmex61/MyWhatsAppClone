package com.fatih.chatapplicationjava.ViewModel;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fatih.chatapplicationjava.R;
import com.fatih.chatapplicationjava.View.MainActivity;
import com.fatih.chatapplicationjava.View.SettingsActivity;
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
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SettingsActivityViewModel extends AndroidViewModel {

    public MutableLiveData<Boolean> progressDialog=new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess=new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess1=new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess3=new MutableLiveData<>();
    public MutableLiveData<Map<String,Object>> myMap=new MutableLiveData<>();




    public SettingsActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void selectImage(View view, Context context, Activity activity, ActivityResultLauncher<String> permissionLauncher,ActivityResultLauncher<Intent> activityResultLauncher){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view, "Need Permission " ,Snackbar.LENGTH_LONG ).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else{
                //permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }else{
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        }
    }
    public void save(Uri selectedUri,StorageReference reference,String userName,String about){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.setValue(true);
        isSuccess.setValue(false);
        isSuccess1.setValue(false);
        isSuccess3.setValue(false);
        if(selectedUri!=null){
            UUID uuid=UUID.randomUUID();
            String referenceFolder="images/"+uuid+".png";
            reference.child(referenceFolder).putFile(selectedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReference= FirebaseStorage.getInstance().getReference(referenceFolder);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FirebaseFirestore.getInstance().collection("User").document(user.getUid()).update("photo", uri.toString(),"userName",userName,"status",about).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.setValue(false);
                                    isSuccess.setValue(true);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    isSuccess1.setValue(true);
                                }
                            });

                        }
                    });
                }
            });
        }else{
            isSuccess3.setValue(true);
        }
    }
    public void getData(Context context){
        FirebaseFirestore.getInstance().collection("User").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                myMap.setValue(documentSnapshot.getData());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
