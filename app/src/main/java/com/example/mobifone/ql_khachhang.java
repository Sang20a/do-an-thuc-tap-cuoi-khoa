package com.example.mobifone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ql_khachhang extends AppCompatActivity {

    private DatabaseHelper myDbHelper;
    private ListView myListView;
    private List<khachhang> khachhangList;
    private khachhangAdapter khachhangAdapter;
    private Button btnthemkh, btntim;
    EditText edttim;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_khachhang);

        myListView = findViewById(R.id.ql_lvkh);
        myDbHelper = new DatabaseHelper(this);
        khachhangList = new ArrayList<>();
        btnthemkh = findViewById(R.id.btnthemkh);
        imageButton = findViewById(R.id.imgbtnback);
        btntim = findViewById(R.id.btntim);
        edttim = findViewById(R.id.edttimsdt);

        btntim.setOnClickListener(v -> {
            String sdtCanTim = edttim.getText().toString().trim();

            khachhangList.clear();
            myDbHelper.openDataBase();

            Cursor cursor;

            if (sdtCanTim.isEmpty()) {

                cursor = myDbHelper.getData("SELECT * FROM khachhang");
            } else if (sdtCanTim.length() != 10) {
                edttim.setError("Vui lòng nhập đúng 10 số!");
                myDbHelper.close();
                return;
            } else {
                cursor = myDbHelper.getData("SELECT * FROM khachhang WHERE sdt = '" + sdtCanTim + "'");
            }

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String idKH = cursor.getString(cursor.getColumnIndex("idkh"));
                    @SuppressLint("Range") String sDT= cursor.getString(cursor.getColumnIndex("sdt"));
                    @SuppressLint("Range") String hoTen = cursor.getString(cursor.getColumnIndex("hoten"));
                    @SuppressLint("Range") String cCCD = cursor.getString(cursor.getColumnIndex("cccd"));
                    @SuppressLint("Range") String ngaySinh = cursor.getString(cursor.getColumnIndex("ngaysinh"));
                    @SuppressLint("Range") String gioiTinh= cursor.getString(cursor.getColumnIndex("gioitinh"));
                    @SuppressLint("Range") String matKhau = cursor.getString(cursor.getColumnIndex("matkhau"));
                    @SuppressLint("Range") String idGC = cursor.getString(cursor.getColumnIndex("idgc"));
                    @SuppressLint("Range") double soDu = cursor.getDouble(cursor.getColumnIndex("sodu"));

                    khachhang kh = new khachhang(idKH, sDT, hoTen, cCCD, ngaySinh, gioiTinh, matKhau, idGC, soDu);
                    khachhangList.add(kh);
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "Không tìm thấy khách hàng!", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            myDbHelper.close();

            khachhangAdapter.notifyDataSetChanged();
        });


        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        myDbHelper.openDataBase();

        Cursor cursor = myDbHelper.getData("SELECT * FROM khachhang");
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String idKH = cursor.getString(cursor.getColumnIndex("idkh"));
                @SuppressLint("Range") String sDT= cursor.getString(cursor.getColumnIndex("sdt"));
                @SuppressLint("Range") String hoTen = cursor.getString(cursor.getColumnIndex("hoten"));
                @SuppressLint("Range") String cCCD = cursor.getString(cursor.getColumnIndex("cccd"));
                @SuppressLint("Range") String ngaySinh = cursor.getString(cursor.getColumnIndex("ngaysinh"));
                @SuppressLint("Range") String gioiTinh= cursor.getString(cursor.getColumnIndex("gioitinh"));
                @SuppressLint("Range") String matKhau = cursor.getString(cursor.getColumnIndex("matkhau"));
                @SuppressLint("Range") String idGC = cursor.getString(cursor.getColumnIndex("idgc"));
                @SuppressLint("Range") double soDu = cursor.getDouble(cursor.getColumnIndex("sodu"));
                khachhang khachHang = new khachhang(idKH, sDT, hoTen, cCCD, ngaySinh, gioiTinh, matKhau, idGC, soDu);
                khachhangList.add(khachHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        myDbHelper.close();

        khachhangAdapter = new khachhangAdapter(this, khachhangList);
        myListView.setAdapter(khachhangAdapter);

        myListView.setOnItemClickListener((parent, view1, position, id) -> {
            khachhang selected = khachhangList.get(position);
            Intent intent = new Intent(this, qlitem_khachhang.class);
            intent.putExtra("idkh", selected.getIdkh());
            intent.putExtra("sdt", selected.getSdt());
            intent.putExtra("hoten", selected.getHoten());
            intent.putExtra("cccd", selected.getCccd());
            intent.putExtra("ngaysinh", selected.getNgaysinh());
            intent.putExtra("gioitinh", selected.getGioitinh());
            intent.putExtra("matkhau", selected.getMatkhau());
            intent.putExtra("idgc", selected.getIdgc());
            intent.putExtra("sodu", selected.getSodu());
            startActivity(intent);
        });

        btnthemkh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ql_khachhang.this, dangky.class);
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