package com.example.apkmenajemenkeuangan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apkmenajemenkeuangan.storage.PreferenceLogin;

public class MainActivity extends AppCompatActivity {

    TextView txLblMessageWelcome;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        String hiUser = PreferenceLogin.getInstance(MainActivity.this).getNameOfUser();

        txLblMessageWelcome = (TextView) findViewById(R.id.txLblHallo);

        txLblMessageWelcome.setText("Hi-Welcome " + hiUser);

    }
}