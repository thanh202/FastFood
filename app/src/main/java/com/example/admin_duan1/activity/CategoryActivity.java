package com.example.admin_duan1.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin_duan1.common.Common;
import com.example.admin_duan1.R;
import com.example.admin_duan1.adapter.viewholder.CategoryViewHolder;
import com.example.admin_duan1.dto.Category;
import com.example.admin_duan1.interfaces.ItemClick;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class CategoryActivity extends AppCompatActivity {
    private FirebaseRecyclerOptions options;
    private FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;
    private DatabaseReference cateDbr;
    private RecyclerView rcv_menu;
    private FloatingActionButton btnadd;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Category category;
    private TextInputEditText edt_name;
    private Button btnthemanh, btnUpload;
    private Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 71;


    private BottomNavigationView bottom_NaView;
    private Toolbar tool_barMenu;
    private TextView tv_TbTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        rcv_menu = findViewById(R.id.rcv_menu);
        btnadd = findViewById(R.id.floatAddmenu);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogThem();
            }
        });
        cateDbr = FirebaseDatabase.getInstance().getReference("Category");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        loadCate();


        //
        tool_barMenu = findViewById(R.id.tool_barMenu);

        setSupportActionBar(tool_barMenu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //
        tv_TbTitle = findViewById(R.id.tv_TbTitle);
        tv_TbTitle.setText("Menu");

        //
        bottom_NaView = findViewById(R.id.bottom_NaView);
        bottom_NaView.setSelectedItemId(R.id.menu_Menu);
        bottom_NaView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_Home:
                        startActivity(new Intent(getApplicationContext(), CuaHangActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.menu_Menu:
                        break;
                    case R.id.menu_HoaDon:
                        startActivity(new Intent(getApplicationContext(), HoaDonActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.menu_CaNhan:
                        startActivity(new Intent(getApplicationContext(), CaNhanActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });
    }

    private void loadCate() {
        GridLayoutManager manager = new GridLayoutManager(CategoryActivity.this, 2);
        rcv_menu.setLayoutManager(manager);
        options = new FirebaseRecyclerOptions.Builder<Category>().setQuery(cateDbr, Category.class).build();
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Category model) {
                holder.tv_CateItem.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.img_CateItem);
                Category category = model;
                holder.setItemClick(new ItemClick() {
                    @Override
                    public void itemClick(View view, int position, boolean isLongClick) {

                        Intent intent = new Intent(CategoryActivity.this, FoodActivity.class);
//                        Tạo key Category;
                        intent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_cate, parent, false);
                return new CategoryViewHolder(view);
            }
        };
        adapter.startListening();
        rcv_menu.setAdapter(adapter);
    }

    //Hiện dialog thêm;
    private void showDialogThem() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryActivity.this);
        alertDialog.setTitle("Thêm mới Thực Đơn");
        alertDialog.setMessage("Vui lòng điền đủ !");
        LayoutInflater inflater = this.getLayoutInflater();

        alertDialog.setIcon(R.drawable.ic_category);
        View add_menu_layout = inflater.inflate(R.layout.dialog_them_menu, null);
        edt_name = add_menu_layout.findViewById(R.id.edt_tenmonan);
        btnthemanh = add_menu_layout.findViewById(R.id.btn_themanh);
        btnUpload = add_menu_layout.findViewById(R.id.btn_upload);
        alertDialog.setView(add_menu_layout);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });
        btnthemanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        alertDialog.setPositiveButton("Chấp thuận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (category != null) {
                    cateDbr.push().setValue(category);
                }
            }
        });
        alertDialog.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    //Upload lên Firebase;
    private void UploadImage() {
        if (saveUri != null) {
            ProgressDialog mDialog = new ProgressDialog(CategoryActivity.this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName = UUID.randomUUID().toString();
            StorageReference imageFolder = storageReference.child("image/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(CategoryActivity.this, "Upload thành công.", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            category = new Category(edt_name.getText().toString(), uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(CategoryActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    mDialog.setMessage("Đang upload...");
                }
            });

        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data.getData() != null && resultCode == RESULT_OK && data != null) {
            saveUri = data.getData();
            btnthemanh.setText("Chọn lại ảnh");
        }
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)) {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
            Toast.makeText(this, "Sửa thành công.", Toast.LENGTH_SHORT).show();
        } else if ((item.getTitle().equals(Common.DELETE))) {
            DeleteCatehory(adapter.getRef(item.getOrder()).getKey());
            Toast.makeText(this, "Xoá thành công.", Toast.LENGTH_SHORT).show();

        }
        return super.onContextItemSelected(item);

    }


    //Xoá;
    private void DeleteCatehory(String key) {
        cateDbr.child(key).removeValue();
    }


    //Cập nhật;
    private void showUpdateDialog(String key, Category item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryActivity.this);
        alertDialog.setTitle("Cập nhật Category");
        alertDialog.setMessage("Xin mời nhập đủ !");
        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.dialog_them_menu, null);
        edt_name = add_menu_layout.findViewById(R.id.edt_tenmonan);
        btnthemanh = add_menu_layout.findViewById(R.id.btn_themanh);
        btnUpload = add_menu_layout.findViewById(R.id.btn_upload);
        edt_name.setText(item.getName());
        alertDialog.setView(add_menu_layout);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });
        btnthemanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setName(edt_name.getText().toString());
                cateDbr.child(key).setValue(item);
            }
        });
        alertDialog.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    //Đổi ảnh;
    private void changeImage(Category item) {
        if (saveUri != null) {
            ProgressDialog mDialog = new ProgressDialog(CategoryActivity.this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName = UUID.randomUUID().toString();
            StorageReference imageFolder = storageReference.child("image/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(CategoryActivity.this, "Upload thành công.", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            item.setImage(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(CategoryActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    mDialog.setMessage("Đang upload...");
                }
            });

        }
    }
}