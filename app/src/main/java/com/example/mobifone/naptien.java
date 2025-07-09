package com.example.mobifone;

public class naptien {

    String idnt;
    String idnv;
    String idkh;
    Double sotien;
    String ngaynap;

    public naptien(String idnt, String idnv, String idkh, Double sotien, String ngaynap) {
        this.idnt = idnt;
        this.idnv = idnv;
        this.idkh = idkh;
        this.sotien = sotien;
        this.ngaynap = ngaynap;
    }

    public String getIdnt() {
        return idnt;
    }

    public void setIdnt(String idnt) {
        this.idnt = idnt;
    }

    public String getIdnv() {
        return idnv;
    }

    public void setIdnv(String idnv) {
        this.idnv = idnv;
    }

    public String getIdkh() {
        return idkh;
    }

    public void setIdkh(String idkh) {
        this.idkh = idkh;
    }

    public Double getSotien() {
        return sotien;
    }

    public void setSotien(Double sotien) {
        this.sotien = sotien;
    }

    public String getNgaynap() {
        return ngaynap;
    }

    public void setNgaynap(String ngaynap) {
        this.ngaynap = ngaynap;
    }
}
