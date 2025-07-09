package com.example.mobifone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class gioithieu extends AppCompatActivity {

    private Button btnDNTK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gioithieu);

        btnDNTK = findViewById(R.id.btnDN);

        btnDNTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(gioithieu.this, dangnhap.class);
                startActivity(intent);
            }
        });
    }
}
