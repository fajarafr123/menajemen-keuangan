package com.example.apkmenajemenkeuangan;

import static com.example.apkmenajemenkeuangan.LoginActivity.SIGNAL_SETUP_LOGIN_PARAM;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.apkmenajemenkeuangan.function.RegisterFunction;
import com.example.apkmenajemenkeuangan.storage.PreferenceRegister;
import com.example.apkmenajemenkeuangan.util.HashingUtil; // Tambahkan import HashingUtil

public class RegisterActivity extends AppCompatActivity {

    private EditText etNamaLengkap, etEmail, etUserName, etPassWord, etConfirPassword;
    private TextView lblInfoErrorRegist;
    private Button btnRegister;
    private RegisterFunction funcRegister;
    private ImageView ivTogglePassword, ivToggleConfirmPassword;
    private ProgressBar loadingProgress;

    public static final String SIGNAL_PROCESS_REGISTER = "id.apk_menajemenkeuangan.SIGNAL_PROCESS_REGISTER";
    public static final String SIGNAL_REGISTER_SUCCES = "id.apk_menajemenkeuangan.SIGNAL_REGISTER_SUCCES";
    public static final String SIGNAL_ACCOUNT_IS_READY = "id.apk_menajemenkeuangan.SIGNAL_REGISTER_ERROR";

    private final BroadcastReceiver receiverRegister = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actOfReceive = intent.getAction();
            if (actOfReceive == null) return;

            switch (actOfReceive) {
                case SIGNAL_PROCESS_REGISTER:
                    loadingProgress.setVisibility(View.VISIBLE);
                    btnRegister.setEnabled(false);
                    btnRegister.setBackgroundTintList(getResources().getColorStateList(R.color.disabled_button));
                    break;
                case SIGNAL_REGISTER_SUCCES:
                   Bundle paketData = new Bundle();
                   paketData.putString("EMAIL",PreferenceRegister.getInstance(RegisterActivity.this).getEmailRegist());
                   paketData.putString("PWD", etConfirPassword.getText().toString());

                   Intent prosesKirim = new Intent(RegisterActivity.this, LoginActivity.class);
                    prosesKirim.putExtras(paketData);
                   startActivity(prosesKirim);
                    break;
                case SIGNAL_ACCOUNT_IS_READY:
                    String getMessage = PreferenceRegister.getInstance(RegisterActivity.this).getSaveMessageError();

                    lblInfoErrorRegist.setVisibility(View.VISIBLE);
                    lblInfoErrorRegist.setText(getMessage);

                    loadingProgress.setVisibility(View.GONE);
                    btnRegister.setEnabled(true);
                    btnRegister.setBackgroundTintList(getResources().getColorStateList(R.color.enabled_button));
                    break;
            }
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();
        registerIntentBroadcast();
        funcRegister = new RegisterFunction(this);

        setupListeners();
    }

    private void initializeViews() {
        etNamaLengkap = findViewById(R.id.edtNamaLengkap);
        etEmail = findViewById(R.id.edtEmail);
        etUserName = findViewById(R.id.edtUserName);
        etPassWord = findViewById(R.id.edtPassWord);
        etConfirPassword = findViewById(R.id.edtConfirPassword);
        btnRegister = findViewById(R.id.btnSimpanDataUser);
        loadingProgress = findViewById(R.id.pbProcessing);
        lblInfoErrorRegist = findViewById(R.id.lblInfoErrorRegister);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        ivToggleConfirmPassword = findViewById(R.id.ivToggleConfirPassword);

        btnRegister.setEnabled(false);
        btnRegister.setBackgroundTintList(getResources().getColorStateList(R.color.disabled_button));
        etPassWord.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
        etConfirPassword.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
    }

    private void setupListeners() {
        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility(etPassWord, ivTogglePassword));
        ivToggleConfirmPassword.setOnClickListener(v -> togglePasswordVisibility(etConfirPassword, ivToggleConfirmPassword));

        etEmail.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateEmail(s.toString());
            }
        });

        etPassWord.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validatePasswordStrength(s.toString());
            }
        });

        etConfirPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validatePasswordMatch(etPassWord.getText().toString(), s.toString());
            }
        });

        btnRegister.setOnClickListener(view -> validateAndRegister());
    }

    private void validateAndRegister() {
        String namaLengkap = etNamaLengkap.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String username = etUserName.getText().toString().trim();
        String password = etPassWord.getText().toString().trim();
        String confirmPassword = etConfirPassword.getText().toString().trim();

        if (!validateFields(namaLengkap, email, username, password, confirmPassword)) return;

        // Hash password sebelum dikirim
        String hashedPassword = HashingUtil.hashPassword(password);
        if (hashedPassword != null) {
            funcRegister.processRegister(namaLengkap, email, username, hashedPassword);
        } else {
            lblInfoErrorRegist.setVisibility(View.VISIBLE);
            lblInfoErrorRegist.setText("Terjadi kesalahan saat memproses password.");
        }
    }

    private boolean validateFields(String namaLengkap, String email, String username, String password, String confirmPassword) {
        if (namaLengkap.isEmpty()) {
            etNamaLengkap.setError("Nama lengkap wajib diisi");
            return false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email tidak valid");
            return false;
        }

        if (username.isEmpty()) {
            etUserName.setError("Username wajib diisi");
            return false;
        }

        if (password.isEmpty() || password.length() < 8) {
            etPassWord.setError("Password minimal 8 karakter");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirPassword.setError("Password tidak cocok");
            return false;
        }

        return true;
    }

    private void validateEmail(CharSequence s) {
        if (s.toString().isEmpty()) {
            etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.delete), null);
        } else if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.checklist), null);
        } else {
            etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.delete), null);
        }
    }

    private void validatePasswordStrength(CharSequence s) {
        if (s.length() >= 8) {
            etPassWord.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.checklist), null);
        } else {
            etPassWord.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.delete), null);
        }
    }

    private void validatePasswordMatch(String password, String confirmPassword) {
        if (confirmPassword.equals(password)) {
            etConfirPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.checklist), null);
            btnRegister.setEnabled(true);
            btnRegister.setBackgroundTintList(getResources().getColorStateList(R.color.enabled_button));
        } else {
            etConfirPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.delete), null);
            btnRegister.setEnabled(false);
            btnRegister.setBackgroundTintList(getResources().getColorStateList(R.color.disabled_button));
        }
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleIcon) {
        if (editText.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod) {
            editText.setTransformationMethod(null);
            toggleIcon.setImageResource(R.drawable.ic_eye_on);
        } else {
            editText.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
            toggleIcon.setImageResource(R.drawable.ic_eye_off);
        }
    }

    private void handleRegisterSuccess(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(SIGNAL_PROCESS_REGISTER));
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void handleRegisterError() {
        String errorMessage = PreferenceRegister.getInstance(this).getSaveMessageError();
        lblInfoErrorRegist.setVisibility(View.VISIBLE);
        lblInfoErrorRegist.setText(errorMessage);
        showLoading(false);
        btnRegister.setEnabled(true);
        btnRegister.setBackgroundTintList(getResources().getColorStateList(R.color.enabled_button));
    }

    private void showLoading(boolean isLoading) {
        loadingProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!isLoading);
        int color = isLoading ? R.color.disabled_button : R.color.enabled_button;
        btnRegister.setBackgroundTintList(getResources().getColorStateList(color));
    }

    private void registerIntentBroadcast() {
        IntentFilter filterLocal = new IntentFilter();
        filterLocal.addAction(SIGNAL_PROCESS_REGISTER);
        filterLocal.addAction(SIGNAL_REGISTER_SUCCES);
        filterLocal.addAction(SIGNAL_ACCOUNT_IS_READY);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverRegister, filterLocal);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverRegister);
    }

    private abstract class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}
    }
}
