package com.example.admin_duan1.adapter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_duan1.R;
import com.example.admin_duan1.adapter.viewholder.InfoDonHangViewHolder;
import com.example.admin_duan1.dto.OderDetail;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InfoDonHangAdapter extends RecyclerView.Adapter<InfoDonHangViewHolder> {

    public ArrayList<OderDetail> listOder;

    public InfoDonHangAdapter(ArrayList<OderDetail> listOder) {
        this.listOder = listOder;
    }

    @NonNull
    @Override
    public InfoDonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_info_don_hang, parent, false);
        return new InfoDonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoDonHangViewHolder holder, int position) {
        OderDetail oderDetail = listOder.get(position);

        Picasso.get().load(oderDetail.getImage()).into(holder.img_FoodItemTT);
        holder.tv_nameFoodItemTT.setText("Tên: " + oderDetail.getProductName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String tien = decimalFormat.format(Integer.parseInt(oderDetail.getPrice()));
        holder.tv_TienFoodItemTT.setText("Đơn giá: " + tien + " đ");
        holder.tv_SlFoodItemTT.setText("Số lượng: " + oderDetail.getQuanTity());


    }

    @Override
    public int getItemCount() {
        if (listOder != null)
            return listOder.size();
        return 0;
    }
}
