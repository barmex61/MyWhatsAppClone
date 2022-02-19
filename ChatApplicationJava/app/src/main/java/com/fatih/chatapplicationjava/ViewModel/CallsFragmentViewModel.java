package com.fatih.chatapplicationjava.ViewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.fatih.chatapplicationjava.Model.Contacts;

import java.util.ArrayList;
import java.util.List;

public class CallsFragmentViewModel extends AndroidViewModel {
   public MutableLiveData<List<Contacts>> contactList=new MutableLiveData<>();
    public CallsFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("Range")
    public void getPhoneData(){
        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
        Cursor phones =getApplication().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        List<Contacts> userList = new ArrayList<>();
        String lastPhoneName = " ";
            if(phones.getCount()>0)
                while (phones.moveToNext()) {
                     String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                     String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                     String contactId = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
                    if (!name.equalsIgnoreCase(lastPhoneName)) {
                        lastPhoneName = name;
                        Contacts user = new Contacts();
                        user.setContactName(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                        user.setContactNumber(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        userList.add(user);
                        Log.d("getContactsList", name + "---" + phoneNumber + " -- " + contactId  );
                    }
                }
                contactList.setValue(userList);
                phones.close();
            }
    }

