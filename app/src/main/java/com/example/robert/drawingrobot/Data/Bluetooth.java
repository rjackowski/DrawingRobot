package com.example.robert.drawingrobot.Data;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.robert.drawingrobot.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Rob on 05.01.2018.
 */
public class Bluetooth implements Serializable{

    private BluetoothAdapter BA;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private final Handler mHandler ;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address;

    public Bluetooth(BluetoothAdapter BA) {
        this.BA = BA;
        this.mHandler = new Handler();
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
    public boolean isReady() {
        if(mmInStream == null || mmSocket == null || mmDevice ==null || BA == null || mmOutStream ==null ) {
            return false;
        }
        else
            return true;
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

//
//    public void sendData()
//    {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(),  Integer.toString(iterator), Toast.LENGTH_LONG).show();
//                byte[] temp;
//                temp = dane.get(iterator).getBytes();
//                String result="";
//                for(String a : dane) {
//                    result += a +";";
//                }
//                result="";
//                Toast.makeText(getApplicationContext(), dane.get(iterator), Toast.LENGTH_LONG).show();
//                myBluetooth.write(temp);
//                temp = ";".getBytes();
//                myBluetooth.write(temp);
//                iterator++;
//                if(iterator<dane.size()) // gdy iterator nie przekroczy rozmaiaru tablicy wracamy do tej samej funkcji
//                    sendData();
//                else {
//                    temp="Y".getBytes();
//                    myBluetooth.write(temp);
//                }
//            }
//        }, 50);
//    }
    public void sendString(String a) {
        byte[] temp;
        temp = a.getBytes();
        write(temp);
    }

// method to send all ArrayList to robot
    public void sendArrayList(ArrayList<String> dataArrayList) {
        byte[] temp;
        temp="X".getBytes(); // X is signal for robot about starting receiving data
        write(temp);
        for(int i=0; i < dataArrayList.size();i++) {
            temp = dataArrayList.get(i).getBytes();
            write(temp);
            temp = ";".getBytes();
            write(temp);
        }
            temp="Y".getBytes(); // Y is signal for robot about ending receiving data
            write(temp);
    }

    public void write(final byte[] bytes) {
        // 50 ms delay between sending data
        mHandler.postDelayed(new Runnable() {
            @Override
          public void run() {
                try {
                    mmOutStream.write(bytes);
                } catch (
                        IOException e) {
                }
            }
        },50);
    }
    // wylaczanie bluetootha
    public void  turnOff() {
        BA.disable();
    }

}
