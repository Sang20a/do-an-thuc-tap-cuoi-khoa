package com.example.mobifone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class trangql extends AppCompatActivity {

    private Button btnqlgc, btnqlkh, btnqllsnap, btnqllsdkgc;

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangql);

        btnqlgc = findViewById(R.id.btnqlgc);
        btnqlkh = findViewById(R.id.btnqlkh);
        btnqllsnap = findViewById(R.id.btnqllsnap);
        btnqllsdkgc = findViewById(R.id.btnqllsdkgc);
        imageButton = findViewById(R.id.imgbtnback);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnqllsdkgc.setOnClickListener(v ->{
            Intent intent = new Intent(trangql.this, ql_lsdkgc.class);
            startActivity(intent);
        });

        btnqllsnap.setOnClickListener(v ->{
            Intent intent = new Intent(trangql.this, ql_lsnaptien.class);
            startActivity(intent);
        });

        btnqlgc.setOnClickListener(v -> {
            Intent intent = new Intent(trangql.this, ql_goicuoc.class);
            startActivity(intent);
        });

        btnqlkh.setOnClickListener(v -> {
            Intent intent = new Intent(trangql.this, ql_khachhang.class);
            startActivity(intent);
        });
    }
}