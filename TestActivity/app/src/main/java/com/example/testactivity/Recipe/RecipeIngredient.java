package com.example.testactivity.Recipe;

public class RecipeIngredient {
        private float id;
        private float amount;
        private String unit;
        private String unitLong;
        private String unitShort;
        private String name;
        private String original;
        private String originalName;
        private String image;

    public RecipeIngredient(float id, String name, String original, String image) {
        this.id = id;
        this.original = original;
        this.image = image;
        this.name = name;

    }

// Getter Methods

        public float getId() {
            return id;
        }

        public float getAmount() {
            return amount;
        }

        public String getUnit() {
            return unit;
        }

        public String getUnitLong() {
            return unitLong;
        }

        public String getUnitShort() {
            return unitShort;
        }



        public String getName() {
            return name;
        }

        public String getOriginal() {
            return original;
        }

        public String getOriginalName() {
            return originalName;
        }

        public String getImage() {
            return image;
        }

    }

