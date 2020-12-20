package com.mark.wallpaperarena;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import maes.tech.intentanim.CustomIntent;

public class
AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder>{
    private List<ModelRv> modelRvList;
    private List<ModelRv> modelRvListhd;
    private Context context;
    public AdapterCategory(Context context, List<ModelRv> modelRvList, List<ModelRv> modelRvListhd) {
        this.modelRvList = modelRvList;
        this.modelRvListhd = modelRvListhd;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view,viewGroup,false);
        return new ViewHolder(view);
    }
    

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //final int random = (int) (Math.random() * (1760 - 0)) + 0;
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);

        Glide
                .with(context)
                .load(modelRvList.get(position).getPhoto())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOptions)
                .into(holder.Image);

        holder.Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Intent intent = new Intent(context,Open_Wallpaper.class);
                intent.putExtra("imageuri",modelRvListhd.get(position).getPhoto());
                intent.putExtra("imageuri1",modelRvList.get(position).getPhoto());
                context.startActivity(intent);
                CustomIntent.customType(context,"fadein-to-fadeout");
            }
        });

    }
    @Override
    public int getItemCount() {
        return modelRvList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView Image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.img_item);
            AppCompatActivity activity = (AppCompatActivity) context;
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int devicew = metrics.widthPixels;
            int deviceh = metrics.heightPixels;
            int cw = (int) (devicew/2.27);
            int ch = (int) (deviceh/3.0);
            Image.getLayoutParams().height = ch;
            Image.getLayoutParams().width = cw;
            Image.requestLayout();
        }

    }


}
