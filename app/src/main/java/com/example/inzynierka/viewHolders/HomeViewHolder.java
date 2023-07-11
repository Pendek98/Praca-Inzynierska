package com.example.inzynierka.viewHolders;


import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inzynierka.R;
import com.example.inzynierka.adapters.HomeAdapter;

public class HomeViewHolder extends RecyclerView.ViewHolder {
    public TextView ingredientName;
    public TextView ingredientAmount;
    public ImageButton imageButton;



    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);
        ingredientName = itemView.findViewById(R.id.IngredientName);
        ingredientAmount = itemView.findViewById(R.id.IngredientAmount);
        imageButton = itemView.findViewById(R.id.DeleteIngredient);
    }

}
