package com.example.quanlyphongtro_btl_mobile.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.quanlyphongtro_btl_mobile.R;
import com.example.quanlyphongtro_btl_mobile.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BaoCaoActivity extends BaseMenuActivity {

    private TextView txtDoanhThuThang, txtTieuDeDoanhThu;
    private ListView lvNo;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_cao);

        khoiTaoMenuDieuHuong();
        dbHelper = new DatabaseHelper(this);

        txtDoanhThuThang = findViewById(R.id.txtDoanhThuThang);
        txtTieuDeDoanhThu = findViewById(R.id.txtTieuDeDoanhThu);
        lvNo = findViewById(R.id.lvHoaDonNo);

        taiBaoCao();
    }

    private void taiBaoCao() {
        Calendar cal = Calendar.getInstance();
        String thangNay = String.format("%02d/%d", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
        
        double doanhThu = dbHelper.layDoanhThuThang(thangNay);
        txtTieuDeDoanhThu.setText("Doanh thu tháng " + thangNay);
        txtDoanhThuThang.setText(String.format("%,d VNĐ", (int)doanhThu));

        List<String> listNo = new ArrayList<>();
        Cursor c = dbHelper.layHoaDonNo();
        if (c != null) {
            while (c.moveToNext()) {
                String s = "Phòng " + c.getString(c.getColumnIndexOrThrow("tenPhong")) + 
                           " (Tháng " + c.getString(c.getColumnIndexOrThrow("thang")) + "): " +
                           String.format("%,d", (int)c.getDouble(c.getColumnIndexOrThrow("tongTien"))) + " VNĐ";
                listNo.add(s);
            }
            c.close();
        }
        
        if (listNo.isEmpty()) listNo.add("Không có phòng nào nợ tiền.");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listNo);
        lvNo.setAdapter(adapter);
    }
}
