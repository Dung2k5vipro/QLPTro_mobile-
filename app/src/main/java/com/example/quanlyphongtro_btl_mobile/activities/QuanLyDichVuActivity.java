package com.example.quanlyphongtro_btl_mobile.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlyphongtro_btl_mobile.R;
import com.example.quanlyphongtro_btl_mobile.adapters.DichVuAdapter;
import com.example.quanlyphongtro_btl_mobile.database.DatabaseHelper;
import com.example.quanlyphongtro_btl_mobile.models.DichVu;

import java.util.ArrayList;
import java.util.List;

public class QuanLyDichVuActivity extends BaseMenuActivity {

    // UI
    private EditText edtTimKiemDichVu;
    private LinearLayout btnThemDichVu;
    private ListView lvDichVu;

    private LinearLayout layoutFormDichVu;
    private ImageView imgDongForm;

    private EditText edtTen, edtGia, edtDonVi, edtGhiChu;
    private Button btnLuu, btnHuy, btnXoa;

    // DATA
    private DatabaseHelper dbHelper;
    private List<DichVu> danhSachGoc;
    private List<DichVu> danhSachHienThi;
    private DichVuAdapter adapter;

    // STATE
    private boolean laThemMoi = true;
    private DichVu dichVuDangChon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_dich_vu);

        khoiTaoMenuDieuHuong();

        dbHelper = new DatabaseHelper(this);

        danhSachGoc = new ArrayList<>();
        danhSachHienThi = new ArrayList<>();

        anhXa();

        // 🔥 CHECK NULL TRÁNH CRASH
        if (lvDichVu == null) {
            Toast.makeText(this, "Lỗi: lvDichVu không tồn tại trong XML", Toast.LENGTH_LONG).show();
            return;
        }

        taiDuLieu();
        xuLySuKien();
    }

    // ================= ÁNH XẠ =================
    private void anhXa() {

        edtTimKiemDichVu = findViewById(R.id.edtTimKiemDichVu);
        btnThemDichVu = findViewById(R.id.btnThemDichVu);
        lvDichVu = findViewById(R.id.lvDichVu);

        layoutFormDichVu = findViewById(R.id.layoutFormDichVu);
        imgDongForm = findViewById(R.id.imgDongFormDichVu);

        edtTen = findViewById(R.id.edtTenDichVu);
        edtGia = findViewById(R.id.edtDonGia);
        edtDonVi = findViewById(R.id.edtDonViTinh);
        edtGhiChu = findViewById(R.id.edtGhiChu);

        btnLuu = findViewById(R.id.btnFormLuuDichVu);
        btnHuy = findViewById(R.id.btnFormHuyDichVu);
        btnXoa = findViewById(R.id.btnFormXoaDichVu);
    }

    // ================= LOAD DATA =================
    private void taiDuLieu() {
        danhSachGoc.clear();
        danhSachHienThi.clear();

        android.database.Cursor cursor = dbHelper.layDanhSachDichVu();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                danhSachGoc.add(new DichVu(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            }
            cursor.close();
        }

        danhSachHienThi.addAll(danhSachGoc);

        if (adapter == null) {
            adapter = new DichVuAdapter(this, danhSachHienThi, this::moFormSua);
            lvDichVu.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    // ================= EVENT =================
    private void xuLySuKien() {

        btnThemDichVu.setOnClickListener(v -> moFormThem());

        imgDongForm.setOnClickListener(v -> dongForm());
        btnHuy.setOnClickListener(v -> dongForm());

        btnLuu.setOnClickListener(v -> {
            if (laThemMoi) themMoi();
            else capNhat();
        });

        btnXoa.setOnClickListener(v -> {
            if (dichVuDangChon != null) {
                if (dbHelper.xoaDichVu(dichVuDangChon.getMaDichVu())) {
                    taiDuLieu();
                    dongForm();
                    toast("Đã xoá dịch vụ");
                } else {
                    toast("Lỗi khi xoá");
                }
            }
        });

        edtTimKiemDichVu.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                timKiem(s.toString());
            }
        });
    }

    // ================= THÊM =================
    private void themMoi() {
        if (!validate()) return;

        String ten = edtTen.getText().toString();
        double gia = parseDouble(edtGia.getText().toString());
        String donVi = edtDonVi.getText().toString();
        String ghiChu = edtGhiChu.getText().toString();

        if (dbHelper.themDichVu(ten, gia, donVi, ghiChu)) {
            taiDuLieu();
            dongForm();
            toast("Thêm thành công");
        } else {
            toast("Thêm thất bại");
        }
    }

    // ================= SỬA =================
    private void capNhat() {
        if (dichVuDangChon == null) return;
        if (!validate()) return;

        int id = dichVuDangChon.getMaDichVu();
        String ten = edtTen.getText().toString();
        double gia = parseDouble(edtGia.getText().toString());
        String donVi = edtDonVi.getText().toString();
        String ghiChu = edtGhiChu.getText().toString();

        if (dbHelper.suaDichVu(id, ten, gia, donVi, ghiChu)) {
            taiDuLieu();
            dongForm();
            toast("Cập nhật thành công");
        } else {
            toast("Cập nhật thất bại");
        }
    }

    // ================= FORM =================
    private void moFormThem() {
        laThemMoi = true;
        dichVuDangChon = null;

        btnXoa.setVisibility(View.GONE);
        clearForm();
        layoutFormDichVu.setVisibility(View.VISIBLE);
    }

    private void moFormSua(DichVu dv) {
        laThemMoi = false;
        dichVuDangChon = dv;

        edtTen.setText(dv.getTenDichVu());
        edtGia.setText(String.valueOf(dv.getDonGia()));
        edtDonVi.setText(dv.getDonViTinh());
        edtGhiChu.setText(dv.getGhiChu());

        btnXoa.setVisibility(View.VISIBLE);
        layoutFormDichVu.setVisibility(View.VISIBLE);
    }

    private void dongForm() {
        layoutFormDichVu.setVisibility(View.GONE);
        clearForm();
        dichVuDangChon = null;
        laThemMoi = true;
    }

    // ================= SEARCH =================
    private void timKiem(String key) {
        danhSachHienThi.clear();
        String k = loaiBoDau(key);

        for (DichVu dv : danhSachGoc) {
            String tenKhongDau = loaiBoDau(dv.getTenDichVu());
            String dvTinhKhongDau = loaiBoDau(dv.getDonViTinh());

            if (tenKhongDau.contains(k) || dvTinhKhongDau.contains(k)) {
                danhSachHienThi.add(dv);
            }
        }
        adapter.notifyDataSetChanged();
    }

    // ================= UTILS =================
    private void clearForm() {
        edtTen.setText("");
        edtGia.setText("");
        edtDonVi.setText("");
        edtGhiChu.setText("");
    }

    private boolean validate() {

        if (TextUtils.isEmpty(edtTen.getText())) {
            toast("Nhập tên dịch vụ");
            return false;
        }

        if (TextUtils.isEmpty(edtGia.getText())) {
            toast("Nhập đơn giá");
            return false;
        }

        return true;
    }

    private double parseDouble(String s) {
        try { return Double.parseDouble(s); }
        catch (Exception e) { return 0; }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}