package com.example.clayou.sechandtransdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 10295 on 2018/4/15.
 */

public class CommodityAdapter extends ArrayAdapter<Commodity> {

    private int resourceId;

    public CommodityAdapter(Context context, int textViewResourceId, List<Commodity> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Commodity commodity = getItem(position);
        View view;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }else {
            view = convertView;
        }
        ImageView commodityImg = view.findViewById(R.id.img);
        TextView commodityName = view.findViewById(R.id.CommodityName);
        TextView commodityPrice = view.findViewById(R.id.CommodityPrice);
        commodityImg.setImageResource(commodity.getImageId());
        commodityName.setText(commodity.getName());
        commodityPrice.setText(String.valueOf(commodity.getPrice())+"元");
        return view;
    }
}
