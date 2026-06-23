package com.example.quanlyphongtro_btl_mobile.activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.quanlyphongtro_btl_mobile.adapters.HopDongAdapter;
import com.example.quanlyphongtro_btl_mobile.database.DatabaseHelper;
import com.example.quanlyphongtro_btl_mobile.models.HopDong;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QuanLyHopDongActivity extends BaseMenuActivity {

    private Button btnFilterTatCa, btnFilterConHieuLuc, btnFilterHetHan;
    private ListView lvHopDong;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fabThemHd;

    // Thành phần điều khiển Form
    private LinearLayout layoutFormHd;
    private TextView txtTieuDeFormHd;
    private ImageView imgDongFormHd;
    private Spinner spFormKhachHd, spFormPhongHd, spFormTrangThaiHd;
    private EditText edtFormNgayBatDau, edtFormNgayKetThuc, edtFormTienCoc, edtFormDieuKhoan;
    private Button btnFormXoaHd, btnFormHuyHd, btnFormLuuHd, btnFormInHd;

    private DatabaseHelper dbHelper;
    private List<HopDong> danhSachGocHd;
    private List<HopDong> danhSachHienThiHd;
    private HopDongAdapter adapterHd;

    private List<Integer> listIdKhach = new ArrayList<>();
    private List<Integer> listIdPhong = new ArrayList<>();
    private boolean laHanhDongThemMoi = true;
    private HopDong hdDangChonSua = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_hop_dong);

        khoiTaoMenuDieuHuong();
        dbHelper = new DatabaseHelper(this);
        danhSachGocHd = new ArrayList<>();
        danhSachHienThiHd = new ArrayList<>();
        listIdKhach = new ArrayList<>();
        listIdPhong = new ArrayList<>();

        anhXaGiaoDienHd();
        caiDatSpinnerTrangThai();
        napDuLieuSpinner(false); // Nạp danh sách khách và phòng trước
        taiDanhSachHopDong();

        // Cài đặt sự kiện chọn ngày tiện lợi
        edtFormNgayBatDau.setOnClickListener(v -> hienThiLich(edtFormNgayBatDau));
        edtFormNgayKetThuc.setOnClickListener(v -> hienThiLich(edtFormNgayKetThuc));

        // SỰ KIỆN NÚT THÊM MỚI (+)
        fabThemHd.setOnClickListener(v -> {
            laHanhDongThemMoi = true;
            hdDangChonSua = null;
            txtTieuDeFormHd.setText("Chi tiết hợp đồng");
            btnFormXoaHd.setVisibility(View.GONE);
            btnFormInHd.setVisibility(View.GONE);

            edtFormNgayBatDau.setText("");
            edtFormNgayKetThuc.setText("");
            edtFormTienCoc.setText("");
            edtFormDieuKhoan.setText("- Bên A cam kết bàn giao phòng đúng hạn.\n- Bên B cam kết đóng tiền đúng ngày 05 hàng tháng.\n- Không làm ồn sau 23h.\n- Giữ gìn vệ sinh chung.");

            napDuLieuSpinner(true); // Chỉ nạp các phòng trạng thái "Trống"
            layoutFormHd.setVisibility(View.VISIBLE);
        });

        imgDongFormHd.setOnClickListener(v -> layoutFormHd.setVisibility(View.GONE));
        btnFormHuyHd.setOnClickListener(v -> layoutFormHd.setVisibility(View.GONE));
        btnFormXoaHd.setOnClickListener(v -> xuLyXoaHdCucBo());
        btnFormLuuHd.setOnClickListener(v -> xuLyLuuHdCucBo());
        btnFormInHd.setOnClickListener(v -> {
            if (hdDangChonSua != null) xuLyInHopDong(hdDangChonSua);
        });

        // Bộ lọc nhanh danh sách
        btnFilterTatCa.setOnClickListener(v -> locHd("Tất cả", btnFilterTatCa));
        btnFilterConHieuLuc.setOnClickListener(v -> locHd("Còn hiệu lực", btnFilterConHieuLuc));
        btnFilterHetHan.setOnClickListener(v -> locHd("Hết hạn", btnFilterHetHan));

        // SỰ KIỆN CLICK VÀO ITEM ĐỂ SỬA
        lvHopDong.setOnItemClickListener((parent, view, position, id) -> {
            if (adapterHd != null) {
                HopDong hd = danhSachHienThiHd.get(position);
                moFormSuaHopDong(hd);
            }
        });
    }

    private void moFormSuaHopDong(HopDong hd) {
        laHanhDongThemMoi = false;
        hdDangChonSua = hd;
        txtTieuDeFormHd.setText("Chi tiết hợp đồng");
        btnFormXoaHd.setVisibility(View.VISIBLE);
        btnFormInHd.setVisibility(View.VISIBLE);

        napDuLieuSpinner(false); // Khi sửa phải nạp tất cả phòng để hiển thị phòng đang thuê hiện tại

        edtFormNgayBatDau.setText(hd.getNgayBatDau());
        edtFormNgayKetThuc.setText(hd.getNgayKetThuc());
        edtFormTienCoc.setText(String.valueOf((int) hd.getTienCoc()));
        edtFormDieuKhoan.setText(hd.getDieuKhoan());

        // Trỏ Spinner về đúng Khách và Phòng cũ
        int idxKhach = listIdKhach.indexOf(hd.getMaKhach());
        if (idxKhach != -1) spFormKhachHd.setSelection(idxKhach);
        int idxPhong = listIdPhong.indexOf(hd.getMaPhong());
        if (idxPhong != -1) spFormPhongHd.setSelection(idxPhong);

        if (hd.getTrangThai().equals("Còn hiệu lực")) spFormTrangThaiHd.setSelection(0);
        else spFormTrangThaiHd.setSelection(1);

        layoutFormHd.setVisibility(View.VISIBLE);
    }

    private void anhXaGiaoDienHd() {
        btnFilterTatCa = findViewById(R.id.btnFilterTatCa);
        btnFilterConHieuLuc = findViewById(R.id.btnFilterConHieuLuc);
        btnFilterHetHan = findViewById(R.id.btnFilterHetHan);
        lvHopDong = findViewById(R.id.lvHopDong);
        fabThemHd = findViewById(R.id.fabThemHd);

        layoutFormHd = findViewById(R.id.layoutFormHd);
        txtTieuDeFormHd = findViewById(R.id.txtTieuDeFormHd);
        imgDongFormHd = findViewById(R.id.imgDongFormHd);
        spFormKhachHd = findViewById(R.id.spFormKhachHd);
        spFormPhongHd = findViewById(R.id.spFormPhongHd);
        spFormTrangThaiHd = findViewById(R.id.spFormTrangThaiHd);
        edtFormNgayBatDau = findViewById(R.id.edtFormNgayBatDau);
        edtFormNgayKetThuc = findViewById(R.id.edtFormNgayKetThuc);
        edtFormTienCoc = findViewById(R.id.edtFormTienCoc);
        edtFormDieuKhoan = findViewById(R.id.edtFormDieuKhoan);
        btnFormXoaHd = findViewById(R.id.btnFormXoaHd);
        btnFormHuyHd = findViewById(R.id.btnFormHuyHd);
        btnFormLuuHd = findViewById(R.id.btnFormLuuHd);
        btnFormInHd = findViewById(R.id.btnFormInHd);
    }

    private void caiDatSpinnerTrangThai() {
        String[] arrStt = {"Còn hiệu lực", "Hết hạn"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrStt);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFormTrangThaiHd.setAdapter(adapter);
    }

    private void hienThiLich(EditText edt) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            edt.setText(String.format("%02d/%02d/%d", dayOfMonth, (month + 1), year));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void napDuLieuSpinner(boolean chiLayPhongTrong) {
        // 1. Nạp danh sách khách thuê
        listIdKhach.clear();
        List<String> listTenKhach = new ArrayList<>();
        Cursor cKhach = dbHelper.layDanhSachKhachThue();
        if (cKhach != null) {
            while (cKhach.moveToNext()) {
                listIdKhach.add(cKhach.getInt(0));
                listTenKhach.add(cKhach.getString(1));
            }
            cKhach.close();
        }
        ArrayAdapter<String> adKhach = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listTenKhach);
        adKhach.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFormKhachHd.setAdapter(adKhach);

        // 2. Nạp danh sách phòng (Áp dụng logic ẩn phòng đã thuê khi tạo mới)
        listIdPhong.clear();
        List<String> listTenPhong = new ArrayList<>();
        Cursor cPhong = chiLayPhongTrong ? dbHelper.layPhongTrongSpinner() : dbHelper.layTatCaPhongSpinner();
        if (cPhong != null) {
            while (cPhong.moveToNext()) {
                listIdPhong.add(cPhong.getInt(0));
                listTenPhong.add(cPhong.getString(1));
            }
            cPhong.close();
        }
        ArrayAdapter<String> adPhong = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listTenPhong);
        adPhong.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFormPhongHd.setAdapter(adPhong);
    }

    private void taiDanhSachHopDong() {
        danhSachGocHd.clear();
        Cursor cursor = dbHelper.layDanhSachHopDong();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                danhSachGocHd.add(new HopDong(
                        cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                        cursor.getInt(3), cursor.getString(4), cursor.getString(5),
                        cursor.getDouble(6), cursor.getString(7),
                        cursor.getString(8), cursor.getString(9), // dieuKhoan, nguoiLap
                        cursor.getString(10), cursor.getString(11) // tenPhong, tenKhach (vì JOIN lấy thêm)
                ));
            }
            cursor.close();
        }
        danhSachHienThiHd.clear();
        danhSachHienThiHd.addAll(danhSachGocHd);

        adapterHd = new HopDongAdapter(this, danhSachHienThiHd, new HopDongAdapter.OnHopDongActionListener() {
            @Override
            public void onSua(HopDong hd) {
                moFormSuaHopDong(hd);
            }

            @Override public void onXoa(HopDong hd) { hdDangChonSua = hd; xuLyXoaHdCucBo(); }

            @Override
            public void onIn(HopDong hd) {
                xuLyInHopDong(hd);
            }
        });
        lvHopDong.setAdapter(adapterHd);
    }



    private void xuLyLuuHdCucBo() {
        if (spFormKhachHd.getSelectedItem() == null || spFormPhongHd.getSelectedItem() == null) {
            Toast.makeText(this, "Thiếu thông tin khách hoặc phòng! Hãy tạo chúng trước.", Toast.LENGTH_SHORT).show();
            return;
        }
        int maKhach = listIdKhach.get(spFormKhachHd.getSelectedItemPosition());
        int maPhongMoi = listIdPhong.get(spFormPhongHd.getSelectedItemPosition());
        String ngayBd = edtFormNgayBatDau.getText().toString().trim();
        String ngayKt = edtFormNgayKetThuc.getText().toString().trim();
        String cocStr = edtFormTienCoc.getText().toString().trim();
        String trangThai = spFormTrangThaiHd.getSelectedItem().toString();
        String dieuKhoan = edtFormDieuKhoan.getText().toString().trim();

        if (ngayBd.isEmpty() || ngayKt.isEmpty() || cocStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ dữ liệu!", Toast.LENGTH_SHORT).show();
            return;
        }
        double tiềnCọc = Double.parseDouble(cocStr);

        boolean check;
        if (laHanhDongThemMoi) {
            String soHd = "HD-" + Calendar.getInstance().get(Calendar.YEAR) + "-" + String.format("%03d", (danhSachGocHd.size() + 1));
            String nguoiLap = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("hoTen", "Chưa xác định");
            check = dbHelper.themHopDong(soHd, maPhongMoi, maKhach, ngayBd, ngayKt, tiềnCọc, trangThai, dieuKhoan, nguoiLap);
        } else {
            if (hdDangChonSua == null) return;
            check = dbHelper.suaHopDong(hdDangChonSua.getMaHd(), maPhongMoi, hdDangChonSua.getMaPhong(), maKhach, ngayBd, ngayKt, tiềnCọc, trangThai, dieuKhoan);
        }

        if (check) {
            Toast.makeText(this, "Thao tác hợp đồng thành công!", Toast.LENGTH_SHORT).show();
            layoutFormHd.setVisibility(View.GONE);
            taiDanhSachHopDong();
        }
    }

    private void xuLyXoaHdCucBo() {
        if (hdDangChonSua == null) return;
        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(this);
        alert.setTitle("Xóa hợp đồng");
        alert.setMessage("Hệ thống sẽ xóa hợp đồng này và tự động trả phòng về trạng thái 'Trống'?");
        alert.setPositiveButton("Xóa", (dialog, which) -> {
            if (dbHelper.xoaHopDong(hdDangChonSua.getMaHd(), hdDangChonSua.getMaPhong())) {
                Toast.makeText(this, "Đã xóa hợp đồng thành công!", Toast.LENGTH_SHORT).show();
                layoutFormHd.setVisibility(View.GONE);
                taiDanhSachHopDong();
            }
        }).setNegativeButton("Hủy", null).show();
    }

    private void xuLyInHopDong(HopDong hd) {
        android.print.PrintManager printManager = (android.print.PrintManager) this.getSystemService(android.content.Context.PRINT_SERVICE);
        String jobName = "HopDong_" + hd.getSoHd();

        printManager.print(jobName, new android.print.PrintDocumentAdapter() {
            @Override
            public void onWrite(android.print.PageRange[] pages, android.os.ParcelFileDescriptor destination, android.os.CancellationSignal cancellationSignal, WriteResultCallback callback) {
                android.graphics.pdf.PdfDocument pdfDocument = new android.graphics.pdf.PdfDocument();
                android.graphics.pdf.PdfDocument.PageInfo pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size
                android.graphics.pdf.PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                android.graphics.Canvas canvas = page.getCanvas();
                android.graphics.Paint paint = new android.graphics.Paint();
                paint.setColor(android.graphics.Color.BLACK);

                int y = 50;
                paint.setTextSize(20);
                paint.setFakeBoldText(true);
                canvas.drawText("HỢP ĐỒNG THUÊ PHÒNG TRỌ", 150, y, paint);

                y += 40;
                paint.setTextSize(14);
                paint.setFakeBoldText(false);
                canvas.drawText("Số HĐ: " + hd.getSoHd(), 50, y, paint);
                y += 25;
                canvas.drawText("Phòng: " + hd.getTenPhong(), 50, y, paint);
                y += 25;
                canvas.drawText("Khách thuê: " + hd.getTenKhach(), 50, y, paint);
                y += 25;
                canvas.drawText("Ngày bắt đầu: " + hd.getNgayBatDau(), 50, y, paint);
                y += 25;
                canvas.drawText("Ngày kết thúc: " + hd.getNgayKetThuc(), 50, y, paint);
                y += 25;
                canvas.drawText("Tiền cọc: " + String.format("%,d VNĐ", (int)hd.getTienCoc()), 50, y, paint);

                y += 40;
                paint.setFakeBoldText(true);
                canvas.drawText("ĐIỀU KHOẢN HỢP ĐỒNG:", 50, y, paint);
                y += 25;
                paint.setFakeBoldText(false);
                
                // Vẽ điều khoản (hỗ trợ xuống dòng đơn giản)
                String[] lines = hd.getDieuKhoan().split("\n");
                for (String line : lines) {
                    canvas.drawText(line, 60, y, paint);
                    y += 20;
                }

                y += 50;
                canvas.drawText("Người lập hợp đồng (Ký tên)", 350, y, paint);
                y += 60;
                paint.setFakeBoldText(true);
                canvas.drawText(hd.getNguoiLap(), 350, y, paint);

                pdfDocument.finishPage(page);

                try {
                    pdfDocument.writeTo(new java.io.FileOutputStream(destination.getFileDescriptor()));
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                } finally {
                    pdfDocument.close();
                }
                callback.onWriteFinished(new android.print.PageRange[]{android.print.PageRange.ALL_PAGES});
            }

            @Override
            public void onLayout(android.print.PrintAttributes oldAttributes, android.print.PrintAttributes newAttributes, android.os.CancellationSignal cancellationSignal, LayoutResultCallback callback, android.os.Bundle extras) {
                if (cancellationSignal.isCanceled()) {
                    callback.onLayoutCancelled();
                    return;
                }
                android.print.PrintDocumentInfo pdi = new android.print.PrintDocumentInfo.Builder(jobName).setContentType(android.print.PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
                callback.onLayoutFinished(pdi, true);
            }
        }, null);
    }

    private void locHd(String loai, Button btn) {
        btnFilterTatCa.setBackgroundColor(Color.parseColor("#E5E5E5")); btnFilterTatCa.setTextColor(Color.parseColor("#333333"));
        btnFilterConHieuLuc.setBackgroundColor(Color.parseColor("#E5E5E5")); btnFilterConHieuLuc.setTextColor(Color.parseColor("#333333"));
        btnFilterHetHan.setBackgroundColor(Color.parseColor("#E5E5E5")); btnFilterHetHan.setTextColor(Color.parseColor("#333333"));
        btn.setBackgroundColor(Color.parseColor("#2ECC71")); btn.setTextColor(Color.WHITE);

        danhSachHienThiHd.clear();
        if (loai.equals("Tất cả")) danhSachHienThiHd.addAll(danhSachGocHd);
        else {
            for (HopDong h : danhSachGocHd) {
                if (h.getTrangThai().equalsIgnoreCase(loai)) danhSachHienThiHd.add(h);
            }
        }
        adapterHd.notifyDataSetChanged();
    }
}