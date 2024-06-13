package com.example.testactivity.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testactivity.Adapters.IngredientSelectorAdapter;
import com.example.testactivity.Adapters.SearchItems;
import com.example.testactivity.FoodItem;
import com.example.testactivity.IngredientSelectorActivity;
import com.example.testactivity.R;
import com.example.testactivity.RecipeResultsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IngredientSelectorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientSelectorFragment extends Fragment {
    private ArrayList<FoodItem> ingredientList = new ArrayList<>();
    public static RecyclerView recyclerIngredient;
    private IngredientSelectorAdapter adapter; // change adapter
    private Button button;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IngredientSelectorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientSelectorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientSelectorFragment newInstance(String param1, String param2) {
        IngredientSelectorFragment fragment = new IngredientSelectorFragment();
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
         View view =inflater.inflate(R.layout.fragment_ingredient_selector, container, false);

        recyclerIngredient = view.findViewById(R.id.ingredientRecyclerF);
        recyclerIngredient.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerIngredient.setAdapter(new SearchItems(ingredientList, getActivity()));
        getData();
        //ingredientList = ExpandableViewFragment.expiringSoonItems;
        button = view.findViewById(R.id.btnGenerateF);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<FoodItem> temp = IngredientSelectorAdapter.selectedItems;
                String query = generateQuery(temp);
                Intent intent = new Intent(getActivity(), RecipeResultsActivity.class);
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

         return view;
    }

    public void setIngredientList(ArrayList<FoodItem> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getData() {
        //arrayList.clear();
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
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
                adapter = new IngredientSelectorAdapter(ingredientList,getActivity());
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