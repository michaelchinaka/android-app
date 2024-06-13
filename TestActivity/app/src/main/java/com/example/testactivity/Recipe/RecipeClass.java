package com.example.testactivity.Recipe;

import java.util.ArrayList;

public class RecipeClass {
    private float id;
    private String image;
    private float missedIngredientCount;
    ArrayList< RecipeIngredient > missedIngredients = new ArrayList < RecipeIngredient > ();
    private String title;
    ArrayList < RecipeIngredient > unusedIngredients = new ArrayList < RecipeIngredient > ();
    private float usedIngredientCount;
    ArrayList < RecipeIngredient > usedIngredients = new ArrayList < RecipeIngredient > ();

    public RecipeClass(float id, String image,
                       float missedIngredientCount, ArrayList<RecipeIngredient> missedIngredients,
                       String title, ArrayList<RecipeIngredient> unusedIngredients, float usedIngredientCount,
                       ArrayList<RecipeIngredient> usedIngredients) {
        this.id = id;
        this.image = image;
        this.missedIngredientCount = missedIngredientCount;
        this.missedIngredients = missedIngredients;
        this.title = title;
        this.unusedIngredients = unusedIngredients;
        this.usedIngredientCount = usedIngredientCount;
        this.usedIngredients = usedIngredients;
    }

// Getter Methods

    public float getId() {
        return id;
    }

    public String getImage() {
        return image;
    }


    public ArrayList<RecipeIngredient> getMissedIngredients() {
        return missedIngredients;
    }

    public ArrayList<RecipeIngredient> getUnusedIngredients() {
        return unusedIngredients;
    }

    public ArrayList<RecipeIngredient> getUsedIngredients() {
        return usedIngredients;
    }

    public float getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public String getTitle() {
        return title;
    }

    public float getUsedIngredientCount() {
        return usedIngredientCount;
    }

    // Setter Methods

    public void setId(float id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public void setMissedIngredientCount(float missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsedIngredientCount(float usedIngredientCount) {
        this.usedIngredientCount = usedIngredientCount;
    }
}

