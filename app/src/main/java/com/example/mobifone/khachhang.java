package com.example.mobifone;

public class khachhang {

    String idkh;
    String sdt;
    String hoten;
    String cccd;
    String ngaysinh;
    String gioitinh;
    String matkhau;
    String idgc;
    Double sodu;

    public khachhang(String idkh, String sdt, String hoten, String cccd, String ngaysinh, String gioitinh, String matkhau, String idgc, Double sodu) {
        this.idkh = idkh;
        this.sdt = sdt;
        this.hoten = hoten;
        this.cccd = cccd;
        this.ngaysinh = ngaysinh;
        this.gioitinh = gioitinh;
        this.matkhau = matkhau;
        this.idgc = idgc;
        this.sodu = sodu;
    }

    public String getIdkh() {
        return idkh;
    }

    public void setIdkh(String idkh) {
        this.idkh = idkh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getIdgc() {
        return idgc;
    }

    public void setIdgc(String idgc) {
        this.idgc = idgc;
    }

    public Double getSodu() {
        return sodu;
    }

    public void setSodu(Double sodu) {
        this.sodu = sodu;
    }
}
