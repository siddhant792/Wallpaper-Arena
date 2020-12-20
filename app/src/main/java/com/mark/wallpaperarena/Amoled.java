package com.mark.wallpaperarena;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Amoled extends Fragment{

    private final String JSON_URL = "https://raw.githubusercontent.com/siddhant792/Wallpaper-Arena/master/Amoled1.json";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private List<ModelRv> modelRvList;
    private List<ModelRv> modelRvListhd;
    public Amoled() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amoled, container, false);
        modelRvList = new ArrayList<>();
        modelRvListhd = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        jsonrequest();
        return view;
    }
    private void jsonrequest() {
        JsonObjectRequest ObjectRequest = new JsonObjectRequest(Request.Method.GET,JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray array = response.getJSONArray("photos");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                JSONObject srcs = jsonObject.getJSONObject("src");
                                ModelRv modelRv = new ModelRv(R.drawable.animals);
                                modelRv.setPhoto(srcs.getString("medium"));
                                modelRvList.add(modelRv);
                                ModelRv modelRv1 = new ModelRv(R.drawable.animals);
                                modelRv1.setPhoto(srcs.getString("portrait"));
                                modelRvListhd.add(modelRv1);
                            }
                            setuprecyclerview(modelRvList);
                        } catch (JSONException e) {
                            e.printStackTrace();
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
    private void setuprecyclerview(List<ModelRv> modelRvList) {
        final  AdapterCategory adapter = new AdapterCategory(getContext(), modelRvList,modelRvListhd);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }
}