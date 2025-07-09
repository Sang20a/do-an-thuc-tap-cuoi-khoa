package com.example.mobifone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ql_suakh extends AppCompatActivity {

    private EditText edtsuahotenkh, edtsuacccdkh, edtsuangaysinhkh;
    private RadioGroup radioGroup2;
    private RadioButton radioButtonNam, radioButtonNu;
    private Button btnsuattkh, btnhuysuakh;
    private DatabaseHelper dbHelper;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_suakh);

        edtsuahotenkh = findViewById(R.id.edtsuahotenkh);
        edtsuacccdkh = findViewById(R.id.edtsuacccdkh);
        edtsuangaysinhkh = findViewById(R.id.edtsuangaysinhkh);
        radioGroup2 = findViewById(R.id.radioGroup2);
        radioButtonNam = findViewById(R.id.radioButton);
        radioButtonNu = findViewById(R.id.radioButton2);
        btnsuattkh = findViewById(R.id.btnsuattkh);
        btnhuysuakh = findViewById(R.id.btnhuysuakh);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String idkh = intent.getStringExtra("idkh");
        String hoten = intent.getStringExtra("hoten");
        String cccd = intent.getStringExtra("cccd");
        String ngaysinh = intent.getStringExtra("ngaysinh");
        String gioitinh = intent.getStringExtra("gioitinh");

        edtsuahotenkh.setText(hoten);
        edtsuacccdkh.setText(cccd);
        edtsuangaysinhkh.setText(ngaysinh);

        if ("Nam".equalsIgnoreCase(gioitinh)) {
            radioButtonNam.setChecked(true);
        } else if ("Nữ".equalsIgnoreCase(gioitinh)) {
            radioButtonNu.setChecked(true);
        }

        btnsuattkh.setOnClickListener(v -> {
            String newHoten = edtsuahotenkh.getText().toString().trim();
            String newCccd = edtsuacccdkh.getText().toString().trim();
            String newNgaysinh = edtsuangaysinhkh.getText().toString().trim();
            String newGioitinh = radioButtonNam.isChecked() ? "Nam" : "Nữ";

            if (newHoten.isEmpty()) {
                edtsuahotenkh.setError("Vui lòng nhập họ tên!");
                edtsuahotenkh.requestFocus();
                return;
            }
            if (newCccd.isEmpty()) {
                edtsuacccdkh.setError("Vui lòng nhập CCCD!");
                edtsuacccdkh.requestFocus();
                return;
            }
            if (newNgaysinh.isEmpty()) {
                edtsuangaysinhkh.setError("Vui lòng nhập ngày sinh!");
                edtsuangaysinhkh.requestFocus();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("hoten", newHoten);
            values.put("cccd", newCccd);
            values.put("ngaysinh", newNgaysinh);
            values.put("gioitinh", newGioitinh);

            int rowsAffected = db.update("khachhang", values, "idkh=?", new String[]{idkh});

            if (rowsAffected > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Thông báo");
                builder.setMessage("Sửa thông tin khách hàng thành công.");
                builder.setCancelable(false);
                builder.setNegativeButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent1 = new Intent(ql_suakh.this, ql_khachhang.class);
                    startActivity(intent1);
                    finish();
                });
                builder.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Thông báo");
                builder.setMessage("Sửa thông tin khách hàng thất bại!");
                builder.setCancelable(false);
                builder.setNegativeButton("OK", (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        });

        btnhuysuakh.setOnClickListener(v -> finish());
    }
}