package com.example.quanlyphongtro_btl_mobile.activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlyphongtro_btl_mobile.R;
import com.example.quanlyphongtro_btl_mobile.adapters.KhachThueAdapter;
import com.example.quanlyphongtro_btl_mobile.database.DatabaseHelper;
import com.example.quanlyphongtro_btl_mobile.models.KhachThue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class QuanLyKhachActivity extends BaseMenuActivity {

    private EditText edtTimKiemKhach;
    private LinearLayout btnThemKhachNhanh;
    private ListView lvKhachThue;

    // Thành phần Form đa năng gộp chung theo ảnh mẫu
    private LinearLayout layoutFormKhach;
    private TextView txtTieuDeFormKhach;
    private ImageView imgDongFormKhach;
    private EditText edtFormHoTen, edtFormSdt, edtFormCccd, edtFormDiaChi, edtFormNgaySinh;
    private Spinner spFormGioiTinh;
    private Button btnFormXoaKhach, btnFormHuyKhach, btnFormLuuKhach;

    private DatabaseHelper dbHelper;
    private List<KhachThue> danhSachGocKhach;
    private List<KhachThue> danhSachHienThiKhach;
    private KhachThueAdapter adapterKhach;

    private boolean laHanhDongThemMoi = true;
    private KhachThue khachDangChonSua = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_khach);

        khoiTaoMenuDieuHuong();
        dbHelper = new DatabaseHelper(this);
        danhSachGocKhach = new ArrayList<>();
        danhSachHienThiKhach = new ArrayList<>();

        anhXaGiaoDienKhachThue();
        caiDatSpinnerGioiTinh();
        taiDuLieuKhachThue();

        // 1. SỰ KIỆN CLICK Ô NGÀY SINH -> BẬT LỊCH ĐIỆN THOẠI
        edtFormNgaySinh.setOnClickListener(v -> hienThiHopThoaiChonNgay());

        // 2. SỰ KIỆN BẤM NÚT "THÊM KHÁCH THUÊ" LỚN MÀU XANH
        btnThemKhachNhanh.setOnClickListener(v -> {
            laHanhDongThemMoi = true;
            khachDangChonSua = null;
            txtTieuDeFormKhach.setText("Thông tin khách thuê");
            btnFormLuuKhach.setText("Thêm mới");
            btnFormXoaKhach.setVisibility(View.GONE); // Đang thêm mới thì ẩn nút xóa

            // Xóa trống form nhập liệu
            edtFormHoTen.setText("");
            edtFormSdt.setText("");
            edtFormCccd.setText("");
            edtFormDiaChi.setText("");
            edtFormNgaySinh.setText("");
            spFormGioiTinh.setSelection(0);

            layoutFormKhach.setVisibility(View.VISIBLE);
        });

        // 3. SỰ KIỆN ĐÓNG FORM KHI BẤM NÚT (X) HOẶC NÚT HỦY
        imgDongFormKhach.setOnClickListener(v -> layoutFormKhach.setVisibility(View.GONE));
        btnFormHuyKhach.setOnClickListener(v -> layoutFormKhach.setVisibility(View.GONE));

        // 4. SỰ KIỆN NÚT LƯU (THÊM HOẶC CẬP NHẬT)
        btnFormLuuKhach.setOnClickListener(v -> xuLyLuuDuLieuKhach());

        // 5. SỰ KIỆN NÚT XÓA KHÁCH THUÊ
        btnFormXoaKhach.setOnClickListener(v -> xuLyXoaKhachThue());

        // 6. THANH TÌM KIẾM THEO TÊN, SĐT, CCCD ĐA NĂNG
        edtTimKiemKhach.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                // Chỉ xử lý tìm kiếm khi nội dung thực sự thay đổi và không rỗng 
                // để tránh gây lag cho bộ gõ tiếng Việt
                String query = s.toString();
                xuLyTimKiemKhachThue(query);
            }
        });
    }

    private void anhXaGiaoDienKhachThue() {
        edtTimKiemKhach = findViewById(R.id.edtTimKiemKhach);
        btnThemKhachNhanh = findViewById(R.id.btnThemKhachNhanh);
        lvKhachThue = findViewById(R.id.lvKhachThue);

        layoutFormKhach = findViewById(R.id.layoutFormKhach);
        txtTieuDeFormKhach = findViewById(R.id.txtTieuDeFormKhach);
        imgDongFormKhach = findViewById(R.id.imgDongFormKhach);
        edtFormHoTen = findViewById(R.id.edtFormHoTen);
        edtFormSdt = findViewById(R.id.edtFormSdt);
        edtFormCccd = findViewById(R.id.edtFormCccd);
        edtFormDiaChi = findViewById(R.id.edtFormDiaChi);
        edtFormNgaySinh = findViewById(R.id.edtFormNgaySinh);
        spFormGioiTinh = findViewById(R.id.spFormGioiTinh);
        btnFormXoaKhach = findViewById(R.id.btnFormXoaKhach);
        btnFormHuyKhach = findViewById(R.id.btnFormHuyKhach);
        btnFormLuuKhach = findViewById(R.id.btnFormLuuKhach);
    }

    private void caiDatSpinnerGioiTinh() {
        String[] mangGioiTinh = {"Nam", "Nữ"};
        ArrayAdapter<String> adapterSpin = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mangGioiTinh);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFormGioiTinh.setAdapter(adapterSpin);
    }

    private void hienThiHopThoaiChonNgay() {
        Calendar lich = Calendar.getInstance();
        int nam = lich.get(Calendar.YEAR);
        int thang = lich.get(Calendar.MONTH);
        int ngay = lich.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog hopThoaiLich = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String ngayFormat = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (month + 1), year);
            edtFormNgaySinh.setText(ngayFormat);
        }, nam, thang, ngay);
        hopThoaiLich.show();
    }

    private void taiDuLieuKhachThue() {
        danhSachGocKhach.clear();
        Cursor cursor = dbHelper.layDanhSachKhachThue();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                danhSachGocKhach.add(new KhachThue(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        danhSachHienThiKhach.clear();
        danhSachHienThiKhach.addAll(danhSachGocKhach);

        // Khởi tạo Adapter đồng thời lắng nghe sự kiện bấm vào nút sửa (Cây bút)
        adapterKhach = new KhachThueAdapter(this, danhSachHienThiKhach, khachThue -> {
            laHanhDongThemMoi = false;
            khachDangChonSua = khachThue;

            txtTieuDeFormKhach.setText("Thông tin khách thuê");
            btnFormLuuKhach.setText("Cập nhật");
            btnFormXoaKhach.setVisibility(View.VISIBLE); // Hiện nút xóa khi sửa

            // Đổ dữ liệu cũ lên ô nhập
            edtFormHoTen.setText(khachThue.getHoTen());
            edtFormSdt.setText(khachThue.getSoDienThoai());
            edtFormCccd.setText(khachThue.getCccd());
            edtFormDiaChi.setText(khachThue.getDiaChi());
            edtFormNgaySinh.setText(khachThue.getNgaySinh());
            if (khachThue.getGioiTinh().equals("Nam")) spFormGioiTinh.setSelection(0);
            else spFormGioiTinh.setSelection(1);

            layoutFormKhach.setVisibility(View.VISIBLE);
        });
        lvKhachThue.setAdapter(adapterKhach);
    }

    private void xuLyLuuDuLieuKhach() {
        String ten = edtFormHoTen.getText().toString().trim();
        String sdt = edtFormSdt.getText().toString().trim();
        String cccd = edtFormCccd.getText().toString().trim();
        String diaChi = edtFormDiaChi.getText().toString().trim();
        String gioiTinh = spFormGioiTinh.getSelectedItem().toString();
        String ngaySinh = edtFormNgaySinh.getText().toString().trim();

        if (ten.isEmpty() || sdt.isEmpty() || cccd.isEmpty() || diaChi.isEmpty() || ngaySinh.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đủ thông tin khách!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean check;
        if (laHanhDongThemMoi) {
            check = dbHelper.themKhachThue(ten, sdt, cccd, diaChi, gioiTinh, ngaySinh);
            if (check) Toast.makeText(this, "Đã thêm khách thuê thành công!", Toast.LENGTH_SHORT).show();
        } else {
            if (khachDangChonSua == null) return;
            check = dbHelper.suaKhachThue(khachDangChonSua.getMaKhach(), ten, sdt, cccd, diaChi, gioiTinh, ngaySinh);
            if (check) Toast.makeText(this, "Đã cập nhật thông tin khách thuê!", Toast.LENGTH_SHORT).show();
        }

        if (check) {
            layoutFormKhach.setVisibility(View.GONE);
            taiDuLieuKhachThue();
        } else {
            Toast.makeText(this, "Thao tác cơ sở dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void xuLyXoaKhachThue() {
        if (khachDangChonSua == null) return;

        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(this);
        alert.setTitle("Xác Nhận Xóa Khách Thuê");
        alert.setMessage("Bạn có chắc chắn muốn xóa khách hàng này khỏi hệ thống quản lý?");
        alert.setPositiveButton("Xóa", (dialog, which) -> {
            boolean check = dbHelper.xoaKhachThue(khachDangChonSua.getMaKhach());
            if (check) {
                Toast.makeText(this, "Đã xóa khách thuê thành công!", Toast.LENGTH_SHORT).show();
                layoutFormKhach.setVisibility(View.GONE);
                taiDuLieuKhachThue();
            }
        });
        alert.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        alert.show();
    }

    private void xuLyTimKiemKhachThue(String tuKhoa) {
        danhSachHienThiKhach.clear();
        String tk = tuKhoa.toLowerCase();
        for (KhachThue k : danhSachGocKhach) {
            if (k.getHoTen().toLowerCase().contains(tk) ||
                    k.getSoDienThoai().contains(tk) ||
                    k.getCccd().contains(tk)) {
                danhSachHienThiKhach.add(k);
            }
        }
        adapterKhach.notifyDataSetChanged();
    }
}
