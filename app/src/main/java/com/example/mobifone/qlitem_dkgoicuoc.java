package com.example.mobifone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class qlitem_dkgoicuoc extends AppCompatActivity {

    private TextView txtqldkidkh, txtqldkidgc, txtqldkngaydk, txtqldkngayhh, txtqldktt;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlitem_dkgoicuoc);

        dbHelper = new DatabaseHelper(this);

        txtqldkidkh = findViewById(R.id.txtqldkidkh);
        txtqldkidgc = findViewById(R.id.txtqldkidgc);
        txtqldkngaydk = findViewById(R.id.txtqldkngaydk);
        txtqldkngayhh = findViewById(R.id.txtqldkngayhh);
        txtqldktt = findViewById(R.id.txtqldktt);

        Intent intent = getIntent();
        String iddk = intent.getStringExtra("iddk");
        String idkh = intent.getStringExtra("idkh");
        String idgc = intent.getStringExtra("idgc");
        String ngaydk = intent.getStringExtra("ngaydk");
        String ngayhh = intent.getStringExtra("ngayhh");
        String trangthai = intent.getStringExtra("trangthai");

        DatabaseHelper db = new DatabaseHelper(this);
        String sdt = db.laysdtTheoIdkh(idkh);
        String tengc = db.layTengcTheoIdgc(idgc);

        txtqldkidkh.setText("SDT khách hàng: " + sdt);
        txtqldkidgc.setText("Tên gói cước: " + tengc);
        txtqldkngaydk.setText("Ngày đk: " + ngaydk);
        txtqldkngayhh.setText("Ngày hh: " + ngayhh);
        txtqldktt.setText("Trạng thái: " + trangthai);
    }
}