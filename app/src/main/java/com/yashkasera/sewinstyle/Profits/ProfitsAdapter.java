package com.yashkasera.sewinstyle.Profits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yashkasera.sewinstyle.R;

import java.util.ArrayList;

public class ProfitsAdapter extends BaseAdapter {
    Context context;
    ArrayList<ProfitItems> arrayList;
    public ProfitsAdapter(Context context, ArrayList<ProfitItems> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProfitItems profitItems = arrayList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expense_info2, null);
        }
        TextView date = convertView.findViewById(R.id.date);
        date.setText(profitItems.getItem());
        TextView piece = convertView.findViewById(R.id.piece);
        piece.setText(profitItems.getCostPrice());
        TextView desc = convertView.findViewById(R.id.desc);
        desc.setText(profitItems.getSellingPrice());
        TextView amt = convertView.findViewById(R.id.amt);
        amt.setText(profitItems.getProfit());
        return convertView;
    }
}
