package com.vignesh.revealmydestination.Model;

import com.vignesh.revealmydestination.Helper.Common;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vignesh on 18/7/17.
 */

public class Trip extends RealmObject {

    @PrimaryKey
    private String id;
    private String src_location;
    private double src_latitude;
    private double src_longitude;
    private String dst_location;
    private double dst_latitude;
    private double dst_longitude;
    private Date date;
    private Date created_at;
    private Date updated_at;


    public String getSrc_location() {
        return src_location;
    }

    public void setSrc_location(String src_location) {
        this.src_location = src_location;
    }

    public double getSrc_latitude() {
        return src_latitude;
    }

    public void setSrc_latitude(double src_latitude) {
        this.src_latitude = src_latitude;
    }

    public double getSrc_longitude() {
        return src_longitude;
    }

    public void setSrc_longitude(double src_longitude) {
        this.src_longitude = src_longitude;
    }

    public String getDst_location() {
        return dst_location;
    }

    public void setDst_location(String dst_location) {
        this.dst_location = dst_location;
    }

    public double getDst_latitude() {
        return dst_latitude;
    }

    public void setDst_latitude(double dst_latitude) {
        this.dst_latitude = dst_latitude;
    }

    public double getDst_longitude() {
        return dst_longitude;
    }

    public void setDst_longitude(double dst_longitude) {
        this.dst_longitude = dst_longitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

}
