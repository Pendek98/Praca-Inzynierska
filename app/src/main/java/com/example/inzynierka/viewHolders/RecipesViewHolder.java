package com.example.inzynierka.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inzynierka.R;

public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView recipeName;
    public TextView recipeDescription;
    public ImageView recipeImg;

    public RecipesViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        recipeImg = itemView.findViewById(R.id.RecipeImg);
        recipeName = itemView.findViewById(R.id.RecipeTitle);
        recipeDescription = itemView.findViewById(R.id.RecipiDescription);
    }

    @Override
    public void onClick(View v) {

    }
}
