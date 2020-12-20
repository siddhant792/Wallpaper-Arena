package com.mark.wallpaperarena;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder> {
    private List<Model2> modelRvList;
    ChipNavigationBar nav;
    private Context context;
    public Adapter2(Context context, List<Model2> modelRvList) {
        this.modelRvList = modelRvList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_category,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pic = modelRvList.get(position).getPhoto();
        String text = modelRvList.get(position).getTitle();
        holder.setData(pic,text);
    }

    @Override
    public int getItemCount() {
        return modelRvList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView Image;
        private TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.img_item2);
            text = itemView.findViewById(R.id.text_item2);
        }
        private void setData(int resource, final String title){
            Image.setBackgroundResource(resource);
            text.setText(title);
            Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,cat_frag.class);
                    intent.putExtra("cat",title);
                    context.startActivity(intent);
                    CustomIntent.customType(context,"fadein-to-fadeout");
                }
            });
        }
    }
}
