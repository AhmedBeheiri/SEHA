package com.aghazy.healthassistant.activities;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aghazy.coolradio.DeviceListActivity;
import com.aghazy.healthassistant.R;
import com.aghazy.healthassistant.adapters.MainAdapter;
import com.aghazy.healthassistant.model.MainModel;
import com.aghazy.healthassistant.services.DataService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Handler bluetoothin;
    final int handlerstate=0;
    private BluetoothAdapter adapter;
    private BluetoothSocket socket;
    private StringBuilder stringBuilder=new StringBuilder();
    private  ConnectedThread connectedThread;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address;
    private String heartbeatvalue;
    private ArrayList<MainModel>values;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        values=new ArrayList<>();
        values=DataService.getInstance().getMainData();

        final MainAdapter mainAdapter = new MainAdapter(values);


        recyclerView.addItemDecoration(new VerticalSpaceItemDecorator(10));
        recyclerView.setAdapter(mainAdapter);
        bluetoothin=new Handler(){
            public void handleMessage(Message msg){
                if (msg.what == handlerstate) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;
                    // msg.arg1 = bytes from connect thread
                    Log.i("message",String.valueOf(msg.arg1));
                    Log.i("message",String.valueOf(msg.obj));
                    stringBuilder.append(readMessage);                                      //keep appending to string until ~
                    int endOfLineIndex = stringBuilder.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = stringBuilder.substring(0, endOfLineIndex);    // extract string
                        //txtString.setText("Data Received = " + dataInPrint);
                        int dataLength = dataInPrint.length();                          //get length of data received
                       // txtStringLength.setText("String Length = " + String.valueOf(dataLength));

                        if (stringBuilder.charAt(0) == '#')                             //if it starts with # we know it is what we are looking for
                        {
                            String sensor0 = stringBuilder.substring(1, endOfLineIndex);             //get sensor value from string between indices 1-5
                            //String sensor1 = recDataString.substring(6, 10);            //same again...
                            //String sensor2 = recDataString.substring(11, 15);
                            //String sensor3 = recDataString.substring(16, 20);

                           values.set(5,new MainModel("Heart Rate",sensor0+" BPM","heart"));
                           mainAdapter.notifyDataSetChanged();
                           //update the textviews with sensor values
                        }
                        stringBuilder.delete(0, stringBuilder.length());                    //clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }
            }
        };
        adapter=BluetoothAdapter.getDefaultAdapter();
        checkBTState();

    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = adapter.getRemoteDevice(address);

        try {
            socket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            socket.connect();
        } catch (IOException e) {
            try
            {
                socket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            socket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }



    private void checkBTState() {

        if(adapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (adapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }

    }
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothin.obtainMessage(handlerstate, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
    }
}


class VerticalSpaceItemDecorator extends RecyclerView.ItemDecoration {

    private final int spacer;

    VerticalSpaceItemDecorator(int spacer) {
        this.spacer = spacer;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.bottom = spacer;
    }


}

