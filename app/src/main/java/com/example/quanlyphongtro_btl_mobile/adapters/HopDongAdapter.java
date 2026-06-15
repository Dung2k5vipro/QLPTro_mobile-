package com.example.quanlyphongtro_btl_mobile.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.quanlyphongtro_btl_mobile.R;
import com.example.quanlyphongtro_btl_mobile.models.HopDong;
import java.util.List;

public class HopDongAdapter extends BaseAdapter {
    private Context context;
    private List<HopDong> listHd;
    private OnHopDongActionListener listener;

    public interface OnHopDongActionListener {
        void onSua(HopDong hd);
        void onXoa(HopDong hd);
        void onIn(HopDong hd);
    }

    public HopDongAdapter(Context context, List<HopDong> listHd, OnHopDongActionListener listener) {
        this.context = context;
        this.listHd = listHd;
        this.listener = listener;
    }

    @Override public int getCount() { return listHd.size(); }
    @Override public Object getItem(int position) { return listHd.get(position); }
    @Override public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hop_dong, parent, false);
        }
        HopDong hd = listHd.get(position);

        TextView txtMa = convertView.findViewById(R.id.txtMaHdItem);
        TextView txtPhong = convertView.findViewById(R.id.txtTenPhongHdItem);
        TextView txtKhach = convertView.findViewById(R.id.txtTenKhachHdItem);
        TextView txtNgayBd = convertView.findViewById(R.id.txtNgayBatDauItem);
        TextView txtNgayKt = convertView.findViewById(R.id.txtNgayKetThucItem);
        TextView txtTien = convertView.findViewById(R.id.txtTienCocItem);
        TextView txtTrangThai = convertView.findViewById(R.id.txtTrangThaiHdItem);
        ImageView imgIn = convertView.findViewById(R.id.imgInHdItem);
        ImageView imgSua = convertView.findViewById(R.id.imgSuaHdItem);
        ImageView imgXoa = convertView.findViewById(R.id.imgXoaHdItem);

        txtMa.setText("Mã: " + hd.getSoHd());
        txtPhong.setText(hd.getTenPhong());
        txtKhach.setText(hd.getTenKhach());
        txtNgayBd.setText(hd.getNgayBatDau());
        txtNgayKt.setText(hd.getNgayKetThuc());
        txtTien.setText(String.format("%,dđ", (int)hd.getTienCoc()));
        txtTrangThai.setText(hd.getTrangThai().toUpperCase());

        if (hd.getTrangThai().equalsIgnoreCase("Còn hiệu lực")) {
            txtTrangThai.setBackgroundColor(Color.parseColor("#DBF9EB"));
            txtTrangThai.setTextColor(Color.parseColor("#2ECC71"));
        } else {
            txtTrangThai.setBackgroundColor(Color.parseColor("#FCE4D6"));
            txtTrangThai.setTextColor(Color.parseColor("#C55A11"));
        }

        imgSua.setOnClickListener(v -> listener.onSua(hd));
        imgXoa.setOnClickListener(v -> listener.onXoa(hd));
        imgIn.setOnClickListener(v -> listener.onIn(hd));

        return convertView;
    }
}