package com.mark.wallpaperarena;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.badge.BadgeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import cn.ymex.popup.controller.ProgressController;
import cn.ymex.popup.dialog.DialogManager;
import cn.ymex.popup.dialog.PopupDialog;
import maes.tech.intentanim.CustomIntent;

public class home extends Fragment {

    private static final int DIALOUGE = 1;
    private final String JSON_URL2 = "https://raw.githubusercontent.com/siddhant792/Wallpaper-Arena/master/homefinal.json";
    private final String JSON_URL3 = "https://raw.githubusercontent.com/siddhant792/Wallpaper-Arena/master/Testnow.json";
    private JsonArrayRequest request;
    ImageView searchbar;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    String mainjsoncontainer;
    Button about,share;
    DialogManager manager;
    private List<ModelRv> modelRvList;
    private List<ModelRv> modelRvListhd;
    public home() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        modelRvList = new ArrayList<>();
        share = view.findViewById(R.id.sharenow);
        about = view.findViewById(R.id.privacy_view);
        modelRvListhd = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        searchbar = view.findViewById(R.id.search_bar);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
                CustomIntent.customType(getContext(),"fadein-to-fadeout");
            }
        });
        boolean isFilePresent = isFilePresent(getContext(), "storage.json");
        if(isFilePresent) {
            String jsonString = read(getContext(), "storage.json");
            //do the json parsing here and do the rest of functionality of app
            try {
                jsonrequestOnPresent(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    manager.show(DIALOUGE);
                }
            }, 1000);
            jsonrequestOnNotPresent();
        }
        setRecyclerView2();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out the most user friendly wallpaper app from PlayStore : tny.sh/LZ5OsmH");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),privacy_policy.class);
                startActivity(intent);
            }
        });
         manager = new DialogManager();
        PopupDialog.create(getContext())
                .manageMe(manager)
                .priority(DIALOUGE)
                .controller(ProgressController.build().message("Loading Resources(1.56 MB).."));
        return view;
    }


    private void jsonrequestOnPresent(String myresponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(myresponse);
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
        setuprecyclerviewForSaved(modelRvList);

    }

    private void jsonrequestOnNotPresent() {
            JsonObjectRequest ObjectRequest = new JsonObjectRequest(Request.Method.GET,JSON_URL2, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            mainjsoncontainer = response.toString();
                            boolean isFileCreated = create(getContext(), "storage.json",mainjsoncontainer);
                            if(isFileCreated) {
                                manager.destroy();
                                onCLickDestroy();
                            } else {
                                //show error or try again.
                                Log.e("Success:","no");
                            }
                        }
                    },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(ObjectRequest);
    }
    private void setuprecyclerviewForSaved(List<ModelRv> modelRvList) {
        final  Adapter adapter = new Adapter(getContext(), modelRvList,modelRvListhd);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

    }
    private void setRecyclerView2(){
        List<Model2> model2 = new ArrayList<>();
        model2.add(new Model2(R.drawable.city,"CITY"));
        model2.add(new Model2(R.drawable.cars,"CARS"));
        model2.add(new Model2(R.drawable.nature,"NATURE"));
        model2.add(new Model2(R.drawable.abs,"ABSTRACT"));
        model2.add(new Model2(R.drawable.animals,"ANIMALS"));
        model2.add(new Model2(R.drawable.flower,"FLOWERS"));
        model2.add(new Model2(R.drawable.amoled,"AMOLED"));
        model2.add(new Model2(R.drawable.texture,"TEXTURE"));
        Adapter2 adapter = new Adapter2(getContext(),model2);
        recyclerView2.setLayoutManager(new GridLayoutManager(getContext(),1,LinearLayoutManager.HORIZONTAL,false));
        recyclerView2.setAdapter(adapter);
    }
    private String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    private boolean create(Context context, String fileName, String jsonString){
        String FILENAME = "storage.json";
        try {
            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }

    }
    public boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }
    public void onCLickDestroy() {
        Intent intent = new Intent(getActivity(),MainActivity1.class);
        startActivity(intent);
    }
    }