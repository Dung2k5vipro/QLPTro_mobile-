package com.example.quanlyphongtro_btl_mobile.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.quanlyphongtro_btl_mobile.R;
import com.example.quanlyphongtro_btl_mobile.models.HoaDon;
import java.util.List;

public class HoaDonAdapter extends BaseAdapter {

    private Context context;
    private List<HoaDon> list;
    private OnHoaDonActionListener listener;

    public interface OnHoaDonActionListener {
        void onThanhToan(HoaDon hd);
        void onXoa(HoaDon hd);
        void onShare(HoaDon hd);
    }

    public HoaDonAdapter(Context context, List<HoaDon> list, OnHoaDonActionListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override public int getCount() { return list.size(); }
    @Override public Object getItem(int position) { return list.get(position); }
    @Override public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hoa_don, parent, false);
        }

        HoaDon hd = list.get(position);

        TextView txtTen = convertView.findViewById(R.id.txtTenPhongHd);
        TextView txtThang = convertView.findViewById(R.id.txtThangHd);
        TextView txtTong = convertView.findViewById(R.id.txtTongTienHd);
        TextView txtTrangThai = convertView.findViewById(R.id.txtTrangThaiHd);
        Button btnXoa = convertView.findViewById(R.id.btnXoaHd);
        Button btnThanhToan = convertView.findViewById(R.id.btnThanhToanHd);

        txtTen.setText(hd.getTenPhong());
        txtThang.setText("Tháng " + hd.getThang());
        txtTong.setText(String.format("%,d VNĐ", (int)hd.getTongTien()));
        txtTrangThai.setText(hd.getTrangThai());

        if (hd.getTrangThai().equals("Đã thanh toán")) {
            txtTrangThai.setTextColor(Color.parseColor("#2E7D32"));
            txtTrangThai.setBackgroundColor(Color.parseColor("#E8F5E9"));
            btnThanhToan.setVisibility(View.GONE);
        } else {
            txtTrangThai.setTextColor(Color.parseColor("#D32F2F"));
            txtTrangThai.setBackgroundColor(Color.parseColor("#FFEBEE"));
            btnThanhToan.setVisibility(View.VISIBLE);
        }

        convertView.setOnClickListener(v -> listener.onShare(hd));
        btnXoa.setOnClickListener(v -> listener.onXoa(hd));
        btnThanhToan.setOnClickListener(v -> listener.onThanhToan(hd));

        return convertView;
    }
}
