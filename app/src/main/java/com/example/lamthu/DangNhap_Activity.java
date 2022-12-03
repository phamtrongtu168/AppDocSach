package com.example.lamthu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DangNhap_Activity extends AppCompatActivity {

    private TextView tv_dk,tv_qmk;
    private Button btn_dn;
    private FirebaseAuth mAuth;
    private EditText txt_email,txt_pass;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng đợi");
        progressDialog.setCanceledOnTouchOutside(false);
        AnhXa();

        tv_dk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhap_Activity.this, DangKi_Activity.class);
                startActivity(intent);
            }
        });

        btn_dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DangNhap();
            }
        });

        tv_qmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuenMatKhau(Gravity.CENTER);
            }
        });
    }

    private void QuenMatKhau(int gravity) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sent_pass_dialog);

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
        EditText txt_email_qmk = dialog.findViewById(R.id.txt_email_qmk);
        Button btn_Huy = dialog.findViewById(R.id.btn_huy_qmk);
        Button btn_XN = dialog.findViewById(R.id.btn_xn_qmk);

        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Thông báo");
        btn_XN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ResetPassword();

            }

            private void ResetPassword() {
                String email = txt_email_qmk.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(DangNhap_Activity.this, "Vui lòng nhập email để lấy lại mật khẩu", Toast.LENGTH_SHORT).show();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(DangNhap_Activity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.setMessage("Vui lòng đợi...");
                    progressDialog.show();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(DangNhap_Activity.this, "Vui lòng kiểm tra emal của bạn ", Toast.LENGTH_SHORT).show();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(DangNhap_Activity.this, "Tai khoản chưa đăng kí", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }


        });


    }




    private void DangNhap() {

        String mail = txt_email.getText().toString().trim();
        String pass = txt_pass.getText().toString().trim();
        if (TextUtils.isEmpty(mail)){
            Toast.makeText(this, "Vui lòng nhập email !", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Vui lòng nhập mật khẩu !", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setMessage("Đang đăng nhập...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(DangNhap_Activity.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DangNhap_Activity.this,Home_Activity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(DangNhap_Activity.this, "Sai Tài khoản mật khẩu.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void AnhXa() {
        tv_dk = findViewById(R.id.tv_dk);
        txt_email = findViewById(R.id.txt_name_dn);
        txt_pass = findViewById(R.id.txt_pass_dn);
        btn_dn = findViewById(R.id.btn_dn);
        tv_qmk = findViewById(R.id.tv_qmk);
    }
}