package com.mark.wallpaperarena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.ymex.popup.controller.ProgressController;
import cn.ymex.popup.dialog.PopupDialog;
import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {
    private static int splash =1200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,MainActivity1.class);
                startActivity(intent);
                CustomIntent.customType(MainActivity.this,"fadein-to-fadeout");
                finish();
            }
        },splash);
    }
}