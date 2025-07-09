package com.example.mobifone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class item_khachhang extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_khachhang);

        Intent intent = getIntent();
        String hoten = intent.getStringExtra("hoten");
        String sdt = intent.getStringExtra("sdt");
        String idgc = intent.getStringExtra("idgc");
        double sodu = intent.getDoubleExtra("sodu", 0.0);

        TextView txthoten = findViewById(R.id.txthotenkh);
        TextView txtsdt = findViewById(R.id.txtsdtkh);
        TextView txtidgc = findViewById(R.id.txtidgckh);
        TextView txtsodu = findViewById(R.id.txtsodukh);

        txthoten.setText(hoten);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###");
        formatter.setDecimalFormatSymbols(symbols);

        String soduFormatted = formatter.format(sodu) + " đ";
        txtsodu.setText(soduFormatted);

        txtsdt.setText(sdt);
        txtidgc.setText(idgc);
    }
}