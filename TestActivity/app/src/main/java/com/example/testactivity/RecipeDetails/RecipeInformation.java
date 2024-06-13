package com.example.testactivity.RecipeDetails;

import com.example.testactivity.Recipe.RecipeIngredient;

import java.util.ArrayList;

public class RecipeInformation {
    public class Codebeautify {
        private float id;
        private String title;
        private String image;
        private float servings;
        private float readyInMinutes;
        ArrayList < Object > analyzedInstructions = new ArrayList < Object > ();
        private String instructions;
        ArrayList<RecipeIngredient> extendedIngredients = new ArrayList < RecipeIngredient > ();
        private String summary;



        // Getter Methods

        public float getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }


        public float getServings() {
            return servings;
        }

        public float getReadyInMinutes() {
            return readyInMinutes;
        }


        public String getInstructions() {
            return instructions;
        }



        public String getSummary() {
            return summary;
        }


        // Setter Methods


        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }


        public void setSummary(String summary) {
            this.summary = summary;
        }


    }


}
