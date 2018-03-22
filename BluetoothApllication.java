package com.example.robert.drawingrobot;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;

import com.example.robert.drawingrobot.Data.Bluetooth;

/**
 * Created by Rob on 07.01.2018.
 */

public class BluetoothApllication    extends Application {
    public Bluetooth bluetoothObject;

    @Override
    public void onCreate() {
        super.onCreate();
        BluetoothAdapter BA = BluetoothAdapter.getDefaultAdapter();
        this.bluetoothObject = new Bluetooth(BA);
    }


}
