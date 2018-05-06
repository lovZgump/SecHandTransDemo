package com.example.clayou.sechandtransdemo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 10295 on 2018/5/6.
 */

public class CommodityRecyAdapter extends RecyclerView.Adapter<CommodityRecyAdapter.ViewHolder> {

    private Context mContext;

    private List<Commodity> mCommodityList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView commodityImg;
        TextView commodityName;
        TextView commodityPrice;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            commodityImg = view.findViewById(R.id.img);
            commodityName = view.findViewById(R.id.CommodityName);
            commodityPrice = view.findViewById(R.id.CommodityPrice);
        }
    }


    public CommodityRecyAdapter(List<Commodity> commodityList) {
        this.mCommodityList = commodityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.commodity,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Commodity commodity = mCommodityList.get(position);
        holder.commodityName.setText(commodity.getName());
        holder.commodityPrice.setText(String.valueOf(commodity.getPrice())+"元");
        Glide.with(mContext).load(commodity.getImagePath()).into(holder.commodityImg);
    }

    @Override
    public int getItemCount() {
        return mCommodityList.size();
    }
}
