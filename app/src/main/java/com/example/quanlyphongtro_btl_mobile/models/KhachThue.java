package com.example.quanlyphongtro_btl_mobile.models;

public class KhachThue {
    private int maKhach;
    private String hoTen;
    private String soDienThoai;
    private String cccd;
    private String diaChi;
    private String gioiTinh;
    private String ngaySinh;
    private String anhMatTruoc;
    private String anhMatSau;

    public KhachThue(int maKhach, String hoTen, String soDienThoai, String cccd, String diaChi, String gioiTinh, String ngaySinh) {
        this.maKhach = maKhach;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.cccd = cccd;
        this.diaChi = diaChi;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
    }

    public KhachThue(int maKhach, String hoTen, String soDienThoai, String cccd, String diaChi, String gioiTinh, String ngaySinh, String anhMatTruoc, String anhMatSau) {
        this.maKhach = maKhach;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.cccd = cccd;
        this.diaChi = diaChi;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.anhMatTruoc = anhMatTruoc;
        this.anhMatSau = anhMatSau;
    }

    public int getMaKhach() { return maKhach; }
    public String getHoTen() { return hoTen; }
    public String getSoDienThoai() { return soDienThoai; }
    public String getCccd() { return cccd; }
    public String getDiaChi() { return diaChi; }
    public String getGioiTinh() { return gioiTinh; }
    public String getNgaySinh() { return ngaySinh; }
    public String getAnhMatTruoc() { return anhMatTruoc; }
    public void setAnhMatTruoc(String anhMatTruoc) { this.anhMatTruoc = anhMatTruoc; }
    public String getAnhMatSau() { return anhMatSau; }
    public void setAnhMatSau(String anhMatSau) { this.anhMatSau = anhMatSau; }
}