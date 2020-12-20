package com.mark.wallpaperarena;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class
Adapter3 extends RecyclerView.Adapter<Adapter3.ViewHolder>{
    private List<ModelRv> modelRvList;
    private List<ModelRv> modelRvListhd;
    FragmentManager manager;
    SQLiteDatabase sqLiteDatabase;
    private Context context;
    public Adapter3(Context context, List<ModelRv> modelRvList, List<ModelRv> modelRvListhd) {
        this.modelRvList = modelRvList;
        this.modelRvListhd = modelRvListhd;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view2,viewGroup,false);
        sqLiteDatabase = context.openOrCreateDatabase("LikedDatabase",Context.MODE_PRIVATE,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final int random = (int) (Math.random() * (1760 - 0)) + 0;
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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FancyGifDialog.Builder((Activity) context)
                        .setTitle("Remove This Wallpaper ?")
                        .setMessage("You Can Later Add This Wallpaper From Other Sections")
                        .setNegativeBtnText("Cancel")
                        .setPositiveBtnBackground("#FF4081")
                        .setPositiveBtnText("Delete")
                        .setNegativeBtnBackground("#FFA9A7A8")
                        .setGifResource(R.drawable.trash2)
                        .isCancellable(true)
                        .OnPositiveClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {
                                sqLiteDatabase.execSQL("DELETE FROM Favourites WHERE UrlMedium ='"+ modelRvList.get(position).getPhoto() +"'");
                                modelRvList.remove(position);
                                modelRvListhd.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .OnNegativeClicked(new FancyGifDialogListener() {
                            @Override
                            public void OnClick() {

                            }
                        })
                        .build();
            }
        });

    }
    @Override
    public int getItemCount() {
        return modelRvList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView Image,delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.img_item);
            delete = itemView.findViewById(R.id.deletefav);
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
