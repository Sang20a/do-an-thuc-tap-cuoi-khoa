package com.example.mobifone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class qlitem_khachhang extends AppCompatActivity {

    private Button btnsuaTTKH, btnxoakh, btnnaptienkh, btnttgckh;
    private DatabaseHelper dbHelper;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlitem_khachhang);

        dbHelper = new DatabaseHelper(this);

        btnsuaTTKH = findViewById(R.id.btnsuakh);
        btnxoakh = findViewById(R.id.btnxoakh);
        btnnaptienkh = findViewById(R.id.btnnaptienkh);
        btnttgckh = findViewById(R.id.btnttgckh);
        imageButton = findViewById(R.id.imgbtnback);

        Intent intent = getIntent();
        String idkh = intent.getStringExtra("idkh");
        String sdt = intent.getStringExtra("sdt");
        String hoten = intent.getStringExtra("hoten");
        String cccd = intent.getStringExtra("cccd");
        String ngaysinh = intent.getStringExtra("ngaysinh");
        String gioitinh = intent.getStringExtra("gioitinh");
        String matkhau = intent.getStringExtra("matkhau");
        String idgc = intent.getStringExtra("idgc");
        double sodu = intent.getDoubleExtra("sodu", 0.0);

        TextView txthotenkh = findViewById(R.id.txthoten);
        TextView txtsdtkh = findViewById(R.id.txtqlsdt);
        TextView txtcccdkh = findViewById(R.id.txtqlcccd);
        TextView txtngaysinhkh = findViewById(R.id.txtngaysinh);
        TextView txtgioitinhhk = findViewById(R.id.txtgioitinh);
        TextView txtidgckh = findViewById(R.id.txtqlidgckh);
        TextView txtsodukh = findViewById(R.id.txtqlsodu);

        txthotenkh.setText("Họ tên: " + hoten);
        txtsdtkh.setText("SDT: " + sdt);
        txtcccdkh.setText("CCCD: " + cccd);
        txtngaysinhkh.setText("Ngày sinh: " + ngaysinh);
        txtgioitinhhk.setText("Giới tính: " + gioitinh);
        txtidgckh.setText(idgc);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###");
        formatter.setDecimalFormatSymbols(symbols);

        String soDuFormatted = formatter.format(sodu) + " đ";
        txtsodukh.setText("Số dư: " + soDuFormatted);

        btnttgckh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(qlitem_khachhang.this, kh_goicuoc.class);
                intent1.putExtra("idkh", idkh);
                startActivity(intent1);
            }
        });

        btnsuaTTKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qlitem_khachhang.this, ql_suakh.class);
                intent.putExtra("idkh", idkh);
                intent.putExtra("hoten", hoten);
                intent.putExtra("sdt", sdt);
                intent.putExtra("cccd", cccd);
                intent.putExtra("ngaysinh", ngaysinh);
                intent.putExtra("gioitinh", gioitinh);
                intent.putExtra("matkhau", matkhau);
                intent.putExtra("idgc", idgc);
                intent.putExtra("sodu", sodu);
                startActivity(intent);
            }
        });

        btnxoakh.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc muốn xóa khách hàng này?");
            builder.setPositiveButton("Có", (dialog, which) -> {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int deletedRows = db.delete("khachhang", "idkh=?", new String[]{idkh});
                if (deletedRows > 0) {
                    Toast.makeText(this, "Xóa khách hàng thành công", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(qlitem_khachhang.this, ql_khachhang.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(this, "Xóa khách hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Không", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setCancelable(false);
            builder.show();
        });

        btnnaptienkh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qlitem_khachhang.this, ql_naptienkh.class);
                intent.putExtra("idkh", idkh);
                intent.putExtra("hoten", hoten);
                intent.putExtra("sdt", sdt);
                intent.putExtra("cccd", cccd);
                intent.putExtra("ngaysinh", ngaysinh);
                intent.putExtra("gioitinh", gioitinh);
                intent.putExtra("matkhau", matkhau);
                intent.putExtra("idgc", idgc);
                intent.putExtra("sodu", sodu);
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