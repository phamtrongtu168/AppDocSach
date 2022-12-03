package com.example.lamthu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.lamthu.Adapter.BookAdapter;
import com.example.lamthu.Adapter.SachDocNhieuAdapter;
import com.example.lamthu.Ham.Book;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Search_Acticity extends AppCompatActivity {

    private RecyclerView rcv;
    private SachDocNhieuAdapter adapter;
    private TextView tv_tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_acticity);
        Toolbar toolbar = findViewById(R.id.toolbar_Search);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search_Acticity.this,Home_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        rcv= findViewById(R.id.rcv_search);
        rcv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("SachDocNhieu"),Book.class)
                        .build();

        adapter = new SachDocNhieuAdapter(Search_Acticity.this,options);
        rcv.setAdapter(adapter);

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();


    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.search_toobal);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                txtSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void txtSearch(String txt){
        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("SachDocNhieu").orderByChild("ten").startAt(txt).endAt(txt+"~"),Book.class)
                        .build();
        SachDocNhieuAdapter adapter = new SachDocNhieuAdapter(this,options);
        adapter.startListening();
        rcv = findViewById(R.id.rcv_search);
        rcv.setAdapter(adapter);
    }


}