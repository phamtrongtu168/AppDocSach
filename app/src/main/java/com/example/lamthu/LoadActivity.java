package com.example.lamthu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadActivity extends AppCompatActivity {

    Animation top,bottom;
    ImageView avt;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        top = AnimationUtils.loadAnimation(this,R.anim.top_animaton);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        avt = findViewById(R.id.avt_load);
        title = findViewById(R.id.tv_load);

        avt.setAnimation(top);
        title.setAnimation(bottom);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadActivity.this,DangNhap_Activity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }
}