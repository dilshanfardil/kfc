package lk.sayuru.jungleapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import lk.sayuru.jungleapp.contact.ContactListFragment;
import lk.sayuru.jungleapp.content.ContactContent;
import lk.sayuru.jungleapp.db.entity.Contact;
import lk.sayuru.jungleapp.db.repository.ContactRepository;

public class ContactActivity extends AppCompatActivity implements ContactListFragment.OnListFragmentInteractionListener {

    private static final int PERMISSIONS_REQUEST_READ_CONTACT = 144;
    private ContactRepository contactRepository;
    private final int REQUEST_CODE_CONTACT=99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        contactRepository = new ContactRepository(this);

        FloatingActionButton btnNewContact = findViewById(R.id.fabNewContact);
        btnNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContact();
            }
        });
    }

    private void getContact(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        PERMISSIONS_REQUEST_READ_CONTACT);
            }
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent,REQUEST_CODE_CONTACT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSIONS_REQUEST_READ_CONTACT:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    getContact();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Reading contact failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }
    }

    @Override
    public void onListFragmentInteraction(ContactContent.ContactItem item, final ContactListRecyclerViewAdapter contactListRecyclerViewAdapter) {
        MainActivity.contactRepository.deleteById(Integer.parseInt(item.id));
        contactListRecyclerViewAdapter.dataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_CONTACT:
                Contact contact = new Contact();
                String strRequestCode = String.valueOf(resultCode);
                String strOkCode = String.valueOf(Activity.RESULT_OK);
                if (strRequestCode.equals(strOkCode)){
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        contact.setFname(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
                        //contact.setLname(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_SOURCE)));
                        String nums = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                String num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                if(!nums.contains(num))nums+=num+",";
                                Toast.makeText(ContactActivity.this, "Number=" + nums, Toast.LENGTH_LONG).show();
                            }
                        }
                        contact.setPhoneNo(nums);
                        MainActivity.contactRepository.insert(contact);
                    }else {
                        Toast.makeText(getApplicationContext(),
                                "Reading contact failed, please try again.", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
}
