package com.example.quanlyphongtro_btl_mobile.activities;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.quanlyphongtro_btl_mobile.R;
import com.example.quanlyphongtro_btl_mobile.database.DatabaseHelper;
import java.util.Calendar;
import java.util.Locale;

public class BaoCaoActivity extends BaseMenuActivity {

    private TextView txtWelcome, txtDoanhThuThangNay, txtPercentOccupancy;
    private TextView txtCountPhong, txtCountKhach, txtCountHopDong, txtCountHoaDon, txtUnpaidSummary;
    private ProgressBar progressOccupancy;
    private com.google.android.material.card.MaterialCardView cardPhong, cardKhach, cardHopDong, cardHoaDon;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_cao);

        khoiTaoMenuDieuHuong();
        dbHelper = new DatabaseHelper(this);

        anhXa();
        taiDuLieuThongKe();
    }

    private void anhXa() {
        txtWelcome = findViewById(R.id.txtWelcome);
        txtDoanhThuThangNay = findViewById(R.id.txtDoanhThuThangNay);
        txtPercentOccupancy = findViewById(R.id.txtPercentOccupancy);
        txtCountPhong = findViewById(R.id.txtCountPhong);
        txtCountKhach = findViewById(R.id.txtCountKhach);
        txtCountHopDong = findViewById(R.id.txtCountHopDong);
        txtCountHoaDon = findViewById(R.id.txtCountHoaDon);
        txtUnpaidSummary = findViewById(R.id.txtUnpaidSummary);
        progressOccupancy = findViewById(R.id.progressOccupancy);
        
        cardPhong = findViewById(R.id.cardPhong);
        cardKhach = findViewById(R.id.cardKhach);
        cardHopDong = findViewById(R.id.cardHopDong);
        cardHoaDon = findViewById(R.id.cardHoaDon);

        cardPhong.setOnClickListener(v -> startActivity(new android.content.Intent(this, QuanLyPhongActivity.class)));
        cardKhach.setOnClickListener(v -> startActivity(new android.content.Intent(this, QuanLyKhachActivity.class)));
        cardHopDong.setOnClickListener(v -> startActivity(new android.content.Intent(this, QuanLyHopDongActivity.class)));
        cardHoaDon.setOnClickListener(v -> startActivity(new android.content.Intent(this, QuanLyHoaDonActivity.class)));

        findViewById(R.id.cardUnpaidSummary).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, QuanLyHoaDonActivity.class);
            intent.putExtra("FILTER_NO", true);
            startActivity(intent);
        });
    }

    private void taiDuLieuThongKe() {
        // 1. Lấy tên người dùng (giả sử admin)
        txtWelcome.setText("Xin chào, Admin");

        // 2. Doanh thu tháng này
        Calendar cal = Calendar.getInstance();
        String thangNay = String.format(Locale.getDefault(), "%02d/%d", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
        double doanhThu = dbHelper.layDoanhThuThang(thangNay);
        txtDoanhThuThangNay.setText(String.format(Locale.getDefault(), "%,.0fđ", doanhThu));

        // 3. Tỉ lệ lấp đầy
        int totalPhong = dbHelper.getCountPhong();
        int occupiedPhong = dbHelper.getCountPhongDaThue();
        int percent = (totalPhong > 0) ? (occupiedPhong * 100 / totalPhong) : 0;
        
        progressOccupancy.setProgress(percent);
        txtPercentOccupancy.setText(percent + "%");

        // 4. Các con số thống kê nhanh
        txtCountPhong.setText(String.valueOf(totalPhong));
        txtCountKhach.setText(String.valueOf(dbHelper.getCountKhachThue()));
        txtCountHopDong.setText(String.valueOf(dbHelper.getCountHopDong()));
        txtCountHoaDon.setText(String.valueOf(dbHelper.getCountHoaDon()));

        // 5. Thống kê nợ
        double[] thongKeNo = dbHelper.getThongKeNo();
        int soPhieuNo = (int) thongKeNo[0];
        double tongTienNo = thongKeNo[1];
        
        txtUnpaidSummary.setText(String.format(Locale.getDefault(), "%,.0fđ (%d phiếu)", tongTienNo, soPhieuNo));
    }
}
