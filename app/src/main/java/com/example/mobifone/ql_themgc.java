package com.example.mobifone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ql_themgc extends AppCompatActivity {

    private EditText edttengc, edtgiagc, edtdungl, edthansd;
    private Button btnthemgoi, btnhuythem;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_themgc);

        edttengc = findViewById(R.id.edttengc);
        edtgiagc = findViewById(R.id.edtgiagc);
        edtdungl = findViewById(R.id.edtdungl);
        edthansd = findViewById(R.id.edthansd);
        btnthemgoi = findViewById(R.id.btnthemgoi);
        btnhuythem = findViewById(R.id.btnhuythem);

        dbHelper = new DatabaseHelper(this);

        btnthemgoi.setOnClickListener(v -> {
            String tenGoi = edttengc.getText().toString().trim();
            String gia = edtgiagc.getText().toString().trim();
            String dungLuong = edtdungl.getText().toString().trim();
            String hanSd = edthansd.getText().toString().trim();

            if (tenGoi.isEmpty()) {
                edttengc.setError("Vui lòng nhập tên gói cước!");
                edttengc.requestFocus();
                return;
            }
            if (gia.isEmpty()) {
                edtgiagc.setError("Vui lòng nhập giá gói cước!");
                edtgiagc.requestFocus();
                return;
            }
            if (dungLuong.isEmpty()) {
                edtdungl.setError("Vui lòng nhập dung lượng!");
                edtdungl.requestFocus();
                return;
            }
            if (hanSd.isEmpty()) {
                edthansd.setError("Vui lòng nhập hạn sử dụng!");
                edthansd.requestFocus();
                return;
            }

            if (tenGoi.isEmpty() || gia.isEmpty() || dungLuong.isEmpty() || hanSd.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                String idgc = generateNextId();

                ContentValues values = new ContentValues();
                values.put("idgc", idgc);
                values.put("tengoi", tenGoi);
                values.put("gia", Double.parseDouble(gia));
                values.put("dungluong", dungLuong);
                values.put("hansd", hanSd);

                long result = dbHelper.getWritableDatabase().insert("goicuoc", null, values);
                if (result != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Thêm gói cước thành công.");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(ql_themgc.this, ql_goicuoc.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Thêm gói cước thất bại!");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            }
        });

        btnhuythem.setOnClickListener(v -> finish());

    }

    private String generateNextId() {
        String prefix = "gc";
        int maxNumber = 11;

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT idgc FROM goicuoc", null);
        if (cursor.moveToFirst()) {
            do {
                String idgc = cursor.getString(cursor.getColumnIndexOrThrow("idgc"));
                if (idgc.startsWith(prefix)) {
                    String numberPart = idgc.substring(prefix.length());
                    try {
                        int number = Integer.parseInt(numberPart);
                        if (number > maxNumber) {
                            maxNumber = number;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        int nextNumber = maxNumber + 1;
        return prefix + String.format("%02d", nextNumber);
    }
}