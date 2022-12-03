package com.example.lamthu.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lamthu.DangNhap_Activity;
import com.example.lamthu.Ham.User;
import com.example.lamthu.Home_Activity;
import com.example.lamthu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Profile extends Fragment {

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userID;
    private TextView tv_name,tv_mail ;
    private Button btn_dx;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        userID = user.getUid();
        tv_name = view.findViewById(R.id.tv_Name_prf);
        tv_mail = view.findViewById(R.id.tv_mail_prf);
        btn_dx = view.findViewById(R.id.btn_dx);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user_infor = dataSnapshot.getValue(User.class);
                if (user_infor != null){
                    String fullname = user_infor.getName();
                    String email = user.getEmail();
                    tv_name.setText(fullname);
                    tv_mail.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Ko thấy", Toast.LENGTH_SHORT).show();
            }
        });

        btn_dx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DangXuat(Gravity.CENTER);
            }


        });
    }

    private void DangXuat(int gravity) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dangxuat);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAtribute = window.getAttributes();
        windowAtribute.gravity = gravity;
        window.setAttributes(windowAtribute);

        if (Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }
        dialog.show();
        TextView txt_tb = dialog.findViewById(R.id.tv_CauHoi);
        Button btn_XN = dialog.findViewById(R.id.btn_dongy_dx);
        ImageButton btn_clear = dialog.findViewById(R.id.btn_x);


        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        txt_tb.setText("Bạn có muốn đăng xuất khỏi tài khoản này không ?");


        btn_XN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DangNhap_Activity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
    }
}