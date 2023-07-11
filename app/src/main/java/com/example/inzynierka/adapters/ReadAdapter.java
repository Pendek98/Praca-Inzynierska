package com.example.inzynierka.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inzynierka.IngredientItem;
import com.example.inzynierka.R;
import com.example.inzynierka.viewHolders.ReadViewHolder;

import java.util.ArrayList;

public class ReadAdapter extends RecyclerView.Adapter<ReadViewHolder> {
    ArrayList<IngredientItem> items;

    public ReadAdapter(ArrayList<IngredientItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ReadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.read_ingredients,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReadViewHolder holder, int position) {
        try {
            holder.ingredientName.setText(items.get(position).getIngredientName());
            holder.ingredientAmount.setText(items.get(position).getAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        try {
            return items.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
