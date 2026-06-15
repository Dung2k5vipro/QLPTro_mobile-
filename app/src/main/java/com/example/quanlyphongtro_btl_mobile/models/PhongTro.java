package com.example.quanlyphongtro_btl_mobile.models;

public class PhongTro {
    private int maPhong;
    private String tenPhong;
    private double giaThue;
    private double dienTich;
    private String trangThai;
    private String ghiChu;
    private String hinhAnh;

    public PhongTro(int maPhong, String tenPhong, double giaThue, double dienTich, String trangThai, String ghiChu, String hinhAnh) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.giaThue = giaThue;
        this.dienTich = dienTich;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
        this.hinhAnh = hinhAnh; // Ánh xạ
    }

    public int getMaPhong() { return maPhong; }
    public String getTenPhong() { return tenPhong; }
    public double getGiaThue() { return giaThue; }
    public double getDienTich() { return dienTich; }
    public String getTrangThai() { return trangThai; }
    public String getGhiChu() { return ghiChu; }
    public String getHinhAnh() { return hinhAnh; } // Getter mới
}