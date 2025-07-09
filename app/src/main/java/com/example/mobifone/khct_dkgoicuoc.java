package com.example.mobifone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class khct_dkgoicuoc extends AppCompatActivity {

    TextView txtidgc, txtngaydk, txtngayhh, txttrangthai;
    Button btnHuy, btnDangKyLai;
    String idkh, idgc, ngaydk, ngayhh, trangthai;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khct_dkgoicuoc);

        txtidgc = findViewById(R.id.txtkhctidgc);
        txtngaydk = findViewById(R.id.txtkhctngaydk);
        txtngayhh = findViewById(R.id.txtkhctngayhh);
        txttrangthai = findViewById(R.id.txtkhcttt);
        btnHuy = findViewById(R.id.btnhuygc);
        btnDangKyLai = findViewById(R.id.btndklai);
        imageButton = findViewById(R.id.imgbtnback);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(khct_dkgoicuoc.this, kh_goicuoc.class);
                startActivity(intent);
            }
        });

        idkh = getIntent().getStringExtra("idkh");
        idgc = getIntent().getStringExtra("idgc");
        ngaydk = getIntent().getStringExtra("ngaydk");
        ngayhh = getIntent().getStringExtra("ngayhh");
        trangthai = getIntent().getStringExtra("trangthai");

        txtidgc.setText("ID Gói cước: " + idgc);
        txtngaydk.setText("Ngày đăng ký: " + ngaydk);
        txtngayhh.setText("Ngày hết hạn: " + ngayhh);
        txttrangthai.setText("Trạng thái: " + trangthai);

        if ("Đang sử dụng".equalsIgnoreCase(trangthai)) {
            btnHuy.setVisibility(View.VISIBLE);
            btnDangKyLai.setVisibility(View.GONE);
        } else {
            btnHuy.setVisibility(View.GONE);
            btnDangKyLai.setVisibility(View.VISIBLE);
        }

        btnHuy.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận hủy gói cước")
                    .setMessage("Bạn có chắc chắn muốn hủy gói cước này không?")
                    .setPositiveButton("Hủy gói", (dialog, which) -> {
                        DatabaseHelper db = new DatabaseHelper(this);
                        String today = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                        boolean kq = db.huyGoiCuoc(idkh, idgc, today);
                        if (kq) {
                            Toast.makeText(this, "Đã hủy gói cước!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(this, kh_goicuoc.class);
                            intent.putExtra("idkh", idkh);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Hủy thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });

        btnDangKyLai.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(this);
            Cursor cursor = db.getData("SELECT * FROM goicuoc WHERE idgc = ?", new String[]{idgc});
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String tenGoi = cursor.getString(cursor.getColumnIndex("tengoi"));
                @SuppressLint("Range") double gia = cursor.getDouble(cursor.getColumnIndex("gia"));
                @SuppressLint("Range") String dungLuong = cursor.getString(cursor.getColumnIndex("dungluong"));
                @SuppressLint("Range") String hanSD = cursor.getString(cursor.getColumnIndex("hansd"));

                Intent intent = new Intent(this, ctgoicuoc.class);
                intent.putExtra("idgc", idgc);
                intent.putExtra("tengoi", tenGoi);
                intent.putExtra("gia", gia);
                intent.putExtra("dungluong", dungLuong);
                intent.putExtra("hansd", String.valueOf(hanSD));

                startActivity(intent);
            } else {
                Toast.makeText(this, "Không tìm thấy thông tin gói cước!", Toast.LENGTH_SHORT).show();
            }
            if (cursor != null) cursor.close();
        });

    }
}