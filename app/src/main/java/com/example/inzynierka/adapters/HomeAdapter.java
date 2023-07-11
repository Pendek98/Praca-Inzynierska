package com.example.inzynierka.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inzynierka.IngredientItem;
import com.example.inzynierka.MainActivity;
import com.example.inzynierka.R;
import com.example.inzynierka.SelectListener;
import com.example.inzynierka.viewHolders.HomeViewHolder;


import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    ArrayList<IngredientItem> items;
    private SelectListener listener;

    public HomeAdapter(ArrayList<IngredientItem> items, SelectListener listener){
        this.items = items;
        this.listener = listener;
    }

    public HomeAdapter(ArrayList<IngredientItem> items){
        this.items = items;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        try{
            holder.ingredientName.setText(items.get(position).getIngredientName());
            holder.ingredientAmount.setText(items.get(position).getAmount());
            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(holder.getAdapterPosition());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*holder.ingredientName.setText(items.get(position).getIngredientName());
        holder.ingredientAmount.setText(items.get(position).getAmount());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(holder.getAdapterPosition());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
