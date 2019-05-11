package lk.sayuru.jungleapp.db.entity;


import com.google.android.gms.maps.model.LatLng;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

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
@Entity(tableName = "Point")
public class PathPoint {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @TypeConverters(LatLngConvertor.class)
    private LatLng latLng;
    private boolean toView =false;

    @Ignore
    public PathPoint() {
    }
    @Ignore
    public PathPoint(LatLng latLng, boolean toView) {
        this.latLng = latLng;
        this.toView = toView;
    }

    @Ignore
    public PathPoint( LatLng latLng) {
        this.latLng = latLng;
    }
    @Ignore
    public PathPoint(int id, LatLng latLng) {
        this.id = id;
        this.latLng = latLng;
    }

    public PathPoint(int id, LatLng latLng, boolean toView) {
        this.id = id;
        this.latLng = latLng;
        this.toView = toView;
    }

    public boolean isToView() {
        return toView;
    }

    public void setToView(boolean toView) {
        this.toView = toView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }


    public static class LatLngConvertor{
        @TypeConverter
        public LatLng fromString(String s){
            if (s==null)return null;
            String[] split = s.split(":xjx:");
            return new LatLng(Double.valueOf(split[0]),Double.valueOf(split[1]));
        }

        @TypeConverter
        public String latLngToString(LatLng latLng){
            return latLng==null?null: latLng.latitude+":xjx:"+latLng.longitude;
        }
    }

}
