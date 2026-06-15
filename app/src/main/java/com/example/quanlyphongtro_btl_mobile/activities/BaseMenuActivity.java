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
                
                // 1. Toolbar: Đẩy nội dung xuống dưới Status Bar (vùng camera)
                // Nền của toolbar vẫn sẽ tràn lên trên vì padding không làm thay đổi background
                if (layoutToolbar != null) {
                    layoutToolbar.setPadding(
                        layoutToolbar.getPaddingLeft(),
                        systemBars.top, // Padding Top bằng chiều cao vùng camera
                        layoutToolbar.getPaddingRight(),
                        layoutToolbar.getPaddingBottom()
                    );
                }
                
                // 2. Navigation Bar: Đẩy phần dưới lên để không bị thanh điều hướng che (nếu có)
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);

                return WindowInsetsCompat.CONSUMED;
            });
        }

        menuBaoCao = findViewById(R.id.menuBaoCao);
        menuPhongTro = findViewById(R.id.menuPhongTro);
        menuKhachThue = findViewById(R.id.menuKhachThue);
        menuHopDong = findViewById(R.id.menuHopDong);
        menuDichVu = findViewById(R.id.menuDichVu);
        menuHoaDon = findViewById(R.id.menuHoaDon);
        menuDangXuat = findViewById(R.id.menuDangXuat);

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