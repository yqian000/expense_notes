package com.example.expensenotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "data.db";
    public static final String TABLE_NAME = "expense_table";
    public static final String COL1 = "DATE";
    public static final String COL2 = "TIME";
    public static final String COL3 = "AMOUNT";
    public static final String COL4 = "CATEGORY";
    public static final String COL_ID = "ID";

    // default constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // is called only the first time the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL1 + " TEXT, " + COL2 + " TEXT, " + COL3 + " REAL, " + COL4 + " TEXT)";
        db.execSQL(createTable);
    }

    // is called if the database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, expense.getDate());
        contentValues.put(COL2, expense.getTime());
        contentValues.put(COL3, expense.getAmount());
        contentValues.put(COL4, expense.getCategory());

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public ArrayList<Expense> getAll() {
        ArrayList<Expense> transList = new ArrayList<>();

        // get data from database in reverse order
        String query = "SELECT * FROM " + TABLE_NAME  + " ORDER BY " + COL_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(1);
                String time = cursor.getString(2);
                double amount = cursor.getDouble(3);
                String category = cursor.getString(4);

                Expense expense = new Expense(date, time, amount, category);
                transList.add(expense);
            } while (cursor.moveToNext());
        }
        // close cursor and database
        cursor.close();
        db.close();
        return transList;
    }

    public double getTodayTotal(String today) {
        double total = 0;
        String query = "SELECT * FROM " + TABLE_NAME  + " ORDER BY " + COL_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst() && (cursor.getString(1)).equals(today)) {
            do total += cursor.getDouble(3);
            while (cursor.moveToNext() && (cursor.getString(1)).equals(today));
        }
        // close cursor and database
        cursor.close();
        db.close();
        return total;
    }

    public String getReport(String month) {
        double total = 0;
        double arr[] = new double[7];
        String query = "SELECT * FROM " + TABLE_NAME  + " ORDER BY " + COL_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()&& (cursor.getString(1).substring(0, 2)).equals(month)) {
            do {
                String c = cursor.getString(4);
                double d = cursor.getDouble(3);
                total += d;
                switch(c) {
                    case "Food":
                        arr[0] += d;
                        break;
                    case "Housing":
                        arr[1] += d;
                        break;
                    case "Clothing":
                        arr[2] += d;
                        break;
                    case "Transportation":
                        arr[3] += d;
                        break;
                    case "Health":
                        arr[4] += d;
                        break;
                    case "Recreation":
                        arr[5] += d;
                        break;
                    default:
                        arr[6] += d;
                }
            } while (cursor.moveToNext() && (cursor.getString(1).substring(0, 2)).equals(month));
        }
        // close cursor and database
        cursor.close();
        db.close();
        for (int i = 0; i < arr.length; ++i)
            arr[i] = arr[i]/total*100;
        if (total == 0) return "No report available for the current month";
        else
            return String.format("Monthly Total: %.2f%n%n %s - %.0f%%%n %s - %.0f%%%n %s - %.0f%%%n %s - %.0f%%%n %s - %.0f%%%n %s - %.0f%%%n %s - %.0f%%%n", total, "Food", arr[0], "Housing", arr[1], "Clothing", arr[2], "Transportation", arr[3], "Health", arr[4], "Recreation", arr[5], "Misc.", arr[6]);
    }

}
