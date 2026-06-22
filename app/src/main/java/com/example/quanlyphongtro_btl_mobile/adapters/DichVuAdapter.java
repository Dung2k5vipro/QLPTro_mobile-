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
import com.example.quanlyphongtro_btl_mobile.models.DichVu;

import java.util.List;

public class DichVuAdapter extends BaseAdapter {

    private Context context;
    private List<DichVu> danhSachDichVu;
    private OnDichVuClickListener listener;

    public interface OnDichVuClickListener {
        void onSuaClick(DichVu dichVu);
    }

    public DichVuAdapter(Context context,
                         List<DichVu> danhSachDichVu,
                         OnDichVuClickListener listener) {
        this.context = context;
        this.danhSachDichVu = danhSachDichVu;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return danhSachDichVu.size();
    }

    @Override
    public Object getItem(int position) {
        return danhSachDichVu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ================= VIEW HOLDER =================
    static class ViewHolder {
        TextView txtTen;
        TextView txtGia;
        TextView txtDonVi;
        TextView txtGhiChu;
        ImageView imgSua;
        View cardItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_dich_vu, parent, false);

            holder = new ViewHolder();

            holder.txtTen = convertView.findViewById(R.id.txtTenDV);
            holder.txtGia = convertView.findViewById(R.id.txtGiaDV);
            holder.txtDonVi = convertView.findViewById(R.id.txtDonViDV);
            holder.txtGhiChu = convertView.findViewById(R.id.txtGhiChuDV);
            holder.imgSua = convertView.findViewById(R.id.imgSuaDV);
            holder.cardItem = convertView.findViewById(R.id.cardItemDV);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DichVu dv = danhSachDichVu.get(position);

        // ================= SET DATA =================
        holder.txtTen.setText(dv.getTenDichVu() != null ? dv.getTenDichVu() : "");
        holder.txtGia.setText("Giá: " + dv.getDonGia());
        holder.txtDonVi.setText(dv.getDonViTinh() != null ? dv.getDonViTinh() : "");
        holder.txtGhiChu.setText(dv.getGhiChu() != null ? dv.getGhiChu() : "");

        // ================= UI COLOR (giống KhachThue style) =================
        if (dv.getTenDichVu() != null &&
                dv.getTenDichVu().toLowerCase().contains("điện")) {

            holder.cardItem.setBackgroundColor(Color.parseColor("#E3F2FD"));
        }
        else if (dv.getTenDichVu() != null &&
                dv.getTenDichVu().toLowerCase().contains("nước")) {

            holder.cardItem.setBackgroundColor(Color.parseColor("#E8F5E9"));
        }
        else {
            holder.cardItem.setBackgroundColor(Color.WHITE);
        }

        // ================= CLICK SỬA =================
        holder.imgSua.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSuaClick(dv);
            }
        });

        holder.cardItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSuaClick(dv);
            }
        });

        return convertView;
    }
}