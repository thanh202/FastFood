package com.example.admin_duan1.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_duan1.R;

public class ThongKeViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_idHoaDonItem1, tv_statusHoaDonItem1, tv_userHoaDonItem1, tv_phoneHoaDonItem1, tv_addressHoaDonItem1,tv_timeHoaDonItem1,tv_tienHoaDonItem1;
    public ThongKeViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_idHoaDonItem1 = itemView.findViewById(R.id.tv_idHoaDonItem1);
        tv_statusHoaDonItem1 = itemView.findViewById(R.id.tv_statusHoaDonItem1);
        tv_userHoaDonItem1 = itemView.findViewById(R.id.tv_userHoaDonItem1);
        tv_phoneHoaDonItem1 = itemView.findViewById(R.id.tv_phoneHoaDonItem1);
        tv_addressHoaDonItem1 = itemView.findViewById(R.id.tv_addressHoaDonItem1);
        tv_timeHoaDonItem1= itemView.findViewById(R.id.tv_timeHoaDonItem1);
        tv_tienHoaDonItem1 = itemView.findViewById(R.id.tv_totalHoaDonItem1);

        //     itemView.setOnClickListener(this);
    }
}
