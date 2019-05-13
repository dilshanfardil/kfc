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

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
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

    public List<LatLng> getAllPoints(){
        List<LatLng> points=new ArrayList<>();
        List<PathPoint> all = pathPontDao.getAll();
        for (PathPoint pathPoint : all) {
            if (pathPoint.isToView())continue;
            points.add(pathPoint.getLatLng());
        }
        return points;
    }

    public void deleteAllDataAddedPoints(){
        List<PathPoint> all = pathPontDao.getAll();
        List<PathPoint> all2=new ArrayList<>(all);
        for (PathPoint pathPoint : all2) {
            if (pathPoint.isToView())all.remove(pathPoint);
        }
        pathPontDao.deleteAll(all);
    }

    public void deleteAllViewPoints(){
        List<PathPoint> all = pathPontDao.getAll();
        List<PathPoint> all2=new ArrayList<>(all);
        for (PathPoint pathPoint : all2) {
            if (!pathPoint.isToView())all.remove(pathPoint);
        }
        pathPontDao.deleteAll(all);
    }

    public void removeLastPoint(){
        List<PathPoint> all = pathPontDao.getAll();
        for (int i=all.size()-1;i>=0;i--){
            PathPoint pathPoint = all.get(i);
            if (!pathPoint.isToView()){
                pathPontDao.delete(pathPoint);
                break;
            }
        }
    }

    public boolean isAddedPontLeft(){
        List<PathPoint> all = pathPontDao.getAll();
        for (PathPoint pathPoint : all) {
            if (!pathPoint.isToView()){
                return true;
            }
        }
        return false;
    }
}
