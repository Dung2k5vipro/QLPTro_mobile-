package com.example.quanlyphongtro_btl_mobile.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.quanlyphongtro_btl_mobile.R;
import com.example.quanlyphongtro_btl_mobile.adapters.PhongTroAdapter;
import com.example.quanlyphongtro_btl_mobile.database.DatabaseHelper;
import com.example.quanlyphongtro_btl_mobile.models.PhongTro;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class QuanLyPhongActivity extends BaseMenuActivity {

    private EditText edtTimKiem;
    private TextView txtTongSoPhong;
    private Button btnLocLocTatCa, btnLocTrong, btnLocDaThue;
    private ListView lvPhongTro;
    private FloatingActionButton fabThemPhong;

    // Ánh xạ Form đa năng tích hợp chung
    private LinearLayout layoutFormChucNang;
    private TextView txtTieuDeForm;
    private EditText edtFormTenPhong, edtFormGiaThue, edtFormDienTich, edtFormGhiChu;
    private android.widget.Spinner spnFormTrangThai;
    private Button btnFormHuy, btnFormLuu, btnFormXoa;
    private ImageView btnFormDong;

    // THÊM: Biến xử lý chọn hình ảnh
    private ImageView imgFormPreview;
    private Button btnFormChonAnh;
    private String uriHinhAnhDuocChon = "";
    private ActivityResultLauncher<Intent> thuThapHinhAnh;

    private DatabaseHelper dbHelper;
    private List<PhongTro> danhSachGocPhongTro;
    private List<PhongTro> danhSachHienThiPhongTro;
    private PhongTroAdapter adapterPhongTro;

    // Biến trạng thái để phân biệt hành động
    private boolean laHanhDongThemMoi = true;
    private PhongTro phongDangChonSua = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_phong);

        khoiTaoMenuDieuHuong();

        dbHelper = new DatabaseHelper(this);
        danhSachGocPhongTro = new ArrayList<>();
        danhSachHienThiPhongTro = new ArrayList<>();

        anhXaGiaoDienChuyenBiet();

        // KHỞI TẠO BỘ LẮNG NGHE KẾT QUẢ TRẢ VỀ TỪ THƯ VIỆN ẢNH (GALLERY)
        thuThapHinhAnh = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            // Cấp quyền đọc vĩnh viễn để ảnh không bị mất khi reset app
                            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            uriHinhAnhDuocChon = uri.toString();
                            imgFormPreview.setImageURI(uri); // Hiển thị ảnh xem trước
                        }
                    }
                }
        );

        taiDuLieuPhongTro();

        // SỰ KIỆN: BẤM NÚT CHỌN ẢNH TỪ MÁY
        btnFormChonAnh.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            thuThapHinhAnh.launch(intent);
        });

        // 1. SỰ KIỆN KHI BẤM NÚT THÊM (+)
        fabThemPhong.setOnClickListener(v -> {
            laHanhDongThemMoi = true;
            phongDangChonSua = null;
            txtTieuDeForm.setText(R.string.title_them_phong_moi);
            btnFormXoa.setVisibility(View.GONE);

            // Xóa trống các ô nhập liệu
            edtFormTenPhong.setText("");
            spnFormTrangThai.setSelection(0); // Mặc định là Trống
            edtFormGiaThue.setText("");
            edtFormDienTich.setText("");
            edtFormGhiChu.setText("");
            uriHinhAnhDuocChon = "";
            imgFormPreview.setImageResource(android.R.drawable.ic_menu_gallery);

            layoutFormChucNang.setVisibility(View.VISIBLE);
        });

        // 2. SỰ KIỆN KHI NHẤN GIỮ ĐỂ SỬA / XÓA
        lvPhongTro.setOnItemLongClickListener((parent, view, position, id) -> {
            laHanhDongThemMoi = false;
            phongDangChonSua = danhSachHienThiPhongTro.get(position);

            txtTieuDeForm.setText("CHỈNH SỬA PHÒNG");
            btnFormXoa.setVisibility(View.VISIBLE);

            // Đổ dữ liệu cũ vào form
            edtFormTenPhong.setText(phongDangChonSua.getTenPhong());
            
            // Set Spinner trạng thái
            if (phongDangChonSua.getTrangThai().equals("Đã thuê")) {
                spnFormTrangThai.setSelection(1);
            } else {
                spnFormTrangThai.setSelection(0);
            }

            edtFormGiaThue.setText(String.valueOf((int) phongDangChonSua.getGiaThue()));
            edtFormDienTich.setText(String.valueOf(phongDangChonSua.getDienTich()));
            edtFormGhiChu.setText(phongDangChonSua.getGhiChu());

            // Load lại ảnh cũ (nếu có)
            uriHinhAnhDuocChon = phongDangChonSua.getHinhAnh();
            if (uriHinhAnhDuocChon != null && !uriHinhAnhDuocChon.isEmpty()) {
                try {
                    imgFormPreview.setImageURI(Uri.parse(uriHinhAnhDuocChon));
                } catch (Exception e) {
                    imgFormPreview.setImageResource(android.R.drawable.ic_menu_gallery);
                }
            } else {
                imgFormPreview.setImageResource(android.R.drawable.ic_menu_gallery);
            }

            layoutFormChucNang.setVisibility(View.VISIBLE);
            return true;
        });

        // 3. SỰ KIỆN NÚT HỦY / ĐÓNG FORM
        btnFormHuy.setOnClickListener(v -> dongFormChucNang());
        btnFormDong.setOnClickListener(v -> dongFormChucNang());

        // 4. SỰ KIỆN NÚT XÁC NHẬN LƯU (Xử lý thông minh rẽ nhánh Thêm hoặc Sửa)
        btnFormLuu.setOnClickListener(v -> xuLyNutXacNhanLuu());

        // 5. SỰ KIỆN NÚT XÓA PHÒNG
        btnFormXoa.setOnClickListener(v -> xuLyXoaPhongCucBo());

        // Tìm kiếm và bộ lọc nhanh
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { xuLyTimKiemPhongTro(s.toString()); }
            @Override public void afterTextChanged(Editable s) {}
        });
        btnLocLocTatCa.setOnClickListener(v -> xuLyLocPhongTro("Tất cả", btnLocLocTatCa));
        btnLocTrong.setOnClickListener(v -> xuLyLocPhongTro("Trống", btnLocTrong));
        btnLocDaThue.setOnClickListener(v -> xuLyLocPhongTro("Đã thuê", btnLocDaThue));
    }

    private void anhXaGiaoDienChuyenBiet() {
        edtTimKiem = findViewById(R.id.edtTimKiem);
        txtTongSoPhong = findViewById(R.id.txtTongSoPhong);
        btnLocLocTatCa = findViewById(R.id.btnLocTatCa);
        btnLocTrong = findViewById(R.id.btnLocTrong);
        btnLocDaThue = findViewById(R.id.btnLocDaThue);
        lvPhongTro = findViewById(R.id.lvPhongTro);
        fabThemPhong = findViewById(R.id.fabThemPhong);

        layoutFormChucNang = findViewById(R.id.layoutFormChucNang);
        txtTieuDeForm = findViewById(R.id.txtTieuDeForm);
        btnFormDong = findViewById(R.id.btnFormDong);
        edtFormTenPhong = findViewById(R.id.edtFormTenPhong);
        spnFormTrangThai = findViewById(R.id.spnFormTrangThai);
        edtFormGiaThue = findViewById(R.id.edtFormGiaThue);
        edtFormDienTich = findViewById(R.id.edtFormDienTich);
        edtFormGhiChu = findViewById(R.id.edtFormGhiChu);
        btnFormHuy = findViewById(R.id.btnFormHuy);
        btnFormLuu = findViewById(R.id.btnFormLuu);
        btnFormXoa = findViewById(R.id.btnFormXoa);

        // Thiết lập Spinner trạng thái
        String[] dsTrangThai = {"Trống", "Đã thuê"};
        android.widget.ArrayAdapter<String> adapterSpin = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dsTrangThai);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFormTrangThai.setAdapter(adapterSpin);

        // Ánh xạ thành phần hình ảnh
        imgFormPreview = findViewById(R.id.imgFormPreview);
        btnFormChonAnh = findViewById(R.id.btnFormChonAnh);
    }

    private void dongFormChucNang() {
        layoutFormChucNang.setVisibility(View.GONE);
        phongDangChonSua = null;
    }

    private void xuLyNutXacNhanLuu() {
        String ten = edtFormTenPhong.getText().toString().trim();
        String trangThai = spnFormTrangThai.getSelectedItem().toString();
        String giaStr = edtFormGiaThue.getText().toString().trim();
        String dienTichStr = edtFormDienTich.getText().toString().trim();
        String ghiChu = edtFormGhiChu.getText().toString().trim();

        if (ten.isEmpty() || giaStr.isEmpty() || dienTichStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        double gia = Double.parseDouble(giaStr);
        double dienTich = Double.parseDouble(dienTichStr);

        boolean ketQua;
        if (laHanhDongThemMoi) {
            // TRUYỀN THÊM BIẾN uriHinhAnhDuocChon VÀO HÀM
            ketQua = dbHelper.themPhongTro(ten, gia, dienTich, trangThai, ghiChu, uriHinhAnhDuocChon);
            if (ketQua) Toast.makeText(this, "Thêm phòng mới thành công!", Toast.LENGTH_SHORT).show();
        } else {
            if (phongDangChonSua == null) return;
            // TRUYỀN THÊM BIẾN uriHinhAnhDuocChon VÀO HÀM
            ketQua = dbHelper.suaPhongTro(phongDangChonSua.getMaPhong(), ten, gia, dienTich, trangThai, ghiChu, uriHinhAnhDuocChon);
            if (ketQua) Toast.makeText(this, "Cập nhật dữ liệu thành công!", Toast.LENGTH_SHORT).show();
        }

        if (ketQua) {
            dongFormChucNang();
            taiDuLieuPhongTro();
        } else {
            Toast.makeText(this, "Thao tác dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void xuLyXoaPhongCucBo() {
        if (phongDangChonSua == null) return;

        androidx.appcompat.app.AlertDialog.Builder thongBao = new androidx.appcompat.app.AlertDialog.Builder(this);
        thongBao.setTitle("Xác Nhận Xóa Phòng");
        thongBao.setMessage("Bạn chắc chắn muốn xóa phòng này khỏi CSDL?");
        thongBao.setPositiveButton("Xóa", (dialog, which) -> {
            boolean kiemTraXoa = dbHelper.xoaPhongTro(phongDangChonSua.getMaPhong());
            if (kiemTraXoa) {
                Toast.makeText(this, "Đã xóa phòng thành công!", Toast.LENGTH_SHORT).show();
                dongFormChucNang();
                taiDuLieuPhongTro();
            }
        });
        thongBao.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        thongBao.show();
    }

    private void taiDuLieuPhongTro() {
        danhSachGocPhongTro.clear();
        Cursor cursor = dbHelper.layDanhSachPhongTro();
        if (cursor != null && cursor.moveToFirst()) {
            int indexMa = cursor.getColumnIndex("maPhong");
            int indexTen = cursor.getColumnIndex("tenPhong");
            int indexGia = cursor.getColumnIndex("giaThue");
            int indexDT = cursor.getColumnIndex("dienTich");
            int indexTT = cursor.getColumnIndex("trangThai");
            int indexGC = cursor.getColumnIndex("ghiChu");
            int indexHA = cursor.getColumnIndex("hinhAnh");

            do {
                danhSachGocPhongTro.add(new PhongTro(
                        indexMa != -1 ? cursor.getInt(indexMa) : 0,
                        indexTen != -1 ? cursor.getString(indexTen) : "",
                        indexGia != -1 ? cursor.getDouble(indexGia) : 0,
                        indexDT != -1 ? cursor.getDouble(indexDT) : 0,
                        indexTT != -1 ? cursor.getString(indexTT) : "",
                        indexGC != -1 ? cursor.getString(indexGC) : "",
                        indexHA != -1 ? cursor.getString(indexHA) : ""
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        danhSachHienThiPhongTro.clear();
        danhSachHienThiPhongTro.addAll(danhSachGocPhongTro);
        txtTongSoPhong.setText(danhSachGocPhongTro.size() + " Phòng");
        adapterPhongTro = new PhongTroAdapter(QuanLyPhongActivity.this, danhSachHienThiPhongTro);
        lvPhongTro.setAdapter(adapterPhongTro);
    }

    private void xuLyTimKiemPhongTro(String tuKhoa) {
        danhSachHienThiPhongTro.clear();
        for (PhongTro phong : danhSachGocPhongTro) {
            if (phong.getTenPhong().toLowerCase().contains(tuKhoa.toLowerCase())) danhSachHienThiPhongTro.add(phong);
        }
        adapterPhongTro.notifyDataSetChanged();
    }

    private void xuLyLocPhongTro(String loaiTrangThai, Button nutDuocBam) {
        btnLocLocTatCa.setBackgroundColor(Color.parseColor("#E5E5E5"));
        btnLocLocTatCa.setTextColor(Color.parseColor("#333333"));
        btnLocTrong.setBackgroundColor(Color.parseColor("#E5E5E5"));
        btnLocTrong.setTextColor(Color.parseColor("#333333"));
        btnLocDaThue.setBackgroundColor(Color.parseColor("#E5E5E5"));
        btnLocDaThue.setTextColor(Color.parseColor("#333333"));

        nutDuocBam.setBackgroundColor(Color.parseColor("#2ECC71"));
        nutDuocBam.setTextColor(Color.WHITE);

        danhSachHienThiPhongTro.clear();
        if (loaiTrangThai.equals("Tất cả")) {
            danhSachHienThiPhongTro.addAll(danhSachGocPhongTro);
        } else {
            for (PhongTro phong : danhSachGocPhongTro) {
                if (phong.getTrangThai().equalsIgnoreCase(loaiTrangThai)) danhSachHienThiPhongTro.add(phong);
            }
        }
        adapterPhongTro.notifyDataSetChanged();
    }
}