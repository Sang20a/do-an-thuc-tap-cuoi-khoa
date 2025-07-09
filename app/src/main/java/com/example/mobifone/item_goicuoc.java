package com.example.mobifone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class item_goicuoc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_goicuoc);

        Intent intent = getIntent();
        String tenGoi = intent.getStringExtra("tengoi");
        double giaCuoc = intent.getDoubleExtra("gia", 0.0);
        String thoiGian = intent.getStringExtra("hansd");
        String dungLuong = intent.getStringExtra("dungluong");

        TextView tenGoiTextView = findViewById(R.id.txtten);
        TextView giaCuocTextView = findViewById(R.id.txtgia);
        TextView thoiGianTextView = findViewById(R.id.txtthoigian);
        TextView daTaTextView = findViewById(R.id.txtdata);

        tenGoiTextView.setText(tenGoi);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###");
        formatter.setDecimalFormatSymbols(symbols);

        String giaCuocFormatted = formatter.format(giaCuoc) + " đ";
        giaCuocTextView.setText(giaCuocFormatted);

        thoiGianTextView.setText(thoiGian);
        daTaTextView.setText(dungLuong);
    }
}