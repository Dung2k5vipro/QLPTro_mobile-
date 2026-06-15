package com.example.quanlyphongtro_btl_mobile.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.quanlyphongtro_btl_mobile.R;
import com.example.quanlyphongtro_btl_mobile.models.PhongTro;
import java.util.List;

public class PhongTroAdapter extends BaseAdapter {

    private Context context;
    private List<PhongTro> danhSachPhong;

    public PhongTroAdapter(Context context, List<PhongTro> danhSachPhong) {
        this.context = context;
        this.danhSachPhong = danhSachPhong;
    }

    @Override
    public int getCount() { return danhSachPhong.size(); }

    @Override
    public Object getItem(int position) { return danhSachPhong.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dong_du_lieu, parent, false);
        }

        PhongTro phong = danhSachPhong.get(position);

        TextView txtTenPhong = convertView.findViewById(R.id.txtTenPhongItem);
        TextView txtTrangThai = convertView.findViewById(R.id.txtTrangThaiItem);
        TextView txtGhiChu = convertView.findViewById(R.id.txtGhiChuItem);
        TextView txtGiaThue = convertView.findViewById(R.id.txtGiaThueItem);
        TextView txtDienTich = convertView.findViewById(R.id.txtDienTichItem);
        android.widget.ImageView imgHinhAnh = convertView.findViewById(R.id.imgHinhAnhItem);

        txtTenPhong.setText(phong.getTenPhong());
        txtGhiChu.setText(phong.getGhiChu());
        txtDienTich.setText("📐 " + phong.getDienTich() + "m²");

        double trieuDong = phong.getGiaThue() / 1000000;
        txtGiaThue.setText("💵 " + trieuDong + "tr/tháng");

        txtTrangThai.setText(phong.getTrangThai().toUpperCase());
        if (phong.getTrangThai().equalsIgnoreCase("Trống")) {
            txtTrangThai.setTextColor(Color.parseColor("#2ECC71"));
            txtTrangThai.setBackgroundColor(Color.parseColor("#E8F8F5"));
        } else {
            txtTrangThai.setTextColor(Color.parseColor("#E74C3C"));
            txtTrangThai.setBackgroundColor(Color.parseColor("#FDEDEC"));
        }

        // Hiển thị hình ảnh
        String hinhAnhUri = phong.getHinhAnh();
        if (hinhAnhUri != null && !hinhAnhUri.isEmpty()) {
            try {
                imgHinhAnh.setImageURI(android.net.Uri.parse(hinhAnhUri));
            } catch (Exception e) {
                imgHinhAnh.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            imgHinhAnh.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        return convertView;
    }
}