package com.example.inzynierka;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.Toast;

import com.example.inzynierka.adapters.RecipesListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Spliterator;


public class FirstFragment extends Fragment implements SelectListener{

    public ArrayList<RecipesItem> items = new ArrayList<>();
    public ArrayList<RecipesItem> filtered = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://praca-inzynierska-2edb2-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference databaseReference = database.getReference().child("Recipes");
    RecyclerView recyclerView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.RecipesTags);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(view.getContext(),R
                .array.spinnerList, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        EditText name = (EditText) view.findViewById(R.id.RecipesNames);
        recyclerView = view.findViewById(R.id.RecipesList);
        RecipesListAdapter adapter = new RecipesListAdapter(items,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        getdata();
        adapter.notifyDataSetChanged();


        return view;
    }

    public void selected(){
        recyclerView.setAdapter(new RecipesListAdapter(filtered,this));
        recyclerView.getAdapter().notifyDataSetChanged();

    }
    public void difselect(){
        recyclerView.setAdapter(new RecipesListAdapter(items,this));
        recyclerView.getAdapter().notifyDataSetChanged();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText searchName = view.findViewById(R.id.RecipesNames);
        RecyclerView recyclerView = view.findViewById(R.id.RecipesList);
        RecipesListAdapter adapter = new RecipesListAdapter(items,this);
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();


        view.findViewById(R.id.AddRecipes).setOnClickListener(v -> {
            CreateRecipeFragment createRecipeFragment = new CreateRecipeFragment();
            ((MainActivity)getActivity()).CreateRecipe(4);
        });

        view.findViewById(R.id.FindRecipes).setOnClickListener(v -> {
            Spliterator<RecipesItem> recipes = items.spliterator();
            Toast.makeText(getContext(),searchName.getText(),Toast.LENGTH_LONG).show();
            filtered.removeAll(filtered);
            recipes.forEachRemaining((n) ->search(n,searchName.getText().toString()));
            if (filtered.isEmpty())recyclerView.setAdapter(new RecipesListAdapter(items,this));
            else recyclerView.setAdapter(new RecipesListAdapter(filtered,this));
            recyclerView.getAdapter().notifyDataSetChanged();
        });

        Spinner spinner = (Spinner) view.findViewById(R.id.RecipesTags);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(view.getContext(),R
                .array.spinnerList, android.R.layout.simple_spinner_item);


        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spliterator<RecipesItem> recipes = items.spliterator();
                filtered.removeAll(filtered);

                switch (position){
                    case 1:
                        recipes.forEachRemaining((n) ->filtr(n,"Ostre"));
                        selected();
                        break;
                    case 2:
                        recipes.forEachRemaining((n) ->filtr(n,"Łagodne"));
                        selected();
                        break;
                    case 3:
                        recipes.forEachRemaining((n) ->filtr(n,"Słone"));
                        selected();
                        break;
                    case 4:
                        recipes.forEachRemaining((n) ->filtr(n,"Mięsne"));
                        selected();
                        break;
                    case 5:
                        recipes.forEachRemaining((n) ->filtr(n,"Słodkie"));
                        selected();
                        break;
                    case 6:
                        recipes.forEachRemaining((n) ->filtr(n,"Orientalne"));
                        selected();
                        break;
                    case 7:
                        recipes.forEachRemaining((n) ->filtr(n,"Włoskie"));
                        selected();
                        break;
                    case 8:
                        recipes.forEachRemaining((n) ->filtr(n,"Wege"));
                        selected();
                        break;
                    case 9:
                        recipes.forEachRemaining((n) ->filtr(n,"Ryba"));
                        selected();
                        break;
                    case 10:
                        recipes.forEachRemaining((n) ->filtr(n,"Zupa"));
                        selected();
                        break;
                    default:
                        difselect();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                difselect();
                recyclerView.getAdapter().notifyDataSetChanged();

            }
        });
    }


    private void filtr(RecipesItem n,String tag) {
        if (n.tags.contains(tag))filtered.add(n);
    }

    private void search(RecipesItem n,String name) {
        if (n.recipesName.contains(name))filtered.add(n);
    }

    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //items.removeAll(items);


                HashMap<String, RecipesItem> hashMap = (HashMap<String, RecipesItem>) snapshot.getValue();

                Log.v("Tags",""+hashMap.keySet());
                Set<String> keySet = hashMap.keySet();
                keySet.forEach((n)->{
                    DataSnapshot newsnapshot = snapshot.child(n);
                    RecipesItem recipesItem = newsnapshot.getValue(RecipesItem.class);
                    items.add(recipesItem);
                    Log.v("Tags",""+recipesItem.getRecipesName());
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(int position) {
        ((MainActivity)getActivity()).openRecipe(items.get(position));
    }
}
