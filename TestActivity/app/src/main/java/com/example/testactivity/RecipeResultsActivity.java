package com.example.testactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.testactivity.Adapters.RecipeAdapter;
import com.example.testactivity.Recipe.RecipeClass;
import com.example.testactivity.Recipe.RecipeIngredient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeResultsActivity extends AppCompatActivity {
    private ArrayList<RecipeClass> recipeList = new ArrayList< RecipeClass >();
    private ProgressDialog dialog;
    private RecipeAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_results);
        //String query = "rice,+chicken";
        String query = getIntent().getStringExtra("query");
        //dialog.setTitle("Loading");
        recyclerView = findViewById(R.id.recyclerRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new RecipeAdapter(recipeList,RecipeResultsActivity.this);
        recyclerView.setAdapter(adapter);
        getData(query);
        //dialog.dismiss();
       // dialog.show();
        System.out.println(recipeList.size());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_bar, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem help = menu.findItem(R.id.help);
        MenuItem settings = menu.findItem(R.id.settingsPart);

        help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent helpIntent = new Intent(RecipeResultsActivity.this, WebViewActivity.class);
                startActivity(helpIntent);

                return false;
            }
        });

        settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent helpIntent = new Intent(RecipeResultsActivity.this, SettingsActivity.class);
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

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=" + query + "&number=2&apiKey=bde5a5b3c824476bbc24aca5e61beb25";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Log.d("my-api", "==== " + jsonObject.getInt("id"));
                        int id = jsonObject.getInt("id");
                        Log.d("my-api", "==== " + jsonObject.getString("title"));
                        String title = jsonObject.getString("title");
                        Log.d("my-api", "==== " + jsonObject.getString("image"));
                        String image = jsonObject.getString("image");
                        Log.d("my-api", "==== " + jsonObject.getString("imageType"));
                        Log.d("my-api", "==== " + jsonObject.getInt("usedIngredientCount"));
                        int usedIngredientCount = jsonObject.getInt("usedIngredientCount");
                        Log.d("my-api", "==== " + jsonObject.getInt("missedIngredientCount"));
                        int missedIngredientCount = jsonObject.getInt("missedIngredientCount");
                        Log.d("my-api", "==== " + jsonObject.getString("usedIngredients"));
                        ArrayList< RecipeIngredient > usedIngredients = createRecipeIngredient(jsonObject.getJSONArray("usedIngredients"));
                        Log.d("my-api", "==== " + jsonObject.getString("unusedIngredients"));
                        ArrayList< RecipeIngredient > unusedIngredients = createRecipeIngredient(jsonObject.getJSONArray("unusedIngredients"));
                        Log.d("my-api", "==== " + jsonObject.getString("missedIngredients"));
                        ArrayList< RecipeIngredient > missedIngredients = createRecipeIngredient(jsonObject.getJSONArray("missedIngredients"));
                        System.out.println("success");

                        RecipeClass thisRecipe = new RecipeClass(id,image,missedIngredientCount,missedIngredients,title,unusedIngredients,usedIngredientCount,usedIngredients);
                        recipeList.add(thisRecipe);
                        adapter = new RecipeAdapter(recipeList,RecipeResultsActivity.this);
                        //arrayList.add(new FoodItem(jsonObject.getString("name"), jsonObject.getString("image")));
                        //adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        System.out.println("failed");
                        e.printStackTrace();
                    }
                }
            }

            private ArrayList< RecipeIngredient > createRecipeIngredient(JSONArray jsonArray) {
                ArrayList< RecipeIngredient > ingredients = new ArrayList < RecipeIngredient > ();
                try {

                    for(int i = 0; i<jsonArray.length();i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        int id = jsonObject.getInt("id");
                        String original = jsonObject.getString("original");
                        String name = jsonObject.getString("name");
                        String image = jsonObject.getString("image");
                        RecipeIngredient thisIngredient = new RecipeIngredient(id, name, original, image);
                        ingredients.add(thisIngredient);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return ingredients;
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