package com.example.yogaclassmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditClassActivity extends Activity {

    private TextView textClassDetails;
    private EditText editInstanceDateTime;
    private Button buttonAddInstance, buttonEditClass;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        textClassDetails = findViewById(R.id.text_class_details);
        editInstanceDateTime = findViewById(R.id.edit_instance_date_time);
        buttonAddInstance = findViewById(R.id.button_add_instance);
        //buttonEditClass = findViewById(R.id.button_edit_class);

        databaseHelper = new DatabaseHelper(this);

        String classDetails = getIntent().getStringExtra("classDetails");
        textClassDetails.setText(classDetails);

        buttonAddInstance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instanceDateTime = editInstanceDateTime.getText().toString();
                int classId = getClassIdFromDetails(classDetails);
                if (databaseHelper.insertInstance(classId, instanceDateTime)) {
                    Toast.makeText(EditClassActivity.this, "Instance added: " + instanceDateTime, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditClassActivity.this, "Failed to add instance", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*buttonEditClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditClassActivity.this, "Edit class feature not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private int getClassIdFromDetails(String classDetails) {
        String[] parts = classDetails.split(" - ");
        return Integer.parseInt(parts[0]); // Assuming the first part is the ID
    }
}