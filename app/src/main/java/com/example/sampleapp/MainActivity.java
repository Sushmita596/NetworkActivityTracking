package com.example.sampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button Accept;
    Button Deny;
    ConstraintLayout privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Accept = findViewById(R.id.button1);
        Deny = findViewById(R.id.button2);
        privacy = findViewById(R.id.privacy);
        checkAccepted();

//----------------------Assigning Function To The Accept and Deny Buttons------------------------------------------------------------------------
        Deny.setOnClickListener(v -> {
                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("isAccepted",false).apply();
                    finishAffinity();
                }
        );
        Accept.setOnClickListener(v -> {
                    getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit().putBoolean("isAccepted",true).apply();
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, NetworkActivity.class);
                    startActivity(intent);
                    finish();
                }
        );
    }

    //-----------------------------Checking If Privacy Term is Accepted on Startup-------------------------------------------------------------------------
    private void  checkAccepted(){
        boolean isAccepted = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isAccepted", false);
        if(isAccepted){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, NetworkActivity.class);
            startActivity(intent);
            finish();
        }
    }
}


