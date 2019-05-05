package lk.sayuru.jungleapp.db.repository;
/*
 * Developed by Lahiru Muthumal on 4/19/2019.
 * Last modified $file.lastModified.
 *
 * (C) Copyright 2019.year avalanche.lk (Pvt) Limited.
 * All Rights Reserved.
 *
 * These materials are unpublished, proprietary, confidential source code of
 * avalanche.lk (Pvt) Limited and constitute a TRADE SECRET
 * of avalanche.lk (Pvt) Limited.
 *
 * avalanche.lk (Pvt) Limited retains all title to and intellectual
 * property rights in these materials.
 *
 */

import android.content.Context;

import java.util.List;

import androidx.lifecycle.LiveData;
import lk.sayuru.jungleapp.db.AppDatabase;
import lk.sayuru.jungleapp.db.dao.ContactDao;
import lk.sayuru.jungleapp.db.entity.Contact;

public class ContactRepository {
    private final AppDatabase APP_DATABASE_INSTANCE;
    private final ContactDao contactDao;

    public ContactRepository(Context context) {
        APP_DATABASE_INSTANCE= AppDatabase.getDatabase(context);
        contactDao=APP_DATABASE_INSTANCE.contactDao();
    }

    public void insert(Contact contact){
        contactDao.insert(contact);
    }

    public void delete(Contact contact){
        contactDao.delete(contact);
    }

    public void deleteById(int id){
        contactDao.deleteById(id);
    }

    public LiveData<List<Contact>> getAllLive(){
        LiveData<List<Contact>> all = contactDao.getAllLive();
        return all;
    }

    public List<Contact> getAll(){
        return contactDao.getAll();
    }

    public void update(Contact contact){
        contactDao.update(contact);
    }


}
