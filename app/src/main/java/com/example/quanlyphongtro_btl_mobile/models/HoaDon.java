package com.example.quanlyphongtro_btl_mobile.models;

public class HoaDon {
    private int maHoaDon;
    private int maPhong;
    private String tenPhong; // Lấy từ JOIN
    private String thang;
    private double tienPhong;
    private int chiSoDienCu;
    private int chiSoDienMoi;
    private int chiSoNuocCu;
    private int chiSoNuocMoi;
    private double tienDien;
    private double tienNuoc;
    private double tienInternet;
    private double tienRac;
    private double tongTien;
    private String trangThai; // "Chưa thanh toán", "Đã thanh toán"
    private String soDienThoaiKhach; // Lấy từ JOIN để share Zalo

    public HoaDon() {}

    public HoaDon(int maHoaDon, int maPhong, String thang, double tienPhong, int chiSoDienCu, int chiSoDienMoi, int chiSoNuocCu, int chiSoNuocMoi, double tienDien, double tienNuoc, double tienInternet, double tienRac, double tongTien, String trangThai) {
        this.maHoaDon = maHoaDon;
        this.maPhong = maPhong;
        this.thang = thang;
        this.tienPhong = tienPhong;
        this.chiSoDienCu = chiSoDienCu;
        this.chiSoDienMoi = chiSoDienMoi;
        this.chiSoNuocCu = chiSoNuocCu;
        this.chiSoNuocMoi = chiSoNuocMoi;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.tienInternet = tienInternet;
        this.tienRac = tienRac;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public String getThang() { return thang; }
    public void setThang(String thang) { this.thang = thang; }

    public double getTienPhong() { return tienPhong; }
    public void setTienPhong(double tienPhong) { this.tienPhong = tienPhong; }

    public int getChiSoDienCu() { return chiSoDienCu; }
    public void setChiSoDienCu(int chiSoDienCu) { this.chiSoDienCu = chiSoDienCu; }

    public int getChiSoDienMoi() { return chiSoDienMoi; }
    public void setChiSoDienMoi(int chiSoDienMoi) { this.chiSoDienMoi = chiSoDienMoi; }

    public int getChiSoNuocCu() { return chiSoNuocCu; }
    public void setChiSoNuocCu(int chiSoNuocCu) { this.chiSoNuocCu = chiSoNuocCu; }

    public int getChiSoNuocMoi() { return chiSoNuocMoi; }
    public void setChiSoNuocMoi(int chiSoNuocMoi) { this.chiSoNuocMoi = chiSoNuocMoi; }

    public double getTienDien() { return tienDien; }
    public void setTienDien(double tienDien) { this.tienDien = tienDien; }

    public double getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(double tienNuoc) { this.tienNuoc = tienNuoc; }

    public double getTienInternet() { return tienInternet; }
    public void setTienInternet(double tienInternet) { this.tienInternet = tienInternet; }

    public double getTienRac() { return tienRac; }
    public void setTienRac(double tienRac) { this.tienRac = tienRac; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getSoDienThoaiKhach() { return soDienThoaiKhach; }
    public void setSoDienThoaiKhach(String soDienThoaiKhach) { this.soDienThoaiKhach = soDienThoaiKhach; }
}
