package com.example.admin_duan1.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_duan1.R;

public class InfoDonHangViewHolder extends RecyclerView.ViewHolder {

    public ImageView img_FoodItemTT;
    public TextView tv_nameFoodItemTT, tv_TienFoodItemTT, tv_SlFoodItemTT;


    public InfoDonHangViewHolder(@NonNull View itemView) {
        super(itemView);

        img_FoodItemTT = itemView.findViewById(R.id.img_FoodItemTT);
        tv_nameFoodItemTT = itemView.findViewById(R.id.tv_nameFoodItemTT);
        tv_TienFoodItemTT = itemView.findViewById(R.id.tv_TienFoodItemTT);
        tv_SlFoodItemTT = itemView.findViewById(R.id.tv_SlFoodItemTT);


    }
}
