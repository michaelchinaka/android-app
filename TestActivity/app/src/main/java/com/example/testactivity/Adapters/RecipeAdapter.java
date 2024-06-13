package com.example.testactivity.Adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testactivity.ExpandableView;
import com.example.testactivity.FoodItem;
import com.example.testactivity.R;
import com.example.testactivity.Recipe.RecipeClass;
import com.example.testactivity.RecipeDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

    public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeView> {
        private ArrayList<RecipeClass> recipeResults;
        private Context mContext;


        public RecipeAdapter(ArrayList<RecipeClass> arrayList, Context applicationContext) {
            this.recipeResults = arrayList;
            this.mContext = applicationContext;
        }


        @NonNull
        @Override
        public RecipeView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view= inflater.inflate(R.layout.single_recipe,parent,false);
            return new RecipeView(view);

        }


        @Override
        public void onBindViewHolder(@NonNull RecipeView holder, int position) {
            // get properties from foodItem class
            String title =recipeResults.get(position).getTitle();
            String image =recipeResults.get(position).getImage();
            int missedIngredientCount = (int) recipeResults.get(position).getMissedIngredientCount();
            int usedIngredientCount = (int) recipeResults.get(position).getUsedIngredientCount();
            int unusedIngredientCount = (int) recipeResults.get(position).getUnusedIngredients().size();


            //set values retrieved to holder
            Picasso.get().load(image).into(holder.image);
            holder.title.setText(title);
            holder.title.setSelected(true);
            holder.missed.setText("Missed Ingredients (" + missedIngredientCount + ")");
            holder.unused.setText("Unused Ingredients (" + unusedIngredientCount + ")");
            holder.used.setText("Used Ingredients (" + usedIngredientCount + ")");


            holder.itemView.setOnClickListener(v -> {
                int id  = (int) recipeResults.get(position).getId();

                Intent intent = new Intent(mContext, RecipeDetailActivity.class); // open recipe information page
                intent.putExtra("id",id);
                mContext.startActivity(intent);
            });

        }


        @Override
        public int getItemCount() {
            return recipeResults.size();
        }

        public class RecipeView extends RecyclerView.ViewHolder {
            CardView recipeContainer;
            TextView title;
            TextView missed;
            TextView used;
            TextView unused;
            ImageView image;

            //OnFoodSearchListener onFoodSearchListener;


            public RecipeView(@NonNull View itemView) {

                super(itemView);
                title = (TextView) itemView.findViewById(R.id.recipeTitle);
                image = (ImageView) itemView.findViewById(R.id.recipdImage);
                missed = (TextView) itemView.findViewById(R.id.missed_ingredients);
                used = (TextView) itemView.findViewById(R.id.used_ingredients);
                unused = (TextView) itemView.findViewById(R.id.unused_ingredients);

                //this.onFoodSearchListener = onFoodSearchListener;


            }}
    }




