package com.example.testactivity;

public class ShoppingItem {
    private String item;
    private boolean bought;

    public ShoppingItem(String item, boolean bought) {
        this.item = item;
        this.bought = bought;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }



}
