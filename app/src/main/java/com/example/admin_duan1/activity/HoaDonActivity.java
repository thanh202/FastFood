package com.example.admin_duan1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin_duan1.common.Common;
import com.example.admin_duan1.R;
import com.example.admin_duan1.adapter.viewholder.HoaDonViewHolder;
import com.example.admin_duan1.dto.Request;
import com.example.admin_duan1.interfaces.ItemClick;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class HoaDonActivity extends AppCompatActivity {


    private Toolbar tool_barHoaDon;
    private RecyclerView re_ViewListHoaDon;
    private TextView tv_TbTitle;
    private BottomNavigationView bottom_NaView;
    //
    private FirebaseRecyclerOptions options;
    private FirebaseRecyclerAdapter<Request, HoaDonViewHolder> adapter;
    private DatabaseReference table_GioHangDbr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoadon);


        re_ViewListHoaDon = findViewById(R.id.re_ViewListHoaDon);


        //
        tool_barHoaDon = findViewById(R.id.tool_barHoaDon);
        setSupportActionBar(tool_barHoaDon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //
        tv_TbTitle = findViewById(R.id.tv_TbTitle);
        tv_TbTitle.setText("Hoá Đơn");
        //Lấy bảng Requests;
        table_GioHangDbr = FirebaseDatabase.getInstance().getReference("Requests");
        //
        bottom_NaView = findViewById(R.id.bottom_NaView);
        bottom_NaView.setSelectedItemId(R.id.menu_HoaDon);
        bottom_NaView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_Home:
                        startActivity(new Intent(getApplicationContext(), CuaHangActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.menu_Menu:
                        startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.menu_HoaDon:
                        break;

                    case R.id.menu_CaNhan:
                        startActivity(new Intent(getApplicationContext(), CaNhanActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });


        loadHoaDon();
    }

    private void loadHoaDon() {
        LinearLayoutManager manager = new LinearLayoutManager(HoaDonActivity.this, LinearLayoutManager.VERTICAL, false);
        re_ViewListHoaDon.setLayoutManager(manager);

        options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(table_GioHangDbr, Request.class).build();
        adapter = new FirebaseRecyclerAdapter<Request, HoaDonViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HoaDonViewHolder holder, int position, @NonNull Request model) {
                holder.tv_idHoaDonItem.setText(adapter.getRef(position).getKey());
                holder.tv_statusHoaDonItem.setText(coverStatus(model.getStatus()));
                holder.tv_userHoaDonItem.setText(model.getUser());
                holder.tv_addressHoaDonItem.setText(model.getAddress());
                holder.tv_phoneHoaDonItem.setText(model.getPhone());
                holder.tv_timeHoaDonItem.setText(Common.getTime(Long.parseLong(adapter.getRef(position).getKey())));
                Locale locale = new Locale("vi", "VN");
                NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
                holder.tv_totalHoaDonItem.setText(nf.format(Integer.parseInt(String.valueOf(model.getTotal()))));

                holder.setItemClick(new ItemClick() {
                    @Override
                    public void itemClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(HoaDonActivity.this, ThongTinDonHangActivity.class);
                        Common.request = model;
                        intent.putExtra("id", adapter.getRef(position).getKey());  //Put id;
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public HoaDonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_hoadon, parent, false);
                return new HoaDonViewHolder(view);
            }


        };


        adapter.startListening();
        re_ViewListHoaDon.setAdapter(adapter);
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
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.CHAP_NHAN)) {
            if (Validate_Chap_nhan(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()))) {
                Toast.makeText(this, "Không thể chấp nhận đơn hàng", Toast.LENGTH_SHORT).show();
            } else {
                Chap_nhan(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
            }
        } else if ((item.getTitle().equals(Common.HUY))) {

            if (Validate_Huy(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()))) {
                Toast.makeText(this, "Không thể hủy đơn hàng", Toast.LENGTH_SHORT).show();
            } else {
                Kh_Chap_nhan(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
            }
        }
        return super.onContextItemSelected(item);
    }

    private boolean Validate_Huy(String key, Request item) {
        if (item.getStatus().equals("2") || item.getStatus().equals("1")) {
            return true;
        }
        return false;
    }

    private boolean Validate_Chap_nhan(String key, Request item) {
        if (!item.getStatus().equals("0")) {
            return true;
        }
        return false;
    }

    private void Kh_Chap_nhan(String key, Request item) {
        item.setStatus("3");
        table_GioHangDbr.child(key).setValue(item);
    }

    private void Chap_nhan(String key, Request item) {
        item.setStatus("1");
        table_GioHangDbr.child(key).setValue(item);
    }
}