package com.example.quanlyphongtro_btl_mobile.models;

public class HopDong {
    private int maHd;
    private String soHd; // Ví dụ: HD-2024-001
    private int maPhong;
    private int maKhach;
    private String ngayBatDau;
    private String ngayKetThuc;
    private double tienCoc;
    private String trangThai;
    private String dieuKhoan;
    private String nguoiLap;

    // Thêm trường bổ trợ để hiển thị text ra danh sách không cần query lại
    private String tenPhong;
    private String tenKhach;

    public HopDong(int maHd, String soHd, int maPhong, int maKhach, String ngayBatDau, String ngayKetThuc, double tienCoc, String trangThai, String tenPhong, String tenKhach) {
        this.maHd = maHd;
        this.soHd = soHd;
        this.maPhong = maPhong;
        this.maKhach = maKhach;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.tienCoc = tienCoc;
        this.trangThai = trangThai;
        this.tenPhong = tenPhong;
        this.tenKhach = tenKhach;
    }

    public HopDong(int maHd, String soHd, int maPhong, int maKhach, String ngayBatDau, String ngayKetThuc, double tienCoc, String trangThai, String dieuKhoan, String nguoiLap, String tenPhong, String tenKhach) {
        this.maHd = maHd;
        this.soHd = soHd;
        this.maPhong = maPhong;
        this.maKhach = maKhach;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.tienCoc = tienCoc;
        this.trangThai = trangThai;
        this.dieuKhoan = dieuKhoan;
        this.nguoiLap = nguoiLap;
        this.tenPhong = tenPhong;
        this.tenKhach = tenKhach;
    }

    public int getMaHd() { return maHd; }
    public String getSoHd() { return soHd; }
    public int getMaPhong() { return maPhong; }
    public int getMaKhach() { return maKhach; }
    public String getNgayBatDau() { return ngayBatDau; }
    public String getNgayKetThuc() { return ngayKetThuc; }
    public double getTienCoc() { return tienCoc; }
    public String getTrangThai() { return trangThai; }
    public String getTenPhong() { return tenPhong; }
    public String getTenKhach() { return tenKhach; }
    public String getDieuKhoan() { return dieuKhoan; }
    public void setDieuKhoan(String dieuKhoan) { this.dieuKhoan = dieuKhoan; }
    public String getNguoiLap() { return nguoiLap; }
    public void setNguoiLap(String nguoiLap) { this.nguoiLap = nguoiLap; }
}