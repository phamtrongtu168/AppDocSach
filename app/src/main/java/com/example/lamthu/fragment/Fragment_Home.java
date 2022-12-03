package com.example.lamthu.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lamthu.Adapter.BookAdapter;
import com.example.lamthu.Adapter.NewBookAdapter;
import com.example.lamthu.Adapter.SachDocNhieuAdapter;
import com.example.lamthu.Ham.Book;
import com.example.lamthu.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Fragment_Home extends Fragment {
    private RecyclerView rcv_Book,rcv_BookNew,rcv_docNhieu;
    private BookAdapter bookAdapter;
    private NewBookAdapter newBookAdapter;
    private SachDocNhieuAdapter sachDocNhieuAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProgressDialog progressDialog =new ProgressDialog(getContext());
        progressDialog.setTitle("Vui lòng đợi");
        progressDialog.setMessage("Đang tải...");
        progressDialog.show();

        rcv_Book = view.findViewById(R.id.rcv_book);
        rcv_BookNew = view.findViewById(R.id.rcv_bookNew);
        rcv_docNhieu = view.findViewById(R.id.rcv_book_docNhieu);
        SachHomNay();
        SachMoi();
        SachDocNhieu();
        progressDialog.dismiss();

        // Longclick cho RecycleView
        registerForContextMenu(rcv_Book);
        registerForContextMenu(rcv_BookNew);
        registerForContextMenu(rcv_docNhieu);



    }



    private void SachDocNhieu() {
        rcv_docNhieu.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));



        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("SachDocNhieu"),Book.class)
                        .build();

        sachDocNhieuAdapter = new SachDocNhieuAdapter(getContext(),options);
        rcv_docNhieu.setAdapter(sachDocNhieuAdapter);
    }

    private void SachHomNay() {

        rcv_Book.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));



        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Book"),Book.class)
                        .build();

        bookAdapter = new BookAdapter(getContext(),options);
        rcv_Book.setAdapter(bookAdapter);

    }

    private void SachMoi(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        rcv_BookNew.setLayoutManager(gridLayoutManager);

        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("NewBook"),Book.class)
                        .build();
        newBookAdapter = new NewBookAdapter(getContext(),options);
        rcv_BookNew.setAdapter(newBookAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        bookAdapter.startListening();
        newBookAdapter.startListening();
        sachDocNhieuAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        bookAdapter.stopListening();
        newBookAdapter.startListening();
        sachDocNhieuAdapter.startListening();

    }


}