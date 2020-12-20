package com.mark.wallpaperarena;

import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class SaveImageHelper implements Target {
    private Context context;
    private  WeakReference<ContentResolver> contentResolverWeakReference;
    private  String name,desc;

    public SaveImageHelper(Context context, ContentResolver contentResolver, String name, String desc) {
        this.context = context;
        this.contentResolverWeakReference = new WeakReference<ContentResolver>(contentResolver);
        this.name = name;
        this.desc = desc;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        ContentResolver r = contentResolverWeakReference.get();
        if (r != null){
            MediaStore.Images.Media.insertImage(r,bitmap,name,desc);
            Toast.makeText(context, "Saved To Gallery", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
