package com.example.robert.drawingrobot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothConfiguration extends Activity {


    ArrayList<String> dane;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    Button btnBluetoothInit,btnBluetoothTurnOff, btnDiscoverDevices, btnSend;
    ListView listPairedDevices, listDiscoveredDevices;
    private ArrayAdapter<String> adapter ;
    private ArrayAdapter<String> adapterForDiscoveredDevices;
    ArrayList<String> list = new ArrayList<>();
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address;
    private  BluetoothSocket mmSocket;
    private  BluetoothDevice mmDevice;
   private  InputStream mmInStream;
    private  OutputStream mmOutStream;
    String znak="c";

    ArrayList<String> urls;
    // Register the BroadcastReceiver
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_configuration);
        BA = BluetoothAdapter.getDefaultAdapter();
        btnBluetoothInit=(Button)findViewById(R.id.btnBluetoothInit);
        btnBluetoothTurnOff=(Button)findViewById(R.id.btnBluetoothTurnOff);
        btnDiscoverDevices=(Button)findViewById(R.id.btnDiscoverDevices);
        btnSend=(Button)findViewById(R.id.btnSend);
        listPairedDevices=(ListView) findViewById(R.id.listPairedDevices);
        listDiscoveredDevices=(ListView) findViewById(R.id.listDiscoveredDevices);

        btnBluetoothInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothInit();
            }
        });
        btnBluetoothTurnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothTurnOff();
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
                byte[] temp;
                temp=znak.getBytes();
                write(temp);
                for(int i=0; i< dane.size(); i++) {
                    //temp = dane.get(i).getBytes();
                    Toast.makeText(getApplicationContext(), dane.get(i), Toast.LENGTH_LONG).show();
                    write(temp);
                }
                temp=znak.getBytes();
                write(temp);
            }
        });


        listDiscoveredDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos,   long id) {
                address= list.get(pos);
            }
        });

    }

    public void ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        try {
            Toast.makeText(getApplicationContext(), "wyslano skarpetke", Toast.LENGTH_LONG).show();
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { Toast.makeText(getApplicationContext(), "zlapano wyjatka", Toast.LENGTH_LONG).show();}
        mmSocket = tmp;
    }


    public void  init(BluetoothSocket socket) {
        mmSocket = socket;
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
            Toast.makeText(getApplicationContext(), "probujemy sie polaczyc :)", Toast.LENGTH_LONG).show();
        } catch (IOException connectException) {
            try {
                mmSocket.close();
                Toast.makeText(getApplicationContext(), "nie udalo sie", Toast.LENGTH_LONG).show();
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



    protected void bluetoothInit() // uruchamianie bluetootha
    {
        if(!BA.isEnabled())
        {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();
        }
    }

    protected void bluetoothTurnOff() // wylaczanie bluetootha
    {
        BA.disable();
    }

    protected void getPairedDevices() // pozyskiwanie i wyswietlanie sparowanych urzadzen
    {
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

    protected void discoverNewDevices() // poszukiwanie i wyswietlanie nowych urzadzen
    {
        BA.startDiscovery();
        getPairedDevices();
    }

    protected void showNewDevices(ArrayList<String> list) //wyswietlanie znalezionych urzadzen Bluetooth
    {
        adapterForDiscoveredDevices = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listDiscoveredDevices.setAdapter(adapterForDiscoveredDevices);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                ConnectThread(device);
                run();
                list.add(device.getAddress());
                showNewDevices(list);
            }
        }
    };

    public void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, filter);
    }

}
