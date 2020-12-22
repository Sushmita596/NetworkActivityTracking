package com.example.sampleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class DataBase extends AppCompatActivity {

    TextView data;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
        data = findViewById(R.id.data);
        myDb = new DatabaseHelper(this);
        data.setMovementMethod(new ScrollingMovementMethod());

        Cursor res = myDb.getAllData();
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :"+res.getString(0)+"\n");
            buffer.append("CreationTime :"+res.getString(1)+"\n");
            buffer.append("UpdatedTime :"+res.getString(2)+"\n");
            buffer.append("Event :"+res.getString(3)+"\n");
            buffer.append("\n");
        }

        data.setText("\n"+buffer.toString()+"\n");
    }
}
