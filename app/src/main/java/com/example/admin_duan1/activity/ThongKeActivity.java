package com.example.admin_duan1.activity;


import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_duan1.R;
import com.example.admin_duan1.adapter.viewholder.ThongKeViewHolder;
import com.example.admin_duan1.common.Common;
import com.example.admin_duan1.dto.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ThongKeActivity extends AppCompatActivity {
    List<Request> list = new ArrayList<>();
    private Toolbar tool_barThongKe;
    private RecyclerView re_ViewListThongKe;
    private TextView tv_TbTitle, tv_TongTienTK;

    //
    private FirebaseRecyclerOptions options;
    private FirebaseRecyclerAdapter<Request, ThongKeViewHolder> adapter;
    private DatabaseReference table_GioHangDbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_thong_ke);
        re_ViewListThongKe = findViewById(R.id.re_ViewListThongKe);
        //
        tool_barThongKe = findViewById(R.id.tool_barThongKe);
        setSupportActionBar(tool_barThongKe);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tool_barThongKe.setNavigationIcon(R.drawable.ic_quaylai);
        tool_barThongKe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //
        tv_TbTitle = findViewById(R.id.tv_TbTitle);
        tv_TbTitle.setText("Thống kê");

        tv_TongTienTK = findViewById(R.id.tv_TongTienTK);

        //Lấy bảng Requests;
        table_GioHangDbr = FirebaseDatabase.getInstance().getReference("Requests");
        loadThongKe();
        ThongKe();
    }

    private void loadThongKe() {
        LinearLayoutManager manager = new LinearLayoutManager(ThongKeActivity.this, LinearLayoutManager.VERTICAL, false);
        re_ViewListThongKe.setLayoutManager(manager);

        options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(table_GioHangDbr.orderByChild("status").equalTo("2"), Request.class).build();
        adapter = new FirebaseRecyclerAdapter<Request, ThongKeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ThongKeViewHolder holder, int position, @NonNull Request model) {
                holder.tv_idHoaDonItem1.setText(adapter.getRef(position).getKey());
                holder.tv_statusHoaDonItem1.setText(coverStatus(model.getStatus()));
                holder.tv_userHoaDonItem1.setText(model.getUser());
                holder.tv_addressHoaDonItem1.setText(model.getAddress());
                holder.tv_phoneHoaDonItem1.setText(model.getPhone());
                holder.tv_timeHoaDonItem1.setText(Common.getTime(Long.parseLong(adapter.getRef(position).getKey())));
                Locale locale = new Locale("vi", "VN");
                NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
                holder.tv_tienHoaDonItem1.setText(nf.format(Integer.parseInt(String.valueOf(model.getTotal()))));
            }

            @NonNull
            @Override
            public ThongKeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_thongke, parent, false);
                return new ThongKeViewHolder(view);
            }


        };
        adapter.startListening();
        re_ViewListThongKe.setAdapter(adapter);
    }


    private void ThongKe() {
        table_GioHangDbr.orderByChild("status").equalTo("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;
                for (DataSnapshot a : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) a.getValue();
                    Object price = String.valueOf(map.get("total"));
                    Locale locale = new Locale("vi", "VN");
                    NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
                    int value =  Integer.parseInt(String.valueOf(price));
                    sum+=value;
                    tv_TongTienTK.setText(nf.format(Integer.parseInt(String.valueOf(sum))));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private String coverStatus(String status) {
        if (status.equals("0")) {
            return "Đặt hàng";
        } else if (status.equals("2")) {
            return "Vận chuyển thành công";
        } else if (status.equals("1")) {
            return "Đang Giao";
        } else {
            return "Đã Hủy";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
