package com.example.testactivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FoodItem {

    private String name;
    private String image;
    private String purchaseDate;
    private String expiryDate;
    private int quantity =1 ;
    private boolean recurring;
    private boolean selected;
    private double daysLeft;

    public FoodItem( String name, String image) {
        this.name = name;
        this.image =  "https://spoonacular.com/cdn/ingredients_250x250/" + image;
        this.purchaseDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        this.expiryDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

    }


    public FoodItem(String name, String image, int quantity,String purchaseDate, boolean recurring,String expiryDate) {
        this.name = name;
        this.image = image;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.recurring = recurring;
    }


    public double getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(double daysLeft) {
        this.daysLeft = daysLeft;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
