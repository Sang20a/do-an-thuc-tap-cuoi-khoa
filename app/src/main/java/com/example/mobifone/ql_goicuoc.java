package com.example.mobifone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ql_goicuoc extends AppCompatActivity {

    private DatabaseHelper myDbHelper;
    private ListView myListView;
    private List<goicuoc> goiCuocList;
    private goicuocAdapter goiCuocAdapter;
    private Button btnthemgc;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_goicuoc);

        myListView = findViewById(R.id.ql_lvgc);
        myDbHelper = new DatabaseHelper(this);
        goiCuocList = new ArrayList<>();
        btnthemgc = findViewById(R.id.btnthemgc);
        imageButton = findViewById(R.id.imgbtnback);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        myDbHelper.openDataBase();

        Cursor cursor = myDbHelper.getData("SELECT * FROM goicuoc");
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String idGC = cursor.getString(cursor.getColumnIndex("idgc"));
                @SuppressLint("Range") String tenGoi = cursor.getString(cursor.getColumnIndex("tengoi"));
                @SuppressLint("Range") double giaCuoc = cursor.getDouble(cursor.getColumnIndex("gia"));
                @SuppressLint("Range") String dungLuong = cursor.getString(cursor.getColumnIndex("dungluong"));
                @SuppressLint("Range") String hanSD = cursor.getString(cursor.getColumnIndex("hansd"));
                goicuoc goiCuoc = new goicuoc(idGC, tenGoi, giaCuoc, dungLuong, hanSD);
                goiCuocList.add(goiCuoc);
            } while (cursor.moveToNext());
        }
        cursor.close();
        myDbHelper.close();

        goiCuocAdapter = new goicuocAdapter(this, goiCuocList);
        myListView.setAdapter(goiCuocAdapter);

        myListView.setOnItemClickListener((parent, view1, position, id) -> {
            goicuoc selected = goiCuocList.get(position);
            Intent intent = new Intent(this, qlitem_goicuoc.class);
            intent.putExtra("idgc", selected.getIdgc());
            intent.putExtra("tengoi", selected.getTengoi());
            intent.putExtra("gia", selected.getGia());
            intent.putExtra("dungluong", selected.getDungluong());
            intent.putExtra("hansd", selected.getHansd());
            startActivity(intent);
        });

        btnthemgc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ql_goicuoc.this, ql_themgc.class);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}