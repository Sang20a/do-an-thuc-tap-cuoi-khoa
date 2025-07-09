package com.example.mobifone;

public class goicuoc {

    String idgc;
    String tengoi;
    double gia;
    String dungluong;
    String hansd;

    public goicuoc(String idgc, String tengoi, double giacuoc, String dungluong, String hansd) {
        this.idgc = idgc;
        this.tengoi = tengoi;
        this.gia = giacuoc;
        this.dungluong = dungluong;
        this.hansd = hansd;
    }

    public String getIdgc() {
        return idgc;
    }

    public void setIdgc(String idgc) {
        this.idgc = idgc;
    }

    public String getTengoi() {
        return tengoi;
    }

    public void setTengoi(String tengoi) {
        this.tengoi = tengoi;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getDungluong() {
        return dungluong;
    }

    public void setDungluong(String dungluong) {
        this.dungluong = dungluong;
    }

    public String getHansd() {
        return hansd;
    }

    public void setHansd(String hansd) {
        this.hansd = hansd;
    }
}
