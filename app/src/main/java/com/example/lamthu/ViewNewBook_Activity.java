package com.example.lamthu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewNewBook_Activity extends AppCompatActivity {

    private TextView tv_name,tv_tacGia,tv_moTa;
    private ImageView avt_book;
    private DatabaseReference dataRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_new_book);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_name = findViewById(R.id.tv_tenNewBook_view);
        Toolbar toolbar = findViewById(R.id.toolbar_TT_newBook);
        tv_tacGia = findViewById(R.id.tv_tenTacGia_newBook_view);
        tv_moTa = findViewById(R.id.tv_moTa_newBook);
        avt_book = findViewById(R.id.avt_newbook_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewNewBook_Activity.this,Home_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        dataRef = FirebaseDatabase.getInstance().getReference().child("NewBook");
        String maSach = getIntent().getStringExtra("key");
        dataRef.child(maSach).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("ten").getValue().toString();
                    String tacGia = snapshot.child("tacGia").getValue().toString();
                    String moTa = snapshot.child("mota").getValue().toString();
                    String img = snapshot.child("url").getValue().toString();

                    tv_moTa.setText(moTa);
                    tv_name.setText(name);
                    tv_tacGia.setText(tacGia);
                    Picasso.get().load(img).into(avt_book);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}