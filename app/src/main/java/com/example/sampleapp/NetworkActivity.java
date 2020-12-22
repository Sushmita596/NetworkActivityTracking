package com.example.sampleapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class NetworkActivity extends AppCompatActivity {
    TextView textwifi, textair, textsim, serial, time, time2;
    String androidid;
    String air, wifi, sim;
    DatabaseHelper myDb;
    String event;
    Button viewDataBtn;
    int count=0;
    private WifiManager wifiManager;
    long currentTime;
    long updatedTime;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        myDb = new DatabaseHelper(this);
        androidid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        textwifi = findViewById(R.id.textwifi);
        textair = findViewById(R.id.textair);
        textsim = findViewById(R.id.textsim);
        time = findViewById(R.id.time);
        time2 = findViewById(R.id.time2);
        serial = findViewById(R.id.serial);
        viewDataBtn = findViewById(R.id.button);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

//------------------------------------Every 2 hours a new data gets added to the table with an increment in the Serial ID---------------------------------------------------------------
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask testing = new TimerTask() {
            public void run() {
                count++;
                handler.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void run() {
                        Date now = new Date();
                        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                        updatedTime = currentTime;
                        currentTime = now.toInstant().toEpochMilli();
                        checkConnection();
                        insert(count);
                        time2.setText("Updated Time: " + updatedTime);
                    }
                });
            }
        };
//        timer.schedule(testing, 0, 30000);
        timer.schedule(testing, 0, 20000);


//-------------------------Updating UI and Getting Current Time  every 2 seconds----------------------------------------------------------------
        final Handler handler2 = new Handler();
        Timer timer2 = new Timer();
        TimerTask testing2 = new TimerTask() {
            public void run() {
                handler2.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void run() {
                        checkConnection();
                    }
                });
            }
        };
        timer2.schedule(testing2, 0, 1000);


//-----------------------------View DataBase Button Method----------------------------------------------------

        viewDataBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(NetworkActivity.this, DataBase.class);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiStateReceiver, intentFilter);
    }
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiStateReceiver);
    }

    private final BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiStateExtra) {
                case WifiManager.WIFI_STATE_ENABLED:
                case WifiManager.WIFI_STATE_DISABLED:
                    Date now = new Date();
                    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                    updatedTime = currentTime;
                    currentTime = now.toInstant().toEpochMilli();
                    boolean isUpdated = myDb.updateData(androidid+count,
                            currentTime,
                            updatedTime,
                            event);
                   /* if(isUpdated)
                        Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();*/
                    time2.setText("Updated Time: " + updatedTime);
                    break;
            }
        }
    };


    //---------------------------Checking AirPlane Mode Status-------------------------------------------------------------------------------------
    public static boolean isAirPlaneModeOn(Context context){
        return Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    //---------------------------------Checking WIFI Status----------------------------------------------------------------------------------------------------
    public static boolean isWiFiOn(Context context){
        return Settings.System.getInt(context.getContentResolver(), Settings.Global.WIFI_ON, 0) != 0;
    }



    //------------------------------The Status Checking Method-------------------------------------------------------------------------------------
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo MobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

//----------------------------------Getting Unique Device ID---------------------------------------------------------------------------------------------------
        serial.setText(getString(R.string.serial) + androidid);

//---------------------------Updating Current Time---------------------------------------------------------------------------------------------------------
        Date now = new Date();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        updatedTime = currentTime;
        currentTime = now.toInstant().toEpochMilli();
        time.setText("Current Time: " + currentTime);


//---------------------------------------Getting the Events---------------------------------------------------------------------------------------
        if (isAirPlaneModeOn(getApplicationContext())) {
            textair.setText("airplane_mode=ON");
            air = "airplane_mode=ON";
        } else {
            textair.setText("airplane_mode=OFF");
            air = "airplane_mode=OFF";
        }
        if (isWiFiOn(getApplicationContext())) {
            textwifi.setText("wifi=ON");
            wifi = "wifi=ON";
        } else {
            textwifi.setText("wifi=OFF");
            wifi = "wifi=OFF";
        }
        if (MobileNetwork.isConnectedOrConnecting()) {
            textsim.setText("simData=ON");
            sim = "simData=ON";
        } else {
            textsim.setText("simData=OFF");
            sim = "simData=OFF";
        }

//------------------------------------------Inserting Data To Database Table-----------------------------------------------------------------------------------------------
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insert(int i){
        event = wifi+";"+air+";"+sim;
        Date now = new Date();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        updatedTime = currentTime;
        currentTime = now.toInstant().toEpochMilli();
        boolean inserted = myDb.insertData(androidid+i,
                currentTime,
                updatedTime,
                event);
       /* if(inserted)
            Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_SHORT).show();*/
    }

}
