package com.example.lamthu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewBook_Activity extends AppCompatActivity {

    private TextView tv_name,tv_tacGia,tv_moTa;
    private ImageView avt_book;
    private DatabaseReference dataRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_name = findViewById(R.id.tv_tenBook_view);
        Toolbar toolbar = findViewById(R.id.toolbar_TT);
        tv_tacGia = findViewById(R.id.tv_tenTacGia_view);
        tv_moTa = findViewById(R.id.tv_moTa);
        avt_book = findViewById(R.id.avt_book_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewBook_Activity.this,Home_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        dataRef = FirebaseDatabase.getInstance().getReference().child("Book");
        String maSach = getIntent().getStringExtra("key");
        dataRef.child(maSach).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("ten").getValue().toString();
                    String tacGia = snapshot.child("tacGia").getValue().toString();
                    String url = snapshot.child("url").getValue().toString();
                    String mota = snapshot.child("mota").getValue().toString();

                    tv_name.setText(name);
                    tv_tacGia.setText(tacGia);
                    tv_moTa.setText(mota);
                    Picasso.get().load(url).into(avt_book);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}