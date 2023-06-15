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


public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener {


    public ImageView img_CateItem;
    public TextView tv_CateItem;
    public ItemClick itemClick;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        img_CateItem = itemView.findViewById(R.id.img_CateItem);
        tv_CateItem = itemView.findViewById(R.id.tv_CateItem);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public void onClick(View view) {
        itemClick.itemClick(view, getLayoutPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(0,0,getLayoutPosition(), Common.UPDATE);
        menu.add(0,1,getLayoutPosition(),Common.DELETE);

    }
}
