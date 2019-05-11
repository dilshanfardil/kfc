package lk.sayuru.jungleapp.contact;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import lk.sayuru.jungleapp.db.entity.Contact;
import lk.sayuru.jungleapp.db.repository.ContactRepository;

public class ContactViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    public Contact contact;
    public ContactRepository contactRepository;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        contactRepository= new ContactRepository(application);
    }


    public void insertNewContact(){
        contactRepository.insert(contact);
    }
}
