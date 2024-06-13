package com.example.testactivity.Adapters;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testactivity.DBHandler;
import com.example.testactivity.ExpandableView;
import com.example.testactivity.FoodItem;
import com.example.testactivity.R;
import com.example.testactivity.ShoppingItem;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ShoppingListItems extends RecyclerView.Adapter<ShoppingListItems.ListView> {

    private Context context;
    private ArrayList<ShoppingItem> shopItems;
    private DBHandler dbHandler;


    public ShoppingListItems(Context context, ArrayList<ShoppingItem> shopItems) {
        this.context = context;
        this.shopItems = shopItems;

    }

    @NonNull
    @Override
    public ShoppingListItems.ListView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dbHandler = new DBHandler(context);

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.shopping_item,parent,false);
        return new ShoppingListItems.ListView(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ShoppingListItems.ListView holder, int position) {
        String name = shopItems.get(position).getItem();
        boolean checked = shopItems.get(position).isBought();

        holder.itemName.setText(name);
        holder.bought.setChecked(checked);

        holder.bought.setOnClickListener(v -> {
            boolean marked = holder.bought.isChecked();
            shopItems.get(position).setBought(marked);
            ShoppingItem temp = shopItems.get(position);
            dbHandler.updateShopItem(temp.getItem(),temp.isBought());
            sort();  //this where i should sort based on boolean
            dbHandler.getShoppinglist();
            notifyDataSetChanged();

        });
    }
// this thing doesn't work
    private void sort(){

        Collections.sort(shopItems, new Comparator<ShoppingItem>(){
            @Override
            public int compare(ShoppingItem shoppingItem, ShoppingItem t1) {
                boolean b1 = shoppingItem.isBought();
                boolean b2 = t1.isBought();

                if (b1 == !b2){
                    return 1;
                }
                if (!b1 == b2){
                    return -1;
                }
                return 0;
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopItems.size();
    }

    public class ListView extends RecyclerView.ViewHolder {
        TextView itemName;
        CheckBox bought;


        public ListView(@NonNull View itemView) {

            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.groceryItem);
            bought = (CheckBox) itemView.findViewById(R.id.groceryCheckbox);

        }}




}
