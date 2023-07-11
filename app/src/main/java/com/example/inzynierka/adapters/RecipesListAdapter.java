package com.example.inzynierka.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.example.inzynierka.MainActivity;
import com.example.inzynierka.R;
import com.example.inzynierka.RecipesItem;
import com.example.inzynierka.SelectListener;
import com.example.inzynierka.viewHolders.RecipesViewHolder;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesViewHolder> {
    ArrayList<RecipesItem> items;
    private SelectListener listener;


    public RecipesListAdapter(ArrayList<RecipesItem> items,SelectListener listener){
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        try {
            Picasso.get().load(items.get(position).getRecipeUri()).into(holder.recipeImg);
        }finally {
            holder.recipeName.setText(items.get(position).getRecipesName());
            holder.recipeDescription.setText(items.get(position).getDescription());
            holder.recipeDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(holder.getAdapterPosition());
                }
            });
            holder.recipeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(holder.getAdapterPosition());
                }
            });
        }

    }
    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (Exception e) {
        }
        return bm;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
