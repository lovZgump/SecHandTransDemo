package com.example.clayou.sechandtransdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 10295 on 2018/4/15.
 */

public class CommodityAdapter extends ArrayAdapter<Commodity> {

    private Context mContext;
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
        ViewHolder viewHolder;

        if (mContext == null) {
            mContext = parent.getContext();
        }

        if (convertView == null){
            view = LayoutInflater.from(mContext).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.commodityImg = view.findViewById(R.id.img);
            viewHolder.commodityName = view.findViewById(R.id.CommodityName);
            viewHolder.commodityPrice = view.findViewById(R.id.CommodityPrice);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }



        // 获取图片
//        byte[] images = commodity.getImagePath();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(images, 0, images.length);
//        commodityImg.setImageBitmap(bitmap);

//        Bitmap bitmap = BitmapFactory.decodeFile(commodity.getImagePath());
//        viewHolder.commodityImg.setImageBitmap(bitmap);

        Glide.with(mContext).load(commodity.getImagePath()).into(viewHolder.commodityImg);

        viewHolder.commodityName.setText(commodity.getName());
        viewHolder.commodityPrice.setText(String.valueOf(commodity.getPrice())+"元");
        return view;
    }


    class ViewHolder {

        ImageView commodityImg;

        TextView commodityName;

        TextView commodityPrice;
    }
}
