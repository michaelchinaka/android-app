package com.example.testactivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testactivity.Adapters.SearchItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class SearchActivity extends AppCompatActivity{
    private ArrayList<FoodItem> arrayList = new ArrayList<>();
    public static RecyclerView recyclerView;
    private SearchView searchview;
    private SearchItems foodItems;
   // private SearchItems.OnFoodSearchListener onFoodSearchListener; // my addition


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_search);

        //setOnClickListener();

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SearchItems(arrayList, getApplicationContext()));
        searchview = findViewById(R.id.txtItem);


//        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getData(query);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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

                Intent helpIntent = new Intent(SearchActivity.this, WebViewActivity.class);
                startActivity(helpIntent);

                return false;
            }
        });
        // Return true to display menu
        return true;
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


    public void getData(String query) {
        arrayList.clear();
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.spoonacular.com/food/ingredients/search?query=" + query + "&number=8&apiKey=InsertYourApiKey";
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
                        arrayList.add(new FoodItem(jsonObject.getString("name"), jsonObject.getString("image")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                foodItems = new SearchItems(arrayList,getApplicationContext());
                recyclerView.setAdapter(foodItems);
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