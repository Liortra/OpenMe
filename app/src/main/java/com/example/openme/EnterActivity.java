package com.example.openme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.nikartm.button.FitButton;

public class EnterActivity extends AppCompatActivity {
    private FitButton enter_BTN_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        init();
    }

    private void init() {
        enter_BTN_done = (FitButton) findViewById(R.id.enter_BTN_done);
    }
}