package com.mark.wallpaperarena;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment{
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    Adapter3 adapter;
    RelativeLayout nowallpaper;
    Button chagesbtn;
    private List<ModelRv> modelRvList;
    private List<ModelRv> modelRvListhd;
    private static int splash =1000;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        nowallpaper =view.findViewById(R.id.nowall);
        nowallpaper.setVisibility(View.GONE);
        modelRvList = new ArrayList<>();
        modelRvListhd = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        chagesbtn = view.findViewById(R.id.refreshbtn);
        sqLiteDatabase = getActivity().openOrCreateDatabase("LikedDatabase", Context.MODE_PRIVATE,null);
        final Cursor c = sqLiteDatabase.rawQuery("Select * From Favourites",null);
        if (c.getCount()!=0){
            while(c.moveToNext()){
                ModelRv modelRv = new ModelRv(R.drawable.animals);
                modelRv.setPhoto(c.getString(1));
                modelRvList.add(modelRv);
                ModelRv modelRv1 = new ModelRv(R.drawable.animals);
                modelRv1.setPhoto(c.getString(2));
                modelRvListhd.add(modelRv1);
            }
            setuprecyclerview(modelRvList);
        }
        else {
            nowallpaper.setVisibility(View.VISIBLE);
        }
        final Cursor chh = sqLiteDatabase.rawQuery("Select * From Refresh",null);
        if (chh.getCount()!=0){
            sqLiteDatabase.execSQL("DELETE FROM Refresh WHERE State ='"+ "delete" +"'");
        }
        chagesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chh.getCount()!=0){
                    sqLiteDatabase.execSQL("DELETE FROM Refresh WHERE State ='"+ "delete" +"'");
                }
                onCLickDestroy();
            }
        });

        return view;
    }
    public void setuprecyclerview(List<ModelRv> modelRvList) {
        adapter = new Adapter3(getContext(), modelRvList,modelRvListhd);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }
    public void onCLickDestroy() {
        FragmentManager manager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        Fragment currentFragment = manager.findFragmentById(this.getId());
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
    }

}