package com.example.testactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import androidx.appcompat.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testactivity.Recipe.RecipeIngredient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {
    private ImageView image;
    private TextView summary;
    private TextView ingredients;
    private TextView method;
    private TextView recipeName;
    private ShareActionProvider mShareActionProvider;
    private boolean clockBool = true;
    private boolean notificationsBool = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSettings();
        setContentView(R.layout.activity_recipe_detail);
        image = findViewById(R.id.meal_image);
        summary = findViewById(R.id.summary);
        ingredients = findViewById(R.id.ingredientsDescriotion);
        recipeName = findViewById(R.id.meal_name);
        int id = getIntent().getIntExtra("id", 0);

//        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//            summary.se
//
//        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
//            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        }

        getData(id);

    }

    private void loadSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        clockBool = sp.getBoolean("clock",true); // this needs to be addressed
        notificationsBool = sp.getBoolean("notifications",true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.share, menu);

        // Locate MenuItem with ShareActionProvider
        //MenuItem item = menu.findItem(R.id.mShare);
        MenuItem clock = menu.findItem(R.id.clock);
        MenuItem help = menu.findItem(R.id.help);
        MenuItem settings = menu.findItem(R.id.settingsPart);


        help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                    Intent helpIntent = new Intent(RecipeDetailActivity.this, WebViewActivity.class);
                    startActivity(helpIntent);

                return false;
            }
        });

        settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent helpIntent = new Intent(RecipeDetailActivity.this, SettingsActivity.class);
                startActivity(helpIntent);

                return false;
            }
        });

        clock.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(clockBool) {
                    Intent openClockIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
                    openClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(openClockIntent);
                }else{
                    Toast.makeText(RecipeDetailActivity.this, "Turn on Clock PermISIION In Settings", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
        // Fetch and store ShareActionProvider
        //mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // Return true to display menu
        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }}

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
        loadSettings();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getData(int id) {

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.spoonacular.com/recipes/716429/information?includeNutrition=false&apiKey=InsertYourApiKey";

        //String url = "https://api.spoonacular.com/recipes/" + id + "/information?includeNutrition=false&apiKey=InsertYourApiKey";
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String result = response.toString();
                    try {
                        JSONObject jsonObject = response;
                        String thisImage = jsonObject.getString("image");
                        Log.d("my-api", "==== " + jsonObject.getString("title"));
                        String thisTitle = jsonObject.getString("title");
                        Log.d("my-api", "==== " + jsonObject.getString("sourceUrl"));
                        String thisSourceUrl = jsonObject.getString("sourceUrl");
                        Log.d("my-api", "==== " + jsonObject.getString("summary"));
                        String thisSummary = jsonObject.getString("summary");
                        Log.d("my-api", "==== " + jsonObject.getString("readyInMinutes"));
                        int thisDuration = jsonObject.getInt("readyInMinutes");
                        Log.d("my-api", "==== " + jsonObject.getString("servings"));
                        int thisServings = jsonObject.getInt("servings");
                        Log.d("my-api", "==== " + jsonObject.getJSONArray("extendedIngredients"));
                        JSONArray extendedIngredients = jsonObject.getJSONArray("extendedIngredients");

                        ArrayList<RecipeIngredient> ingredientArray = createRecipeIngredient(extendedIngredients);

                        // set values of view items
                        summary.setText(thisSummary);
                        Picasso.get().load(thisImage).into(image);
                        recipeName.setText(thisTitle);
                        ingredients.setText(makeString(ingredientArray));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            private String makeString(ArrayList<RecipeIngredient> ingredientArray) {
                String result = "";
                for (int i = 0;i<ingredientArray.size();i++){
                    result += "- " + ingredientArray.get(i).getOriginal() +"\n\n";
                }
                return result;
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


    private ArrayList<RecipeIngredient> createRecipeIngredient(JSONArray jsonArray) {
        ArrayList<RecipeIngredient> ingredients = new ArrayList<RecipeIngredient>();
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
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
}