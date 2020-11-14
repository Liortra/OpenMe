package com.example.openme;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nikartm.button.FitButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private FitButton main_BTN_enter;
    private Button main_BTN_move;
    private RelativeLayout main_RLT_screen;
    private TextInputEditText main_TIP_password;

    private boolean move = false;
    private final String packageNameToCheck = "com.whatsapp";
    private float x;
    private float y;

    private BatteryManager myBatteryManager;
    private BluetoothAdapter myBluetoothAdapter;
    private PackageManager myPackageManager;
    private LocationManager locationManager;
    private NfcAdapter adapter;
    private NfcManager nfcManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        listeners();
    }

    private void init() {
        this.main_BTN_enter = (FitButton) findViewById(R.id.main_BTN_enter);
        this.main_TIP_password = (TextInputEditText) findViewById(R.id.main_TIP_password);
        this.main_RLT_screen = (RelativeLayout) findViewById(R.id.main_RLT_screen);
        this.main_BTN_move = (Button) findViewById(R.id.main_BTN_move);

        myBatteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
        myPackageManager = getPackageManager();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        nfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        adapter = nfcManager.getDefaultAdapter();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listeners() {
        main_BTN_enter.setOnClickListener(v -> {
            activateSecurity();
        });

        main_RLT_screen.setOnTouchListener((v, event) ->{
            x = event.getX();
            y = event.getY();
            if (event.getAction() == 2) {
                this.main_BTN_move.setX(x);
                this.main_BTN_move.setY(y);
            }
            this.move = true;
            return true;
        });
    }

    private void activateSecurity() {
        if (!checkIfPhoneIsCharging()) {
            toast("You aren't charging you phone....");
        } else if (!moveTheButton()) {
            toast("Read the instruction in the Button!!");
        } else if (!checkManufacturer()) {
            toast("Which device do you have? only the company");
        } else if (!isAppInstalled(packageNameToCheck)) {
            toast("You don't have whatsapp on your phone? wow man...");
        } else if (!sensorsActivate()) {
            toast("Check if your BLUETOOTH or NFC or GPS is enable...");
        } else {
            toast("You Break The Code");
            startActivity(new Intent(this, EnterActivity.class));
            finish();
        }
    }

    private boolean sensorsActivate() {
        if (!isNfcEnable() || !isGpsEnable() || !isBTEnable()) {
            return false;
        }
        return true;
    }

    public boolean isGpsEnable() {
        return this.locationManager.isProviderEnabled("gps");
    }

    public boolean isBTEnable() {
        BluetoothAdapter bluetoothAdapter = this.myBluetoothAdapter;
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public boolean isNfcEnable() {
        return adapter != null && adapter.isEnabled();
    }

    private boolean isAppInstalled(String packageName) {
        try {
            myPackageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private boolean checkManufacturer() {
        if (this.main_TIP_password.getText().toString().toLowerCase().trim().equals(Build.MANUFACTURER.toLowerCase())) {
            return true;
        }
        return false;
    }

    private boolean moveTheButton() {
        return this.move;
    }

    private boolean checkIfPhoneIsCharging() {
        return this.myBatteryManager.isCharging();
    }

    private void toast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}