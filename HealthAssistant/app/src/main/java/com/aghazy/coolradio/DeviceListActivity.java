package com.aghazy.coolradio;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aghazy.healthassistant.R;
import com.aghazy.healthassistant.activities.MainActivity;

import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {

    public static   String EXTRA_DEVICE_ADDRESS="device_address";

    private TextView connectionTextView;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String>pairedDeviceArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBTState(this);
        connectionTextView=(TextView)findViewById(R.id.connecting);
        connectionTextView.setText("");
        pairedDeviceArrayAdapter=new ArrayAdapter<String>(this,R.layout.device_name);
        ListView paireddeviceslistview=(ListView)findViewById(R.id.paired_devices);
        paireddeviceslistview.setAdapter(pairedDeviceArrayAdapter);
        paireddeviceslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                connectionTextView.setText(getText(R.string.connection));
                String info=((TextView)view).getText().toString();
                String address=info.substring(info.length()-17);
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(EXTRA_DEVICE_ADDRESS,address);
                startActivity(intent);
            }
        });

        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice>paireddevices=bluetoothAdapter.getBondedDevices();
        if (paireddevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);//make title viewable
            for (BluetoothDevice device : paireddevices) {
                pairedDeviceArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices =getString(R.string.error_text);
            pairedDeviceArrayAdapter.add(noDevices);
        }
    }

    public  void checkBTState(Context context) {
        // Check device has Bluetooth and that it is turned on
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (bluetoothAdapter.isEnabled()) {
                Log.d("Enabled", "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }
}
