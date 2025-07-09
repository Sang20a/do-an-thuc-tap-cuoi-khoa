package com.example.mobifone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class qlitem_goicuoc extends AppCompatActivity {

    private Button btnsuagoi, btnxoagc;
    private DatabaseHelper dbHelper;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlitem_goicuoc);

        dbHelper = new DatabaseHelper(this);

        btnsuagoi = findViewById(R.id.btnsuagoi);
        btnxoagc = findViewById(R.id.btnxoagc);
        imageButton = findViewById(R.id.imgbtnback);

        Intent intent = getIntent();
        String idgc = intent.getStringExtra("idgc");
        String tenGoi = intent.getStringExtra("tengoi");
        double giaCuoc = intent.getDoubleExtra("gia", 0.0);
        String thoiGian = intent.getStringExtra("hansd");
        String dungLuong = intent.getStringExtra("dungluong");

        TextView tenGoiTextView = findViewById(R.id.txtten);
        TextView giaCuocTextView = findViewById(R.id.txtgia);
        TextView thoiGianTextView = findViewById(R.id.txtthoigian);
        TextView daTaTextView = findViewById(R.id.txtdata);

        tenGoiTextView.setText("Tên gói cước: " + tenGoi);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###");
        formatter.setDecimalFormatSymbols(symbols);

        String giaCuocFormatted = formatter.format(giaCuoc) + " đ";
        giaCuocTextView.setText("Giá gói: " + giaCuocFormatted);
        thoiGianTextView.setText("Thời gian sử dụng: " + thoiGian + " ngày");
        daTaTextView.setText("Dung lượng: " + dungLuong);


        btnsuagoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qlitem_goicuoc.this, ql_suagc.class);
                intent.putExtra("idgc", idgc);
                intent.putExtra("tengoi", tenGoi);
                intent.putExtra("gia", giaCuoc);
                intent.putExtra("hansd", thoiGian);
                intent.putExtra("dungluong", dungLuong);
                startActivity(intent);
            }
        });

        btnxoagc.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc muốn xóa gói cước này?");
            builder.setPositiveButton("Có", (dialog, which) -> {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int deletedRows = db.delete("goicuoc", "idgc=?", new String[]{idgc});
                if (deletedRows > 0) {
                    Toast.makeText(this, "Xóa gói cước thành công", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(qlitem_goicuoc.this, ql_goicuoc.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(this, "Xóa gói cước thất bại", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Không", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setCancelable(false);
            builder.show();
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}