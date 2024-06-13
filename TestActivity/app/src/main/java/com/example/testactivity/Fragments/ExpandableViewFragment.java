package com.example.testactivity.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.testactivity.Adapters.AllFoodItems;
import com.example.testactivity.DBHandler;
import com.example.testactivity.FoodItem;
import com.example.testactivity.R;
import com.example.testactivity.SearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpandableViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpandableViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static ArrayList<FoodItem> arrayList = new ArrayList<>();
    public static ArrayList<FoodItem> expiringSoonItems = new ArrayList<>();
    public static ArrayList<FoodItem> expiredItems = new ArrayList<>();
    public static RecyclerView recyclerView;
    private AllFoodItems foodItems;
    private FloatingActionButton floatingButton;
    private DBHandler dbHandler;
    private Button button;

    public ExpandableViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpandableViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpandableViewFragment newInstance(String param1, String param2) {
        ExpandableViewFragment fragment = new ExpandableViewFragment();
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
        View view = inflater.inflate(R.layout.fragment_expandable_view, container, false);


        //initialise window items
        dbHandler = new DBHandler(getActivity());
        arrayList = dbHandler.getFridgeContents();
        // caluculateDays();
        recyclerView = view.findViewById(R.id.fridgeListF);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new AllFoodItems(arrayList,getActivity()));
        floatingButton = view.findViewById(R.id.floatingAddF);
        //button = findViewById(R.id.btnTest);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);

        System.out.println(arrayList.size());

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchPage();
            }
        });

        return view;
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

    public void onStart() {
        super.onStart();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void openSearchPage() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
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
            dbHandler.deleteFridgeItem(temp.getName());
            recyclerView.getAdapter().notifyDataSetChanged();

        }


    };
}