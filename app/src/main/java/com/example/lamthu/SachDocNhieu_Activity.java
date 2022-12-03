package com.example.lamthu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lamthu.Adapter.SachDocNhieuAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SachDocNhieu_Activity extends AppCompatActivity {

    private TextView tv_name,tv_tacGia,tv_moTa;
    private ImageView avt_book;
    private DatabaseReference dataRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sach_doc_nhieu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tv_name = findViewById(R.id.tv_tenSachDocNhieu_view);
        Toolbar toolbar = findViewById(R.id.toolbar_TT_sachDocNhieu);
        tv_tacGia = findViewById(R.id.tv_tenTacGia_DocNhieu_view);
        tv_moTa = findViewById(R.id.tv_moTa_docNhieu);
        avt_book = findViewById(R.id.avt_sachDocNhieu_view);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SachDocNhieu_Activity.this,Home_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        dataRef = FirebaseDatabase.getInstance().getReference().child("SachDocNhieu");
        String maSach = getIntent().getStringExtra("key");
        dataRef.child(maSach).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("ten").getValue().toString();
                    String tacGia = snapshot.child("tacGia").getValue().toString();
                    String mota = snapshot.child("mota").getValue().toString();
                    String url = snapshot.child("url").getValue().toString();

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