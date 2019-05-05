package lk.sayuru.jungleapp.db.entity;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
@Entity(tableName = "Contact")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fname;
    private String lname;
    private String relation;
    private String phoneNo;

    @Ignore
    public Contact() {
    }
    @Ignore
    public Contact(String fname, String phoneNo) {
        this.fname = fname;
        this.phoneNo = phoneNo;
    }

    public Contact(String fname, String lname, String relation, String phoneNo) {
        this.setFname(fname);
        this.setLname(lname);
        this.setRelation(relation);
        this.setPhoneNo(phoneNo);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
