package com.example.inzynierka;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inzynierka.adapters.HomeAdapter;
import com.example.inzynierka.adapters.ReadAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ReadRecipeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_read_recipe, container, false);
        Button button = view.findViewById(R.id.Buy_Ingredients);
        TextView name = view.findViewById(R.id.ReadName);
        TextView desc = view.findViewById(R.id.ReadDescription);
        try {
            ImageView readImg = view.findViewById(R.id.ReadRecipeImg);
            Picasso.get().load(((MainActivity)getActivity()).tmpRecipesItem.getRecipeUri()).into(readImg);
        }catch (Exception e){
        }
        name.setText(((MainActivity)getActivity()).tmpRecipesItem.getRecipesName());
        desc.setText(((MainActivity)getActivity()).tmpRecipesItem.getDescription());
        RecyclerView recyclerView = view.findViewById(R.id.ReadIngredients);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new ReadAdapter(((MainActivity)getActivity()).tmpRecipesItem.getIngredients()));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAll(((MainActivity)getActivity()).tmpRecipesItem.getIngredients());
                ((MainActivity)getActivity()).CreateRecipe(1);
            }
        });

        return view;
    }

    public void addAll(ArrayList<IngredientItem> items){
        items.forEach(ingredientItem -> {
            ((MainActivity)getActivity()).writeNewShoppingItem(ingredientItem);
        });
    }
}