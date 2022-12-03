package com.example.lamthu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Add_Activity extends AppCompatActivity {

    private TextView txt_ten,txt_tacGia,txt_noiDung,txt_url;
    private Button btn_xn;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar_Them);

        // Trở lại trang chủ
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Activity.this,Home_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        // Ánh Xạ
        AnhXa();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Vui lòng đợi");
        dialog.setCanceledOnTouchOutside(false);

        // set Sư kiện cho button thêm sách
        btn_xn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    insertData();


            }
        });


    }

    private void insertData() {
        dialog.setMessage("Đang tiến hành");
        dialog.show();
        Map<String,Object> map = new HashMap<>();
        map.put("ten",txt_ten.getText().toString());
        map.put("tacGia",txt_tacGia.getText().toString());
        map.put("mota",txt_noiDung.getText().toString());
        map.put("url",txt_url.getText().toString());

        String ten = txt_ten.getText().toString().trim();
        String tacGia = txt_tacGia.getText().toString().trim();
        String mota = txt_noiDung.getText().toString().trim();
        String url = txt_url.getText().toString().trim();

        if (TextUtils.isEmpty(ten)){
            dialog.dismiss();
            Toast.makeText(this, "Vui lòng nhập tên sách", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(tacGia)){
            dialog.dismiss();
            Toast.makeText(this, "Vui lòng nhập tên tác giả", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(mota)){
            dialog.dismiss();
            Toast.makeText(this, "Vui lòng nhập mô tả", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(url)){
            dialog.dismiss();
            Toast.makeText(this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
            return;
        }else {

            FirebaseDatabase.getInstance().getReference().child("NewBook").push()
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.dismiss();
                            Toast.makeText(Add_Activity.this, "Thêm Thành công", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(Add_Activity.this, "Thất Bại", Toast.LENGTH_SHORT).show();
                        }
                    });

        }


    }

    private void AnhXa() {
        btn_xn = findViewById(R.id.btn_add);
        txt_noiDung = findViewById(R.id.txt_moTa_them);
        txt_tacGia = findViewById(R.id.txt_tacGia);
        txt_ten = findViewById(R.id.txt_tenSach);
        txt_url = findViewById(R.id.txt_url);
    }
}