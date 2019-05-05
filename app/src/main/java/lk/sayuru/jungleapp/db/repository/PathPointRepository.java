package lk.sayuru.jungleapp.db.repository;
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


import android.content.Context;

import java.util.List;

import androidx.lifecycle.LiveData;
import lk.sayuru.jungleapp.db.AppDatabase;
import lk.sayuru.jungleapp.db.dao.PathPontDao;
import lk.sayuru.jungleapp.db.entity.PathPoint;

public class PathPointRepository {
    private final AppDatabase APP_DATABASE_INSTANCE;
    private final PathPontDao pathPontDao;

    public PathPointRepository(Context context){
        APP_DATABASE_INSTANCE=AppDatabase.getDatabase(context);
        pathPontDao=APP_DATABASE_INSTANCE.pathPontDao();
    }

    public void insert(PathPoint pathPoint){
        pathPontDao.insert(pathPoint);
    }

    public LiveData<List<PathPoint>> getAllLive(){
        return pathPontDao.getAllLive();
    }

    public List<PathPoint> getAll(){
        return pathPontDao.getAll();
    }

    public void deleteAllData(){
        List<PathPoint> all = pathPontDao.getAll();
        pathPontDao.deleteAll(all);
    }
}
