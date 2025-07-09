package com.example.mobifone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class dkgoicuocAdapter extends ArrayAdapter<dkgoicuoc> {

    private boolean isNhanVien;
    private DatabaseHelper dbHelper;

    public dkgoicuocAdapter(Context context, List<dkgoicuoc> dkgoiCuocList, boolean isNhanVien) {
        super(context, 0, dkgoiCuocList);
        this.isNhanVien = isNhanVien;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        dkgoicuoc dkgoiCuoc = getItem(position);

        if (convertView == null) {
            int layoutId = isNhanVien ? R.layout.activity_qlitem_dkgoicuoc : R.layout.activity_khitem_dkgoicuoc;
            convertView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        }

        TextView txtidkh, txtidgc, txtngaydk, txtngayhh, txttrangthai;

        if (isNhanVien) {
            txtidkh = convertView.findViewById(R.id.txtqldkidkh);
            txtidgc = convertView.findViewById(R.id.txtqldkidgc);
            txtngaydk = convertView.findViewById(R.id.txtqldkngaydk);
            txtngayhh = convertView.findViewById(R.id.txtqldkngayhh);
            txttrangthai = convertView.findViewById(R.id.txtqldktt);
        } else {
            txtidkh = convertView.findViewById(R.id.txtkhidkh);
            txtidgc = convertView.findViewById(R.id.txtkhidgc);
            txtngaydk = convertView.findViewById(R.id.txtkhngaydk);
            txtngayhh = convertView.findViewById(R.id.txtkhngayhh);
            txttrangthai = convertView.findViewById(R.id.txtkhtt);
        }

        String sdtKH = dbHelper.laysdtTheoIdkh(dkgoiCuoc.getIdkh());

        String tenGoi = dbHelper.layTengcTheoIdgc(dkgoiCuoc.getIdgc());

        txtidkh.setText("SĐT KH: " + (sdtKH != null ? sdtKH : "Không rõ"));
        txtidgc.setText("Tên gói: " + (tenGoi != null ? tenGoi : "Không rõ"));

        txtngaydk.setText("Ngày ĐK: " + dkgoiCuoc.getNgaydk());
        txtngayhh.setText("Ngày HH: " + dkgoiCuoc.getNgayhh());
        txttrangthai.setText("Trạng thái: " + dkgoiCuoc.getTrangthai());

        return convertView;
    }
}
