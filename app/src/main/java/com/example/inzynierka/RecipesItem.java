package com.example.inzynierka;

import java.util.ArrayList;
import java.util.UUID;

public class RecipesItem {
    String recipesName;
    String description;
    String recipesID;
    ArrayList<String> tags;
    ArrayList<IngredientItem> ingredients;
    String recipeUri = null;

    public RecipesItem(){

    }

    public RecipesItem(String recipesName, String description, ArrayList<String> tags, ArrayList<IngredientItem> ingredients){
        this.recipesName = recipesName;
        this.description = description;
        this.tags = tags;
        this.ingredients = ingredients;
    }

    public String getRecipeUri() {
        return recipeUri;
    }

    public void setRecipeUri(String recipeUri) {
        this.recipeUri = recipeUri;
    }

    public String getRecipesID() {
        return recipesID;
    }

    public void setRecipesID(String recipesID) {
        this.recipesID = recipesID;
    }

    public String getRecipesName() {
        return recipesName;
    }

    public void setRecipesName(String recipesName) {
        this.recipesName = recipesName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void addTags(ArrayList<String> tags) {
        this.tags.addAll(tags);
    }

    public void deleteTags(String tag){
        if (this.tags.contains(tag))this.tags.remove(this.tags.indexOf(tag));
    }

    public ArrayList<IngredientItem> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<IngredientItem> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredients(ArrayList<IngredientItem> ingredients) {
        this.ingredients.addAll(ingredients);
    }

    public void deleteIngredients(String ingredient){
        if (this.ingredients.contains(ingredient))this.ingredients.remove(this.ingredients.indexOf(ingredient));
    }
}
