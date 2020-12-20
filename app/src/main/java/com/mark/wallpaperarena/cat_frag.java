package com.mark.wallpaperarena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class cat_frag extends AppCompatActivity {
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_frag);
        Fragment fragment = null;
        switch (getIntent().getStringExtra("cat")) {
            case "CITY":
                fragment = new City();
                break;
            case "CARS":
                fragment = new Cars();
                break;
            case "FLOWERS":
                fragment = new Flowers();
                break;
            case "ABSTRACT":
                fragment = new Abstract();
                break;
            case "NATURE":
                fragment = new Nature();
                break;
            case "ANIMALS":
                fragment = new Animal();
                break;
            case "AMOLED":
                fragment = new Amoled();
                break;
            case "TEXTURE":
                fragment = new Texture();
                break;
        }
            if (fragment!=null){
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.cont2,fragment)
                        .commit();
            }
    }
}