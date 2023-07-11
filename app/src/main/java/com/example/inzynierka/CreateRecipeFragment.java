package com.example.inzynierka;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.inzynierka.adapters.HomeAdapter;

import java.util.ArrayList;

public class CreateRecipeFragment extends Fragment implements SelectListener{

    public ArrayList<IngredientItem> items = new ArrayList<>();
    public View tmp;
    ArrayList<String> tags = new ArrayList<>();
    RecyclerView recyclerView;
    ImageView RecipeImage;
    Uri uri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_create, container, false);
        tmp = view;
        RecipeImage = view.findViewById(R.id.NewRecipeImg);
        recyclerView = view.findViewById(R.id.RecipeCreateIngridients);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new HomeAdapter(items,this));

        RecipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg();
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.CreateAddIngredient).setOnClickListener(v -> {

            try {
                ((MainActivity)getActivity()).addToRecipeIngr();
                if (items.size()==0)items.add(new IngredientItem(((MainActivity)getActivity()).ingredien.getIngredientName(),((MainActivity)getActivity()).ingredien.getAmount()));
                else items.add(0,((MainActivity)getActivity()).ingredien);
                if (!items.isEmpty())recyclerView.setAdapter(new HomeAdapter(items,this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        view.findViewById(R.id.AddNewRecipe).setOnClickListener(v -> {
            CheckBox chbox1 = (CheckBox)view.findViewById(R.id.ChBoxFish);
            CheckBox chbox2 = (CheckBox)view.findViewById(R.id.ChBoxSalty);
            CheckBox chbox3 = (CheckBox)view.findViewById(R.id.ChBoxSoup);
            CheckBox chbox4 = (CheckBox)view.findViewById(R.id.ChBoxSpicy);
            CheckBox chbox5 = (CheckBox)view.findViewById(R.id.ChBoxMeat);
            CheckBox chbox6 = (CheckBox)view.findViewById(R.id.ChBoxMild);
            CheckBox chbox7 = (CheckBox)view.findViewById(R.id.ChBoxSweet);
            CheckBox chbox8 = (CheckBox)view.findViewById(R.id.ChBoxOriental);
            CheckBox chbox9 = (CheckBox)view.findViewById(R.id.ChBoxItalian);
            CheckBox chbox10 = (CheckBox)view.findViewById(R.id.ChBoxVege);

            if(chbox1.isChecked())tags.add("Ryba");
            if(chbox2.isChecked())tags.add("Słone");
            if(chbox1.isChecked())tags.add("Ryba");
            if(chbox2.isChecked())tags.add("Słone");
            if(chbox3.isChecked())tags.add("Zupa");
            if(chbox4.isChecked())tags.add("Ostre");
            if(chbox5.isChecked())tags.add("Mięsne");
            if(chbox6.isChecked())tags.add("Łagodne");
            if(chbox7.isChecked())tags.add("Słodkie");
            if(chbox8.isChecked())tags.add("Orientalne");
            if(chbox9.isChecked())tags.add("Włoskie");
            if(chbox10.isChecked())tags.add("Wege");
            EditText nametext = (EditText) view.findViewById(R.id.CreateRecipeName);
            EditText desctext = (EditText) view.findViewById(R.id.CreateRecipesDescription);

            try {
                RecipesItem recipesItem = new RecipesItem(nametext.getText().toString(), desctext.getText().toString(), tags, items);
                ((MainActivity) getActivity()).addRecipe(recipesItem, uri);
                ((MainActivity) getActivity()).CreateRecipe(0);
            } catch (Exception e){
                Log.v("error",e.toString());
            }


        });

    }


    @Override
    public void onItemClicked(int position) {
        try {
            items.remove(position);
            RecyclerView recyclerView = tmp.findViewById(R.id.ShoppingList);
            if (!items.isEmpty())recyclerView.setAdapter(new HomeAdapter(items,this));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectImg(){
        Intent getPicture = new Intent();
        getPicture.setType("image/*");
        getPicture.setAction(Intent.ACTION_GET_CONTENT);
        launchPickActivity.launch(getPicture);
    }
    ActivityResultLauncher<Intent> launchPickActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        try {
                            uri = selectedImageUri;
                            RecipeImage.setImageURI(uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
}