package com.example.yogaclassmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "YogaClasses.db";
    public static final String TABLE_NAME = "classes";
    private static final String COL_ID = "id";
    private static final String COL_DAY = "day_of_week";
    private static final String COL_TIME = "time";
    private static final String COL_CAPACITY = "capacity";
    private static final String COL_DURATION = "duration";
    private static final String COL_PRICE = "price";
    private static final String COL_TYPE = "type_of_class";
    private static final String COL_DESCRIPTION = "description";

    public static final String TABLE_INSTANCES = "instances";
    private static final String COL_INSTANCE_ID = "instance_id";
    private static final String COL_CLASS_ID = "class_id"; // Foreign key to classes
    private static final String COL_INSTANCE_DATE_TIME = "instance_date_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createClassesTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DAY + " TEXT, " +
                COL_TIME + " TEXT, " +
                COL_CAPACITY + " INTEGER, " +
                COL_DURATION + " TEXT, " +
                COL_PRICE + " REAL, " +
                COL_TYPE + " TEXT, " +
                COL_DESCRIPTION + " TEXT)";
        db.execSQL(createClassesTable);

        String createInstancesTable = "CREATE TABLE " + TABLE_INSTANCES + " (" +
                COL_INSTANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CLASS_ID + " INTEGER, " +
                COL_INSTANCE_DATE_TIME + " TEXT, " +
                "FOREIGN KEY(" + COL_CLASS_ID + ") REFERENCES " + TABLE_NAME + "(" + COL_ID + "))";
        db.execSQL(createInstancesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertClass(String day, String time, int capacity, String duration, double price, String type, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DAY, day);
        contentValues.put(COL_TIME, time);
        contentValues.put(COL_CAPACITY, capacity);
        contentValues.put(COL_DURATION, duration);
        contentValues.put(COL_PRICE, price);
        contentValues.put(COL_TYPE, type);
        contentValues.put(COL_DESCRIPTION, description);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // Return true if insert was successful
    }

    public ArrayList<String> getAllClasses() {
        ArrayList<String> classes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String classDetails = cursor.getString(1) + " - " + // Day
                        cursor.getString(2) + " - " + // Time
                        cursor.getInt(3) + " - " + // Capacity
                        cursor.getString(4) + " - " + // Duration
                        cursor.getDouble(5) + " - " + // Price
                        cursor.getString(6); // Type
                classes.add(classDetails);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return classes;
    }

    public boolean insertInstance(int classId, String dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CLASS_ID, classId);
        contentValues.put(COL_INSTANCE_DATE_TIME, dateTime);
        long result = db.insert(TABLE_INSTANCES, null, contentValues);
        return result != -1;
    }

    public ArrayList<String> getInstancesByClassId(int classId) {

        ArrayList<String> instances = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INSTANCES + " WHERE " + COL_CLASS_ID + " = ?", new String[]{String.valueOf(classId)});

        if (cursor.moveToFirst()) {
            do {
                String instanceDetails = cursor.getString(2); // Instance date and time
                instances.add(instanceDetails);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return instances;
    }

    public boolean deleteInstance(int instanceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_INSTANCES, COL_INSTANCE_ID + " = ?", new String[]{String.valueOf(instanceId)}) > 0;
    }
}