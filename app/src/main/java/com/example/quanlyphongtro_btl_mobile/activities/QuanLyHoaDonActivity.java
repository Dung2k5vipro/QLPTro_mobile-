package com.example.quanlyphongtro_btl_mobile.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import com.example.quanlyphongtro_btl_mobile.R;
import com.example.quanlyphongtro_btl_mobile.adapters.HoaDonAdapter;
import com.example.quanlyphongtro_btl_mobile.database.DatabaseHelper;
import com.example.quanlyphongtro_btl_mobile.models.HoaDon;
import com.example.quanlyphongtro_btl_mobile.models.DichVu;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QuanLyHoaDonActivity extends BaseMenuActivity {

    private ListView lvHoaDon;
    private Button btnFilterTatCa, btnFilterNo;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fabThem;

    private LinearLayout layoutForm, layoutCheckboxes;
    private Spinner spnPhong;
    private EditText edtThang, edtDienMoi, edtNuocMoi;
    private TextView txtXemTruoc;
    private Button btnLuu, btnHuy;

    private DatabaseHelper dbHelper;
    private List<HoaDon> danhSachGoc = new ArrayList<>();
    private List<HoaDon> danhSachHienThi = new ArrayList<>();
    private HoaDonAdapter adapter;

    private List<Integer> listIdPhong = new ArrayList<>();
    private List<Double> listGiaPhong = new ArrayList<>();
    private List<DichVu> tatCaDichVu = new ArrayList<>();
    private List<CheckBox> listCheckBoxes = new ArrayList<>();
    
    private double giaDien = 0, giaNuoc = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_hoa_don);

        khoiTaoMenuDieuHuong();
        dbHelper = new DatabaseHelper(this);
        
        giaDien = dbHelper.layDonGiaDichVu("Điện");
        giaNuoc = dbHelper.layDonGiaDichVu("Nước");
        
        anhXa();
        taiDanhSachHoaDon();
        xuLySuKien();
    }

    private void anhXa() {
        lvHoaDon = findViewById(R.id.lvHoaDon);
        btnFilterTatCa = findViewById(R.id.btnFilterTatCa);
        btnFilterNo = findViewById(R.id.btnFilterNo);
        fabThem = findViewById(R.id.fabThemHoaDon);

        layoutForm = findViewById(R.id.layoutFormHoaDon);
        layoutCheckboxes = findViewById(R.id.layoutDichVuCheckboxes);
        spnPhong = findViewById(R.id.spnPhongHd);
        edtThang = findViewById(R.id.edtThangHd);
        edtDienMoi = findViewById(R.id.edtDienMoi);
        edtNuocMoi = findViewById(R.id.edtNuocMoi);
        txtXemTruoc = findViewById(R.id.txtXemTruocTong);
        btnLuu = findViewById(R.id.btnLuuHd);
        btnHuy = findViewById(R.id.btnHuyHd);
        findViewById(R.id.imgDongForm).setOnClickListener(v -> layoutForm.setVisibility(View.GONE));
    }

    private void taiDanhSachHoaDon() {
        danhSachGoc.clear();
        Cursor cursor = dbHelper.layDanhSachHoaDon();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HoaDon hd = new HoaDon(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getDouble(3), cursor.getInt(4), cursor.getInt(5),
                        cursor.getInt(6), cursor.getInt(7), cursor.getDouble(8),
                        cursor.getDouble(9), 0, 0, // internet và rác cũ không dùng
                        cursor.getDouble(12), cursor.getString(13)
                );
                hd.setTenPhong(cursor.getString(14));
                hd.setSoDienThoaiKhach(cursor.getString(15));
                danhSachGoc.add(hd);
            }
            cursor.close();
        }
        danhSachHienThi.clear();
        danhSachHienThi.addAll(danhSachGoc);
        
        adapter = new HoaDonAdapter(this, danhSachHienThi, new HoaDonAdapter.OnHoaDonActionListener() {
            @Override public void onThanhToan(HoaDon hd) { xuLyThanhToan(hd); }
            @Override public void onXoa(HoaDon hd) { xuLyXoa(hd); }
            @Override public void onShare(HoaDon hd) { xuLyShare(hd); }
        });
        lvHoaDon.setAdapter(adapter);
    }

    private void xuLySuKien() {
        fabThem.setOnClickListener(v -> moFormLapHoaDon());
        btnHuy.setOnClickListener(v -> layoutForm.setVisibility(View.GONE));
        btnLuu.setOnClickListener(v -> luuHoaDon());

        btnFilterTatCa.setOnClickListener(v -> {
            danhSachHienThi.clear();
            danhSachHienThi.addAll(danhSachGoc);
            adapter.notifyDataSetChanged();
            updateFilterButton(btnFilterTatCa);
        });

        btnFilterNo.setOnClickListener(v -> {
            danhSachHienThi.clear();
            for (HoaDon h : danhSachGoc) if (h.getTrangThai().equals("Chưa thanh toán")) danhSachHienThi.add(h);
            adapter.notifyDataSetChanged();
            updateFilterButton(btnFilterNo);
        });

        TextWatcher tw = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { tinhToanXemTruoc(); }
            @Override public void afterTextChanged(Editable s) {}
        };
        edtDienMoi.addTextChangedListener(tw);
        edtNuocMoi.addTextChangedListener(tw);
    }

    private void updateFilterButton(Button active) {
        btnFilterTatCa.setBackgroundColor(Color.parseColor("#E5E5E5"));
        btnFilterTatCa.setTextColor(Color.BLACK);
        btnFilterNo.setBackgroundColor(Color.parseColor("#E5E5E5"));
        btnFilterNo.setTextColor(Color.BLACK);
        active.setBackgroundColor(Color.parseColor("#2ECC71"));
        active.setTextColor(Color.WHITE);
    }

    private void moFormLapHoaDon() {
        listIdPhong.clear();
        listGiaPhong.clear();
        List<String> tenPhong = new ArrayList<>();
        Cursor c = dbHelper.layPhongDaThueSpinner();
        if (c != null) {
            while (c.moveToNext()) {
                listIdPhong.add(c.getInt(0));
                tenPhong.add(c.getString(1));
                listGiaPhong.add(c.getDouble(2));
            }
            c.close();
        }
        if (tenPhong.isEmpty()) {
            Toast.makeText(this, "Không có phòng nào đang được thuê!", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenPhong);
        spnPhong.setAdapter(ad);

        tatCaDichVu.clear();
        listCheckBoxes.clear();
        layoutCheckboxes.removeAllViews();
        Cursor cdv = dbHelper.layDanhSachDichVu();
        if (cdv != null) {
            while (cdv.moveToNext()) {
                String ten = cdv.getString(1);
                if (ten.equalsIgnoreCase("Điện") || ten.equalsIgnoreCase("Nước")) continue;

                DichVu dv = new DichVu(cdv.getInt(0), ten, cdv.getDouble(2), cdv.getString(3), cdv.getString(4));
                tatCaDichVu.add(dv);

                CheckBox cb = new CheckBox(this);
                cb.setText(ten + " (" + String.format("%,d", (int)dv.getDonGia()) + ")");
                cb.setOnCheckedChangeListener((buttonView, isChecked) -> tinhToanXemTruoc());
                layoutCheckboxes.addView(cb);
                listCheckBoxes.add(cb);
            }
            cdv.close();
        }

        Calendar cal = Calendar.getInstance();
        edtThang.setText(String.format("%02d/%d", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
        edtDienMoi.setText("");
        edtNuocMoi.setText("");
        layoutForm.setVisibility(View.VISIBLE);
    }

    private void tinhToanXemTruoc() {
        try {
            int maPhong = listIdPhong.get(spnPhong.getSelectedItemPosition());
            double tPhong = listGiaPhong.get(spnPhong.getSelectedItemPosition());
            int dCu = dbHelper.layChiSoDienCu(maPhong);
            int nCu = dbHelper.layChiSoNuocCu(maPhong);
            
            String dmStr = edtDienMoi.getText().toString();
            String nmStr = edtNuocMoi.getText().toString();
            int dMoi = dmStr.isEmpty() ? dCu : Integer.parseInt(dmStr);
            int nMoi = nmStr.isEmpty() ? nCu : Integer.parseInt(nmStr);

            double tDien = (dMoi - dCu) * giaDien;
            double tNuoc = (nMoi - nCu) * giaNuoc;

            double tDichVuKhac = 0;
            for (int i = 0; i < listCheckBoxes.size(); i++) {
                if (listCheckBoxes.get(i).isChecked()) {
                    tDichVuKhac += tatCaDichVu.get(i).getDonGia();
                }
            }

            double tong = tPhong + tDien + tNuoc + tDichVuKhac;
            txtXemTruoc.setText(String.format("Dự kiến tổng: %,d VNĐ\n(Điện cũ: %d, Nước cũ: %d)", (int)tong, dCu, nCu));
        } catch (Exception e) {
            txtXemTruoc.setText("Dữ liệu nhập chưa hợp lệ");
        }
    }

    private void luuHoaDon() {
        int maPhong = listIdPhong.get(spnPhong.getSelectedItemPosition());
        double tPhong = listGiaPhong.get(spnPhong.getSelectedItemPosition());
        String thang = edtThang.getText().toString().trim();
        int dCu = dbHelper.layChiSoDienCu(maPhong);
        int nCu = dbHelper.layChiSoNuocCu(maPhong);
        
        if (edtDienMoi.getText().toString().isEmpty() || edtNuocMoi.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập chỉ số mới!", Toast.LENGTH_SHORT).show();
            return;
        }
        int dMoi = Integer.parseInt(edtDienMoi.getText().toString());
        int nMoi = Integer.parseInt(edtNuocMoi.getText().toString());

        double tDien = (dMoi - dCu) * giaDien;
        double tNuoc = (nMoi - nCu) * giaNuoc;

        List<DichVu> dsDichVuChon = new ArrayList<>();
        double tDichVuKhac = 0;
        for (int i = 0; i < listCheckBoxes.size(); i++) {
            if (listCheckBoxes.get(i).isChecked()) {
                dsDichVuChon.add(tatCaDichVu.get(i));
                tDichVuKhac += tatCaDichVu.get(i).getDonGia();
            }
        }

        double tong = tPhong + tDien + tNuoc + tDichVuKhac;

        if (dbHelper.themHoaDon(maPhong, thang, tPhong, dCu, dMoi, nCu, nMoi, tDien, tNuoc, tong, "Chưa thanh toán", dsDichVuChon)) {
            Toast.makeText(this, "Lập hóa đơn thành công!", Toast.LENGTH_SHORT).show();
            layoutForm.setVisibility(View.GONE);
            taiDanhSachHoaDon();
        }
    }

    private void xuLyThanhToan(HoaDon hd) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận thanh toán")
                .setMessage("Khách đã đóng đủ " + String.format("%,d VNĐ", (int)hd.getTongTien()) + "?")
                .setPositiveButton("Xác nhận", (d, w) -> {
                    if (dbHelper.capNhatTrangThaiHoaDon(hd.getMaHoaDon(), "Đã thanh toán")) {
                        taiDanhSachHoaDon();
                    }
                }).setNegativeButton("Hủy", null).show();
    }

    private void xuLyXoa(HoaDon hd) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa hóa đơn")
                .setMessage("Bạn có chắc chắn muốn xóa hóa đơn này?")
                .setPositiveButton("Xóa", (d, w) -> {
                    if (dbHelper.xoaHoaDon(hd.getMaHoaDon())) taiDanhSachHoaDon();
                }).setNegativeButton("Hủy", null).show();
    }

    private void xuLyShare(HoaDon hd) {
        StringBuilder dvMsg = new StringBuilder();
        Cursor c = dbHelper.layDichVuCuaHoaDon(hd.getMaHoaDon());
        if (c != null) {
            while (c.moveToNext()) {
                dvMsg.append("- ").append(c.getString(c.getColumnIndexOrThrow("tenDichVu")))
                     .append(": ").append(String.format("%,d", (int)c.getDouble(c.getColumnIndexOrThrow("donGia"))))
                     .append("\n");
            }
            c.close();
        }

        String msg = "THÔNG BÁO TIỀN PHÒNG - Tháng " + hd.getThang() + "\n" +
                "Phòng: " + hd.getTenPhong() + "\n" +
                "- Tiền phòng: " + String.format("%,d", (int)hd.getTienPhong()) + "\n" +
                "- Tiền điện (" + hd.getChiSoDienCu() + " -> " + hd.getChiSoDienMoi() + "): " + String.format("%,d", (int)hd.getTienDien()) + "\n" +
                "- Tiền nước (" + hd.getChiSoNuocCu() + " -> " + hd.getChiSoNuocMoi() + "): " + String.format("%,d", (int)hd.getTienNuoc()) + "\n" +
                dvMsg.toString() +
                "==> TỔNG CỘNG: " + String.format("%,d VNĐ", (int)hd.getTongTien()) + "\n" +
                "Trạng thái: " + hd.getTrangThai();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Gửi hóa đơn cho khách"));
    }
}
