package com.example.testactivity.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testactivity.Adapters.ShoppingListItems;
import com.example.testactivity.DBHandler;
import com.example.testactivity.ExpandableView;
import com.example.testactivity.FoodItem;
import com.example.testactivity.R;
import com.example.testactivity.ShoppingItem;
import com.example.testactivity.ShoppingListActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment {

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingListFragment newInstance(String param1, String param2) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_shopping_list, container, false);


        firebaseDatabase = FirebaseDatabase.getInstance();
        //below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("ShoppingItem");

        recyclerShopping = view.findViewById(R.id.shoppingRecyclerF);
        dbHandler = new DBHandler(getActivity());
        shopList = dbHandler.getShoppinglist();
        addExpiredItems();
        textEdit = view.findViewById(R.id.txtItemF);
        addButton = view.findViewById(R.id.btnAddF);
        recyclerShopping.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerShopping.setAdapter(new ShoppingListItems(getActivity(),shopList));




        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerShopping);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = textEdit.getText().toString();
                if(text == null || text.length() == 0) {
                    Toast.makeText(getActivity(), "Field empty, Please enter an item", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        return view;
    }

    //adds expired items to shopping list
    private void addExpiredItems() {
        ArrayList<FoodItem> temp = ExpandableViewFragment.expiredItems;

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
    public void onStart() {
        super.onStart();
        addExpiredItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        addExpiredItems();
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
                Toast.makeText(getActivity(), "Fail to get data.", Toast.LENGTH_SHORT).show();
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