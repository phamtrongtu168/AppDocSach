package com.example.lamthu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DangKi_Activity extends AppCompatActivity {

    private EditText txt_name, txt_Email, txt_Pass, txt_CfPass;
    private Button btn_DK;
    private TextView tv_dn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng đợi");
        progressDialog.setCanceledOnTouchOutside(false);

        AnhXa();

        btn_DK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XacThucTaiKhoan();
            }
        });

        tv_dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private String name,email,pass,cfpass;
    private void XacThucTaiKhoan() {
        name = txt_name.getText().toString().trim();
        email = txt_Email.getText().toString().trim();
        pass = txt_Pass.getText().toString().trim();
        cfpass = txt_CfPass.getText().toString().trim();


        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Vui lòng nhập tên của bạn !", Toast.LENGTH_SHORT).show();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Vui lòng nhập mật khẩu !", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(cfpass)){
            Toast.makeText(this, "Vui lòng xác nhận mật khẩu !", Toast.LENGTH_SHORT).show();
        }else if (!pass.equals(cfpass)){
            Toast.makeText(this, "Mật khẩu không trùng khớp !", Toast.LENGTH_SHORT).show();
        }else {
            TaoTaiKhoan();
        }
    }

    private void TaoTaiKhoan() {
        progressDialog.setMessage("Tạo tài khoản...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                CapNhatTaiKhoan();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(DangKi_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CapNhatTaiKhoan() {
        progressDialog.setMessage("Lưu tài khoản");
        //
        String uid = mAuth.getUid();

        //
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id",uid);
        hashMap.put("email", email);
        hashMap.put("password", pass);
        hashMap.put("name", name);
        hashMap.put("userType", "user");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(DangKi_Activity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(DangKi_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void AnhXa() {
        tv_dn = findViewById(R.id.tv_dn);
        txt_CfPass= findViewById(R.id.txt_cfpass_dk);
        txt_name = findViewById(R.id.txt_name_dk);
        txt_Pass = findViewById(R.id.txt_pass_dk);
        txt_Email = findViewById(R.id.txt_email_dk);
        btn_DK = findViewById(R.id.btn_dk);
    }
}