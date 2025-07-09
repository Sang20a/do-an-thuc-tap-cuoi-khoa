package com.example.mobifone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ql_lsdkgc extends AppCompatActivity {

    private DatabaseHelper myDbHelper;
    private ListView myListView;
    private List<dkgoicuoc> dkgoiCuocList;
    private dkgoicuocAdapter dkgoiCuocAdapter;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_lsdkgc);

        myListView = findViewById(R.id.ql_lvdkgc);
        myDbHelper = new DatabaseHelper(this);
        dkgoiCuocList = new ArrayList<>();
        imageButton = findViewById(R.id.imgbtnback);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        myDbHelper.openDataBase();

        Cursor cursor = myDbHelper.getData("SELECT * FROM dkgoicuoc");
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String iddk = cursor.getString(cursor.getColumnIndex("iddk"));
                @SuppressLint("Range") String idkh = cursor.getString(cursor.getColumnIndex("idkh"));
                @SuppressLint("Range") String idgc = cursor.getString(cursor.getColumnIndex("idgc"));
                @SuppressLint("Range") String ngaydk = cursor.getString(cursor.getColumnIndex("ngaydk"));
                @SuppressLint("Range") String ngayhh = cursor.getString(cursor.getColumnIndex("ngayhh"));
                @SuppressLint("Range") String trangthai = cursor.getString(cursor.getColumnIndex("trangthai"));
                dkgoicuoc dkgoiCuoc = new dkgoicuoc(iddk, idkh, idgc, ngaydk, ngayhh, trangthai);
                dkgoiCuocList.add(dkgoiCuoc);
            } while (cursor.moveToNext());
        }
        cursor.close();
        myDbHelper.close();

        dkgoiCuocAdapter = new dkgoicuocAdapter(this, dkgoiCuocList, true);
        myListView.setAdapter(dkgoiCuocAdapter);

        myListView.setOnItemClickListener((parent, view1, position, id) -> {
            dkgoicuoc selected = dkgoiCuocList.get(position);
            Intent intent = new Intent(this, qlitem_dkgoicuoc.class);
            intent.putExtra("iddk", selected.getIddk());
            intent.putExtra("idkh", selected.getIdkh());
            intent.putExtra("idgc", selected.getIdgc());
            intent.putExtra("ngaydk", selected.getNgaydk());
            intent.putExtra("ngayhh", selected.getNgayhh());
            intent.putExtra("trangthai", selected.getTrangthai());
            startActivity(intent);
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}