package lk.sayuru.jungleapp.db.dao;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import lk.sayuru.jungleapp.db.entity.PathPoint;

/*
 * Developed by Lahiru Muthumal on 5/4/2019.
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
public interface PathPontDao {
    @Insert
    void insert(PathPoint pathPoint);

    @Query("SELECT * FROM Point")
    LiveData<List<PathPoint>> getAllLive();

    @Query("SELECT * FROM Point")
    List<PathPoint> getAll();

    @Delete
    void deleteAll(List<PathPoint> pathPoints);

    @Delete
    void delete(PathPoint pathPoint);

}
