package com.example.mobifone;

public class nhanvien {

    private String idnv;
    private  String htnv;
    private String mknv;
    private String sdtnv;

    public nhanvien(String idnv, String htnv, String mknv, String sdtnv) {
        this.idnv = idnv;
        this.htnv = htnv;
        this.mknv = mknv;
        this.sdtnv = sdtnv;
    }

    public String getIdnv() {
        return idnv;
    }

    public void setIdnv(String idnv) {
        this.idnv = idnv;
    }

    public String getHtnv() {
        return htnv;
    }

    public void setHtnv(String htnv) {
        this.htnv = htnv;
    }

    public String getMknv() {
        return mknv;
    }

    public void setMknv(String mknv) {
        this.mknv = mknv;
    }

    public String getSdtnv() {
        return sdtnv;
    }

    public void setSdtnv(String sdtnv) {
        this.sdtnv = sdtnv;
    }
}
