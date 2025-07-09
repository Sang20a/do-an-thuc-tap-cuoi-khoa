package com.example.mobifone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ctgoicuoc extends AppCompatActivity {

    private Button btnDK;
    private ImageButton imgbtntback;
    private TextView tenGoiTextView;
    private TextView giaCuocTextView;
    private TextView thoiGianTextView;
    private TextView daTaTextView;
    private DatabaseHelper dbHelper;
    private String loggedInSDT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctgoicuoc);

        dbHelper = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        loggedInSDT = prefs.getString("sdt", null);
        Log.d("ctgoicuoc", "Giá trị loggedInSDT khi onCreate (từ SharedPreferences): " + loggedInSDT);

        if (loggedInSDT == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin đăng nhập.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Intent intent = getIntent();
        String tenGoi = intent.getStringExtra("tengoi");
        double giaCuoc = intent.getDoubleExtra("gia", 0.0);
        String thoiGian = intent.getStringExtra("hansd");
        String dungLuong = intent.getStringExtra("dungluong");
        String idGoiCuoc = intent.getStringExtra("idgc");

        tenGoiTextView = findViewById(R.id.txttengoi);
        giaCuocTextView = findViewById(R.id.txttien);
        thoiGianTextView = findViewById(R.id.txthansd);
        daTaTextView = findViewById(R.id.txtdungluong);
        btnDK = findViewById(R.id.btnDK);
        imgbtntback = findViewById(R.id.imgbtnback);

        tenGoiTextView.setText("Tên gói: " + tenGoi);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);
        String giaCuocFormatted = formatter.format(giaCuoc) + " đ";

        giaCuocTextView.setText("Giá cước: " + giaCuocFormatted);
        thoiGianTextView.setText("Thời gian: " + thoiGian + " ngày");
        daTaTextView.setText(dungLuong);

        imgbtntback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final double finalGiaCuoc = giaCuoc;
        final String finalIdGoiCuoc = idGoiCuoc;
        final String finalTenGoi = tenGoi;

        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
                boolean isNhanVien = prefs.getBoolean("isNhanVien", false);

                if (isNhanVien) {
                    new AlertDialog.Builder(ctgoicuoc.this)
                            .setTitle("Đăng ký không thành công")
                            .setMessage("Nhân viên không được đăng ký gói cước")
                            .setPositiveButton("OK", null)
                            .setCancelable(false)
                            .show();
                }else {
                    kiemTraVaDangKy(loggedInSDT, finalGiaCuoc, finalIdGoiCuoc, finalTenGoi);
                }
            }
        });
    }

    private void kiemTraVaDangKy(String sdt, double giaCuoc, String idGoiCuocMoi, String tenGoiMoi) {
        try {
            dbHelper.openDataBase();
            dbHelper.getWritableDatabase().beginTransaction();

            Cursor cursor = dbHelper.getData("SELECT sodu, idgc FROM khachhang WHERE sdt = ?", new String[]{sdt});

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") int currentSodu = cursor.getInt(cursor.getColumnIndex("sodu"));
                @SuppressLint("Range") String currentIdGoiCuoc = cursor.getString(cursor.getColumnIndex("idgc"));

                if (currentSodu >= giaCuoc) {

                    String updateGoiCuocQuery = "UPDATE khachhang SET idgc = ? WHERE sdt = ?";
                    dbHelper.getWritableDatabase().execSQL(updateGoiCuocQuery, new Object[]{idGoiCuocMoi, sdt});

                    int soduMoi = currentSodu - (int) giaCuoc;
                    String updateSoduQuery = "UPDATE khachhang SET sodu = ? WHERE sdt = ?";
                    dbHelper.getWritableDatabase().execSQL(updateSoduQuery, new Object[]{soduMoi, sdt});

                    String iddk = java.util.UUID.randomUUID().toString();
                    String ngaydk = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());

                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    Cursor goiCuocCursor = dbHelper.getData("SELECT hansd FROM goicuoc WHERE idgc = ?", new String[]{idGoiCuocMoi});
                    int soNgayHanSuDung = 30;

                    if (goiCuocCursor != null && goiCuocCursor.moveToFirst()) {
                        @SuppressLint("Range") int hansd = goiCuocCursor.getInt(goiCuocCursor.getColumnIndex("hansd"));
                        soNgayHanSuDung = hansd;
                    }
                    if (goiCuocCursor != null) goiCuocCursor.close();

                    calendar = java.util.Calendar.getInstance();
                    calendar.add(java.util.Calendar.DAY_OF_MONTH, soNgayHanSuDung);
                    String ngayhh = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss", java.util.Locale.getDefault()).format(calendar.getTime());

                    String trangthai = "Đang sử dụng";

                    Cursor khCursor = dbHelper.getData("SELECT idkh FROM khachhang WHERE sdt = ?", new String[]{sdt});
                    if (khCursor != null && khCursor.moveToFirst()) {
                        @SuppressLint("Range") String idkh = khCursor.getString(khCursor.getColumnIndex("idkh"));

                        String insertQuery = "INSERT INTO dkgoicuoc (iddk, idkh, idgc, ngaydk, ngayhh, trangthai) VALUES (?, ?, ?, ?, ?, ?)";
                        dbHelper.getWritableDatabase().execSQL(insertQuery, new Object[]{iddk, idkh, idGoiCuocMoi, ngaydk, ngayhh, trangthai});
                    }
                    if (khCursor != null) khCursor.close();

                    dbHelper.getWritableDatabase().setTransactionSuccessful();

                    SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("idgc", idGoiCuocMoi);
                    editor.putInt("sodu", soduMoi);
                    editor.apply();

                    hienThiDialogThanhCong(tenGoiMoi);

                } else {
                    hienThiDialogKhongDuTien();
                }
                cursor.close();
            } else {
                Toast.makeText(this, "Lỗi: Không tìm thấy thông tin tài khoản.", Toast.LENGTH_SHORT).show();
            }

            dbHelper.getWritableDatabase().endTransaction();
        } catch (Exception e) {
            Log.e("ctgoicuoc", "Lỗi khi đăng ký gói cước: " + e.getMessage());
            if (dbHelper.getWritableDatabase().inTransaction()) {
                dbHelper.getWritableDatabase().endTransaction();
            }
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi đăng ký gói cước.", Toast.LENGTH_SHORT).show();
        } finally {
            dbHelper.close();
        }
    }

    private void hienThiDialogThanhCong(String tenGoi) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng ký")
                .setMessage("Bạn có chắc chắn muốn đăng ký gói cước: " + tenGoi + "?")
                .setPositiveButton("Đăng ký", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new AlertDialog.Builder(ctgoicuoc.this)
                                .setTitle("Đăng ký thành công")
                                .setMessage("Bạn đã đăng ký gói cước: " + tenGoi + " thành công")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ctgoicuoc.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void hienThiDialogKhongDuTien() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Số dư tài khoản không đủ để đăng ký gói cước này.");
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