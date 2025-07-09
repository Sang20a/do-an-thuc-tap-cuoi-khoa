package com.example.mobifone;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import com.example.mobifone.dangky;


public class dangky extends AppCompatActivity {

    private Button btnHDK, btnDK;
    private EditText edtSDT, edtCCCD, edtHT, edtNS, edtMK;
    private Calendar calendar;
    private Spinner spinner;
    private CheckBox chkHMK;
    private RadioGroup rdgGioiTinh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);

        btnHDK = findViewById(R.id.btnHDKTK);
        btnDK = findViewById(R.id.btnDKTK);

        edtSDT = findViewById(R.id.edtSDT);
        edtCCCD = findViewById(R.id.edtCCCD);
        edtHT = findViewById(R.id.edtHT);
        edtNS = findViewById(R.id.edtNS);
        edtMK = findViewById(R.id.edtMK);

        calendar = Calendar.getInstance();
        spinner = findViewById(R.id.spinner);

        chkHMK = findViewById(R.id.chkMK);
        rdgGioiTinh = findViewById(R.id.radioGroup);

        edtNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(dangky.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

                                String formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                                edtNS.setText(formattedDate);
                            }
                        }, year, month, day);

                Calendar limitDate = Calendar.getInstance();
                limitDate.add(Calendar.YEAR, -14);

                datePickerDialog.getDatePicker().setMaxDate(limitDate.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        btnHDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String[] dauso = {"090", "093", "089", "070", "079", "077", "076", "078"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dauso);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        chkHMK.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                edtMK.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                edtMK.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            edtMK.setSelection(edtMK.getText().length());
        });

        btnDK.setOnClickListener(v -> {
            String sdt = edtSDT.getText().toString().trim();
            String cccd = edtCCCD.getText().toString().trim();
            String hoten = edtHT.getText().toString().trim();
            String ngaysinh = edtNS.getText().toString().trim();
            String pass = edtMK.getText().toString().trim();
            int selectedId = rdgGioiTinh.getCheckedRadioButtonId();
            String gioitinh = "Nam";
            String pass1 = edtMK.getText().toString().trim();
            String hashedPass = DatabaseHelper.hashPassword(pass1);

            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                gioitinh = selectedRadioButton.getText().toString();
            }

            String dauso1 = spinner.getSelectedItem().toString();
            String sdtFull = dauso1 + sdt;

            DatabaseHelper dbHelperSDT = new DatabaseHelper(dangky.this);
            SQLiteDatabase dbSDT = dbHelperSDT.getReadableDatabase();
            Cursor checkSDT = dbSDT.rawQuery("SELECT * FROM khachhang WHERE sdt = ?", new String[]{sdtFull});
            if (checkSDT.moveToFirst()) {
                edtSDT.setError("Số điện thoại này đã tồn tại!");
                edtSDT.requestFocus();
                checkSDT.close();
                return;
            }
            checkSDT.close();

            DatabaseHelper dbHelper1 = new DatabaseHelper(dangky.this);
            SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
            android.database.Cursor checkCCCD = db1.rawQuery("SELECT * FROM khachhang WHERE cccd = ?", new String[]{cccd});
            if (checkCCCD.moveToFirst()) {
                edtCCCD.setError("CCCD này đã tồn tại!");
                edtCCCD.requestFocus();
                checkCCCD.close();
                return;
            }
            checkCCCD.close();

            boolean hasError = false;

            if (sdt.isEmpty()) {
                edtSDT.setError("Vui lòng nhập SDT!");
                if (!hasError) {
                    edtSDT.requestFocus();
                    hasError = true;
                }
            } else if (!sdt.matches("\\d{7}")) {
                edtSDT.setError("Cần thêm 7 chữ số!");
                if (!hasError) {
                    edtSDT.requestFocus();
                    hasError = true;
                }
            }

            if (cccd.isEmpty()) {
                edtCCCD.setError("Vui lòng nhập CCCD!");
                if (!hasError) {
                    edtCCCD.requestFocus();
                    hasError = true;
                }
            } else if (!cccd.matches("\\d{12}")) {
                edtCCCD.setError("CCCD phải có 12 số!");
                if (!hasError) {
                    edtCCCD.requestFocus();
                    hasError = true;
                }
            }

            if (hoten.isEmpty()) {
                edtHT.setError("Vui lòng nhập họ tên!");
                if (!hasError) {
                    edtHT.requestFocus();
                    hasError = true;
                }
            }

            if (ngaysinh.isEmpty()) {
                edtNS.setError("Vui lòng chọn ngày sinh!");
                if (!hasError) {
                    edtNS.requestFocus();
                    hasError = true;
                }
            }

            if (pass.isEmpty()) {
                edtMK.setError("Vui lòng nhập mật khẩu!");
                if (!hasError) {
                    edtMK.requestFocus();
                    hasError = true;
                }
            }

            if (!hasError) {
                String dauso2 = spinner.getSelectedItem().toString();
                String sdtFull2 = dauso2 + sdt;

                DatabaseHelper dbHelper = new DatabaseHelper(dangky.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String query = "SELECT idkh FROM khachhang ORDER BY idkh DESC LIMIT 1";
                android.database.Cursor cursor = db.rawQuery(query, null);

                String newIdKh;
                if (cursor.moveToFirst()) {
                    String lastIdKh = cursor.getString(0);
                    int number = Integer.parseInt(lastIdKh.substring(2));
                    number++;
                    newIdKh = String.format("kh%02d", number);
                } else {
                    newIdKh = "kh01";
                }
                cursor.close();

                ContentValues values = new ContentValues();
                values.put("idkh", newIdKh);
                values.put("sdt", sdtFull2);
                values.put("cccd", cccd);
                values.put("hoten", hoten);
                values.put("ngaysinh", ngaysinh);
                values.put("gioitinh", gioitinh);
                values.put("matkhau", hashedPass);
                values.put("sodu", 0);

                long result = db.insert("khachhang", null, values);

                if (result != -1) {
                    Toast.makeText(dangky.this, "Đăng ký thành công!\nBạn hãy đăng nhập lại", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(dangky.this, dangnhap.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(dangky.this, "Đăng ký thất bại, thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}