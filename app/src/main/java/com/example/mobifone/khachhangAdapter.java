package com.example.mobifone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class khachhangAdapter extends ArrayAdapter<khachhang> {

    public khachhangAdapter(Context context, List<khachhang> khachHangList) {
        super(context, 0, khachHangList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        khachhang khachHang = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_khachhang, parent, false);
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);

        String formatted = formatter.format(khachHang.getSodu()) + " đ";

        TextView hotenkh = convertView.findViewById(R.id.txthotenkh);
        TextView sdtkh = convertView.findViewById(R.id.txtsdtkh);
        TextView idgckh = convertView.findViewById(R.id.txtidgckh);
        TextView sodukh = convertView.findViewById(R.id.txtsodukh);

        hotenkh.setText("Họ tên: " + khachHang.getHoten());
        sdtkh.setText("SDT: " + khachHang.getSdt());
        idgckh.setText(khachHang.getIdgc());
        sodukh.setText("Số dư: " + formatted);

        return convertView;
    }
}
