package com.example.admin_duan1.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin_duan1.common.Common;
import com.example.admin_duan1.R;
import com.example.admin_duan1.adapter.viewholder.FoodViewHolder;
import com.example.admin_duan1.dto.Food;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class FoodActivity extends AppCompatActivity {


    private RecyclerView re_Food;
    private FirebaseRecyclerOptions options;
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    private DatabaseReference foodDbr;
    private Uri saveUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Food newFood;
    private final int PICK_IMAGE_REQUEST = 71;
    public static String categoryId = "";
    private TextInputEditText edt_name, edt_price, edt_der;
    private FloatingActionButton btnAddfood;
    private Button btnthemanh, btnUpload;


    private Toolbar tool_barFood;
    private TextView tv_TbTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        re_Food = findViewById(R.id.re_Food);
        btnAddfood = findViewById(R.id.floatAddFood);
        btnAddfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddFood();
            }
        });

        //Tạo bảng Foods;
        foodDbr = FirebaseDatabase.getInstance().getReference("Foods");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //Tạo layout theo chiều dọc;
        LinearLayoutManager manager = new LinearLayoutManager(FoodActivity.this, LinearLayoutManager.VERTICAL, false);
        re_Food.setLayoutManager(manager);

        //Lấy key;
        Intent intent = getIntent();
        if (intent != null) {
            categoryId = intent.getStringExtra("CategoryId");
        }
        if (!categoryId.isEmpty() && categoryId != null) {
            loadFood(categoryId);
        }


        tool_barFood = findViewById(R.id.tool_barFood);
        setSupportActionBar(tool_barFood);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tool_barFood.setNavigationIcon(R.drawable.ic_quaylai);
        tool_barFood.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tv_TbTitle = findViewById(R.id.tv_TbTitle);
        tv_TbTitle.setText("Món ăn");

    }

    private void showDialogAddFood() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodActivity.this);
        alertDialog.setTitle("Thêm mới món ăn");
        alertDialog.setMessage("Vui lòng điền đủ !");
        alertDialog.setIcon(R.drawable.ic_fastfood);
        LayoutInflater inflater = this.getLayoutInflater();

        View add_menu_layout = inflater.inflate(R.layout.dialog_them_food, null);
        edt_name = add_menu_layout.findViewById(R.id.edt_foodname);
        edt_price = add_menu_layout.findViewById(R.id.edt_price);
        edt_der = add_menu_layout.findViewById(R.id.edt_thanhphan);
        btnthemanh = add_menu_layout.findViewById(R.id.btn_themanh1);
        btnUpload = add_menu_layout.findViewById(R.id.btn_upload1);
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
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (newFood != null) {
                    foodDbr.push().setValue(newFood);
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

    //Ảnh;
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    //Thêm món ăn;
    private void UploadImage() {
        if (saveUri != null) {
            ProgressDialog mDialog = new ProgressDialog(FoodActivity.this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName = UUID.randomUUID().toString();
            StorageReference imageFolder = storageReference.child("image/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(FoodActivity.this, "Upload thành công.", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newFood = new Food();
                            newFood.setName(edt_name.getText().toString());
                            newFood.setDescription(edt_der.getText().toString());
                            newFood.setPrice(edt_price.getText().toString());
                            newFood.setMenuid(categoryId);
                            newFood.setImage(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(FoodActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    mDialog.setMessage("Đang upload...");
                }
            });
        }
    }

    //
    private void loadFood(String categoryId) {
        options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodDbr.orderByChild("menuid").equalTo(categoryId), Food.class).build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.tv_nameFoodItem.setText("Tên: " + model.getName());


                Locale locale = new Locale("vi", "VN");
                NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

                holder.tv_tienFoodItem.setText("Đơn giá: " + nf.format(Integer.parseInt(String.valueOf(model.getPrice()))));
                holder.tv_desFoodItem.setText("Mô tả: " + model.getDescription());
                Picasso.get().load(model.getImage()).into(holder.img_FoodItem);
                Food food = model;
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_food, parent, false);
                return new FoodViewHolder(view);
            }
        };


        adapter.startListening();
        Log.d("====", "" + adapter.getItemCount());
        re_Food.setAdapter(adapter);
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
        } else if ((item.getTitle().equals(Common.DELETE))) {
            Deletefood(adapter.getRef(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);
    }

    //Cập nhật món ăn;
    private void showUpdateDialog(String key, Food item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodActivity.this);
        alertDialog.setTitle("Cập nhật món ăn");
        alertDialog.setMessage("Vui lòng điền đủ !");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.dialog_them_food, null);
        edt_name = add_menu_layout.findViewById(R.id.edt_foodname);
        edt_price = add_menu_layout.findViewById(R.id.edt_price);
        edt_der = add_menu_layout.findViewById(R.id.edt_thanhphan);
        btnthemanh = add_menu_layout.findViewById(R.id.btn_themanh1);
        btnUpload = add_menu_layout.findViewById(R.id.btn_upload1);

        //
        edt_name.setText(item.getName());
        edt_price.setText(item.getPrice());
        edt_der.setText(item.getDescription());

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
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setName(edt_name.getText().toString());
                item.setPrice(edt_price.getText().toString());
                item.setDescription(edt_der.getText().toString());
                item.setMenuid(categoryId);
                foodDbr.child(key).setValue(item);
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

    private void changeImage(Food item) {
        if (saveUri != null) {
            ProgressDialog mDialog = new ProgressDialog(FoodActivity.this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName = UUID.randomUUID().toString();
            StorageReference imageFolder = storageReference.child("image/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(FoodActivity.this, "Upload thành công", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(FoodActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    mDialog.setMessage("Đang upload...");
                }
            });

        }
    }

    private void Deletefood(String key) {
        foodDbr.child(key).removeValue();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}