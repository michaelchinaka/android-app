package com.example.testactivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "fridgedb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE1_NAME = "myFridgeContents";

    // below variable is for our table name.
    private static final String TABLE2_NAME = "myShoppinglist";


    // below variable is for our  name column
    private static final String ITEM_COL = "item";

    // below variable id for our  image column.
    private static final String BOUGHT_COL = "isBought";

    // below variable is for our  name column
    private static final String NAME_COL = "name";

    // below variable id for our  image column.
    private static final String IMAGE_COL = "image";

    // below variable for our expiry date column.
    private static final String EXPIRY_COL = "expiry_Date";

    // below variable for our purchase date column.
    private static final String PURCHASE_COL = "purchase_Date";

    // below variable is for our recurring column.
    private static final String RECURRING_COL = "recurring";

    // below variable for our expiry date column.
    private static final String QUANTITY_COL = "quantity";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String queryFridge = "CREATE TABLE " + TABLE1_NAME + " ("
                + QUANTITY_COL + " INTEGER , "
                + NAME_COL + " TEXT,"
                + PURCHASE_COL + " TEXT,"
                + EXPIRY_COL + " TEXT,"
                + RECURRING_COL + " BOOLEAN,"
                + IMAGE_COL + " TEXT)";

        String queryList = "CREATE TABLE " + TABLE2_NAME + " ("
                + ITEM_COL + " TEXT , "
                + BOUGHT_COL + " BOOLEAN)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(queryFridge);
        db.execSQL(queryList);
    }

    // this method is use to add new course to our sqlite database.
    public void addFridgeItem(String name, int quantity, String purchaseDate, String expiryDate,boolean recurring,String image) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, name);
        values.put(QUANTITY_COL, quantity);
        values.put(PURCHASE_COL, purchaseDate);
        values.put(String.valueOf(RECURRING_COL), recurring);
        values.put(IMAGE_COL, image);
        values.put(EXPIRY_COL, expiryDate);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE1_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public void addShoppingitem(String name, boolean isBought) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(ITEM_COL, name);
        values.put(BOUGHT_COL, isBought);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE2_NAME, null, values);

        db.close();
    }

    public ArrayList<FoodItem> getFridgeContents() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorFoods = db.rawQuery("SELECT * FROM " + TABLE1_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<FoodItem> allFoods = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorFoods.moveToFirst()) {
            do {
    System.out.println(cursorFoods.getString(0)); //quantity
                System.out.println(cursorFoods.getString(4)); // isRecurring
                System.out.println(cursorFoods.getString(1)); //name
                System.out.println(cursorFoods.getString(2)); //purchase
                System.out.println(cursorFoods.getString(3)); //exoiry
                System.out.println(cursorFoods.getString(5)); // image

                String temp = cursorFoods.getString(4);
                boolean b = false;
                if(temp.equalsIgnoreCase("1")){
                    b = true;
                }


                // on below line we are adding the data from cursor to our array list.
                //name(1),image(5),quantity(0)purchase(2),recurring(4),expiry(3)
                allFoods.add(new FoodItem(cursorFoods.getString(1),
                        cursorFoods.getString(5),
                        cursorFoods.getInt(0),
                        cursorFoods.getString(2),
                        b,
                        cursorFoods.getString(3)
                ));
            } while (cursorFoods.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorFoods.close();
        return allFoods;
    }

    public ArrayList<ShoppingItem> getShoppinglist() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorFoods = db.rawQuery("SELECT * FROM " + TABLE2_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<ShoppingItem> list = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorFoods.moveToFirst()) {
            do {
                System.out.println(cursorFoods.getString(0)); //quantity
                System.out.println(cursorFoods.getString(1)); // isRecurring
                String test = cursorFoods.getString(1);
                boolean b = false;
                if(test.equalsIgnoreCase("1")){
                    b = true;
                }
                // on below line we are adding the data from cursor to our array list.
                //name(1),image(5)
                list.add(new ShoppingItem(cursorFoods.getString(0),
                        b
                ));
            } while (cursorFoods.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorFoods.close();
        return list;
    }

    // below is the method for updating our courses
    public void updateShopItem(String name, boolean bought) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(ITEM_COL, name);
        values.put(BOUGHT_COL, bought);


        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(TABLE2_NAME, values, "item=?", new String[]{name});
        db.close();
    }

    public void updatefridgeItem(String name, int quantity, String purchaseDate, String expiryDate,boolean recurring,String image) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, name);
        values.put(QUANTITY_COL, quantity);
        values.put(PURCHASE_COL, purchaseDate);
        values.put(String.valueOf(RECURRING_COL), recurring);
        values.put(IMAGE_COL, image);
        values.put(EXPIRY_COL, expiryDate);


        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(TABLE1_NAME, values, "name=?", new String[]{name});
        db.close();
    }



    // below is the method for deleting our course.
    public void deleteFridgeItem(String name) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE1_NAME, "name=?", new String[]{name});
        db.close();
    }

    public void deleteShoppingItem(String name) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE2_NAME, "item=?", new String[]{name});
        db.close();
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        onCreate(db);
    }

}


