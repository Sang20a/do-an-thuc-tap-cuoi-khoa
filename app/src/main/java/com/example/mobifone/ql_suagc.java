package com.example.mobifone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ql_suagc extends AppCompatActivity {

    private EditText edtTenGoi, edtGia, edtDungLuong, edtHanSD;
    private Button btnLuu, btnhuy;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_suagc);

        edtTenGoi = findViewById(R.id.edtsuaten);
        edtGia = findViewById(R.id.edtsuagia);
        edtDungLuong = findViewById(R.id.edtsuadungl);
        edtHanSD = findViewById(R.id.edtsuahansd);
        btnLuu = findViewById(R.id.btnsuagc);
        btnhuy = findViewById(R.id.btnhuysua);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String idgc = intent.getStringExtra("idgc");
        String tenGoi = intent.getStringExtra("tengoi");
        double gia = intent.getDoubleExtra("gia", 0);
        String dungLuong = intent.getStringExtra("dungluong");
        String hanSD = intent.getStringExtra("hansd");

        edtTenGoi.setText(tenGoi);
        edtGia.setText(String.valueOf((int) gia));
        edtDungLuong.setText(dungLuong);
        edtHanSD.setText(hanSD);

        btnLuu.setOnClickListener(v -> {
            String tenGoiMoi = edtTenGoi.getText().toString().trim();
            String giaMoiStr = edtGia.getText().toString().trim();
            String dungLuongMoi = edtDungLuong.getText().toString().trim();
            String hanSDMoi = edtHanSD.getText().toString().trim();
            double giaMoi = Double.parseDouble(giaMoiStr);

            if (tenGoiMoi.isEmpty()) {
                edtTenGoi.setError("Vui lòng nhập tên gói cước!");
                edtTenGoi.requestFocus();
                return;
            }
            if (giaMoiStr.isEmpty()) {
                edtGia.setError("Vui lòng nhập giá gói cước!");
                edtGia.requestFocus();
                return;
            }
            if (dungLuongMoi.isEmpty()) {
                edtDungLuong.setError("Vui lòng nhập dung lượng!");
                edtDungLuong.requestFocus();
                return;
            }
            if (hanSDMoi.isEmpty()) {
                edtHanSD.setError("Vui lòng nhập hạn sử dụng!");
                edtHanSD.requestFocus();
                return;
            }

            if (tenGoiMoi.isEmpty() || giaMoiStr.isEmpty() || dungLuongMoi.isEmpty() || hanSDMoi.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("tengoi", tenGoiMoi);
            values.put("gia", giaMoi);
            values.put("dungluong", dungLuongMoi);
            values.put("hansd", hanSDMoi);

            int rowsAffected = db.update("goicuoc", values, "idgc=?", new String[]{idgc});

            if (rowsAffected > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Thông báo");
                builder.setMessage("Sửa gói cước thành công.");
                builder.setCancelable(false);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(ql_suagc.this, ql_goicuoc.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Thông báo");
                builder.setMessage("Sửa gói cước thất bại!");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });

        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}