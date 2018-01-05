package com.example.robert.drawingrobot.Data;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.robert.drawingrobot.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Rob on 05.01.2018.
 */
// class to keep variables responsible for Bluetooth Connection
public class Bluetooth {



    private BluetoothAdapter BA;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address;

    public Bluetooth(BluetoothAdapter BA) {
        this.BA = BA;
    }

    // łączenie urządzeń, uzyskiwanie mmSocket
    public void ConnectThread(BluetoothDevice device, Context context) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        try {
            Toast.makeText(context, "Socket was send", Toast.LENGTH_LONG).show();
            tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
            mmSocket = tmp;
        } catch (IOException e) { Toast.makeText(context, "Catched Expection connected with socket", Toast.LENGTH_LONG).show();}

    }

    //inicjalizacja kanału do wysyłania i odbioru, mmInStream, mmOutStream
    public void  init(BluetoothSocket socket) {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }



    public void run() {
        BA.cancelDiscovery();
        try {
            mmSocket.connect();
            //Toast.makeText(getApplicationContext(), "probujemy sie polaczyc :)", Toast.LENGTH_LONG).show();
        } catch (IOException connectException) {
            try {
                mmSocket.close();
              //  Toast.makeText(getApplicationContext(), "nie udalo sie", Toast.LENGTH_LONG).show();
            } catch (IOException closeException) { }
            return;
        }
        init(mmSocket);
    }

    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }

    public void bluetoothTurnOff() // wylaczanie bluetootha
    {
        BA.disable();
    }

}
