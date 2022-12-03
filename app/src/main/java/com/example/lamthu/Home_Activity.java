package com.example.lamthu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lamthu.Ham.User;
import com.example.lamthu.fragment.Fragment_Home;
import com.example.lamthu.fragment.Fragment_Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;

    private static final int fragment_Home = 0;
    private static final int fragment_Profile = 1;

    private int mcurent = fragment_Home;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private TextView tv_name_navView;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_View);

        bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setItemIconTintList(null);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        mDrawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng đợi");
        progressDialog.setTitle("Đang tải...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new Fragment_Home());
        progressDialog.dismiss();
        navigationView.setCheckedItem(R.id.nav_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_bottom_profile:
                        OpenFragmentProfile();
                        break;
                    case R.id.nav_bottom_KhamPhaSach:
                        OpenFragmentHome();
                        break;
                }
                setTitleToobar();
                return true;

            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        userID = user.getUid();
        tv_name_navView = navigationView.getHeaderView(0).findViewById(R.id.tv_name_navView);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user_infor = dataSnapshot.getValue(User.class);
                if (user_infor != null){
                    String fullname = user_infor.getName();
                    tv_name_navView.setText(fullname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home_Activity.this, "Ko thấy", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search){
            Intent intent = new Intent(Home_Activity.this, Search_Acticity.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.add){
            Intent intent = new Intent(Home_Activity.this, Add_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                OpenFragmentHome();
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            case R.id.nav_gioiThieu:
                Intent intent_GT = new Intent(Home_Activity.this,GioiThieu_Activity.class);
                startActivity(intent_GT);
                break;

            case R.id.nav_dangXuat:
                DangXuat(Gravity.CENTER);
                break;
            case R.id.nav_search:
                Intent intent_search = new Intent(Home_Activity.this,Search_Acticity.class);
                startActivity(intent_search);
                break;


        }
        setTitleToobar();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    private void DangXuat(int gravity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn đăng xuất khỏi tài khoản này ?")
                .setTitle("Thông Báo")
                .setCancelable(true)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Home_Activity.this,DangNhap_Activity.class);
                        startActivity(intent);
                        Toast.makeText(Home_Activity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                        finish();
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



    }

    private void OpenFragmentHome(){
        if (mcurent != fragment_Home) {
            replaceFragment(new Fragment_Home());
            navigationView.setCheckedItem(R.id.nav_home);
            mcurent = fragment_Home;
        }
    }

    private void OpenFragmentProfile(){
        if (mcurent != fragment_Profile) {
            replaceFragment(new Fragment_Profile());
            navigationView.setCheckedItem(R.id.nav_bottom_profile);
            mcurent = fragment_Profile;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame,fragment);
        transaction.commit();

    }
    private void setTitleToobar(){
        String title = "";
        switch (mcurent){
            case fragment_Home:
                title = getString(R.string.nav_home);
                break;

            case fragment_Profile:
                title = "Tài khoản";
                break;
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }

    }


}