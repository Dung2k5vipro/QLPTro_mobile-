package com.example.quanlyphongtro_btl_mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.quanlyphongtro_btl_mobile.R;

public class BaseMenuActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected ImageView imgMenu;
    protected TextView menuBaoCao, menuPhongTro, menuKhachThue, menuHopDong, menuDichVu, menuHoaDon, menuDangXuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
    }

    // Hàm này gọi ở các Activity con để kích hoạt Menu tự động
    protected void khoiTaoMenuDieuHuong() {
        drawerLayout = findViewById(R.id.drawerLayout);
        imgMenu = findViewById(R.id.imgMenu);
        View layoutToolbar = findViewById(R.id.layoutToolbar);

        // Xử lý tràn viền chuẩn (Edge-to-Edge)
        if (drawerLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(drawerLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                if (layoutToolbar != null) {
                    layoutToolbar.setPadding(
                        layoutToolbar.getPaddingLeft(),
                        systemBars.top,
                        layoutToolbar.getPaddingRight(),
                        layoutToolbar.getPaddingBottom()
                    );
                }
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
                return WindowInsetsCompat.CONSUMED;
            });
        }

        // Tìm các mục menu thông qua NavigationView hoặc trực tiếp
        View navView = findViewById(R.id.navigationView);
        if (navView != null) {
            menuBaoCao = navView.findViewById(R.id.menuBaoCao);
            menuPhongTro = navView.findViewById(R.id.menuPhongTro);
            menuKhachThue = navView.findViewById(R.id.menuKhachThue);
            menuHopDong = navView.findViewById(R.id.menuHopDong);
            menuDichVu = navView.findViewById(R.id.menuDichVu);
            menuHoaDon = navView.findViewById(R.id.menuHoaDon);
            menuDangXuat = navView.findViewById(R.id.menuDangXuat);
        } else {
            menuBaoCao = findViewById(R.id.menuBaoCao);
            menuPhongTro = findViewById(R.id.menuPhongTro);
            menuKhachThue = findViewById(R.id.menuKhachThue);
            menuHopDong = findViewById(R.id.menuHopDong);
            menuDichVu = findViewById(R.id.menuDichVu);
            menuHoaDon = findViewById(R.id.menuHoaDon);
            menuDangXuat = findViewById(R.id.menuDangXuat);
        }

        if (imgMenu != null) {
            imgMenu.setOnClickListener(v -> moMenuBen());
        }

        caiDatSuKienClick();
    }

    private void moMenuBen() {
        if (drawerLayout != null && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    protected void dongMenuBen() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    // Hàm hỗ trợ loại bỏ dấu tiếng Việt để tìm kiếm dễ dàng hơn
    protected String loaiBoDau(String str) {
        if (str == null) return "";
        try {
            String temp = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll("đ", "d");
        } catch (Exception e) {
            return str.toLowerCase();
        }
    }

    private void caiDatSuKienClick() {
        if (menuBaoCao != null) menuBaoCao.setOnClickListener(v -> chuyenManHinh(BaoCaoActivity.class));
        if (menuPhongTro != null) menuPhongTro.setOnClickListener(v -> chuyenManHinh(QuanLyPhongActivity.class));
        if (menuKhachThue != null) menuKhachThue.setOnClickListener(v -> chuyenManHinh(QuanLyKhachActivity.class));
        if (menuHopDong != null) menuHopDong.setOnClickListener(v -> chuyenManHinh(QuanLyHopDongActivity.class));
        if (menuDichVu != null) menuDichVu.setOnClickListener(v -> chuyenManHinh(QuanLyDichVuActivity.class));
        if (menuHoaDon != null) menuHoaDon.setOnClickListener(v -> chuyenManHinh(QuanLyHoaDonActivity.class));
        if (menuDangXuat != null) menuDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void chuyenManHinh(Class<?> classDich) {
        dongMenuBen();
        // Nếu đang ở chính màn hình đó rồi thì không chuyển nữa để tránh lặp lùi màn hình
        if (this.getClass() == classDich) return;

        try {
            startActivity(new Intent(this, classDich));
        } catch (Exception e) {
            Toast.makeText(this, "Chức năng đang được phát triển!", Toast.LENGTH_SHORT).show();
        }
    }
}