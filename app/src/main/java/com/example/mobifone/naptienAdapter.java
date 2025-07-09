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

public class naptienAdapter extends ArrayAdapter<naptien> {

    public naptienAdapter(Context context, List<naptien> napTienList) {
        super(context, 0, napTienList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        naptien napTien = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_qlitem_lsnaptien, parent, false);
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);

        String formatted = formatter.format(napTien.getSotien()) + " đ";

        TextView txtidkh = convertView.findViewById(R.id.txtlsidkh);
        TextView txtidnv = convertView.findViewById(R.id.txtlsidnv);
        TextView txtngaynap = convertView.findViewById(R.id.txtlsngaynap);
        TextView txtsotien = convertView.findViewById(R.id.txtlssotien);

        txtidkh.setText(napTien.getIdkh());
        txtsotien.setText("Số tiền nạp: " + formatted);
        txtidnv.setText(napTien.getIdnv());
        txtngaynap.setText("Ngày nạp tiền: " + napTien.getNgaynap());

        return convertView;
    }
}
