package com.example.mobifone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class ql_naptienkh extends AppCompatActivity {

    public EditText edtsdt, edtsotien, edtnhapso;
    public RadioButton radio10000, radio20000, radio50000, radio100000, radio200000, radio500000;
    public CheckBox chktien;
    public Button btnTiepTuc;
    public ImageButton imageButton;
    public  DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_naptienkh);

        edtsdt = findViewById(R.id.edtnapsdtkh);
        edtsotien = findViewById(R.id.edtnapsotien);
        edtnhapso = findViewById(R.id.edtnapnhapso);
        chktien = findViewById(R.id.chktien);
        btnTiepTuc = findViewById(R.id.btnttnaptien);
        imageButton = findViewById(R.id.imgbtnback);
        db = new DatabaseHelper(this);

        radio10000 = findViewById(R.id.radio_10000);
        radio20000 = findViewById(R.id.radio_20000);
        radio50000 = findViewById(R.id.radio_50000);
        radio100000 = findViewById(R.id.radio_100000);
        radio200000 = findViewById(R.id.radio_200000);
        radio500000 = findViewById(R.id.radio_500000);

        Intent intent = getIntent();
        String sdt = intent.getStringExtra("sdt");
        edtsdt.setText(sdt);

        View.OnClickListener radioClickListener = v -> {
            chktien.setChecked(false);
            uncheckAllRadiosExcept((RadioButton) v);
            edtsotien.setText(((RadioButton) v).getText().toString().replace(".", "").replace("đ", "").trim());
        };

        radio10000.setOnClickListener(radioClickListener);
        radio20000.setOnClickListener(radioClickListener);
        radio50000.setOnClickListener(radioClickListener);
        radio100000.setOnClickListener(radioClickListener);
        radio200000.setOnClickListener(radioClickListener);
        radio500000.setOnClickListener(radioClickListener);

        chktien.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                uncheckAllRadios();
                String sotien = edtnhapso.getText().toString().trim();
                if (sotien.isEmpty()) {
                    edtnhapso.setError("Bạn chưa nhập số tiền");
                    chktien.setChecked(false);
                } else {
                    try {
                        int tien = Integer.parseInt(sotien);
                        if (tien < 2000) {
                            edtnhapso.setError("Số tiền phải lớn hơn 2.000 đ");
                            chktien.setChecked(false);
                        } else {
                            edtsotien.setText(String.valueOf(tien));
                        }
                    } catch (Exception e) {
                        edtnhapso.setError("Số tiền không hợp lệ");
                        chktien.setChecked(false);
                    }
                }
            } else {
                edtnhapso.setText("");
                edtsotien.setText("");
            }
        });

        imageButton.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });

        btnTiepTuc.setOnClickListener(v -> {
            String sdtNhap = edtsdt.getText().toString().trim();
            String soTienNhap = edtsotien.getText().toString().trim();

            if (sdtNhap.isEmpty() || soTienNhap.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác thực nhân viên");

            final EditText inputPass = new EditText(this);
            inputPass.setHint("Nhập pass code");
            inputPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(inputPass);

            builder.setPositiveButton("Xác nhận", (dialog, which) -> {
                String passNhap = inputPass.getText().toString().trim();

                DatabaseHelper db = new DatabaseHelper(this);

                Cursor cursor = db.getData("SELECT idnv FROM nhanvien WHERE passcode = ?", new String[]{passNhap});
                if (cursor.moveToFirst()) {
                    String idnv = cursor.getString(0);

                    getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            .edit()
                            .putString("idnv", idnv)
                            .apply();

                    boolean success = db.napTien(sdtNhap, Integer.parseInt(soTienNhap));
                    if (success) {
                        new AlertDialog.Builder(this)
                                .setTitle("Nạp tiền thành công")
                                .setMessage("Bạn đã nạp " + soTienNhap + " đ thành công, vào tài khoản khách hàng " + sdtNhap)
                                .setPositiveButton("OK", (d, w) -> {
                                    Intent intent1 = new Intent(this, ql_khachhang.class);
                                    startActivity(intent1);
                                    finish();
                                })
                                .setCancelable(false)
                                .show();
                    } else {
                        Toast.makeText(this, "Nạp tiền thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Pass code nhân viên không đúng!", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            });

            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

            builder.show();
        });

    }

    private void uncheckAllRadios() {
        radio10000.setChecked(false);
        radio20000.setChecked(false);
        radio50000.setChecked(false);
        radio100000.setChecked(false);
        radio200000.setChecked(false);
        radio500000.setChecked(false);
    }

    private void uncheckAllRadiosExcept(RadioButton except) {
        for (RadioButton rb : new RadioButton[]{radio10000, radio20000, radio50000, radio100000, radio200000, radio500000}) {
            if (rb != except) rb.setChecked(false);
        }
    }
}
