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

import com.example.inzynierka.adapters.HomeAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


    public class SecondFragment extends Fragment implements SelectListener{


    public ArrayList<IngredientItem> ingredients = new ArrayList<>();
    FirebaseDatabase database2 = FirebaseDatabase.getInstance("https://praca-inzynierska-2edb2-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference databaseReference2 = database2.getReference().child("ShoppingList");
    View tmp;
    HomeAdapter homeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_second, container, false);
        //ingredients.add(0,new IngredientItem("Test","15"));
        //getdata();
        try {
            ingredients.addAll(0, ((MainActivity) getActivity()).getIngredients());
            Log.v("TryMe", "" + ingredients.get(0).getIngredientName());
        } catch (Exception e){
        }




        tmp = view;

        RecyclerView recyclerView = view.findViewById(R.id.ShoppingList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        homeAdapter =new HomeAdapter(ingredients,this);
        recyclerView.setAdapter(homeAdapter);

        return view;
    }

        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            view.findViewById(R.id.AddToShoppingList).setOnClickListener(v -> {
                ((MainActivity)getActivity()).addToShopping();
                ingredients.removeAll(ingredients);
                ingredients.addAll(0,((MainActivity)getActivity()).getIngredients());
                homeAdapter.notifyDataSetChanged();

            });



        }

        @Override
        public void onItemClicked(int position) {
            ((MainActivity)getActivity()).deletedata(ingredients.get(position));
            ingredients.remove(position);
            RecyclerView recyclerView = tmp.findViewById(R.id.ShoppingList);
            recyclerView.setAdapter(new HomeAdapter(ingredients,this));


        }



    /*private void getdata() {

            databaseReference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ingredients.removeAll(ingredients);


                    HashMap<String, IngredientItem> hashMap = (HashMap<String, IngredientItem>) snapshot.getValue();

                    Log.v("Tags",""+hashMap.keySet());
                    Set<String> keySet = hashMap.keySet();
                    keySet.forEach((n)->{
                        DataSnapshot newsnapshot = snapshot.child(n);
                        IngredientItem ingredientItem = newsnapshot.getValue(IngredientItem.class);
                        ingredients.add(0,new IngredientItem(ingredientItem.getIngredientName(),ingredientItem.getAmount()));
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // calling on cancelled method when we receive
                    // any error or we are not able to get the data.
                    //Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }*/


}