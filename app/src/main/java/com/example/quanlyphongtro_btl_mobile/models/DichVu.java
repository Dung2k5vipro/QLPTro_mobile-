package com.example.quanlyphongtro_btl_mobile.models;

public class DichVu {

    private int maDichVu;
    private String tenDichVu;
    private double donGia;
    private String donViTinh;
    private String ghiChu;

    public DichVu() {}

    public DichVu(int maDichVu, String tenDichVu, double donGia, String donViTinh, String ghiChu) {
        this.maDichVu = maDichVu;
        this.tenDichVu = tenDichVu;
        this.donGia = donGia;
        this.donViTinh = donViTinh;
        this.ghiChu = ghiChu;
    }

    public DichVu(String tenDichVu, double donGia, String donViTinh, String ghiChu) {
        this.tenDichVu = tenDichVu;
        this.donGia = donGia;
        this.donViTinh = donViTinh;
        this.ghiChu = ghiChu;
    }

    public int getMaDichVu() { return maDichVu; }
    public void setMaDichVu(int maDichVu) { this.maDichVu = maDichVu; }

    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia < 0 ? 0 : donGia; }

    public String getDonViTinh() { return donViTinh; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    @Override
    public String toString() {
        return tenDichVu + " - " + donGia + "đ/" + donViTinh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DichVu)) return false;
        DichVu d = (DichVu) o;
        return maDichVu == d.maDichVu;
    }
}