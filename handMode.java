package com.example.robert.drawingrobot;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
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


        btnTurnLeft.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myBluetooth.isReady()) {
                    myBluetooth.sendString("L");
                }
                else
                    Toast.makeText(getApplicationContext(), "First configure Bluetooth connection", Toast.LENGTH_LONG).show();
            }
        }));


        btnTurnRight.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myBluetooth.isReady()) {
                    myBluetooth.sendString("R");
                }
                else
                    Toast.makeText(getApplicationContext(), "First configure Bluetooth connection", Toast.LENGTH_LONG).show();
            }
        }));


        btnForward.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myBluetooth.isReady()) {
                    myBluetooth.sendString("F");
                }
                else
                    Toast.makeText(getApplicationContext(), "First configure Bluetooth connection", Toast.LENGTH_LONG).show();
            }
        }));

        btnBackward.setOnTouchListener(new RepeatListener(400, 20, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myBluetooth.isReady()) {
                    myBluetooth.sendString("B");
                }
                else
                    Toast.makeText(getApplicationContext(), "First configure Bluetooth connection", Toast.LENGTH_LONG).show();
            }
        }));

    }

    public class RepeatListener implements View.OnTouchListener {

        private Handler handler = new Handler();

        private int initialInterval;
        private final int normalInterval;
        private final View.OnClickListener clickListener;

        private Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, normalInterval);
                clickListener.onClick(downView);
            }
        };

        private View downView;

        /**
         * @param initialInterval The interval after first click event
         * @param normalInterval The interval after second and subsequent click
         *       events
         * @param clickListener The OnClickListener, that will be called
         *       periodically
         */
        public RepeatListener(int initialInterval, int normalInterval,
                              View.OnClickListener clickListener) {
            if (clickListener == null)
                throw new IllegalArgumentException("null runnable");
            if (initialInterval < 0 || normalInterval < 0)
                throw new IllegalArgumentException("negative interval");

            this.initialInterval = initialInterval;
            this.normalInterval = normalInterval;
            this.clickListener = clickListener;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.removeCallbacks(handlerRunnable);
                    handler.postDelayed(handlerRunnable, initialInterval);
                    downView = view;
                    downView.setPressed(true);
                    clickListener.onClick(view);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    handler.removeCallbacks(handlerRunnable);
                    downView.setPressed(false);
                    downView = null;
                    return true;
            }

            return false;
        }

    }

}
