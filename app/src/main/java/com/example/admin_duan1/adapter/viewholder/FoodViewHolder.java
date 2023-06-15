package com.example.admin_duan1.adapter.viewholder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_duan1.common.Common;
import com.example.admin_duan1.R;
import com.example.admin_duan1.interfaces.ItemClick;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public ImageView img_FoodItem;
    public TextView tv_nameFoodItem, tv_tienFoodItem, tv_desFoodItem;
    ItemClick itemClick;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        img_FoodItem = itemView.findViewById(R.id.img_FoodItem);
        tv_nameFoodItem = itemView.findViewById(R.id.tv_nameFoodItem);
        tv_tienFoodItem = itemView.findViewById(R.id.tv_TienFoodItem);
        tv_desFoodItem = itemView.findViewById(R.id.tv_desFoodItem);
//        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }


//    @Override
//    public void onClick(View view) {
//        itemClick.itemClick(view, getLayoutPosition(), false);
//    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,0,getLayoutPosition(),Common.UPDATE);
        menu.add(0,1,getLayoutPosition(),Common.DELETE);
    }
}
