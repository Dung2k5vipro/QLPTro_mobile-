package com.example.quanlyphongtro_btl_mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuanLyPhongTroMoi.db";
    private static final int DATABASE_VERSION = 7;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Bảng Tài Khoản
        db.execSQL("CREATE TABLE TaiKhoan (maTK INTEGER PRIMARY KEY AUTOINCREMENT, tenDangNhap TEXT NOT NULL UNIQUE, matKhau TEXT NOT NULL, hoTen TEXT);");

        // 2. Bảng Phòng Trọ
        db.execSQL("CREATE TABLE PhongTro (maPhong INTEGER PRIMARY KEY AUTOINCREMENT, tenPhong TEXT NOT NULL, giaThue REAL NOT NULL, dienTich REAL, trangThai TEXT NOT NULL, ghiChu TEXT, hinhAnh TEXT);");

        // 3. Bảng Khách Thuê
        db.execSQL("CREATE TABLE KhachThue (maKhach INTEGER PRIMARY KEY AUTOINCREMENT, hoTen TEXT NOT NULL, soDienThoai TEXT, cccd TEXT, diaChi TEXT, gioiTinh TEXT, ngaySinh TEXT);");

        // 4. Bảng Hợp Đồng
        db.execSQL("CREATE TABLE HopDong (\n" +
                "    maHd INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    soHd TEXT NOT NULL,\n" +
                "    maPhong INTEGER,\n" +
                "    maKhach INTEGER,\n" +
                "    ngayBatDau TEXT,\n" +
                "    ngayKetThuc TEXT,\n" +
                "    tienCoc REAL,\n" +
                "    trangThai TEXT,\n" +
                "    FOREIGN KEY(maPhong) REFERENCES PhongTro(maPhong),\n" +
                "    FOREIGN KEY(maKhach) REFERENCES KhachThue(maKhach)\n" +
                ");");
        // 5. Bảng Dịch Vụ
        db.execSQL("CREATE TABLE DichVu (maDichVu INTEGER PRIMARY KEY AUTOINCREMENT, tenDichVu TEXT NOT NULL, donGia REAL NOT NULL, donViTinh TEXT, ghiChu TEXT);");

        // 6. Bảng Hóa Đơn
        db.execSQL("CREATE TABLE HoaDon (maHoaDon INTEGER PRIMARY KEY AUTOINCREMENT, maPhong INTEGER NOT NULL, thang TEXT NOT NULL, tienPhong REAL, chiSoDienCu INTEGER, chiSoDienMoi INTEGER, chiSoNuocCu INTEGER, chiSoNuocMoi INTEGER, tienDien REAL, tienNuoc REAL, tienInternet REAL, tienRac REAL, tongTien REAL, trangThai TEXT, FOREIGN KEY (maPhong) REFERENCES PhongTro(maPhong));");

        // Chèn dữ liệu mẫu
        db.execSQL("INSERT INTO TaiKhoan(tenDangNhap, matKhau, hoTen) VALUES ('admin', '123456', 'Quản trị viên');");
        db.execSQL("INSERT INTO DichVu(tenDichVu, donGia, donViTinh) VALUES ('Điện', 3500, 'kWh'), ('Nước', 15000, 'm3'), ('Internet', 100000, 'Tháng'), ('Rác', 30000, 'Tháng');");
        db.execSQL("INSERT INTO PhongTro(tenPhong, giaThue, dienTich, trangThai, ghiChu, hinhAnh) VALUES ('Phòng 101', 2500000, 20, 'Trống', 'Lầu 1', '');");
        db.execSQL("INSERT INTO PhongTro(tenPhong, giaThue, dienTich, trangThai, ghiChu, hinhAnh) VALUES ('Phòng 102', 2500000, 20, 'Trống', 'Lầu 1', '');");
        db.execSQL("INSERT INTO KhachThue(hoTen, soDienThoai, cccd, diaChi, gioiTinh, ngaySinh) VALUES ('Nguyễn Văn A', '0901234567', '123456789', 'Hà Nội', 'Nam', '01/01/1995');");
        db.execSQL("INSERT INTO KhachThue(hoTen, soDienThoai, cccd, diaChi, gioiTinh, ngaySinh) VALUES ('Trần Thị B', '0988888888', '987654321', 'TP.HCM', 'Nữ', '15/05/1998');");

        // Dữ liệu mẫu bổ sung
        for (int i = 1; i <= 10; i++) {
            db.execSQL("INSERT INTO PhongTro (tenPhong, giaThue, dienTich, trangThai, ghiChu, hinhAnh) VALUES (" +
                    "'P" + String.format("%02d", i) + "'," +
                    (1500000 + i * 100000) + "," +
                    (18 + i) + "," +
                    "'Trống'," +
                    "'Phong moi'," +
                    "'phong" + i + ".jpg')");
        }

        // 3. KHÁCH THUÊ
        db.execSQL("INSERT INTO KhachThue (hoTen, soDienThoai, cccd, diaChi, gioiTinh, ngaySinh) VALUES ('Nguyen Van A','0901111111','001001001001','Ha Noi','Nam','2000-01-01')");
        db.execSQL("INSERT INTO KhachThue (hoTen, soDienThoai, cccd, diaChi, gioiTinh, ngaySinh) VALUES ('Tran Thi B','0901111112','001001001002','Hai Phong','Nu','2001-02-02')");
        db.execSQL("INSERT INTO KhachThue (hoTen, soDienThoai, cccd, diaChi, gioiTinh, ngaySinh) VALUES ('Le Van C','0901111113','001001001003','Nam Dinh','Nam','1999-03-03')");
        db.execSQL("INSERT INTO KhachThue (hoTen, soDienThoai, cccd, diaChi, gioiTinh, ngaySinh) VALUES ('Pham Thi D','0901111114','001001001004','Thai Binh','Nu','2002-04-04')");
        db.execSQL("INSERT INTO KhachThue (hoTen, soDienThoai, cccd, diaChi, gioiTinh, ngaySinh) VALUES ('Hoang Van E','0901111115','001001001005','Ha Nam','Nam','1998-05-05')");
        db.execSQL("INSERT INTO KhachThue (hoTen, soDienThoai, cccd, diaChi, gioiTinh, ngaySinh) VALUES ('Do Thi F','0901111116','001001001006','Bac Ninh','Nu','2001-06-06')");
        db.execSQL("INSERT INTO KhachThue (hoTen, soDienThoai, cccd, diaChi, gioiTinh, ngaySinh) VALUES ('Vu Van G','0901111117','001001001007','Ha Noi','Nam','2000-07-07')");
        db.execSQL("INSERT INTO KhachThue (hoTen, soDienThoai, cccd, diaChi, gioiTinh, ngaySinh) VALUES ('Bui Thi H','0901111118','001001001008','Hung Yen','Nu','2003-08-08')");
        db.execSQL("INSERT INTO KhachThue (hoTen, soDienThoai, cccd, diaChi, gioiTinh, ngaySinh) VALUES ('Dang Van I','0901111119','001001001009','Ninh Binh','Nam','1997-09-09')");
        db.execSQL("INSERT INTO KhachThue (hoTen, soDienThoai, cccd, diaChi, gioiTinh, ngaySinh) VALUES ('Ngo Thi K','0901111120','001001001010','Thanh Hoa','Nu','2002-10-10');");

        // 4. HỢP ĐỒNG
        for (int i = 1; i <= 10; i++) {
            db.execSQL("INSERT INTO HopDong (soHd, maPhong, maKhach, ngayBatDau, ngayKetThuc, tienCoc, trangThai) VALUES (" +
                    "'HD00" + i + "'," +
                    i + "," +
                    i + "," +
                    "'2025-01-01'," +
                    "'2025-12-31'," +
                    "2000000," +
                    "'Con hieu luc')");
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS HoaDon");
        db.execSQL("DROP TABLE IF EXISTS DichVu");
        db.execSQL("DROP TABLE IF EXISTS HopDong");
        db.execSQL("DROP TABLE IF EXISTS KhachThue");
        db.execSQL("DROP TABLE IF EXISTS PhongTro");
        db.execSQL("DROP TABLE IF EXISTS TaiKhoan");
        onCreate(db);
    }

    // --- QUẢN LÝ TÀI KHOẢN ---
    public boolean kiemTraDangNhap(String user, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ?", new String[]{user, pass});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // --- QUẢN LÝ PHÒNG TRỌ ---
    public Cursor layDanhSachPhongTro() {
        return getReadableDatabase().rawQuery("SELECT * FROM PhongTro", null);
    }

    public boolean themPhongTro(String ten, double gia, double dt, String tt, String gc, String anh) {
        ContentValues v = new ContentValues();
        v.put("tenPhong", ten); v.put("giaThue", gia); v.put("dienTich", dt); v.put("trangThai", tt); v.put("ghiChu", gc); v.put("hinhAnh", anh);
        return getWritableDatabase().insert("PhongTro", null, v) != -1;
    }

    public boolean suaPhongTro(int id, String ten, double gia, double dt, String tt, String gc, String anh) {
        ContentValues v = new ContentValues();
        v.put("tenPhong", ten); v.put("giaThue", gia); v.put("dienTich", dt); v.put("trangThai", tt); v.put("ghiChu", gc); v.put("hinhAnh", anh);
        return getWritableDatabase().update("PhongTro", v, "maPhong = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean xoaPhongTro(int id) {
        return getWritableDatabase().delete("PhongTro", "maPhong = ?", new String[]{String.valueOf(id)}) > 0;
    }

    // --- QUẢN LÝ KHÁCH THUÊ ---
    public Cursor layDanhSachKhachThue() {
        return getReadableDatabase().rawQuery("SELECT * FROM KhachThue ORDER BY maKhach DESC", null);
    }

    public boolean themKhachThue(String ten, String sdt, String cccd, String dc, String gt, String ns) {
        ContentValues v = new ContentValues();
        v.put("hoTen", ten); v.put("soDienThoai", sdt); v.put("cccd", cccd); v.put("diaChi", dc); v.put("gioiTinh", gt); v.put("ngaySinh", ns);
        return getWritableDatabase().insert("KhachThue", null, v) != -1;
    }

    public boolean suaKhachThue(int id, String ten, String sdt, String cccd, String dc, String gt, String ns) {
        ContentValues v = new ContentValues();
        v.put("hoTen", ten); v.put("soDienThoai", sdt); v.put("cccd", cccd); v.put("diaChi", dc); v.put("gioiTinh", gt); v.put("ngaySinh", ns);
        return getWritableDatabase().update("KhachThue", v, "maKhach = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean xoaKhachThue(int id) {
        return getWritableDatabase().delete("KhachThue", "maKhach = ?", new String[]{String.valueOf(id)}) > 0;
    }

    // --- QUẢN LÝ DỊCH VỤ ---
    public Cursor layDanhSachDichVu() {
        return getReadableDatabase().rawQuery("SELECT * FROM DichVu", null);
    }

    public boolean themDichVu(String ten, double gia, String dvi, String gc) {
        ContentValues v = new ContentValues();
        v.put("tenDichVu", ten); v.put("donGia", gia); v.put("donViTinh", dvi); v.put("ghiChu", gc);
        return getWritableDatabase().insert("DichVu", null, v) != -1;
    }

    public boolean suaDichVu(int id, String ten, double gia, String dvi, String gc) {
        ContentValues v = new ContentValues();
        v.put("tenDichVu", ten); v.put("donGia", gia); v.put("donViTinh", dvi); v.put("ghiChu", gc);
        return getWritableDatabase().update("DichVu", v, "maDichVu = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean xoaDichVu(int id) {
        return getWritableDatabase().delete("DichVu", "maDichVu = ?", new String[]{String.valueOf(id)}) > 0;
    }

    // --- QUẢN LÝ HÓA ĐƠN ---
    public Cursor layDanhSachHoaDon() {
        return getReadableDatabase().rawQuery("SELECT HoaDon.*, PhongTro.tenPhong FROM HoaDon JOIN PhongTro ON HoaDon.maPhong = PhongTro.maPhong ORDER BY maHoaDon DESC", null);
    }

    public boolean xoaHoaDon(int id) {
        return getWritableDatabase().delete("HoaDon", "maHoaDon = ?", new String[]{String.valueOf(id)}) > 0;
    }

    // --- QUẢN LÝ HỢP ĐỒNG ---
    // Lấy danh sách phòng TRỐNG để nạp vào Spinner khi tạo hợp đồng mới (Ẩn phòng đã thuê)
    public Cursor layPhongTrongSpinner() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT maPhong, tenPhong FROM PhongTro WHERE trangThai = 'Trống'", null);
    }

    // Lấy toàn bộ danh sách phòng để phục vụ việc SỬA hợp đồng
    public Cursor layTatCaPhongSpinner() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT maPhong, tenPhong FROM PhongTro", null);
    }

    // Lấy danh sách hợp đồng (Dùng lệnh JOIN để lấy tên phòng và tên khách thuê hiển thị)
    public Cursor layDanhSachHopDong() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT h.*, p.tenPhong, k.hoTen FROM HopDong h \n" +
                "LEFT JOIN PhongTro p ON h.maPhong = p.maPhong \n" +
                "LEFT JOIN KhachThue k ON h.maKhach = k.maKhach \n" +
                "ORDER BY h.maHd DESC", null);
    }

    // LOGIC TỰ ĐỘNG 1: THÊM HỢP ĐỒNG VÀ CHUYỂN TRẠNG THÁI PHÒNG SANG ĐÃ THUÊ
    public boolean themHopDong(String soHd, int maPhong, int maKhach, String ngayBd, String ngayKt, double tienCoc, String trangThai) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction(); // Bật cơ chế kiểm soát giao dịch an toàn CSDL
        try {
            ContentValues values = new ContentValues();
            values.put("soHd", soHd);
            values.put("maPhong", maPhong);
            values.put("maKhach", maKhach);
            values.put("ngayBatDau", ngayBd);
            values.put("ngayKetThuc", ngayKt);
            values.put("tienCoc", tienCoc);
            values.put("trangThai", trangThai);
            long idHd = db.insert("HopDong", null, values);

            if (idHd != -1) {
                // Tự động chuyển phòng được chọn sang trạng thái 'Đã thuê'
                db.execSQL("UPDATE PhongTro SET trangThai = 'Đã thuê' WHERE maPhong = ?", new Object[]{maPhong});
                db.setTransactionSuccessful();
                return true;
            }
        } finally {
            db.endTransaction();
        }
        return false;
    }

    // LOGIC TỰ ĐỘNG 2: XÓA HỢP ĐỒNG VÀ TRẢ PHÒNG VỀ TRẠNG THÁI TRỐNG
    public boolean xoaHopDong(int maHd, int maPhong) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            int ret = db.delete("HopDong", "maHd = ?", new String[]{String.valueOf(maHd)});
            if (ret > 0) {
                // Giải phóng phòng cũ quay về trạng thái 'Trống' để cho người khác thuê
                db.execSQL("UPDATE PhongTro SET trangThai = 'Trống' WHERE maPhong = ?", new Object[]{maPhong});
                db.setTransactionSuccessful();
                return true;
            }
        } finally {
            db.endTransaction();
        }
        return false;
    }

    // Cập nhật thông tin sửa đổi hợp đồng
    public boolean suaHopDong(int maHd, int maPhongMoi, int maPhongCu, int maKhach, String ngayBd, String ngayKt, double tienCoc, String trangThai) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("maPhong", maPhongMoi);
            values.put("maKhach", maKhach);
            values.put("ngayBatDau", ngayBd);
            values.put("ngayKetThuc", ngayKt);
            values.put("tienCoc", tienCoc);
            values.put("trangThai", trangThai);

            int check = db.update("HopDong", values, "maHd = ?", new String[]{String.valueOf(maHd)});
            if (check > 0) {
                // Nếu thay đổi sang thuê phòng khác: Trả phòng cũ về Trống, chuyển phòng mới sang Đã thuê
                if (maPhongMoi != maPhongCu) {
                    db.execSQL("UPDATE PhongTro SET trangThai = 'Trống' WHERE maPhong = ?", new Object[]{maPhongCu});
                    db.execSQL("UPDATE PhongTro SET trangThai = 'Đã thuê' WHERE maPhong = ?", new Object[]{maPhongMoi});
                }
                db.setTransactionSuccessful();
                return true;
            }
        } finally {
            db.endTransaction();
        }
        return false;
    }
}
