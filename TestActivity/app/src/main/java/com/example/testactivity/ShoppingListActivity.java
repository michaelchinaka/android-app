package com.example.testactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testactivity.Adapters.AllFoodItems;
import com.example.testactivity.Adapters.ShoppingListItems;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ShoppingListActivity extends AppCompatActivity {

    private RecyclerView recyclerShopping;
    private EditText textEdit;
    private ImageView addButton;
    private ArrayList<ShoppingItem> shopList = new ArrayList<>();
    private ShoppingListItems adapter;
    private DBHandler dbHandler;
    FirebaseDatabase firebaseDatabase;

//     creating a variable for our Database
//     Reference for Firebase.
    DatabaseReference databaseReference;
    ShoppingItem thisItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        firebaseDatabase = FirebaseDatabase.getInstance();
         //below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("ShoppingItem");

        recyclerShopping = findViewById(R.id.shoppingRecycler);
        dbHandler = new DBHandler(this);
        shopList = dbHandler.getShoppinglist();
        addExpiredItems();
        textEdit = findViewById(R.id.txtItem);
        addButton = findViewById(R.id.btnAdd);
        recyclerShopping.setLayoutManager(new LinearLayoutManager(this));
        recyclerShopping.setAdapter(new ShoppingListItems(this,shopList));




        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerShopping);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String text = textEdit.getText().toString();
                    if(text == null || text.length() == 0) {
                        Toast.makeText(ShoppingListActivity.this, "Field empty, Please enter an item", Toast.LENGTH_LONG).show();
                }else{

                        ShoppingItem newItem = new ShoppingItem(text, false);
                        dbHandler.addShoppingitem(newItem.getItem(), newItem.isBought());
                        addDatatoFirebase(newItem);
                        //getdata();
                        shopList.add(newItem);
                        textEdit.setText("");
                        recyclerShopping.getAdapter().notifyDataSetChanged();
                }
            }

            private void addDatatoFirebase(ShoppingItem newItem) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        databaseReference.setValue(newItem);
                       System.out.println("Added to firebase");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // if the data is not added or it is cancelled then
                        // we are displaying a failure toast message.
                        Toast.makeText(ShoppingListActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_bar, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem help = menu.findItem(R.id.help);

        help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent helpIntent = new Intent(ShoppingListActivity.this, WebViewActivity.class);
                startActivity(helpIntent);

                return false;
            }
        });
        // Return true to display menu
        return true;
    }
//adds expired items to shopping list
    private void addExpiredItems() {
        ArrayList<FoodItem> temp = ExpandableView.expiredItems;

        for(int i = 0;i<temp.size();i++){
            //check if expired item was set to be recurring purchase
            if(temp.get(i).isRecurring()){
                ShoppingItem thisItem = new ShoppingItem(temp.get(i).getName(),false);
                //checks if is already on list
                if(!checkPresence(thisItem.getItem())){
                shopList.add(thisItem);
                dbHandler.addShoppingitem(thisItem.getItem(),thisItem.isBought());
                }

            }
        }
    }
    //checks if is already on list
    private boolean checkPresence(String name) {
        boolean present = false;
        for(int i = 0;i<shopList.size();i++){
            if(name.equalsIgnoreCase(shopList.get(i).getItem())){
                present = true;
            }        }
    return present;
    }

    @Override
    protected void onStart() {
        super.onStart();
        addExpiredItems();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()) {
                    String value = ds.child("item").getValue(String.class);
                    System.out.println(value);
                }

                //String value = snapshot.getValue(String.class);

                // after getting the value we are setting
                // our value to our text view in below line.

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(ShoppingListActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            ShoppingItem temp = shopList.get(position);
            shopList.remove(position);
            dbHandler.deleteShoppingItem(temp.getItem());
            recyclerShopping.getAdapter().notifyDataSetChanged();
            //snackbar code below is faulty
//            Snackbar.make(recyclerShopping, viewHolder.getAdapterPosition(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    shopList.add(position,temp);
//                    recyclerShopping.getAdapter().notifyDataSetChanged();
//                }
//            });

        }


    };
}