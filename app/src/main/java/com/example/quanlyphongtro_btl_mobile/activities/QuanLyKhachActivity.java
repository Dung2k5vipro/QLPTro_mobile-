package com.example.quanlyphongtro_btl_mobile.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

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

    private LinearLayout layoutFormKhach, layoutChonAnhCccd;
    private TextView txtTieuDeFormKhach, txtLabelAnhCccd;
    private ImageView imgDongFormKhach, imgFormAnhTruoc, imgFormAnhSau;
    private RelativeLayout btnChonAnhTruoc, btnChonAnhSau;
    private EditText edtFormHoTen, edtFormSdt, edtFormCccd, edtFormDiaChi, edtFormNgaySinh;
    private Spinner spFormGioiTinh;
    private Button btnFormXoaKhach, btnFormHuyKhach, btnFormLuuKhach;

    private DatabaseHelper dbHelper;
    private List<KhachThue> danhSachGocKhach;
    private List<KhachThue> danhSachHienThiKhach;
    private KhachThueAdapter adapterKhach;

    private boolean laHanhDongThemMoi = true;
    private KhachThue khachDangChonSua = null;

    private String duongDanAnhTruoc = "", duongDanAnhSau = "";
    private boolean isChonAnhTruoc = true;

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    // Cấp quyền đọc
                    getContentResolver().takePersistableUriPermission(selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    
                    if (isChonAnhTruoc) {
                        duongDanAnhTruoc = selectedImageUri.toString();
                        imgFormAnhTruoc.setImageURI(selectedImageUri);
                    } else {
                        duongDanAnhSau = selectedImageUri.toString();
                        imgFormAnhSau.setImageURI(selectedImageUri);
                    }
                }
            }
    );

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

        edtFormNgaySinh.setOnClickListener(v -> hienThiHopThoaiChonNgay());

        btnThemKhachNhanh.setOnClickListener(v -> {
            laHanhDongThemMoi = true;
            khachDangChonSua = null;
            txtTieuDeFormKhach.setText("Thêm khách thuê mới");
            btnFormLuuKhach.setText("Thêm mới");
            btnFormXoaKhach.setVisibility(View.GONE);
            txtLabelAnhCccd.setVisibility(View.VISIBLE);
            layoutChonAnhCccd.setVisibility(View.VISIBLE);
            lamTrongForm();
            layoutFormKhach.setVisibility(View.VISIBLE);
        });

        btnChonAnhTruoc.setOnClickListener(v -> {
            isChonAnhTruoc = true;
            moBoSuuTap();
        });
        btnChonAnhSau.setOnClickListener(v -> {
            isChonAnhTruoc = false;
            moBoSuuTap();
        });

        imgDongFormKhach.setOnClickListener(v -> layoutFormKhach.setVisibility(View.GONE));
        btnFormHuyKhach.setOnClickListener(v -> layoutFormKhach.setVisibility(View.GONE));
        btnFormLuuKhach.setOnClickListener(v -> xuLyLuuDuLieuKhach());
        btnFormXoaKhach.setOnClickListener(v -> xuLyXoaKhachThue());

        // SỰ KIỆN NHẤN VÀO ITEM ĐỂ CHỈNH SỬA
        lvKhachThue.setOnItemClickListener((parent, view, position, id) -> {
            hienThiFormSua(danhSachHienThiKhach.get(position));
        });

        edtTimKiemKhach.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                xuLyTimKiemKhachThue(s.toString());
            }
        });
    }

    private void moBoSuuTap() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    private void lamTrongForm() {
        edtFormHoTen.setText("");
        edtFormSdt.setText("");
        edtFormCccd.setText("");
        edtFormDiaChi.setText("");
        edtFormNgaySinh.setText("");
        spFormGioiTinh.setSelection(0);
        duongDanAnhTruoc = "";
        duongDanAnhSau = "";
        imgFormAnhTruoc.setImageResource(android.R.drawable.ic_menu_camera);
        imgFormAnhSau.setImageResource(android.R.drawable.ic_menu_camera);
    }

    private void hienThiChiTietKhach(KhachThue khach) {
        laHanhDongThemMoi = false;
        khachDangChonSua = khach;
        txtTieuDeFormKhach.setText("Chi tiết khách thuê");
        btnFormLuuKhach.setText("Cập nhật");
        btnFormXoaKhach.setVisibility(View.VISIBLE);
        txtLabelAnhCccd.setVisibility(View.VISIBLE);
        layoutChonAnhCccd.setVisibility(View.VISIBLE);

        edtFormHoTen.setText(khach.getHoTen());
        edtFormSdt.setText(khach.getSoDienThoai());
        edtFormCccd.setText(khach.getCccd());
        edtFormDiaChi.setText(khach.getDiaChi());
        edtFormNgaySinh.setText(khach.getNgaySinh());
        spFormGioiTinh.setSelection(khach.getGioiTinh().equals("Nam") ? 0 : 1);

        duongDanAnhTruoc = khach.getAnhMatTruoc();
        duongDanAnhSau = khach.getAnhMatSau();

        if (duongDanAnhTruoc != null && !duongDanAnhTruoc.isEmpty()) {
            try {
                imgFormAnhTruoc.setImageURI(Uri.parse(duongDanAnhTruoc));
            } catch (Exception e) {
                imgFormAnhTruoc.setImageResource(android.R.drawable.ic_menu_camera);
            }
        } else {
            imgFormAnhTruoc.setImageResource(android.R.drawable.ic_menu_camera);
        }

        if (duongDanAnhSau != null && !duongDanAnhSau.isEmpty()) {
            try {
                imgFormAnhSau.setImageURI(Uri.parse(duongDanAnhSau));
            } catch (Exception e) {
                imgFormAnhSau.setImageResource(android.R.drawable.ic_menu_camera);
            }
        } else {
            imgFormAnhSau.setImageResource(android.R.drawable.ic_menu_camera);
        }

        layoutFormKhach.setVisibility(View.VISIBLE);
    }

    private void hienThiFormSua(KhachThue khach) {
        laHanhDongThemMoi = false;
        khachDangChonSua = khach;
        txtTieuDeFormKhach.setText("Cập nhật khách thuê");
        btnFormLuuKhach.setText("Cập nhật");
        btnFormXoaKhach.setVisibility(View.VISIBLE);
        
        // Hiển thị phần ảnh CCCD để có thể sửa
        txtLabelAnhCccd.setVisibility(View.VISIBLE);
        layoutChonAnhCccd.setVisibility(View.VISIBLE);

        edtFormHoTen.setText(khach.getHoTen());
        edtFormSdt.setText(khach.getSoDienThoai());
        edtFormCccd.setText(khach.getCccd());
        edtFormDiaChi.setText(khach.getDiaChi());
        edtFormNgaySinh.setText(khach.getNgaySinh());
        spFormGioiTinh.setSelection(khach.getGioiTinh().equals("Nam") ? 0 : 1);

        duongDanAnhTruoc = khach.getAnhMatTruoc();
        duongDanAnhSau = khach.getAnhMatSau();

        // Nạp ảnh hiện tại vào ImageView
        if (duongDanAnhTruoc != null && !duongDanAnhTruoc.isEmpty()) {
            try {
                imgFormAnhTruoc.setImageURI(Uri.parse(duongDanAnhTruoc));
            } catch (Exception e) {
                imgFormAnhTruoc.setImageResource(android.R.drawable.ic_menu_camera);
            }
        } else {
            imgFormAnhTruoc.setImageResource(android.R.drawable.ic_menu_camera);
        }

        if (duongDanAnhSau != null && !duongDanAnhSau.isEmpty()) {
            try {
                imgFormAnhSau.setImageURI(Uri.parse(duongDanAnhSau));
            } catch (Exception e) {
                imgFormAnhSau.setImageResource(android.R.drawable.ic_menu_camera);
            }
        } else {
            imgFormAnhSau.setImageResource(android.R.drawable.ic_menu_camera);
        }

        layoutFormKhach.setVisibility(View.VISIBLE);
    }

    private void anhXaGiaoDienKhachThue() {
        edtTimKiemKhach = findViewById(R.id.edtTimKiemKhach);
        btnThemKhachNhanh = findViewById(R.id.btnThemKhachNhanh);
        lvKhachThue = findViewById(R.id.lvKhachThue);
        layoutFormKhach = findViewById(R.id.layoutFormKhach);
        txtTieuDeFormKhach = findViewById(R.id.txtTieuDeFormKhach);
        imgDongFormKhach = findViewById(R.id.imgDongFormKhach);
        imgFormAnhTruoc = findViewById(R.id.imgFormAnhTruoc);
        imgFormAnhSau = findViewById(R.id.imgFormAnhSau);
        btnChonAnhTruoc = findViewById(R.id.btnChonAnhTruoc);
        btnChonAnhSau = findViewById(R.id.btnChonAnhSau);
        edtFormHoTen = findViewById(R.id.edtFormHoTen);
        edtFormSdt = findViewById(R.id.edtFormSdt);
        edtFormCccd = findViewById(R.id.edtFormCccd);
        edtFormDiaChi = findViewById(R.id.edtFormDiaChi);
        edtFormNgaySinh = findViewById(R.id.edtFormNgaySinh);
        spFormGioiTinh = findViewById(R.id.spFormGioiTinh);
        layoutChonAnhCccd = findViewById(R.id.layoutChonAnhCccd);
        txtLabelAnhCccd = findViewById(R.id.txtLabelAnhCccd);
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
            int idxId = cursor.getColumnIndex("maKhach");
            int idxTen = cursor.getColumnIndex("hoTen");
            int idxSdt = cursor.getColumnIndex("soDienThoai");
            int idxCccd = cursor.getColumnIndex("cccd");
            int idxDc = cursor.getColumnIndex("diaChi");
            int idxGt = cursor.getColumnIndex("gioiTinh");
            int idxNs = cursor.getColumnIndex("ngaySinh");
            int idxAnhT = cursor.getColumnIndex("anhMatTruoc");
            int idxAnhS = cursor.getColumnIndex("anhMatSau");

            do {
                danhSachGocKhach.add(new KhachThue(
                        cursor.getInt(idxId),
                        cursor.getString(idxTen),
                        cursor.getString(idxSdt),
                        cursor.getString(idxCccd),
                        cursor.getString(idxDc),
                        cursor.getString(idxGt),
                        cursor.getString(idxNs),
                        cursor.getString(idxAnhT),
                        cursor.getString(idxAnhS)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        danhSachHienThiKhach.clear();
        danhSachHienThiKhach.addAll(danhSachGocKhach);

        adapterKhach = new KhachThueAdapter(this, danhSachHienThiKhach, this::hienThiFormSua);
        lvKhachThue.setAdapter(adapterKhach);
    }

    private void xuLyLuuDuLieuKhach() {
        String ten = edtFormHoTen.getText().toString().trim();
        String sdt = edtFormSdt.getText().toString().trim();
        String cccd = edtFormCccd.getText().toString().trim();
        String diaChi = edtFormDiaChi.getText().toString().trim();
        String gioiTinh = spFormGioiTinh.getSelectedItem().toString();
        String ngaySinh = edtFormNgaySinh.getText().toString().trim();

        if (ten.isEmpty() || sdt.isEmpty() || cccd.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập các thông tin cơ bản!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean check;
        if (laHanhDongThemMoi) {
            check = dbHelper.themKhachThue(ten, sdt, cccd, diaChi, gioiTinh, ngaySinh, duongDanAnhTruoc, duongDanAnhSau);
        } else {
            check = dbHelper.suaKhachThue(khachDangChonSua.getMaKhach(), ten, sdt, cccd, diaChi, gioiTinh, ngaySinh, duongDanAnhTruoc, duongDanAnhSau);
        }

        if (check) {
            layoutFormKhach.setVisibility(View.GONE);
            taiDuLieuKhachThue();
            Toast.makeText(this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Thao tác thất bại!", Toast.LENGTH_SHORT).show();
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
