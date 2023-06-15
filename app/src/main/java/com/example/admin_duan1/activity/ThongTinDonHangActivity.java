package com.example.admin_duan1.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_duan1.R;

import com.example.admin_duan1.adapter.adapter.InfoDonHangAdapter;
import com.example.admin_duan1.common.Common;
import com.example.admin_duan1.dto.OderDetail;

import java.util.ArrayList;

public class ThongTinDonHangActivity extends AppCompatActivity {

    private TextView tv_idHoaDonItemTT, tv_TbTitle;
    private String idValue = "";
    private RecyclerView re_InfoDonHang;
    private Toolbar tool_barThongTin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_don_hang);

        tv_idHoaDonItemTT = findViewById(R.id.tv_idHoaDonItemTT);


        re_InfoDonHang = findViewById(R.id.re_InfoDonHang);
        LinearLayoutManager manager = new LinearLayoutManager(ThongTinDonHangActivity.this, LinearLayoutManager.VERTICAL, false);
        re_InfoDonHang.setLayoutManager(manager);

        if (getIntent() != null) {
            idValue = getIntent().getStringExtra("id");  //Lấy id;
        }

        tool_barThongTin = findViewById(R.id.tool_barThongTin);
        setSupportActionBar(tool_barThongTin);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tool_barThongTin.setNavigationIcon(R.drawable.ic_quaylai);
        tool_barThongTin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tv_TbTitle = findViewById(R.id.tv_TbTitle);
        tv_TbTitle.setText("Thông tin hoá đơn");

        //Set value;
        tv_idHoaDonItemTT.setText(idValue);


        InfoDonHangAdapter adapter = new InfoDonHangAdapter((ArrayList<OderDetail>) Common.request.getListOder());
        adapter.notifyDataSetChanged();
        re_InfoDonHang.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}