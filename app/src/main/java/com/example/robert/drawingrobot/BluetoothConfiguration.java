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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class BluetoothConfiguration extends Activity {

    int iterator=0;
    ArrayList<String> dane;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    Button btnBluetoothInit,btnBluetoothTurnOff, btnDiscoverDevices, btnSend;
    ListView listPairedDevices, listDiscoveredDevices;
    private ArrayAdapter<String> adapter ;
    private ArrayAdapter<String> adapterForDiscoveredDevices;
    ArrayList<BluetoothDevice> list = new ArrayList<>();
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address;
    private  BluetoothSocket mmSocket;
    private  BluetoothDevice mmDevice;
   private  InputStream mmInStream;
    private  OutputStream mmOutStream;
    String znak="X";

    ArrayList<String> urls;
    // Register the BroadcastReceiver
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    private final Handler mHandler = new Handler();






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
                iterator=0;
                sendData();
                //Toast.makeText(getApplicationContext(), znak, Toast.LENGTH_LONG).show();
                   ;


            }
        });


        listDiscoveredDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                // address= list.get(pos);
            }
        });

    }

    // łączenie urządzeń, uzyskiwanie mmSocket
    public void ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        try {
            Toast.makeText(getApplicationContext(), "wyslano skarpetke", Toast.LENGTH_LONG).show();
            tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { Toast.makeText(getApplicationContext(), "zlapano wyjatka", Toast.LENGTH_LONG).show();}
        mmSocket = tmp;
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



    public void sendData()
    {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),  Integer.toString(iterator), Toast.LENGTH_LONG).show();
                byte[] temp;
                temp = dane.get(iterator).getBytes();
                Toast.makeText(getApplicationContext(), dane.get(iterator), Toast.LENGTH_LONG).show();
                write(temp);
                temp = ";".getBytes();
                write(temp);
                iterator++;
                if(iterator<dane.size()) // gdy iterator nie przekroczy rozmaiaru tablicy wracamy do tej samej funkcji
                    sendData();
                else
                {
                    temp="Y".getBytes();
                    write(temp);
                }
            }
        }, 1000);

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

    // pozyskiwanie i wyswietlanie sparowanych urzadzen
    protected void getPairedDevices()
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
    // poszukiwanie i wyswietlanie nowych urzadzen
    protected void discoverNewDevices()
    {
        BA.startDiscovery();
        getPairedDevices();
    }

    //wyswietlanie znalezionych urzadzen Bluetooth
    protected void showNewDevices(ArrayList<BluetoothDevice> list)
    {
        adapterForDiscoveredDevices = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listDiscoveredDevices.setAdapter(adapterForDiscoveredDevices);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
               ConnectThread(device); // przeniesc do innego miejsca
                run();
                list.add(device);
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
