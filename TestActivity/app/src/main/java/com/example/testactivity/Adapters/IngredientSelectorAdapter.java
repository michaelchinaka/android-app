package com.example.testactivity.Adapters;

import android.app.DatePickerDialog;
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

import com.example.testactivity.ExpandableView;
import com.example.testactivity.FoodItem;
import com.example.testactivity.Fragments.IngredientSelectorFragment;
import com.example.testactivity.IngredientSelectorActivity;
import com.example.testactivity.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

    public class IngredientSelectorAdapter extends RecyclerView.Adapter<IngredientSelectorAdapter.IngredientView> {
        private ArrayList<FoodItem> expiringItems = new ArrayList<>();;
        public static ArrayList<FoodItem> selectedItems = new ArrayList<>();;
        private Context mContext;


        public IngredientSelectorAdapter(ArrayList<FoodItem> arrayList, Context applicationContext) {
            this.expiringItems = arrayList;
            this.mContext = applicationContext;

        }


        @NonNull
        @Override
        public IngredientSelectorAdapter.IngredientView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view= inflater.inflate(R.layout.single_ingredient_selector,parent,false);
            return new IngredientSelectorAdapter.IngredientView(view);

        }

        @Override
        public void onBindViewHolder(@NonNull IngredientSelectorAdapter.IngredientView holder, int position) {
            // get properties from foodItem class
            String image =expiringItems.get(position).getImage();
            String name = expiringItems.get(position).getName();
            boolean checked = expiringItems.get(position).isSelected();

            //set values retrieved to holder
            Picasso.get().load(image).into(holder.image);
            holder.name.setText(name);
            holder.selected.setChecked(checked);

            holder.selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FoodItem selectedItem = expiringItems.get(position);
                    if(!expiringItems.get(position).isSelected()){
                        selectedItems.add(selectedItem);
                    }
                    else{
                        selectedItems.remove(selectedItem);
                    }
                    expiringItems.get(position).setSelected(!expiringItems.get(position).isSelected());
                    IngredientSelectorActivity.recyclerIngredient.getAdapter().notifyDataSetChanged();
                }
            });

            holder.itemView.setOnClickListener(v -> {
                FoodItem selectedItem = expiringItems.get(position);
                if(!expiringItems.get(position).isSelected()){
                    selectedItems.add(selectedItem);
                    expiringItems.get(position).setSelected(!expiringItems.get(position).isSelected());
                    holder.selected.setChecked(expiringItems.get(position).isSelected());
                }else{
                    selectedItems.remove(selectedItem);
                    expiringItems.get(position).setSelected(!expiringItems.get(position).isSelected());
                    holder.selected.setChecked(expiringItems.get(position).isSelected());
                }


                IngredientSelectorFragment.recyclerIngredient.getAdapter().notifyDataSetChanged();
            });

        }




        @Override
        public int getItemCount() {
            return expiringItems.size();
        }

        public class IngredientView extends RecyclerView.ViewHolder {
            TextView name;
            ImageView image;
            CheckBox selected;
            //OnFoodSearchListener onFoodSearchListener;


            public IngredientView(@NonNull View itemView) {

                super(itemView);
                name = (TextView) itemView.findViewById(R.id.ingredientItem);
                image = (ImageView) itemView.findViewById(R.id.ingredientImage);
                selected = (CheckBox) itemView.findViewById(R.id.ingredientCheckbox);


            }}
    }


