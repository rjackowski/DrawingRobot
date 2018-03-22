package com.example.robert.drawingrobot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.example.robert.drawingrobot.Data.Bluetooth;
import com.example.robert.drawingrobot.Data.SketchSheetView;

import java.util.ArrayList;

public class autoMode extends Activity {

    RelativeLayout relativeLayout;
    private SketchSheetView view;
    Button btnClear, btnConfirm;
    private calculateTrajectory myTrajectory;
    private Bluetooth myBluetooth;
    @Override
    protected void onCreate(Bundle savedInstanceState) { // tworzenie projektu
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_mode);
        relativeLayout = (RelativeLayout) findViewById(R.id.rLayout); // przypisywanie id do zmiennej
        btnClear = (Button)findViewById(R.id.btnClear);
        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        view = new SketchSheetView(autoMode.this); // obiekt o klasie view do rysowania
        myTrajectory = new calculateTrajectory();
        myBluetooth = ((BluetoothApllication)this.getApplicationContext()).bluetoothObject;
        relativeLayout.addView(view, new LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.resetPath();
                myTrajectory.clear();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTrajectory = view.getTrajectory();
                if(myTrajectory.getLocationList()!= null) {
                    myTrajectory.calculate();
                    if(myBluetooth.isReady()) {
                    //    new Thread(new Runnable() {
                  //          public void run() {
                                myBluetooth.sendArrayList(myTrajectory.getCommandList());

                //    }
                //}).start();

                    }
                    else
                        Toast.makeText(getApplicationContext(), "First configure Bluetooth connection", Toast.LENGTH_LONG).show();
                   // Intent dest = new Intent(new Intent(getApplicationContext(), BluetoothConfiguration.class));
                   // Bundle b = new Bundle();
                   //b.putStringArrayList("dane", myTrajectory.commandList);
                   // dest.putExtras(b);
                   // startActivity(dest);
                }
                else {
                    Toast.makeText(getApplicationContext(), "First write something on View", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "First write something on View", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}