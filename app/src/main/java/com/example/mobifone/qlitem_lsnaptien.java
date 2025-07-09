package com.example.mobifone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class qlitem_lsnaptien extends AppCompatActivity {

    private TextView txtlsidnv, txtlsidkh, txtlsngaynap, txtlssotien;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlitem_lsnaptien);

        dbHelper = new DatabaseHelper(this);

        txtlsidnv = findViewById(R.id.txtlsidnv);
        txtlsidkh = findViewById(R.id.txtlsidkh);
        txtlsngaynap = findViewById(R.id.txtlsngaynap);
        txtlssotien = findViewById(R.id.txtlssotien);

        Intent intent = getIntent();
        String idnt = intent.getStringExtra("idnt");
        String idnv = intent.getStringExtra("idnv");
        String idkh = intent.getStringExtra("idkh");
        String ngaynap = intent.getStringExtra("ngaynap");
        double sotien = intent.getDoubleExtra("sotien", 0.0);

        DatabaseHelper db = new DatabaseHelper(this);
        String sdt = db.laysdtTheoIdkh(idkh);
        String tennv = db.layTennvTheoIdnv(idnv);

        txtlsidnv.setText("Tên nhân viên: \n" + tennv);
        txtlsidkh.setText("SDT khách hàng: " + sdt);
        txtlsngaynap.setText("Ngày nạp: " + ngaynap);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###");
        formatter.setDecimalFormatSymbols(symbols);

        String lsnaptienFormatted = formatter.format(sotien) + " đ";
        txtlssotien.setText("Số tiền nạp: " + lsnaptienFormatted);
    }
}