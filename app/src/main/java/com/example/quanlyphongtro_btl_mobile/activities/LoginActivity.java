package com.example.quanlyphongtro_btl_mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlyphongtro_btl_mobile.R;
import com.example.quanlyphongtro_btl_mobile.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnExit;
    private TextView txtForgotPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Anh xa giao dien
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnExit = findViewById(R.id.btnExit);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        dbHelper = new DatabaseHelper(this);

        // Xu ly nut dang nhap
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taiKhoan = edtUsername.getText().toString().trim();
                String matKhau = edtPassword.getText().toString().trim();

                if (taiKhoan.isEmpty() || matKhau.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập tên đăng nhập và mật khẩu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Goi ham tieng Viet da doi o trong DatabaseHelper
                boolean hopLe = dbHelper.kiemTraDangNhap(taiKhoan, matKhau);

                if (hopLe) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Lấy họ tên người dùng và lưu vào SharedPreferences
                    String hoTen = dbHelper.layHoTen(taiKhoan);
                    getSharedPreferences("UserPrefs", MODE_PRIVATE)
                            .edit()
                            .putString("hoTen", hoTen)
                            .apply();

                    // Chuyển sang màn hình Quản lý hợp đồng
                    Intent intent = new Intent(LoginActivity.this, QuanLyHopDongActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xu ly nut thoat
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        // Xu ly quen mat khau
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Tính năng đang được cập nhật!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}