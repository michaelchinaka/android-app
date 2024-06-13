package com.example.testactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testactivity.Adapters.AllFoodItems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpandableView extends AppCompatActivity {
    public static ArrayList<FoodItem> arrayList = new ArrayList<>();
    public static ArrayList<FoodItem> expiringSoonItems = new ArrayList<>();
    public static ArrayList<FoodItem> expiredItems = new ArrayList<>();
    public static RecyclerView recyclerView;
    private AllFoodItems foodItems;
    private FloatingActionButton floatingButton;
    private DBHandler dbHandler;
    private Button button;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_view);

        //get food item from search list
        //getIncomingIntent();


        //initialise window items
        dbHandler = new DBHandler(this);
        arrayList = dbHandler.getFridgeContents();
       // caluculateDays();
        recyclerView = findViewById(R.id.fridgeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AllFoodItems(arrayList,this));
        floatingButton = findViewById(R.id.floatingAdd);
        //button = findViewById(R.id.btnTest);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
        //getData();
        System.out.println(arrayList.size());

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//                Notification notify=new Notification.Builder
//                        (getApplicationContext()).setContentTitle("Expiring").setContentText("There are " +arrayList.size() + "items expiring soon").
//                        setContentTitle("Expiring Soon").setSmallIcon(R.drawable.ic_baseline_warning_24).build();
//
//                notify.flags |= Notification.FLAG_AUTO_CANCEL;
//                notif.notify(0, notify);
//                //addNotification();
//            }





//            private void addNotification() {
//                NotificationCompat.Builder builder =
//                        new NotificationCompat.Builder(getApplicationContext())
//                                .setSmallIcon(R.drawable.ic_baseline_warning_24)
//                                .setContentTitle("Notifications Example")
//                                .setContentText("This is a test notification");
//
//                Intent notificationIntent = new Intent(getApplicationContext(), IngredientSelectorActivity.class);
//                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(contentIntent);
//
//                // Add as notification
//                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.notify(0, builder.build());
//            }
//        });



        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchPage();
            }
        });




    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu_bar, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem help = menu.findItem(R.id.help);
        MenuItem settings = menu.findItem(R.id.settings);

        help.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent helpIntent = new Intent(ExpandableView.this, WebViewActivity.class);
                startActivity(helpIntent);

                return false;
            }
        });

        settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent helpIntent = new Intent(ExpandableView.this, SettingsActivity.class);
                startActivity(helpIntent);

                return false;
            }
        });
        // Return true to display menu
        return true;
    }

    public void caluculateDays(){
        ArrayList <FoodItem> temp = new ArrayList<>();
        for(int i =0;i<arrayList.size();i++){
            SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");
            Date now = new Date();
            try {
                Date expiry = dates.parse(arrayList.get(i).getExpiryDate());
                long difference = Math.abs(expiry.getTime() - now.getTime());
                long differenceDates = difference / (24 * 60 * 60 * 1000);
                double dayDifference = differenceDates;
                arrayList.get(i).setDaysLeft(dayDifference);
                arrange(i);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


    }
// splits fridge contents into expiring soon, expired, and normal
    private void arrange(int i) {
        double daysleft = arrayList.get(i).getDaysLeft();
        if(daysleft<=3 && daysleft >=0){
            expiringSoonItems.add(arrayList.get(i));
            arrayList.remove(i);
        } else if (daysleft<0){
            expiredItems.add(arrayList.get(i));
            arrayList.remove(i);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        //update arraylist here and notify of change

        recyclerView.getAdapter().notifyDataSetChanged();
//        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notify=new Notification.Builder
//                (getApplicationContext()).setContentTitle("Expiring").setContentText("There are " +arrayList.size() + "items expiring soon").
//                setContentTitle("Expiring Soon").setSmallIcon(R.drawable.ic_baseline_warning_24).build();
//
//        notify.flags |= Notification.FLAG_AUTO_CANCEL;
//        notif.notify(0, notify);
        //getIncomingIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void getIncomingIntent()

    {
        if(getIntent().hasExtra("name") && getIntent().hasExtra("image")){
            Bundle extras = getIntent().getExtras();
            String tempName = extras.getString("name");
            String tempImage = extras.getString("image");
            FoodItem newFoodItem = new FoodItem(tempName,tempImage);
            arrayList.add(newFoodItem);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void openSearchPage() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }


    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            FoodItem temp = arrayList.get(position);
            arrayList.remove(position);
            dbHandler.deleteFridgeItem(arrayList.get(position).getName());
            recyclerView.getAdapter().notifyDataSetChanged();
            //snackbar code below is faulty
//            Snackbar.make(recyclerView, viewHolder.getAdapterPosition(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    arrayList.add(position,temp);
//                    foodItems.notifyDataSetChanged();
//                }
//            });

        }


    };


    public void getData() {
        //arrayList.clear();
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.spoonacular.com/food/ingredients/search?query=apples&number=8&apiKey=bde5a5b3c824476bbc24aca5e61beb25";
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
                System.out.println(arrayList.size());
                foodItems = new AllFoodItems(arrayList,getApplicationContext());
                System.out.println(arrayList.size());
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
    }}