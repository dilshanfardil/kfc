package lk.sayuru.jungleapp.db.dao;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import lk.sayuru.jungleapp.db.entity.Contact;

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
@Dao
public interface ContactDao {
    @Insert
    void insert(Contact contact);

    @Delete
    void  delete(Contact contact);

    @Query("DELETE FROM Contact WHERE id=:id")
    void deleteById(int id);

    @Query("SELECT * FROM Contact")
    LiveData<List<Contact>> getAllLive();

    @Query("SELECT * FROM Contact")
    List<Contact> getAll();



    @Update
    void update(Contact contact);
}
