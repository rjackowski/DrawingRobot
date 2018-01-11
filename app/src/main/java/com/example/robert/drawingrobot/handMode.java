package com.example.robert.drawingrobot;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.robert.drawingrobot.Data.Bluetooth;

import java.io.Serializable;

public class handMode extends Activity {

    ImageButton btnTurnLeft;
    ImageButton btnTurnRight;
    ImageButton btnForward;
    ImageButton btnBackward;
    private Bluetooth myBluetooth;
    private Intent intent;
    private  Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_mode);
        intent = this.getIntent();
        bundle = intent.getExtras();
        // Retrievieng object in second Activity
        myBluetooth = ((BluetoothApllication)this.getApplicationContext()).bluetoothObject;
        btnTurnLeft=(ImageButton)findViewById(R.id.btnTurnLeft);
        btnTurnRight=(ImageButton)findViewById(R.id.btnTurnRight);
        btnForward=(ImageButton)findViewById(R.id.btnForward);
        btnBackward=(ImageButton)findViewById(R.id.btnBackward);

        btnTurnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetooth.isReady()) {
                    myBluetooth.sendString("L");
                }
                else
                    Toast.makeText(getApplicationContext(), "First configure Bluetooth connection", Toast.LENGTH_LONG).show();
            }
        });

        btnTurnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetooth.isReady()) {
                    myBluetooth.sendString("R");
                }
                else
                    Toast.makeText(getApplicationContext(), "First configure Bluetooth connection", Toast.LENGTH_LONG).show();

            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetooth.isReady()) {
                    myBluetooth.sendString("F");
                }
                else
                    Toast.makeText(getApplicationContext(), "First configure Bluetooth connection", Toast.LENGTH_LONG).show();

            }
        });

        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetooth.isReady()) {
                    myBluetooth.sendString("B");
                }
                else
                    Toast.makeText(getApplicationContext(), "First configure Bluetooth connection", Toast.LENGTH_LONG).show();
            }
        });
    }



}
