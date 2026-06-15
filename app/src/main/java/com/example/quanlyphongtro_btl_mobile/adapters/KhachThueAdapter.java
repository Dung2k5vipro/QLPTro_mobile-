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
import com.example.quanlyphongtro_btl_mobile.models.KhachThue;
import java.util.List;

public class KhachThueAdapter extends BaseAdapter {
    private Context context;
    private List<KhachThue> danhSachKhach;
    private OnKhachThueClickListener listener;

    public interface OnKhachThueClickListener {
        void onSuaClick(KhachThue khachThue);
    }

    public KhachThueAdapter(Context context, List<KhachThue> danhSachKhach, OnKhachThueClickListener listener) {
        this.context = context;
        this.danhSachKhach = danhSachKhach;
        this.listener = listener;
    }

    @Override public int getCount() { return danhSachKhach.size(); }
    @Override public Object getItem(int position) { return danhSachKhach.get(position); }
    @Override public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_khach_thue, parent, false);
        }

        KhachThue khach = danhSachKhach.get(position);

        TextView txtTen = convertView.findViewById(R.id.txtTenKhachItem);
        TextView txtGioiTinh = convertView.findViewById(R.id.txtGioiTinhItem);
        TextView txtSdt = convertView.findViewById(R.id.txtSdtKhachItem);
        TextView txtCccd = convertView.findViewById(R.id.txtCccdKhachItem);
        TextView txtDiaChi = convertView.findViewById(R.id.txtDiaChiKhachItem);
        TextView txtNgaySinh = convertView.findViewById(R.id.txtNgaySinhKhachItem);
        ImageView imgSua = convertView.findViewById(R.id.imgSuaKhachItem);
        View cardAvatar = convertView.findViewById(R.id.cardAvatar);

        txtTen.setText(khach.getHoTen());
        txtGioiTinh.setText(khach.getGioiTinh());
        txtSdt.setText(khach.getSoDienThoai());
        txtCccd.setText(khach.getCccd());
        txtDiaChi.setText(khach.getDiaChi());
        txtNgaySinh.setText(khach.getNgaySinh());

        // Đổi màu nhãn giới tính linh hoạt chuẩn UI
        if (khach.getGioiTinh().equalsIgnoreCase("Nam")) {
            txtGioiTinh.setBackgroundColor(Color.parseColor("#DBF9EB"));
            txtGioiTinh.setTextColor(Color.parseColor("#2ECC71"));
            cardAvatar.setBackgroundColor(Color.parseColor("#E8F1FF"));
        } else {
            txtGioiTinh.setBackgroundColor(Color.parseColor("#FFF3E0"));
            txtGioiTinh.setTextColor(Color.parseColor("#E65100"));
            cardAvatar.setBackgroundColor(Color.parseColor("#FFE0B2"));
        }

        // Bắt sự kiện click vào icon cây bút để đẩy form sửa lên
        imgSua.setOnClickListener(v -> listener.onSuaClick(khach));

        return convertView;
    }
}