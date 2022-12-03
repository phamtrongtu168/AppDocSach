package com.example.lamthu.Ham;

public class Book {
    private String ten, tacGia,url,mota;

    public Book() {
    }

    public Book(String ten, String tacGia, String url,String mota) {
        this.ten = ten;
        this.tacGia = tacGia;
        this.url = url;
        this.mota = mota;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }
}
