package lk.sayuru.jungleapp.db.dao;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import lk.sayuru.jungleapp.db.entity.Contact;


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
