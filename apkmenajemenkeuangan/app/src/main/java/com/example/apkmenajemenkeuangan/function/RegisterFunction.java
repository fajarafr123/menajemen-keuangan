package com.example.apkmenajemenkeuangan.function;

import static com.example.apkmenajemenkeuangan.LoginActivity.SIGNAL_ERROR_AUTH;
import static com.example.apkmenajemenkeuangan.RegisterActivity.SIGNAL_ACCOUNT_IS_READY;
import static com.example.apkmenajemenkeuangan.RegisterActivity.SIGNAL_REGISTER_SUCCES;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apkmenajemenkeuangan.network.BaseURL;
import com.example.apkmenajemenkeuangan.storage.PreferenceRegister;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterFunction {
    Context context;
    BaseURL baseURL;

    public RegisterFunction(Context context) {
        this.context = context;
    }

    // Menambahkan proses registrasi dengan validasi
    public void processRegister(String namaLengkap, String email, String userName, String passwordAktif) {
        // Validasi input
        if (TextUtils.isEmpty(namaLengkap)) {
            sendErrorMessage("Nama lengkap wajib diisi");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            sendErrorMessage("Email wajib diisi");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            sendErrorMessage("Format email tidak valid");
            return;
        }

        if (TextUtils.isEmpty(userName)) {
            sendErrorMessage("Username wajib diisi");
            return;
        }

        if (TextUtils.isEmpty(passwordAktif) || passwordAktif.length() < 8) {
            sendErrorMessage("Password minimal 8 karakter");
            return;
        }

        // Jika semua validasi berhasil, lanjutkan dengan registrasi
        executeRegister(namaLengkap, email, userName, passwordAktif);
    }

    // Fungsi untuk mengirim pesan error jika ada masalah dengan input
    private void sendErrorMessage(String message) {
        // Menyimpan pesan error ke PreferenceRegister
        PreferenceRegister.getInstance(context).setsaveMessageError(message);

        // Kirim broadcast ke RegisterActivity untuk menampilkan pesan error
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(SIGNAL_ACCOUNT_IS_READY));
    }

    // Fungsi untuk melakukan registrasi ke server
    public void executeRegister(String namaLengkap, String email, String userName, String passwordAktif) {
        baseURL = new BaseURL(context);

        StringRequest requestLogin = new StringRequest(
                Request.Method.POST, baseURL.apiRegister(),
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        Log.i("ERROR_LOG_REGISTER-1", object.toString());

                        boolean statusRegist = object.getBoolean("status");

                        if (statusRegist) {
                            String getDataUser = object.getString("data_user");
                            JSONObject objDataUser = new JSONObject(getDataUser);

                            String emailNewUser = objDataUser.getString("username");
                            String pwdNewUser = objDataUser.getString("password");

                            PreferenceRegister.getInstance(context).setSaveRegisterUser(emailNewUser, pwdNewUser);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(SIGNAL_REGISTER_SUCCES));
                        } else {
                            // Jika email/username sudah terdaftar
                            String messageGetError = object.getString("message");
                            PreferenceRegister.getInstance(context).setsaveMessageError(messageGetError);

                            // Kirim pesan error ke RegisterActivity untuk menampilkan pesan kesalahan
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(SIGNAL_ACCOUNT_IS_READY));
                        }
                    } catch (Exception e) {
                        Log.e("ERROR_LOG_REGISTER-2", e.toString());
                    }
                },
                error -> {
                    Log.e("ERROR_LOG_REGISTER-2", error.toString());
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_user", namaLengkap);
                params.put("email_active", email);
                params.put("username", userName);
                params.put("password", passwordAktif);

                return params;
            }
        };

        requestLogin.setShouldCache(false);
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestLogin.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(requestLogin);
    }

}
