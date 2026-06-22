package com.example.quanlyphongtro_btl_mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quanlyphongtro_btl_mobile.models.DichVu;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuanLyPhongTroMoi.db";
    private static final int DATABASE_VERSION = 21;

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
        db.execSQL("CREATE TABLE KhachThue (maKhach INTEGER PRIMARY KEY AUTOINCREMENT, hoTen TEXT NOT NULL, soDienThoai TEXT, cccd TEXT, diaChi TEXT, gioiTinh TEXT, ngaySinh TEXT, anhMatTruoc TEXT, anhMatSau TEXT);");

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
                "    dieuKhoan TEXT,\n" +
                "    nguoiLap TEXT,\n" +
                "    FOREIGN KEY(maPhong) REFERENCES PhongTro(maPhong),\n" +
                "    FOREIGN KEY(maKhach) REFERENCES KhachThue(maKhach)\n" +
                ");");
        // 5. Bảng Dịch Vụ
        db.execSQL("CREATE TABLE DichVu (maDichVu INTEGER PRIMARY KEY AUTOINCREMENT, tenDichVu TEXT NOT NULL, donGia REAL NOT NULL, donViTinh TEXT, ghiChu TEXT);");

        // 6. Bảng Hóa Đơn
        db.execSQL("CREATE TABLE HoaDon (maHoaDon INTEGER PRIMARY KEY AUTOINCREMENT, maPhong INTEGER NOT NULL, thang TEXT NOT NULL, tienPhong REAL, chiSoDienCu INTEGER, chiSoDienMoi INTEGER, chiSoNuocCu INTEGER, chiSoNuocMoi INTEGER, tienDien REAL, tienNuoc REAL, tongTien REAL, trangThai TEXT, FOREIGN KEY (maPhong) REFERENCES PhongTro(maPhong));");

        // 7. Bảng Chi Tiết Dịch Vụ Hóa Đơn (Mới)
        db.execSQL("CREATE TABLE HoaDonChiTietDichVu (maHoaDon INTEGER, maDichVu INTEGER, tenDichVu TEXT, donGia REAL, PRIMARY KEY(maHoaDon, maDichVu));");

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
                    "'')");
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

        // 4. HỢP ĐỒNG MẪU
        for (int i = 1; i <= 10; i++) {
            db.execSQL("INSERT INTO HopDong (soHd, maPhong, maKhach, ngayBatDau, ngayKetThuc, tienCoc, trangThai, dieuKhoan, nguoiLap) VALUES (" +
                    "'HD00" + i + "'," +
                    i + "," +
                    i + "," +
                    "'2025-01-01'," +
                    "'2025-12-31'," +
                    "2000000," +
                    "'Còn hiệu lực'," +
                    "'- Thanh toán đúng hạn.\n- Giữ gìn vệ sinh chung.\n- Không làm ồn sau 23h.'," +
                    "'Quản trị viên')");
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // CHIẾN LƯỢC CẬP NHẬT AN TOÀN:
        // Không sử dụng DROP TABLE nữa để tránh mất dữ liệu bạn đã nhập tay.
        
        if (oldVersion < 22) {
            // Ví dụ: Nếu sau này bạn muốn thêm một cột mới vào bảng PhongTro ở phiên bản 22
            // hãy viết lệnh ALTER TABLE ở đây thay vì xóa bảng.
            // db.execSQL("ALTER TABLE PhongTro ADD COLUMN moTaMoi TEXT");
        }
        
        // Ghi chú: Nếu bạn muốn thêm dữ liệu mẫu mới mà không ảnh hưởng dữ liệu cũ,
        // hãy viết các lệnh INSERT vào các khối if (oldVersion < ...) tương ứng.
    }

    // --- QUẢN LÝ TÀI KHOẢN ---
    public String layHoTen(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT hoTen FROM TaiKhoan WHERE tenDangNhap = ?", new String[]{user});
        String hoTen = "";
        if (cursor.moveToFirst()) {
            hoTen = cursor.getString(0);
        }
        cursor.close();
        return hoTen;
    }

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

    public boolean themKhachThue(String ten, String sdt, String cccd, String dc, String gt, String ns, String anhTruoc, String anhSau) {
        ContentValues v = new ContentValues();
        v.put("hoTen", ten); v.put("soDienThoai", sdt); v.put("cccd", cccd); v.put("diaChi", dc); v.put("gioiTinh", gt); v.put("ngaySinh", ns);
        v.put("anhMatTruoc", anhTruoc); v.put("anhMatSau", anhSau);
        return getWritableDatabase().insert("KhachThue", null, v) != -1;
    }

    public boolean suaKhachThue(int id, String ten, String sdt, String cccd, String dc, String gt, String ns, String anhTruoc, String anhSau) {
        ContentValues v = new ContentValues();
        v.put("hoTen", ten); v.put("soDienThoai", sdt); v.put("cccd", cccd); v.put("diaChi", dc); v.put("gioiTinh", gt); v.put("ngaySinh", ns);
        v.put("anhMatTruoc", anhTruoc); v.put("anhMatSau", anhSau);
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
        return getReadableDatabase().rawQuery(
                "SELECT h.*, p.tenPhong, k.soDienThoai " +
                        "FROM HoaDon h " +
                        "JOIN PhongTro p ON h.maPhong = p.maPhong " +
                        "LEFT JOIN HopDong hd ON p.maPhong = hd.maPhong AND hd.trangThai = 'Còn hiệu lực' " +
                        "LEFT JOIN KhachThue k ON hd.maKhach = k.maKhach " +
                        "ORDER BY h.maHoaDon DESC", null);
    }

    public boolean themHoaDon(int maPhong, String thang, double tPhong, int dCu, int dMoi, int nCu, int nMoi, double tDien, double tNuoc, double tong, String tt, List<DichVu> dsDichVu) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues v = new ContentValues();
            v.put("maPhong", maPhong); v.put("thang", thang); v.put("tienPhong", tPhong);
            v.put("chiSoDienCu", dCu); v.put("chiSoDienMoi", dMoi);
            v.put("chiSoNuocCu", nCu); v.put("chiSoNuocMoi", nMoi);
            v.put("tienDien", tDien); v.put("tienNuoc", tNuoc);
            v.put("tongTien", tong); v.put("trangThai", tt);
            long idHd = db.insert("HoaDon", null, v);

            if (idHd != -1) {
                for (DichVu dv : dsDichVu) {
                    ContentValues vd = new ContentValues();
                    vd.put("maHoaDon", idHd);
                    vd.put("maDichVu", dv.getMaDichVu());
                    vd.put("tenDichVu", dv.getTenDichVu());
                    vd.put("donGia", dv.getDonGia());
                    db.insert("HoaDonChiTietDichVu", null, vd);
                }
                db.setTransactionSuccessful();
                return true;
            }
        } finally {
            db.endTransaction();
        }
        return false;
    }

    public Cursor layDichVuCuaHoaDon(int maHd) {
        return getReadableDatabase().rawQuery("SELECT * FROM HoaDonChiTietDichVu WHERE maHoaDon = ?", new String[]{String.valueOf(maHd)});
    }

    public boolean capNhatTrangThaiHoaDon(int id, String trangThai) {
        ContentValues v = new ContentValues();
        v.put("trangThai", trangThai);
        return getWritableDatabase().update("HoaDon", v, "maHoaDon = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean xoaHoaDon(int id) {
        return getWritableDatabase().delete("HoaDon", "maHoaDon = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public double layDonGiaDichVu(String ten) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT donGia FROM DichVu WHERE tenDichVu LIKE ?", new String[]{"%" + ten + "%"});
        double gia = 0;
        if (c.moveToFirst()) gia = c.getDouble(0);
        c.close();
        return gia;
    }

    public int layChiSoDienCu(int maPhong) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT chiSoDienMoi FROM HoaDon WHERE maPhong = ? ORDER BY maHoaDon DESC LIMIT 1", new String[]{String.valueOf(maPhong)});
        int so = 0;
        if (c.moveToFirst()) so = c.getInt(0);
        c.close();
        return so;
    }

    public int layChiSoNuocCu(int maPhong) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT chiSoNuocMoi FROM HoaDon WHERE maPhong = ? ORDER BY maHoaDon DESC LIMIT 1", new String[]{String.valueOf(maPhong)});
        int so = 0;
        if (c.moveToFirst()) so = c.getInt(0);
        c.close();
        return so;
    }

    public Cursor layPhongDaThueSpinner() {
        return getReadableDatabase().rawQuery("SELECT maPhong, tenPhong, giaThue FROM PhongTro WHERE trangThai = 'Đã thuê'", null);
    }

    // --- THỐNG KÊ ---
    public double layDoanhThuThang(String thang) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(tongTien) FROM HoaDon WHERE thang = ? AND trangThai = 'Đã thanh toán'", new String[]{thang});
        double tong = 0;
        if (c.moveToFirst()) tong = c.getDouble(0);
        c.close();
        return tong;
    }

    public Cursor layHoaDonNo() {
        return getReadableDatabase().rawQuery(
                "SELECT h.*, p.tenPhong FROM HoaDon h JOIN PhongTro p ON h.maPhong = p.maPhong WHERE h.trangThai = 'Chưa thanh toán'", null);
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
    public boolean themHopDong(String soHd, int maPhong, int maKhach, String ngayBd, String ngayKt, double tienCoc, String trangThai, String dieuKhoan, String nguoiLap) {
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
            values.put("dieuKhoan", dieuKhoan);
            values.put("nguoiLap", nguoiLap);
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
    public boolean suaHopDong(int maHd, int maPhongMoi, int maPhongCu, int maKhach, String ngayBd, String ngayKt, double tienCoc, String trangThai, String dieuKhoan) {
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
            values.put("dieuKhoan", dieuKhoan);

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
