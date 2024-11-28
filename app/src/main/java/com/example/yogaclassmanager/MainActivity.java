package com.example.yogaclassmanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button buttonAddClass;
    private ListView listClasses;
    private ArrayList<String> classes;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAddClass = findViewById(R.id.button_add_class);
        listClasses = findViewById(R.id.list_classes);
        classes = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, classes);
        listClasses.setAdapter(adapter);

        databaseHelper = new DatabaseHelper(this);
        loadClasses();

        buttonAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddClassActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        listClasses.setOnItemClickListener((parent, view, position, id) -> {
            String selectedClass = classes.get(position);
            Intent intent = new Intent(MainActivity.this, EditClassActivity.class);
            intent.putExtra("classDetails", selectedClass);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String[] classDetails = data.getStringExtra("classDetails").split(" - ");
            String day = classDetails[0];
            String time = classDetails[1];
            int capacity = Integer.parseInt(classDetails[2]);
            String duration = classDetails[3];
            double price = Double.parseDouble(classDetails[4]);
            String type = classDetails[5];
            String description = classDetails.length > 6 ? classDetails[6] : "";

            if (databaseHelper.insertClass(day, time, capacity, duration, price, type, description)) {
                loadClasses();
            }
        }
    }

    private void loadClasses() {
        classes.clear();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + databaseHelper.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                String classDetails = cursor.getInt(0) + " - " + // ID
                        cursor.getString(1) + " - " + // Day
                        cursor.getString(2) + " - " + // Time
                        cursor.getInt(3) + " - " + // Capacity
                        cursor.getString(4) + " - " + // Duration
                        cursor.getDouble(5) + " - " + // Price
                        cursor.getString(6); // Type
                classes.add(classDetails);
            } while (cursor.moveToNext());
        } else {
            Log.d("CursorCheck", "No data found in the cursor.");
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }
}