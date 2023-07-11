package com.example.inzynierka;

import java.util.ArrayList;

public class ShoppingList {
    int id;
    ArrayList<IngredientItem> ingredients;
    public ShoppingList(int id, ArrayList<IngredientItem> ingredients) {
        this.id = id;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public ArrayList<IngredientItem> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<IngredientItem> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(String ingredientName, String amount){
        IngredientItem ingredient = new IngredientItem(ingredientName,amount);
        this.ingredients.add(ingredient);
    }

    public void deleteIngredient(String ingredient){
        if (this.ingredients.contains(ingredient))this.ingredients.remove(this.ingredients.indexOf(ingredient));
    }

}
