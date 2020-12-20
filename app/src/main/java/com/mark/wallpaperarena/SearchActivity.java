package com.mark.wallpaperarena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.ymex.popup.controller.ProgressController;
import cn.ymex.popup.dialog.PopupDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class SearchActivity extends AppCompatActivity {

    AutoCompleteTextView searchbar;
    private final String JSON_URL = "https://api.pexels.com/v1/search/?page=1&per_page=80&query=";
    private String ultimate;
    ImageView searchicon;
    String query;
    private RecyclerView recyclerView;
    private List<ModelRv> modelRvList;
    String myResponse;
    private List<ModelRv> modelRvListhd;
    String[] suggestions = new String[]{"nature","city","buildings","animals","wildlife","abstract","texture","dark","dark backgrounds","amoled","mobile","android","iphone","mobile wallpaper","wallpaper","bridges","dog","cat","food","lions","safari","love","couple","birthday","internet","wine","snow","children","network","paintings","wood"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchbar = findViewById(R.id.searchbar);
        searchbar.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
        searchbar.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
        searchbar.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,suggestions));
        searchicon = findViewById(R.id.serachicon);
        modelRvList = new ArrayList<>();
        modelRvListhd = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupDialog.create(SearchActivity.this)
                        .controller(ProgressController.build().message("loading ... "))
                        .dismissTime(3000)
                        .show();
                modelRvList.clear();
                modelRvListhd.clear();
                query = searchbar.getText().toString();
                onQuerySearch();

            }
        });
        searchbar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    PopupDialog.create(SearchActivity.this)
                            .controller(ProgressController.build().message("loading ... "))
                            .dismissTime(3000)
                            .show();
                    modelRvList.clear();
                    modelRvListhd.clear();
                    query = searchbar.getText().toString();
                    onQuerySearch();
                    return true;
                }
                return false;
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SearchActivity.this.finish();
    }
    private void onQuerySearch(){
        ultimate = JSON_URL+query;
        responseString();
    }
    private void responseString(){
        OkHttpClient client = new OkHttpClient();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ultimate)
                .addHeader("Authorization", "563492ad6f91700001000001a1b11c3c425b41dd8ad025dca58647da")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    myResponse = response.body().string();
                    try {
                        request12();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void request12() throws JSONException {
        JSONObject jsonObject = new JSONObject(myResponse);
        JSONArray array = jsonObject.getJSONArray("photos");
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            JSONObject object1 = object.getJSONObject("src");
            ModelRv modelRv = new ModelRv(R.drawable.animals);
            modelRv.setPhoto(object1.getString("medium"));
            modelRvList.add(modelRv);
            ModelRv modelRv1 = new ModelRv(R.drawable.animals);
            modelRv1.setPhoto(object1.getString("portrait"));
            modelRvListhd.add(modelRv1);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setuprecyclerview(modelRvList);
            }
        });
    }
    private void setuprecyclerview(List<ModelRv> modelRvList) {
        final  AdapterCategory adapter = new AdapterCategory(this, modelRvList,modelRvListhd);
        if (modelRvList.isEmpty()){
            Toast.makeText(this, "No Results Found", Toast.LENGTH_SHORT).show();
        }
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }

}