package com.example.inzynierka.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inzynierka.R;

public class ReadViewHolder extends RecyclerView.ViewHolder {
    public TextView ingredientName;
    public TextView ingredientAmount;

    public ReadViewHolder(@NonNull View itemView) {
        super(itemView);
        ingredientName = itemView.findViewById(R.id.ReadIngredientName);
        ingredientAmount = itemView.findViewById(R.id.ReadIngredientAmount);
    }
}
