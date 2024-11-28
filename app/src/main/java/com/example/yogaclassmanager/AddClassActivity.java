package com.example.yogaclassmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddClassActivity extends Activity {

    private EditText editDayOfWeek, editTime, editCapacity, editDuration, editPrice, editTypeOfClass, editDescription;
    private Button buttonSaveClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        editDayOfWeek = findViewById(R.id.edit_day_of_week);
        editTime = findViewById(R.id.edit_time);
        editCapacity = findViewById(R.id.edit_capacity);
        editDuration = findViewById(R.id.edit_duration);
        editPrice = findViewById(R.id.edit_price);
        editTypeOfClass = findViewById(R.id.edit_type_of_class);
        editDescription = findViewById(R.id.edit_description);
        buttonSaveClass = findViewById(R.id.button_save_class);

        buttonSaveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classDetails = editDayOfWeek.getText().toString() + " - " +
                        editTime.getText().toString() + " - " +
                        editCapacity.getText().toString() + " - " +
                        editDuration.getText().toString() + " - " +
                        editPrice.getText().toString() + " - " +
                        editTypeOfClass.getText().toString() + " - " +
                        editDescription.getText().toString();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("classDetails", classDetails);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}