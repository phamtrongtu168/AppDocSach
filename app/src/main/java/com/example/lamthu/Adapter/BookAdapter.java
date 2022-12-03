package com.example.lamthu.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lamthu.Ham.Book;
import com.example.lamthu.R;
import com.example.lamthu.ViewBook_Activity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class BookAdapter extends FirebaseRecyclerAdapter<Book,BookAdapter.myViewHodel> {
    private Context context;
    private FirebaseRecyclerOptions<Book> options;
    public BookAdapter(Context context,@NonNull FirebaseRecyclerOptions<Book> options) {
        super(options);
        this.context = context;
        this.options = options;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHodel holder, @SuppressLint("RecyclerView") int position, @NonNull Book model) {
        String ten = model.getTen();

        holder.tv_ten.setText(model.getTen());
        holder.tv_tacGia.setText(model.getTacGia());
        Glide.with(holder.avt_book.getContext())
                .load(model.getUrl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.avt_book);

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ViewBook_Activity.class);
                intent.putExtra("key",getRef(position).getKey());
                context.startActivity(intent);
            }
        });
        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có muôn xoá không")
                        .setTitle("Thông báo")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase.getInstance().getReference().child("Book")
                                        .child(getRef(position).getKey()).removeValue();
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.update_dialog);

                Window window = dialog.getWindow();
                if (window == null){
                    return;
                }

                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams windowAtribute = window.getAttributes();
                windowAtribute.gravity = Gravity.CENTER;
                window.setAttributes(windowAtribute);
                dialog.setCancelable(true);


                ImageView btn_clear = dialog.findViewById(R.id.btn_clear_update);
                Button btn_xn = dialog.findViewById(R.id.btn_cnUpdate);
                EditText txt_ten = dialog.findViewById(R.id.txt_tenSach_upDate);
                EditText txt_tacGia = dialog.findViewById(R.id.txt_tacGia_update);
                EditText txt_mota = dialog.findViewById(R.id.txt_moTa_upDate);
                EditText txt_url = dialog.findViewById(R.id.txt_url_upDate);

                btn_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                txt_ten.setText(model.getTen());
                txt_tacGia.setText(model.getTacGia());
                txt_mota.setText(model.getMota());
                txt_url.setText(model.getUrl());

                dialog.show();
                btn_xn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("ten",txt_ten.getText().toString());
                        map.put("tacGia",txt_tacGia.getText().toString());
                        map.put("mota",txt_mota.getText().toString());
                        map.put("url",txt_url.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Book")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });



    }

    private void upDate(int gravity) {




    }


    @NonNull
    @Override
    public myViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_books,parent,false);

        return new myViewHodel(view);
    }


    public class myViewHodel  extends RecyclerView.ViewHolder{

          private ImageView avt_book;
          private TextView tv_ten,tv_tacGia;
          public View v;
          private Button btn_edit;
          public myViewHodel(@NonNull View itemView) {
              super(itemView);
              tv_ten = itemView.findViewById(R.id.tv_nameBook);
              tv_tacGia = itemView.findViewById(R.id.tv_tacGia);
              avt_book = itemView.findViewById(R.id.avt_itemBook);
              v = itemView;
              btn_edit = itemView.findViewById(R.id.btn_edit);
          }
    }

}
