package com.example.mobifone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class taikhoan_Fragment extends Fragment {

    private static final String TAG = "TaiKhoanFragment";
    private DatabaseHelper myDbHelper;
    private TextView txtTentk, txtSdt, txtSodu, txtGoiCuoc, txtdoimk;
    private Button btnDX, btnkhdkgc, btntrangql;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDbHelper = new DatabaseHelper(getContext());
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Log.e(TAG, "Không thể tạo cơ sở dữ liệu", ioe);
            if (isAdded() && getActivity() != null) {
                Toast.makeText(getActivity(), "Lỗi cơ sở dữ liệu!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taikhoan, container, false);

        txtTentk = view.findViewById(R.id.txttentk);
        txtSdt = view.findViewById(R.id.txtsdt);
        txtSodu = view.findViewById(R.id.txtsodu);
        txtGoiCuoc = view.findViewById(R.id.txtidgc);
        btnDX = view.findViewById(R.id.btnDX);
        btnkhdkgc = view.findViewById(R.id.btnkhdkgc);
        btntrangql = view.findViewById(R.id.btntrangql);
        txtdoimk = view.findViewById(R.id.txtdoimk);

        txtdoimk.setOnClickListener(v -> {
            SharedPreferences prefs = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            String sdt = prefs.getString("sdt", null);

            if (sdt != null) {
                try {
                    myDbHelper.openDataBase();
                    Cursor cursor = myDbHelper.getData("SELECT idkh FROM khachhang WHERE sdt = ?", new String[]{sdt});

                    if (cursor != null && cursor.moveToFirst()) {
                        @SuppressLint("Range") String idkh = cursor.getString(cursor.getColumnIndex("idkh"));
                        cursor.close();

                        Intent intent = new Intent(getActivity(), doimatkhau.class);
                        intent.putExtra("idkh", idkh);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Không tìm thấy khách hàng", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Lỗi DB: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    myDbHelper.close();
                }
            } else {
                Toast.makeText(getContext(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            }
        });


        btntrangql.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity(), trangql.class);
            startActivity(intent);
        });

        btnkhdkgc.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), kh_goicuoc.class);
            startActivity(intent);
        });

        txtGoiCuoc.setText("Gói cước: Chưa đăng ký");

        btnDX.setOnClickListener(v -> {
            if (getActivity() != null) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Xác nhận đăng xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                        .setPositiveButton("Có", (dialog, which) -> {

                            SharedPreferences prefs = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.clear();
                            editor.apply();

                            Intent intent = new Intent(getActivity(), dangnhap.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume được gọi, đang tải thông tin tài khoản...");
        loadThongTinTaiKhoan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("Range")
    private void loadThongTinTaiKhoan() {
        if (!isAdded() || getActivity() == null || getContext() == null) return;

        SharedPreferences prefs = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String sdt = prefs.getString("sdt", null);
        boolean isNhanVien = prefs.getBoolean("isNhanVien", false);

        if (sdt == null) {
            txtTentk.setText("Lỗi: Chưa đăng nhập.");
            txtSdt.setText("");
            txtSodu.setText("");
            txtGoiCuoc.setText("Thông tin: Không rõ");
            return;
        }

        Cursor cursorKH = null;
        Cursor cursorNV = null;
        Cursor cursorDKGC = null;
        Cursor cursorGoiCuoc = null;

        try {
            myDbHelper.openDataBase();

            if (isNhanVien) {
                cursorNV = myDbHelper.getData("SELECT htnv FROM nhanvien WHERE sdtnv = ?", new String[]{sdt});
                if (cursorNV != null && cursorNV.moveToFirst()) {
                    String hoten = cursorNV.getString(cursorNV.getColumnIndex("htnv"));
                    txtTentk.setText("Tên nhân viên: " + hoten);
                    txtSdt.setText("SĐT nhân viên: " + sdt);
                    txtSodu.setText("");
                    txtGoiCuoc.setText("Vai trò: Nhân viên");

                    btnkhdkgc.setVisibility(View.GONE);
                    btntrangql.setVisibility(View.VISIBLE);
                    txtdoimk.setVisibility(View.GONE);
                } else {
                    txtTentk.setText("Không tìm thấy nhân viên.");
                    txtSdt.setText("");
                    txtGoiCuoc.setText("Thông tin: Không rõ");
                }
            } else {
                cursorKH = myDbHelper.getData("SELECT hoten, sodu FROM khachhang WHERE sdt = ?", new String[]{sdt});
                if (cursorKH != null && cursorKH.moveToFirst()) {
                    String hoten = cursorKH.getString(cursorKH.getColumnIndex("hoten"));
                    double sodu = cursorKH.getDouble(cursorKH.getColumnIndex("sodu"));
                    capNhatTrangThaiGoiCuoc();
                    txtTentk.setText("Họ tên: " + hoten);
                    txtSdt.setText("SĐT: " + sdt);
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setGroupingSeparator('.');
                    DecimalFormat formatter = new DecimalFormat("#,###");
                    formatter.setDecimalFormatSymbols(symbols);
                    txtSodu.setText("Số dư: " + formatter.format(sodu) + " đ");

                    cursorDKGC = myDbHelper.getData(
                            "SELECT idgc FROM dkgoicuoc WHERE idkh = (SELECT idkh FROM khachhang WHERE sdt = ?) AND trangthai = 'Đang sử dụng'",
                            new String[]{sdt});

                    if (cursorDKGC != null && cursorDKGC.moveToFirst()) {
                        String idgc = cursorDKGC.getString(cursorDKGC.getColumnIndex("idgc"));
                        cursorGoiCuoc = myDbHelper.getData("SELECT tengoi FROM goicuoc WHERE idgc = ?", new String[]{idgc});
                        if (cursorGoiCuoc != null && cursorGoiCuoc.moveToFirst()) {
                            txtGoiCuoc.setText("Gói cước: " + cursorGoiCuoc.getString(cursorGoiCuoc.getColumnIndex("tengoi")));
                            btntrangql.setVisibility(View.GONE);
                        } else {
                            txtGoiCuoc.setText("Gói cước: Không tìm thấy");
                        }
                    } else {
                        txtGoiCuoc.setText("Gói cước: Chưa đăng ký");

                        btntrangql.setVisibility(View.GONE);
                        btnkhdkgc.setVisibility(View.VISIBLE);
                    }
                } else {
                    txtTentk.setText("Không tìm thấy khách hàng.");
                    txtSdt.setText("");
                    txtGoiCuoc.setText("Gói cước: Không rõ");
                }
            }

        } catch (Exception e) {
            txtTentk.setText("Lỗi: " + e.getMessage());
            txtGoiCuoc.setText("Thông tin: Lỗi");
        } finally {
            if (cursorKH != null) cursorKH.close();
            if (cursorNV != null) cursorNV.close();
            if (cursorDKGC != null) cursorDKGC.close();
            if (cursorGoiCuoc != null) cursorGoiCuoc.close();
            myDbHelper.close();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void capNhatTrangThaiGoiCuoc() {
        try {
            myDbHelper.openDataBase();

            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            String sql = "UPDATE dkgoicuoc SET trangthai = 'Đã hủy' " +
                    "WHERE trangthai = 'Đang sử dụng' AND date(ngayhh) < date(?)";
            myDbHelper.getWritableDatabase().execSQL(sql, new Object[]{currentDate});

        } catch (Exception e) {
            Log.e("CapNhatTrangThai", "Lỗi cập nhật trạng thái: " + e.getMessage());
        } finally {
            myDbHelper.close();
        }
    }

}
