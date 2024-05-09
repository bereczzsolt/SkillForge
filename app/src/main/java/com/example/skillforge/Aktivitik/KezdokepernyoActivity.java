package com.example.skillforge.Aktivitik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.skillforge.MainActivity;
import com.example.skillforge.R;

public class KezdokepernyoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kezdokepernyo);
        if (getSupportActionBar() != null) {getSupportActionBar().hide();}
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(KezdokepernyoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }
}