package com.example.robert.drawingrobot;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class menu extends Activity {

    Button btnHandMode, btnBluetoothConfiguration, btnAutoMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnHandMode= (Button)findViewById(R.id.btnHandMode);
        btnBluetoothConfiguration=(Button)findViewById(R.id.btnBluetoothConfiguration);
        btnAutoMode= (Button)findViewById(R.id.btnAutoMode);


        btnHandMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), handMode.class));
            }
        });

        btnBluetoothConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BluetoothConfiguration.class));
            }
        });

        btnAutoMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), autoMode.class));
            }
        });

    }


}