package com.example.robert.drawingrobot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Handler;

import com.example.robert.drawingrobot.Data.Bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class BluetoothConfiguration extends Activity {

    int iterator=0;
    ArrayList<String> dane;
    private BluetoothAdapter BA;
    Bluetooth myBluetooth;


    private Intent myIntent;
    private Bundle myBundle;

    private Set<BluetoothDevice> pairedDevices;
    Button btnBluetoothInit,btnBluetoothTurnOff, btnDiscoverDevices, btnSend;
    ListView listPairedDevices, listDiscoveredDevices;
    private ArrayAdapter<String> adapter ;
    private ArrayAdapter<String> adapterForDiscoveredDevices;
    ArrayList<BluetoothDevice> list = new ArrayList<>();



    String znak="X";

    ArrayList<String> urls;
    // Register the BroadcastReceiver
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    private final Handler mHandler = new Handler();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_configuration);
        BA = BluetoothAdapter.getDefaultAdapter();
        myBluetooth = ((BluetoothApllication)this.getApplicationContext()).bluetoothObject;
        //myBluetooth = new Bluetooth(BA);
        btnBluetoothInit=(Button)findViewById(R.id.btnBluetoothInit);
        btnBluetoothTurnOff=(Button)findViewById(R.id.btnBluetoothTurnOff);
        btnDiscoverDevices=(Button)findViewById(R.id.btnDiscoverDevices);
        btnSend=(Button)findViewById(R.id.btnSend);
        listPairedDevices=(ListView) findViewById(R.id.listPairedDevices);
        listDiscoveredDevices=(ListView) findViewById(R.id.listDiscoveredDevices);
        myIntent = this.getIntent();
        myBundle = new Bundle();


        btnBluetoothInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothInit();
            }
        });
        btnBluetoothTurnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBluetooth.turnOff();
            }
        });

        btnDiscoverDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               discoverNewDevices();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dane= getIntent().getStringArrayListExtra("dane");
                myBluetooth.sendArrayList(dane);
            }
        });

        listDiscoveredDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                // address= list.get(pos);
            }
        });

    }

    public void bluetoothInit() { // uruchamianie bluetootha
        if(!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();
        }
    }

    // pozyskiwanie i wyswietlanie sparowanych urzadzen
    protected void getPairedDevices() {
        if(BA.isEnabled()) {
            pairedDevices = BA.getBondedDevices();
            ArrayList<String> list = new ArrayList<String>();

            for (BluetoothDevice bt : pairedDevices)
                list.add(bt.getAddress());

            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            listPairedDevices.setAdapter(adapter);
        }
        else
            Toast.makeText(getApplicationContext(), this.getString(R.string.info_bluetoothOff), Toast.LENGTH_SHORT).show();
    }

    public void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, filter);
    }

    public void onDestroy() {
        super.onDestroy();
        ((BluetoothApllication)this.getApplicationContext()).bluetoothObject = myBluetooth;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                myBluetooth.ConnectThread(device,getApplicationContext());
                myBluetooth.run();
                list.add(device);
                showNewDevices(list);
            }
        }
    };

    // poszukiwanie i wyswietlanie nowych urzadzen
    public void discoverNewDevices() {
        BA.startDiscovery();
        getPairedDevices();
    }

    //wyswietlanie znalezionych urzadzen Bluetooth
    public void showNewDevices(ArrayList<BluetoothDevice> list) {
        adapterForDiscoveredDevices = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listDiscoveredDevices.setAdapter(adapterForDiscoveredDevices);
    }
}
