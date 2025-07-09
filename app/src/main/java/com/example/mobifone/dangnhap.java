package com.example.mobifone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class dangnhap extends AppCompatActivity {

    private EditText edtSDT, edtMK;
    private CheckBox chkMK;
    private Button btnDNTK, btnHDNTK;
    private TextView txtDK;
    private DatabaseHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap);

        edtSDT = findViewById(R.id.edtSDT);
        edtMK = findViewById(R.id.edtMK);
        chkMK = findViewById(R.id.chkMK);
        btnDNTK = findViewById(R.id.btnDN);
        btnHDNTK = findViewById(R.id.btnHDNTK);
        txtDK = findViewById(R.id.txtDK);

        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDataBase();
            dbHelper.openDataBase();
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở CSDL!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        chkMK.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                edtMK.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                edtMK.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            edtMK.setSelection(edtMK.getText().length());
        });

        btnDNTK.setOnClickListener(v -> {
            String sdt = edtSDT.getText().toString().trim();
            String pass = edtMK.getText().toString().trim();

            if (sdt.isEmpty()) {
                edtSDT.setError("Vui lòng nhập số điện thoại!");
                edtSDT.requestFocus();
                return;
            }

            if (!sdt.matches("\\d{10}")) {
                edtSDT.setError("Số điện thoại phải đủ 10 số!");
                edtSDT.requestFocus();
                return;
            }

            if (pass.isEmpty()) {
                edtMK.setError("Vui lòng nhập mật khẩu!");
                edtMK.requestFocus();
                return;
            }

            String hashedPass = DatabaseHelper.hashPassword(pass);
            if (dbHelper.kiemTraDangNhap(sdt, hashedPass))
            {
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                Cursor cursor = dbHelper.getData("SELECT idkh FROM khachhang WHERE sdt = '" + sdt + "'");
                String idkh = "";
                if (cursor.moveToFirst()) {
                    idkh = cursor.getString(0);
                }
                cursor.close();

                SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("sdt", sdt);
                editor.putString("idkh", idkh);
                editor.putBoolean("isNhanVien", false);
                editor.apply();

                startActivity(new Intent(this, MainActivity.class));

                SharedPreferences prefs1 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = prefs1.edit();
                editor1.putBoolean("isLoggedIn", true);
                editor1.putBoolean("fromLogin", true);
                editor1.apply();
            }else if (dbHelper.kiemTraDangNhapNhanVien(sdt, pass)) {
                Toast.makeText(this, "Đăng nhập nhân viên thành công", Toast.LENGTH_SHORT).show();

                SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("sdt", sdt);
                editor.putBoolean("isNhanVien", true);
                editor.apply();

                startActivity(new Intent(this, MainActivity.class));

            } else {
                Toast.makeText(this, "Sai số điện thoại hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });


        btnHDNTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        txtDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dangnhap.this, dangky.class);
                startActivity(intent);
            }
        });
    }
}