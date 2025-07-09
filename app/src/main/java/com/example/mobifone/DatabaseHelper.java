package com.example.mobifone;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "";
    private static String DB_NAME = "qlgoicuoc.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.context = context;
        DB_PATH = myContext.getApplicationInfo().dataDir + "/databases/";
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Lỗi khi sao chép cơ sở dữ liệu");
            }
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor getData(String sql) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public Cursor getData(String sql, String[] selectionArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql, selectionArgs);
    }

    public boolean kiemTraDangNhap(String sdt, String matkhau) {
        try {
            createDataBase();
            openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (sdt == null || matkhau == null) return false;

        Cursor cursor = myDataBase.rawQuery(
                "SELECT * FROM khachhang WHERE sdt = ? AND matkhau = ?",
                new String[]{sdt.trim(), matkhau.trim()}
        );

        boolean ketQua = cursor.moveToFirst();
        Log.d("DangNhap", ketQua ? "Đăng nhập thành công" : "Đăng nhập thất bại");

        cursor.close();
        close();

        return ketQua;
    }

    public boolean kiemTraDangNhapNhanVien(String sdtnv, String matkhau) {
        try {
            createDataBase();
            openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (sdtnv == null || matkhau == null) return false;

        Cursor cursor = myDataBase.rawQuery(
                "SELECT * FROM nhanvien WHERE sdtnv = ? AND mknv = ?", new String[]{sdtnv.trim(), matkhau.trim()}
        );

        boolean ketQua = cursor.moveToFirst();
        Log.d("DangNhapNV", ketQua ? "Đăng nhập nhân viên thành công" : "Thất bại");

        cursor.close();
        close();

        return ketQua;
    }

    public boolean napTien(String sdtKhach, int soTienNap) {
        SQLiteDatabase db = this.getWritableDatabase();

        String idkh = null;
        Cursor cursor = db.rawQuery("SELECT idkh FROM khachhang WHERE sdt = ?", new String[]{sdtKhach});
        if (cursor.moveToFirst()) {
            idkh = cursor.getString(0);
        } else {
            cursor.close();
            return false;
        }
        cursor.close();

        db.execSQL("UPDATE khachhang SET sodu = sodu + ? WHERE idkh = ?", new Object[]{soTienNap, idkh});

        String idnv = getIdNhanVienDangNhap();

        String idnt = "NT" + System.currentTimeMillis();

        String ngaynap = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        ContentValues values = new ContentValues();
        values.put("idnt", idnt);
        values.put("idnv", idnv);
        values.put("idkh", idkh);
        values.put("sotien", soTienNap);
        values.put("ngaynap", ngaynap);

        long result = db.insert("naptien", null, values);
        return result != -1;
    }

    public String getIdNhanVienDangNhap() {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String id = prefs.getString("idnv", "");
        Log.d("SharedPrefs", "ID nhân viên đang đăng nhập: " + id);
        return id;
    }

    public boolean huyGoiCuoc(String idkh, String idgc, String ngayhh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("trangthai", "Hủy gói");
        values.put("ngayhh", ngayhh);
        int rows1 = db.update("dkgoicuoc", values, "idkh = ? AND idgc = ?", new String[]{idkh, idgc});

        ContentValues khValues = new ContentValues();
        khValues.putNull("idgc");

        int rows2 = db.update("khachhang", khValues, "idkh = ?", new String[]{idkh});

        return rows1 > 0 && rows2 > 0;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String layMatKhauKhachHang(String idkh) {
        SQLiteDatabase db = this.getReadableDatabase();
        String pass = null;
        Cursor cursor = db.rawQuery("SELECT matkhau FROM khachhang WHERE idkh = ?", new String[]{idkh});
        if (cursor.moveToFirst()) {
            pass = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return pass;
    }

    public boolean capNhatMatKhauKhachHang(String idkh, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("matkhau", newPassword);
        int rows = db.update("khachhang", values, "idkh = ?", new String[]{idkh});
        db.close();
        return rows > 0;
    }


    public String laysdtTheoIdkh(String idkh) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sdt = null;
        Cursor cursor = db.rawQuery("SELECT sdt FROM khachhang WHERE idkh = ?", new String[]{idkh});
        if (cursor.moveToFirst()) {
            sdt = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return sdt;
    }

    public String laytenkhTheoIdkh(String idkh) {
        SQLiteDatabase db = this.getReadableDatabase();
        String tenkh = null;
        Cursor cursor = db.rawQuery("SELECT hoten FROM khachhang WHERE idkh = ?", new String[]{idkh});
        if (cursor.moveToFirst()) {
            tenkh = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return tenkh;
    }

    public String layTengcTheoIdgc(String idgc) {
        SQLiteDatabase db = this.getReadableDatabase();
        String tenGoi = null;
        Cursor cursor = db.rawQuery("SELECT tengoi FROM goicuoc WHERE idgc = ?", new String[]{idgc});
        if (cursor.moveToFirst()) {
            tenGoi = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return tenGoi;
    }

    public String layTennvTheoIdnv(String idnv) {
        SQLiteDatabase db = this.getReadableDatabase();
        String tennv = null;
        Cursor cursor = db.rawQuery("SELECT htnv FROM nhanvien WHERE idnv = ?", new String[]{idnv});
        if (cursor.moveToFirst()) {
            tennv = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return tennv;
    }
}
