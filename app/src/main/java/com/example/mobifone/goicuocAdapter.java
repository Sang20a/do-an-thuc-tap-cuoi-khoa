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

public class goicuocAdapter extends ArrayAdapter<goicuoc> {

    public goicuocAdapter(Context context, List<goicuoc> goiCuocList) {
        super(context, 0, goiCuocList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        goicuoc goiCuoc = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_item_goicuoc, parent, false);
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);

        String formatted = formatter.format(goiCuoc.getGia()) + " đ";

        TextView tenGoiTextView = convertView.findViewById(R.id.txtten);
        TextView giaCuocTextView = convertView.findViewById(R.id.txtgia);
        TextView thoigianTextView = convertView.findViewById(R.id.txtthoigian);
        TextView dataTextView = convertView.findViewById(R.id.txtdata);

        tenGoiTextView.setText(goiCuoc.getTengoi());
        giaCuocTextView.setText(formatted);
        thoigianTextView.setText("Hạn sử dụng " + goiCuoc.getHansd() + " ngày");
        dataTextView.setText("Dung lượng " + goiCuoc.getDungluong());

        return convertView;
    }
}