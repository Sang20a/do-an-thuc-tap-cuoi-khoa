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

public class ql_lsnaptien extends AppCompatActivity {

    private DatabaseHelper myDbHelper;
    private ListView myListView;
    private List<naptien> napTienList;
    private naptienAdapter napTienAdapter;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_lsnaptien);

        myListView = findViewById(R.id.ql_lvnaptien);
        myDbHelper = new DatabaseHelper(this);
        napTienList = new ArrayList<>();
        imageButton = findViewById(R.id.imgbtnback);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        myDbHelper.openDataBase();

        Cursor cursor = myDbHelper.getData("SELECT * FROM naptien");
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String idnt = cursor.getString(cursor.getColumnIndex("idnt"));
                @SuppressLint("Range") String idnv = cursor.getString(cursor.getColumnIndex("idnv"));
                @SuppressLint("Range") String idkh = cursor.getString(cursor.getColumnIndex("idkh"));
                @SuppressLint("Range") double sotien = cursor.getDouble(cursor.getColumnIndex("sotien"));
                @SuppressLint("Range") String ngaynap = cursor.getString(cursor.getColumnIndex("ngaynap"));
                naptien napTien = new naptien(idnt, idnv, idkh, sotien, ngaynap);
                napTienList.add(napTien);
            } while (cursor.moveToNext());
        }
        cursor.close();
        myDbHelper.close();

        napTienAdapter = new naptienAdapter(this, napTienList);
        myListView.setAdapter(napTienAdapter);

        myListView.setOnItemClickListener((parent, view1, position, id) -> {
            naptien selected = napTienList.get(position);
            Intent intent = new Intent(this, qlitem_lsnaptien.class);
            intent.putExtra("idnt", selected.getIdnt());
            intent.putExtra("idnv", selected.getIdnv());
            intent.putExtra("idkh", selected.getIdkh());
            intent.putExtra("sotien", selected.getSotien());
            intent.putExtra("ngaynap", selected.getNgaynap());
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