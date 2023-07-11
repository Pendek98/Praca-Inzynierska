package com.example.inzynierka;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inzynierka.adapters.HomeAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ThirdFragment extends Fragment implements SelectListener{

    public ArrayList<IngredientItem> items = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://praca-inzynierska-2edb2-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference databaseReference = database.getReference().child("HomeList");
    View tmp;
    RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            items.addAll(0,((MainActivity)getActivity()).getHomeIngredient());
        }catch (Exception e) {
        }

        View view = inflater.inflate(R.layout.fragment_third, container, false);
        tmp = view;

        recyclerView = view.findViewById(R.id.HomeIngredientsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new HomeAdapter(items,this));
        return view;
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.findViewById(R.id.AddHomeIngredient).setOnClickListener(v -> {
            ((MainActivity)getActivity()).addToHome();
            items.removeAll(items);
            items.addAll(0,((MainActivity)getActivity()).getHomeIngredient());
            recyclerView.setAdapter(new HomeAdapter(items,this));

        });



    }
    @Override
    public void onItemClicked(int position) {
        ((MainActivity)getActivity()).deleteHomedata(items.get(position));
        items.remove(position);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    /*
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.HomeIngredientsList);;
        view.findViewById(R.id.DeleteIngredient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = recyclerView.findViewHolderForAdapterPosition();
                items.remove(items.indexOf(this));
            }
        });

    }*/

}