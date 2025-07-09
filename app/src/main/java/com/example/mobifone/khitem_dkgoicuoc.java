package com.example.mobifone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class khitem_dkgoicuoc extends AppCompatActivity {

    private TextView txtidkh, txtidgc, txtngaydk, txtngayhh, txttrangthai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khitem_dkgoicuoc);

        txtidkh = findViewById(R.id.txtkhidkh);
        txtidgc = findViewById(R.id.txtkhidgc);
        txtngaydk = findViewById(R.id.txtkhngaydk);
        txtngayhh = findViewById(R.id.txtkhngayhh);
        txttrangthai = findViewById(R.id.txtkhtt);

        String idkh = getIntent().getStringExtra("idkh");
        String idgc = getIntent().getStringExtra("idgc");
        String ngaydk = getIntent().getStringExtra("ngaydk");
        String ngayhh = getIntent().getStringExtra("ngayhh");
        String trangthai = getIntent().getStringExtra("trangthai");

        DatabaseHelper db = new DatabaseHelper(this);
        String sdt = db.laysdtTheoIdkh(idkh);
        String tengc = db.layTengcTheoIdgc(idgc);

        txtidkh.setText("SDT khách hang: " + sdt);
        txtidgc.setText("Tên gói cước: " + tengc);
        txtngaydk.setText("Ngày đăng ký: " + ngaydk);
        txtngayhh.setText("Ngày hết hạn: " + ngayhh);
        txttrangthai.setText("Trạng thái: " + trangthai);

        Intent intent = new Intent(khitem_dkgoicuoc.this, khct_dkgoicuoc.class);
        intent.putExtra("idkh", idkh);
        intent.putExtra("idgc", idgc);
        intent.putExtra("ngaydk", ngaydk);
        intent.putExtra("ngayhh", ngayhh);
        intent.putExtra("trangthai", trangthai);
        startActivity(intent);
    }
}