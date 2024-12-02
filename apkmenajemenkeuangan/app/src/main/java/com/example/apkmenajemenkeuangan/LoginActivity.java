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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.apkmenajemenkeuangan.function.LoginFunction;
import com.example.apkmenajemenkeuangan.network.BaseURL;
import com.example.apkmenajemenkeuangan.storage.PreferenceLogin;
import com.example.apkmenajemenkeuangan.storage.PreferenceRegister;
import com.example.apkmenajemenkeuangan.storage.PreferenceSessionLogin;
import com.example.apkmenajemenkeuangan.util.HashingUtil; // Tambahkan import untuk HashingUtil

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    LoginFunction loginFunction;

    EditText edtUsername, edtpassword;
    Button btnLogin;
    TextView txLblInfoError;
    ImageView iveTogglePassword; // ImageView untuk tombol mata

    public static final String SIGNAL_ERROR_AUTH = "id.apk_menajemenkeuangan.SIGNAL_ERROR_AUTH";
    public static final String SIGNAL_SETUP_LOGIN_PARAM = "id.apk_menajemenkeuangan.SIGNAL_SETUP_LOGIN_PARAM";

    private final BroadcastReceiver receiverLogin = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actOfReceive = intent.getAction();
            switch (Objects.requireNonNull(actOfReceive)) {
                case SIGNAL_ERROR_AUTH:
                    String messageError = PreferenceLogin.getInstance(LoginActivity.this).getSaveMessageError();
                    Log.e("GETMESSAGEERRORBYSIGNAL", messageError);
                    txLblInfoError.setVisibility(View.VISIBLE);
                    txLblInfoError.setText(messageError);
                    break;
                case SIGNAL_SETUP_LOGIN_PARAM:
                    String emailHasRegist = PreferenceRegister.getInstance(LoginActivity.this).getEmailRegist();
                    String pwdHasRegist = PreferenceRegister.getInstance(LoginActivity.this).getPasswordRegist();

                    edtUsername.setText(emailHasRegist);
                    edtpassword.setText(pwdHasRegist);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi
        loginFunction = new LoginFunction(LoginActivity.this);
        edtUsername = findViewById(R.id.etUsername);
        edtpassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.etLogin);
        txLblInfoError = findViewById(R.id.lblInfoError);
        iveTogglePassword = findViewById(R.id.ivTogglePassword); // Inisialisasi tombol mata

        txLblInfoError.setVisibility(View.GONE);

        // Set password langsung tersembunyi
        edtpassword.setInputType(129); // 129 is for textPassword (tersembunyi)
        iveTogglePassword.setImageResource(R.drawable.ic_eye_off); // Set the eye icon for hidden password

        // Daftar BroadcastReceiver
        registerIntentBroadcast();

        // Klik tombol login
        btnLogin.setOnClickListener(view -> {
            txLblInfoError.setVisibility(View.GONE);

            String txtUsername = edtUsername.getText().toString();
            String txtPassword = edtpassword.getText().toString();

            // Hash password sebelum dikirim
            String hashedPassword = HashingUtil.hashPassword(txtPassword);
            if (hashedPassword != null) {
                // Kirim hashed password untuk proses login
                loginFunction.executeLogin(txtUsername, hashedPassword);
            } else {
                // Jika hashing gagal, tampilkan error
                txLblInfoError.setVisibility(View.VISIBLE);
                txLblInfoError.setText("Terjadi kesalahan saat memproses password.");
            }
        });


        // Fungsi untuk toggle password visibility
        iveTogglePassword.setOnClickListener(view -> {
            if (edtpassword.getInputType() == 129) { // 129 is textPassword
                // Ubah menjadi teks biasa (password terlihat)
                edtpassword.setInputType(1); // 1 is textVisiblePassword
                iveTogglePassword.setImageResource(R.drawable.ic_eye_on); // Ganti gambar mata menjadi terlihat
            } else {
                // Ubah kembali menjadi password tersembunyi
                edtpassword.setInputType(129); // 129 is textPassword
                iveTogglePassword.setImageResource(R.drawable.ic_eye_off); // Ganti gambar mata menjadi tersembunyi
            }
        });

        boolean statusLoginSession = PreferenceSessionLogin.getInstance(LoginActivity.this).getSessionLoginUser();
        Log.e("SESSIONLOGINSTATUS", String.valueOf(statusLoginSession));

        if (statusLoginSession != false) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private void registerIntentBroadcast() {
        IntentFilter filterLocal = new IntentFilter();
        filterLocal.addAction(SIGNAL_ERROR_AUTH);
        filterLocal.addAction(SIGNAL_SETUP_LOGIN_PARAM);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverLogin, filterLocal);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverLogin);
        super.onDestroy();
    }

    public void goToRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
