package com.mark.wallpaperarena;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class Open_Wallpaper extends AppCompatActivity{
    ImageView back,img,setwallpaper,downloadwallpaper,shareit;
    String name;
    int count=0;
    String medium;
    private  static final int PERMISSION_REQUEST_CODE = 1;
    SparkButton sparkButton;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<Integer> Icons;
    ArrayList<String> title;
    ArrayList<Integer> colors;

    @Override
    public void onRequestPermissionsResult(int request, @NonNull String[] permissions,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(request, permissions, grantResults);
        switch (request)
        {
            case PERMISSION_REQUEST_CODE:
            {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Open_Wallpaper.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Open_Wallpaper.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(Open_Wallpaper.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(Open_Wallpaper.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open__wallpaper);
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
        img = findViewById(R.id.img);
        name = getIntent().getStringExtra("imageuri");
        medium = getIntent().getStringExtra("imageuri1");
        Icons = new ArrayList<>();
        title = new ArrayList<>();
        colors = new ArrayList<>();
        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        setData();
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .imagePadding(new Rect(30, 30, 30, 40))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if (index==0){
                                shareImage();
                            }
                            else if (index==1){
                                saveImage();
                            }
                            else if (index==2){
                                setWallp();
                            }
                        }
                    })
                    .normalImageRes(Icons.get(i))
                    .normalColorRes(colors.get(i))
                    .rippleEffect(true)
                    .pieceColorRes(R.color.colorPrimaryDark)
                    .normalText(title.get(i));
            bmb.addBuilder(builder);
        }
        Glide.with(this).load(name).transition(DrawableTransitionOptions.withCrossFade()).into(img);
        sparkButton = findViewById(R.id.spark_button);
        back = findViewById(R.id.back);
        sqLiteDatabase = getApplicationContext().openOrCreateDatabase("LikedDatabase",Context.MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Favourites(Id INTEGER PRIMARY KEY AUTOINCREMENT,UrlMedium VARCHAR(255) , Urlhd VARCHAR(255));");
        final Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM Favourites WHERE UrlMedium ='"+ medium +"'", null);
        if(c.moveToFirst())
        {
            sparkButton.setChecked(true);
        }
        sparkButton.setEventListener(new SparkEventListener(){
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    sqLiteDatabase.execSQL("Insert Into Favourites(UrlMedium,Urlhd)VALUES('" + medium + "','" + name + "');");
                    Toast.makeText(Open_Wallpaper.this, "Added to Liked", Toast.LENGTH_SHORT).show();

                } else {
                    // Button is inactive
                    sqLiteDatabase.execSQL("DELETE FROM Favourites WHERE UrlMedium ='"+ medium +"'");
                    Toast.makeText(Open_Wallpaper.this, "Removed From Liked", Toast.LENGTH_SHORT).show();
                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Refresh(Id INTEGER PRIMARY KEY AUTOINCREMENT,State VARCHAR(255));");
                    Cursor cw = sqLiteDatabase.rawQuery("SELECT * FROM Refresh WHERE State ='"+ "delete" +"'", null);
                    if (cw.getCount()==0){
                        sqLiteDatabase.execSQL("Insert Into Refresh(State)VALUES('" + "delete" + "');");
                    }
                    count++;
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_Wallpaper.this.finish();
                if (count>0){
                    count=0;
                    Toast.makeText(Open_Wallpaper.this, "Refresh To Update", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void saveImage() {

        if (ActivityCompat.checkSelfPermission(Open_Wallpaper.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "You Should Grant Permission", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },PERMISSION_REQUEST_CODE);
            }
            return;
        }
        else {

           FileOutputStream fileOutputStream =null;
           File file = getDisc();
           if (!file.exists() && !file.mkdirs()){
               Toast.makeText(this, "Can't Create Directory", Toast.LENGTH_SHORT).show();
               return;
           }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
           String date = simpleDateFormat.format(new Date());
           String name = "Wallpaper"+date+".jpg";
           String file_name = file.getAbsolutePath()+"/"+name;
           File new_file = new File(file_name);
           try {
               fileOutputStream = new FileOutputStream(new_file);
               Bitmap bitmap = getBitmapFromView(img,img.getWidth(),img.getHeight());
               bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
               Toast.makeText(this, "Saved To Gallery", Toast.LENGTH_SHORT).show();
               fileOutputStream.flush();
               fileOutputStream.close();
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
           refreshGallery(new_file);
        }

    }
    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(file,"Wallpaper");
    }

    private void setWallp() {

        Uri uri = getImageUri(Open_Wallpaper.this,getBitmapFromView(img,img.getWidth(),img.getHeight()));
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("mimeType", "image/*");
        startActivity(Intent.createChooser(intent, "Set as:"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Open_Wallpaper.this.finish();
        if (count>0){
            count=0;
            Toast.makeText(Open_Wallpaper.this, "Refresh To Update", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareImage() {
        Bitmap bitmap = getBitmapFromView(img,img.getWidth(),img.getHeight());
        try {
            File file = new File(this.getExternalCacheDir(),"black.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true,false);
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(Open_Wallpaper.this,BuildConfig.APPLICATION_ID + ".provider",file ));
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_TEXT,"This Wallpaper Is Downloaded From Wallpaper Arena Application . Download now :tny.sh/LZ5OsmH");
            startActivity(Intent.createChooser(intent,"Share Wallpaper"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromView(View view,int width,int height) {
        Bitmap returnBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnBitmap);
        Drawable bgdrawable = view.getBackground();

        if (bgdrawable != null){
            bgdrawable.draw(canvas);
        }
        else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return  returnBitmap;
    }
    public void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
    private void setData() {
        Icons.add(R.drawable.sharetopublic);
        Icons.add(R.drawable.downloadtostorage);
        Icons.add(R.drawable.setwall2);

        title.add("Share");
        title.add("Download");
        title.add("Set Now");

        colors.add(R.color.colorPrimaryDark);
        colors.add(R.color.colorPrimaryDark);
        colors.add(R.color.colorPrimaryDark);
    }
}