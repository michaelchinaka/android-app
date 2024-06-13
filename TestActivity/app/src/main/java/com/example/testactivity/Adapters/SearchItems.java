package com.example.testactivity.Adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testactivity.DBHandler;
import com.example.testactivity.ExpandableView;
import com.example.testactivity.FoodItem;
import com.example.testactivity.Fragments.ExpandableViewFragment;
import com.example.testactivity.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchItems extends RecyclerView.Adapter<SearchItems.UserView> {
        private ArrayList<FoodItem> searchResults;
        private Context mContext;
        private DatePickerDialog datePickerDialog;
        private DBHandler dbHandler;
        //private OnFoodSearchListener mOnFoodSearchListener;

        public SearchItems(ArrayList<FoodItem> arrayList, Context applicationContext) {
            this.searchResults = arrayList;
            this.mContext = applicationContext;
            //this.mOnFoodSearchListener = onFoodSearchListener;
        }


        @NonNull
        @Override
        public SearchItems.UserView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            dbHandler = new DBHandler(mContext);
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view= inflater.inflate(R.layout.single_food,parent,false);
            return new SearchItems.UserView(view);


        }

        @Override
        public void onBindViewHolder(@NonNull SearchItems.UserView holder, int position) {
            // get properties from foodItem class
            String image =searchResults.get(position).getImage();
            String name =searchResults.get(position).getName();


            //set values retrieved to holder
            Picasso.get().load(image).into(holder.image);
            holder.name.setText(name);


            holder.itemView.setOnClickListener(v -> {
                FoodItem selectedItem = searchResults.get(position);
                String tempName = selectedItem.getName();
                String tempImage = selectedItem.getImage();
                String tempExpiry = selectedItem.getExpiryDate();
                String tempPurchase = selectedItem.getPurchaseDate();
                boolean tempRecurring = selectedItem.isRecurring();
                int tempQuantity = selectedItem.getQuantity();

                dbHandler.addFridgeItem(tempName,tempQuantity,tempPurchase,tempExpiry,tempRecurring,tempImage);
                //new stuff
                ExpandableViewFragment.arrayList.add(selectedItem);
                ExpandableViewFragment.recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(mContext, "Item added", Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(mContext, ExpandableView.class);
//                intent.putExtra("name",name);
//                intent.putExtra("image",image);
//                mContext.startActivity(intent);

                // rather update the arraylist here and it will be updated onResume


            });

        }


        @Override
        public int getItemCount() {
            return searchResults.size();
        }

        public class UserView extends RecyclerView.ViewHolder {
            TextView name;
            ImageView image;
            Button btnpurchase;
            Button btnExpiry;
            //OnFoodSearchListener onFoodSearchListener;


            public UserView(@NonNull View itemView) {

                super(itemView);
                name = (TextView) itemView.findViewById(R.id.foodName);
                image = (ImageView) itemView.findViewById(R.id.foodImage);
                btnpurchase = (Button) itemView.findViewById(R.id.purchaseButton);
                btnExpiry = (Button) itemView.findViewById(R.id.expiryButton);


        }}
    }
