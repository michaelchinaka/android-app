package com.example.testactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testactivity.Adapters.AllFoodItems;
import com.example.testactivity.Adapters.IngredientSelectorAdapter;
import com.example.testactivity.Adapters.SearchItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IngredientSelectorActivity extends AppCompatActivity {
    private ArrayList<FoodItem> ingredientList = new ArrayList<>();
    public static RecyclerView recyclerIngredient;
    private IngredientSelectorAdapter adapter; // change adapter
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_selector);
        recyclerIngredient = findViewById(R.id.ingredientRecycler);
        recyclerIngredient.setLayoutManager(new LinearLayoutManager(this));
        recyclerIngredient.setAdapter(new SearchItems(ingredientList, getApplicationContext()));
        getData();
        //ingredientList = ExpandableView.expiringSoonItems;
        button = findViewById(R.id.btnGenerate);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<FoodItem> temp = IngredientSelectorAdapter.selectedItems;
                String query = generateQuery(temp);
                Intent intent = new Intent(getApplicationContext(), RecipeResultsActivity.class);
                intent.putExtra("query",query);
                startActivity(intent);
            }

            private String generateQuery(ArrayList<FoodItem> temp) {
                String result = temp.get(0).getName();
                for (int i =1;i<temp.size();i++){
                    result += ",+" +temp.get(i).getName();
                }
                return result;
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

                Intent helpIntent = new Intent(IngredientSelectorActivity.this, WebViewActivity.class);
                startActivity(helpIntent);

                return false;
            }
        });
        // Return true to display menu
        return true;
    }

    public void setIngredientList(ArrayList<FoodItem> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    public void getData() {
        //arrayList.clear();
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.spoonacular.com/food/ingredients/search?query=apples&number=8&apiKey=InsertYourApiKey";
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray Jarray = null;
                try {
                    Jarray = response.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < Jarray.length(); i++) {
                    try {
                        JSONObject jsonObject = Jarray.getJSONObject(i);
                        Log.d("my-api", "==== " + jsonObject.getString("name"));
                        Log.d("my-api", "==== " + jsonObject.getString("image"));
                        ingredientList.add(new FoodItem(jsonObject.getString("name"), jsonObject.getString("image")));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(ingredientList.size());
                adapter = new IngredientSelectorAdapter(ingredientList,IngredientSelectorActivity.this);
                System.out.println(ingredientList.size());
                recyclerIngredient.setAdapter(adapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.getMessage());
                Log.d("my-api", "went Wrong");
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}