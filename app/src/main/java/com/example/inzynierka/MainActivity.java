package com.example.inzynierka;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.inzynierka.adapters.FragmentAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://praca-inzynierska-2edb2-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference databaseReference = database.getReference();
    FirebaseDatabase database2 = FirebaseDatabase.getInstance("https://praca-inzynierska-2edb2-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference databaseReference2 = database2.getReference().child("ShoppingList");
    public ArrayList<RecipesItem> przepisy = new ArrayList<>();
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentAdapter adapter;
    ArrayList<IngredientItem> ingredientspop = new ArrayList<>();
    IngredientItem ingredien;
    RecipesItem tmpRecipesItem;

    String copyId;

    public ArrayList<IngredientItem> items = new ArrayList<>();
    public ArrayList<IngredientItem> homeitems = new ArrayList<>();
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Uri pickedImgUri = null;

    public ArrayList<IngredientItem> buys = new ArrayList<>();
    public ArrayList<IngredientItem> homes = new ArrayList<>();

    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();

        getdata();





        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        adapter = new FragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setText("Przepisy"));
        tabLayout.addTab(tabLayout.newTab().setText("Lista Zakupów"));
        tabLayout.addTab(tabLayout.newTab().setText("Dom"));
        tabLayout.addTab(tabLayout.newTab().setText("Profil"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }


    public void logout(){
        firebaseAuth.signOut();


        startActivity(new Intent(this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void addRecipe(RecipesItem recipesItem, Uri uri){
        pickedImgUri = uri;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageDownloadLink = uri.toString();
                        recipesItem.setRecipeUri(imageDownloadLink);
                        DatabaseReference recipes = databaseReference.child("Recipes").push();
                        recipes.setValue(recipesItem);
                        recipes.push();
                        Log.v("URI",uri.toString());
                    }
                });
            }
        });
    }



    public void CreateRecipe(int numb) {
        viewPager2.setCurrentItem(numb);
    }

//    public void writeNewRecipe(RecipesItem recipesItem) {
//        Log.v("Dodawanie",recipesItem.getRecipeUri());
//        DatabaseReference recipes = databaseReference.child("Recipes").push();
//        recipes.setValue(recipesItem);
//        recipes.push();
//    }

    public void openRecipe(RecipesItem recipe){
        tmpRecipesItem = recipe;
        viewPager2.setCurrentItem(5);
    }


    public void writeNewShoppingItem(IngredientItem ingredientItem) {
        String id = user.getUid();
        DatabaseReference ingredients = databaseReference.child("ShoppingList").child(id).push();
        ingredients.setValue(ingredientItem);
        ingredients.push();
    }

    public void writeNewHomeItem(IngredientItem ingredientItem) {
        String id = user.getUid();
        DatabaseReference ingredients = databaseReference.child("HomeList").child(id).push();
        ingredients.setValue(ingredientItem);
        ingredients.push();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ingredientmenu, menu);
        return true;
    }

    public String addShareCode(){
        String shareId;
        shareId = user.getUid();
        String cutId = shareId.substring(0,5);
        DatabaseReference dataRef1 = database.getReference().child("ShareCodes").child(cutId);
        dataRef1.push();

        dataRef1.setValue(shareId);
        dataRef1.push();

        return cutId;
    }

    public String findId(String shareCode){
        DatabaseReference dataRef1 = database.getReference().child("ShareCodes").child(shareCode);

        dataRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                copyId = snapshot.getValue(String.class);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return copyId;
    }

    public void halfShare(String shareId){

        try {
        DatabaseReference dataRef1 = database.getReference().child("ShoppingList").child(shareId);
        DatabaseReference dataRef2 = database.getReference().child("HomeList").child(shareId);

        //homes.removeAll(homes);
        //buys.removeAll(buys);

        dataRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {



                    HashMap<String, IngredientItem> hashMap = (HashMap<String, IngredientItem>) snapshot.getValue();

                    Log.v("Tags", "" + hashMap.keySet());
                    Set<String> keySet = hashMap.keySet();
                    keySet.forEach((n) -> {
                        DataSnapshot newsnapshot = snapshot.child(n);
                        IngredientItem recipesItem = newsnapshot.getValue(IngredientItem.class);
                        if (items.contains(recipesItem)){}
                        else{
                        buys.add(0, new IngredientItem(recipesItem.getIngredientName(), recipesItem.getAmount()));
                        buys.get(0).setIngredientID(n);}
                    });
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dataRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {




                    HashMap<String, IngredientItem> hashMap = (HashMap<String, IngredientItem>) snapshot.getValue();

                    Log.v("Tags", "" + hashMap.keySet());
                    Set<String> keySet = hashMap.keySet();
                    keySet.forEach((n) -> {
                        DataSnapshot newsnapshot = snapshot.child(n);
                        IngredientItem recipesItem = newsnapshot.getValue(IngredientItem.class);
                        if (homeitems.contains(recipesItem)){}
                        else{
                        homes.add(0, new IngredientItem(recipesItem.getIngredientName(), recipesItem.getAmount()));
                        homes.get(0).setIngredientID(n);}
                    });
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        homes.forEach(ingredientItem -> {
            writeNewHomeItem(ingredientItem);
        });
        buys.forEach(ingredientItem -> {
            writeNewShoppingItem(ingredientItem);
        });

    }catch (Exception e){}
    }

    public void shareIng(String shareId){


            try {


                DatabaseReference dataRef1 = database.getReference().child("ShoppingList").child(shareId);
                DatabaseReference dataRef2 = database.getReference().child("HomeList").child(shareId);

                dataRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {


                            HashMap<String, IngredientItem> hashMap = (HashMap<String, IngredientItem>) snapshot.getValue();

                            Log.v("Tags", "" + hashMap.keySet());
                            Set<String> keySet = hashMap.keySet();
                            keySet.forEach((n) -> {
                                DataSnapshot newsnapshot = snapshot.child(n);
                                IngredientItem recipesItem = newsnapshot.getValue(IngredientItem.class);
                                buys.add(0, new IngredientItem(recipesItem.getIngredientName(), recipesItem.getAmount()));
                                buys.get(0).setIngredientID(n);
                            });
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                dataRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {


                            HashMap<String, IngredientItem> hashMap = (HashMap<String, IngredientItem>) snapshot.getValue();

                            Log.v("Tags", "" + hashMap.keySet());
                            Set<String> keySet = hashMap.keySet();
                            keySet.forEach((n) -> {
                                DataSnapshot newsnapshot = snapshot.child(n);
                                IngredientItem recipesItem = newsnapshot.getValue(IngredientItem.class);
                                homes.add(0, new IngredientItem(recipesItem.getIngredientName(), recipesItem.getAmount()));
                                homes.get(0).setIngredientID(n);
                            });
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                homes.forEach(ingredientItem -> {
                    writeNewHomeItem(ingredientItem);
                });

                buys.forEach(ingredientItem -> {
                    writeNewShoppingItem(ingredientItem);
                });

            }catch (Exception e){}
    }

    public void shareCode(String shareid){
        dialogBuilder = new AlertDialog.Builder(this);
        final View sharePopup = getLayoutInflater().inflate(R.layout.share_code_popup,null);
        dialogBuilder.setView(sharePopup);
        TextView code = sharePopup.findViewById(R.id.ShareID);
        code.setText(shareid);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void downloadCode(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View downloadPopup = getLayoutInflater().inflate(R.layout.download_popup,null);
        dialogBuilder.setView(downloadPopup);
        dialog = dialogBuilder.create();
        dialog.show();
        EditText code = downloadPopup.findViewById(R.id.downloadCode);
        Button zpowt = downloadPopup.findViewById(R.id.pobieranieAll);
        Button bezpowt = downloadPopup.findViewById(R.id.pobieranieSelect);
        zpowt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIng(findId(code.getText().toString()));
                //dialog.dismiss();
            }
        });
        bezpowt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                halfShare(findId(code.getText().toString()));
                //dialog.dismiss();
            }
        });

    }

    public void addToShopping() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        ImageView imageView = contactPopupView.findViewById(R.id.meats);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String[] values = getResources().getStringArray(R.array.meats);
                openCategoryShopping(values);
            }
        });

        ImageView fruit = contactPopupView.findViewById(R.id.fruits);

        fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.fruits);
                dialog.dismiss();
                openCategoryShopping(values);
            }
        });

        ImageView vegetable = contactPopupView.findViewById(R.id.vegetables);

        vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.vegetables);
                dialog.dismiss();
                openCategoryShopping(values);
            }
        });

        ImageView sweet = contactPopupView.findViewById(R.id.sweets);

        sweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.sweets);
                dialog.dismiss();
                openCategoryShopping(values);
            }
        });

        ImageView grain = contactPopupView.findViewById(R.id.grains);

        grain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.grains);
                dialog.dismiss();
                openCategoryShopping(values);
            }
        });

        ImageView spice = contactPopupView.findViewById(R.id.spices);

        spice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.spices);
                dialog.dismiss();
                openCategoryShopping(values);
            }
        });

        ImageView milk = contactPopupView.findViewById(R.id.milk);

        milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.milk);
                dialog.dismiss();
                openCategoryShopping(values);
            }
        });
        TextView wlasne = contactPopupView.findViewById(R.id.selfIngridient);

        wlasne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openWlasneShopping();

            }
        });
    }
        public void openWlasneShopping(){
            dialogBuilder = new AlertDialog.Builder(this);
            final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_alternative, null);
            dialogBuilder.setView(contactPopupView);
            dialog = dialogBuilder.create();
            dialog.show();
            EditText nazwa = contactPopupView.findViewById(R.id.newIngredientName);
            EditText ilosc = contactPopupView.findViewById(R.id.newIngredientAmount_Alt);
            Button save = contactPopupView.findViewById(R.id.saveIngredient);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    writeNewShoppingItem(new IngredientItem(nazwa.getText().toString(), ilosc.getText().toString()));
                    dialog.dismiss();
                }
            });
        }
    public void openWlasneHome(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup_alternative, null);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        EditText nazwa = contactPopupView.findViewById(R.id.newIngredientName);
        EditText ilosc = contactPopupView.findViewById(R.id.newIngredientAmount_Alt);
        Button save = contactPopupView.findViewById(R.id.saveIngredient);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewHomeItem(new IngredientItem(nazwa.getText().toString(), ilosc.getText().toString()));
                dialog.dismiss();
            }
        });
    }

    public void openCategoryShopping(String[] listvalues){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup22, null);
        ArrayAdapter<String> listAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listvalues);
        ListView list =(ListView) contactPopupView.findViewById(R.id.selectList);
        list.setAdapter(listAdapter);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ingr = list.getItemAtPosition(position).toString();
                dialog.dismiss();
                Log.v("Test", ingr);
                selectAmountToIngrShopping(ingr);


            }
        });
    }



    public void selectAmountToIngrShopping(String name) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup2, null);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        EditText  ingredientAmount;
        Button savebtn;
        ingredientAmount = (EditText) contactPopupView.findViewById(R.id.newIngredientAmount);
        savebtn = (Button) contactPopupView.findViewById(R.id.saveIngredient);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                writeNewShoppingItem(new IngredientItem(name, ingredientAmount.getText().toString()));
                ingredientspop.add(new IngredientItem(name, ingredientAmount.getText().toString()));

            }
        });


    }

    public void addToRecipeIngr() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        TextView wlasne = contactPopupView.findViewById(R.id.selfIngridient);

        wlasne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openWlasneShopping();

            }
        });

        ImageView imageView = contactPopupView.findViewById(R.id.meats);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.meats);
                dialog.dismiss();
                openCategoryRecipe(values);
            }
        });


        ImageView fruit = contactPopupView.findViewById(R.id.fruits);

        fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.fruits);
                dialog.dismiss();
                openCategoryRecipe(values);
            }
        });

        ImageView vegetable = contactPopupView.findViewById(R.id.vegetables);

        vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.vegetables);
                dialog.dismiss();
                openCategoryRecipe(values);
            }
        });

        ImageView sweet = contactPopupView.findViewById(R.id.sweets);

        sweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.sweets);
                dialog.dismiss();
                openCategoryRecipe(values);
            }
        });

        ImageView grain = contactPopupView.findViewById(R.id.grains);

        grain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.grains);
                dialog.dismiss();
                openCategoryRecipe(values);
            }
        });

        ImageView spice = contactPopupView.findViewById(R.id.spices);

        spice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.spices);
                dialog.dismiss();
                openCategoryRecipe(values);
            }
        });

        ImageView milk = contactPopupView.findViewById(R.id.milk);

        milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.milk);
                dialog.dismiss();
                openCategoryRecipe(values);
            }
        });


    }

    public void openCategoryRecipe(String[] listvalues){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup22, null);
        ArrayAdapter<String> listAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listvalues);
        ListView list =(ListView) contactPopupView.findViewById(R.id.selectList);
        list.setAdapter(listAdapter);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                String ingr = list.getItemAtPosition(position).toString();
                Log.v("Test", ingr);
                selectAmountToIngrRecipe(ingr);


            }
        });
    }

    public void selectAmountToIngrRecipe(String name) {

        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup2, null);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        EditText  ingredientAmount;
        Button savebtn;
        ingredientAmount = (EditText) contactPopupView.findViewById(R.id.newIngredientAmount);
        savebtn = (Button) contactPopupView.findViewById(R.id.saveIngredient);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredien = new IngredientItem(name, ingredientAmount.getText().toString());
                dialog.dismiss();
            }
        });
    }


    public ArrayList<IngredientItem> getIngredients() {
        return items;
    }



    public void deletedata(IngredientItem ing) {
        String id = user.getUid();
        databaseReference2.child(id).child(ing.ingredientID).removeValue();
    }
    public void deletealldata() {
        String id = user.getUid();
        DatabaseReference dataRefnew = database.getReference().child("HomeList").child(id);
        dataRefnew.removeValue();
        databaseReference2.child(id).removeValue();
    }
    public void deleteHomedata(IngredientItem ing){
        String id = user.getUid();
        DatabaseReference dataRefnew = database.getReference().child("HomeList").child(id);
        dataRefnew.child(ing.ingredientID).removeValue();
    }

    public ArrayList<RecipesItem> getPrzepisy(){
        return przepisy;
    }



    private void getdata() {
        String id = user.getUid();


        // calling add value event listener method
        DatabaseReference dataRef1 = database.getReference().child("ShoppingList").child(id);
        DatabaseReference dataRef2 = database.getReference().child("Recipes").child(id);
        DatabaseReference dataRef3 = database.getReference().child("HomeList").child(id);
        // for getting the values from database.
        dataRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {


                    items.removeAll(items);
                    HashMap<String, IngredientItem> hashMap = (HashMap<String, IngredientItem>) snapshot.getValue();

                    Log.v("Tags", "" + hashMap.keySet());
                    Set<String> keySet = hashMap.keySet();
                    keySet.forEach((n) -> {
                        DataSnapshot newsnapshot = snapshot.child(n);
                        IngredientItem recipesItem = newsnapshot.getValue(IngredientItem.class);
                        items.add(0, new IngredientItem(recipesItem.getIngredientName(), recipesItem.getAmount()));
                        items.get(0).setIngredientID(n);
                    });
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dataRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {


                przepisy.removeAll(przepisy);
                HashMap<String, RecipesItem> hashMap = (HashMap<String, RecipesItem>) snapshot.getValue();

                Log.v("Tags",""+hashMap.keySet());
                Set<String> keySet = hashMap.keySet();
                keySet.forEach((n)->{
                    DataSnapshot newsnapshot = snapshot.child(n);
                    RecipesItem recipesItem = newsnapshot.getValue(RecipesItem.class);
                    przepisy.add(recipesItem);
                    Log.v("Tags",""+recipesItem.getRecipesName());
                });
            }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dataRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {


                    homeitems.removeAll(homeitems);
                    HashMap<String, IngredientItem> hashMap = (HashMap<String, IngredientItem>) snapshot.getValue();

                    Log.v("Tags", "" + hashMap.keySet());
                    Set<String> keySet = hashMap.keySet();
                    keySet.forEach((n) -> {
                        DataSnapshot newsnapshot = snapshot.child(n);
                        IngredientItem recipesItem = newsnapshot.getValue(IngredientItem.class);
                        homeitems.add(0, new IngredientItem(recipesItem.getIngredientName(), recipesItem.getAmount()));
                        homeitems.get(0).setIngredientID(n);
                    });
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public ArrayList<IngredientItem> getHomeIngredient() {
        return homeitems;
    }

    public void addToHome() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        ImageView meat = contactPopupView.findViewById(R.id.meats);

        meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.meats);
                dialog.dismiss();
                openCategoryHome(values);
            }
        });
        TextView wlasne = contactPopupView.findViewById(R.id.selfIngridient);

        wlasne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openWlasneHome();

            }
        });

        ImageView fruit = contactPopupView.findViewById(R.id.fruits);

        fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.fruits);
                dialog.dismiss();
                openCategoryHome(values);
            }
        });

        ImageView vegetable = contactPopupView.findViewById(R.id.vegetables);

        vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.vegetables);
                dialog.dismiss();
                openCategoryHome(values);
            }
        });

        ImageView sweet = contactPopupView.findViewById(R.id.sweets);

        sweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.sweets);
                dialog.dismiss();
                openCategoryHome(values);
            }
        });

        ImageView grain = contactPopupView.findViewById(R.id.grains);

        grain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.grains);
                dialog.dismiss();
                openCategoryHome(values);
            }
        });

        ImageView spice = contactPopupView.findViewById(R.id.spices);

        spice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.spices);
                dialog.dismiss();
                openCategoryHome(values);
            }
        });

        ImageView milk = contactPopupView.findViewById(R.id.milk);

        milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = getResources().getStringArray(R.array.milk);
                dialog.dismiss();
                openCategoryHome(values);
            }
        });

    }

    public void openCategoryHome(String[] listvalues){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup22, null);
        ArrayAdapter<String> listAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listvalues);
        ListView list =(ListView) contactPopupView.findViewById(R.id.selectList);
        list.setAdapter(listAdapter);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ingr = list.getItemAtPosition(position).toString();
                Log.v("Test", ingr);
                selectAmountToIngrHome(ingr);
            }
        });
    }

    public void selectAmountToIngrHome(String name) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup2, null);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        EditText ingredientAmount;
        Button savebtn;
        ingredientAmount = (EditText) contactPopupView.findViewById(R.id.newIngredientAmount);
        savebtn = (Button) contactPopupView.findViewById(R.id.saveIngredient);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewHomeItem(new IngredientItem(name, ingredientAmount.getText().toString()));
                ingredientspop.add(new IngredientItem(name, ingredientAmount.getText().toString()));
                dialog.dismiss();
            }
        });

    }

}

