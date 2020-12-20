package com.mark.wallpaperarena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class card_view extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int devicew = metrics.widthPixels;
        int deviceh = metrics.heightPixels;

        int cw = (int) (devicew/2.45);
        int ch = (int) (deviceh/3.08);

        ImageView view = findViewById(R.id.img_item);
        view.getLayoutParams().height = ch;
        view.getLayoutParams().width = cw;
        view.requestLayout();
    }
}