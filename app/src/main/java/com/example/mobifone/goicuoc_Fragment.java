package com.example.mobifone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class goicuoc_Fragment extends Fragment {


    private DatabaseHelper myDbHelper;
    private ListView myListView;
    private List<goicuoc> goiCuocList;
    private goicuocAdapter goiCuocAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_goicuoc, container, false);

        myListView = view.findViewById(R.id.lvgc);
        myDbHelper = new DatabaseHelper(getContext());
        goiCuocList = new ArrayList<>();

        Spinner spinnerGia = view.findViewById(R.id.spntheogia);
        Spinner spinnerNgay = view.findViewById(R.id.spntheongay);

        ArrayAdapter<String> giaAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Tất cả", "Dưới 5.000", "5.000 - 50.000", "50.000 - 100.000", "Trên 100.000"});
        giaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGia.setAdapter(giaAdapter);

        ArrayAdapter<String> ngayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Tất cả", "1 - 7 ngày", "8 - 30 ngày", "Trên 30 ngày"});
        ngayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNgay.setAdapter(ngayAdapter);

        spinnerGia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int position, long id) {
                filterData(spinnerGia.getSelectedItem().toString(), spinnerNgay.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerNgay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int position, long id) {
                filterData(spinnerGia.getSelectedItem().toString(), spinnerNgay.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


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

        goiCuocAdapter = new goicuocAdapter(getContext(), goiCuocList);
        myListView.setAdapter(goiCuocAdapter);

        myListView.setOnItemClickListener((parent, view1, position, id) -> {
            goicuoc selected = goiCuocList.get(position);
            Intent intent = new Intent(getActivity(), ctgoicuoc.class);
            intent.putExtra("idgc", selected.getIdgc());
            intent.putExtra("tengoi", selected.getTengoi());
            intent.putExtra("gia", selected.getGia());
            intent.putExtra("dungluong", selected.getDungluong());
            intent.putExtra("hansd", selected.getHansd());
            startActivity(intent);
        });

        return view;
    }

    private void filterData(String giaFilter, String ngayFilter) {
        goiCuocList.clear();
        myDbHelper.openDataBase();

        String query = "SELECT * FROM goicuoc WHERE 1=1";

        switch (giaFilter) {
            case "Dưới 5.000":
                query += " AND gia <= 5000";
                break;
            case "5.000 - 50.000":
                query += " AND gia > 5000 AND gia <= 50000";
                break;
            case "50.000 - 100.000":
                query += " AND gia > 50000 AND gia <= 100000";
                break;
            case "Trên 100.000":
                query += " AND gia > 100000";
                break;
        }

        switch (ngayFilter) {
            case "1 - 7 ngày":
                query += " AND CAST(REPLACE(hansd, ' ngày', '') AS INTEGER) BETWEEN 1 AND 7";
                break;
            case "8 - 30 ngày":
                query += " AND CAST(REPLACE(hansd, ' ngày', '') AS INTEGER) BETWEEN 8 AND 30";
                break;
            case "Trên 30 ngày":
                query += " AND CAST(REPLACE(hansd, ' ngày', '') AS INTEGER) > 30";
                break;
        }

        Cursor cursor = myDbHelper.getData(query);
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

        goiCuocAdapter.notifyDataSetChanged();
    }


}