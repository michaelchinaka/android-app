package com.example.testactivity.Adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testactivity.DBHandler;
import com.example.testactivity.FoodItem;
import com.example.testactivity.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class AllFoodItems extends RecyclerView.Adapter<AllFoodItems.UserView> {
    private ArrayList<FoodItem> arrayList;
    private Context mContext;
    private DBHandler dbHandler;
    public  AllFoodItems(ArrayList<FoodItem> arrayList, Context applicationContext) {
        this.arrayList = arrayList;
        this.mContext = applicationContext;
    }

    public void setArrayList(ArrayList<FoodItem> arrayList) {
        this.arrayList = arrayList;
    }

    private DatePickerDialog datePickerDialog;

    @NonNull
    @Override
    public UserView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.single_food,parent,false);
        dbHandler = new DBHandler(mContext);
        return new UserView(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserView holder, int position) {
        // get properties from foodItem class
        String image =arrayList.get(position).getImage();
        String name =arrayList.get(position).getName();
        String purchaseDate = arrayList.get(position).getPurchaseDate();
        if(arrayList.get(position).getExpiryDate() == null){
        arrayList.get(position).setExpiryDate(purchaseDate);}
        String expiry = arrayList.get(position).getExpiryDate();
        String quantity = "" +arrayList.get(position).getQuantity();
        boolean recurring = arrayList.get(position).isRecurring();

        //set values retrieved to holder
        Picasso.get().load(image).into(holder.image);
        holder.name.setText(name);
        holder.purchaseButton.setText(purchaseDate);
        holder.expiryButton.setText(expiry);
        holder.quantity.setText(quantity);
        holder.recurring.setChecked(recurring);

        holder.itemView.setOnClickListener(v -> {
            View subDetails = holder.itemView.findViewById(R.id.subDetails);
            int visible = subDetails.getVisibility();
            boolean expanded = false;
            if(visible == 0){
                expanded = false;
            }else{ expanded = true;}
            subDetails.setVisibility(expanded ? View.VISIBLE : View.GONE);

        });


    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class UserView extends RecyclerView.ViewHolder{
        TextView name;
        ImageView image;
        Button purchaseButton;
        Button expiryButton;
        EditText quantity;
        Switch recurring;
        Button confirm;



        public UserView(@NonNull View itemView) {

            super(itemView);
            name = (TextView) itemView.findViewById(R.id.foodName);
            image = (ImageView) itemView.findViewById(R.id.foodImage);
            purchaseButton = (Button) itemView.findViewById(R.id.purchaseButton);
            expiryButton = (Button) itemView.findViewById(R.id.expiryButton);
            quantity = (EditText) itemView.findViewById(R.id.quantity);
            recurring = (Switch) itemView.findViewById(R.id.recurring);
            confirm = (Button) itemView.findViewById(R.id.confirm_button);


            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = arrayList.get(getPosition()).getName();
                    String image = arrayList.get(getPosition()).getImage();
                    int amount = Integer.parseInt(quantity.getText().toString()) ;
                    arrayList.get(getPosition()).setQuantity(amount);
                    arrayList.get(getPosition()).setPurchaseDate(purchaseButton.getText().toString());
                    arrayList.get(getPosition()).setExpiryDate(expiryButton.getText().toString());
                    String pDate = arrayList.get(getPosition()).getPurchaseDate();
                    String eDate = arrayList.get(getPosition()).getExpiryDate();
                    boolean recurring = arrayList.get(getPosition()).isRecurring();


                    dbHandler.updatefridgeItem(name,amount,pDate,eDate,recurring,image);
                    Toast.makeText(mContext, "Item Updated", Toast.LENGTH_SHORT).show();
                }
            });


            recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    arrayList.get(getPosition()).setRecurring(b);

                }
            });

            purchaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo","purchase button clicked" + purchaseButton.getText());
                    initDatePicker(purchaseButton);
                    String date = ""+ purchaseButton.getText();
                    arrayList.get(getPosition()).setPurchaseDate(date);

                }
            });

            expiryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initDatePicker(expiryButton);
                    String date = ""+ expiryButton.getText();
                    arrayList.get(getPosition()).setExpiryDate(date);

                }
            });

        }

        private void initDatePicker(Button button)
        {
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day)
                {
                    month = month + 1;
                    String date = makeDateString(day, month, year);
                    button.setText(date);
                    datePickerDialog.hide();
                }
            };

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            int style = AlertDialog.THEME_HOLO_LIGHT;

            datePickerDialog = new DatePickerDialog(button.getContext(), style, dateSetListener, year, month, day);
            datePickerDialog.show();

        }

        private String makeDateString(int day, int month, int year) {
            return dateNumberFormatter(day) + "/" + dateNumberFormatter(month) + "/" + year;
        }

        private String dateNumberFormatter(int number){

            if(number <10){
                return "0" + number;
            }
            else {return "" +number;}

        }
        public void openDatePicker(View view)
        {
            datePickerDialog.show();
    }
}}