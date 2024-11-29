package com.example.apkmenajemenkeuangan;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.apkmenajemenkeuangan.function.LoginFunction;
import com.example.apkmenajemenkeuangan.storage.PreferenceLogin;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    LoginFunction loginFunction;

    EditText edtUsername, edtpassword;
    Button btnLogin;
    TextView txLblInfoError;

    public static final String SIGNAL_ERROR_AUTH = "id.apk_menajemenkeuangan.SIGNAL_ERROR_AUTH";

    private final BroadcastReceiver receiverLogin = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actOfReceive = intent.getAction();
            // Pastikan actOfReceive tidak null sebelum melakukan pengecekan
            switch (Objects.requireNonNull(actOfReceive)) {
                case SIGNAL_ERROR_AUTH:
                    String messageError = PreferenceLogin.getInstance(LoginActivity.this).getSaveMessageError();
                    // Perbaikan di bagian Log (gunakan Log.e bukan log.e)
                    Log.e("GETMESSAGEERRORBYSIGNAL", messageError);
                    txLblInfoError.setVisibility(View.VISIBLE);
                    txLblInfoError.setText(messageError);
                    break;
                // Anda dapat menambahkan case lainnya jika diperlukan
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Inisialisasi
        loginFunction = new LoginFunction(LoginActivity.this);
        edtUsername = findViewById(R.id.etUsername);
        edtpassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.etLogin);
        txLblInfoError = findViewById(R.id.lblInfoError);
        txLblInfoError.setVisibility(View.GONE);

        // Daftar BroadcastReceiver
        registerIntentBroadcast();

        // Klik tombol login
        btnLogin.setOnClickListener(view -> {
            txLblInfoError.setVisibility(View.GONE);

            String txtUsername = edtUsername.getText().toString();
            String txtPassword = edtpassword.getText().toString();
            loginFunction.executeLogin(txtUsername, txtPassword);
        });
    }

    private void registerIntentBroadcast() {
        IntentFilter filterLocal = new IntentFilter();
        filterLocal.addAction(SIGNAL_ERROR_AUTH);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverLogin, filterLocal);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverLogin);
        super.onDestroy();
    }
}
