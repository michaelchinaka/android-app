 package com.example.testactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.testactivity.Fragments.ExpandableViewFragment;
import com.example.testactivity.Fragments.IngredientSelectorFragment;
import com.example.testactivity.Fragments.ShoppingListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

 public class MainActivity extends AppCompatActivity {
     private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ExpandableViewFragment()).commit();
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

                 Intent helpIntent = new Intent(MainActivity.this, WebViewActivity.class);
                 startActivity(helpIntent);

                 return false;
             }
         });

         settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
             @Override
             public boolean onMenuItemClick(MenuItem menuItem) {

                 Intent helpIntent = new Intent(MainActivity.this, SettingsActivity.class);
                 startActivity(helpIntent);

                 return false;
             }
         });
         // Return true to display menu
         return true;
     }

     private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
         @Override
         public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
             Fragment fragment = null;
             switch (menuItem.getItemId()){
                 case R.id.fridgePart:
                     fragment = new ExpandableViewFragment();
                     break;

                 case R.id.shoppingListPart:
                     fragment = new ShoppingListFragment();
                     break;

                 case R.id.expiringPart:
                     fragment = new IngredientSelectorFragment();
                     break;

             }
             getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

             return true;
         }
     };
}