package com.example.apkmenajemenkeuangan.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceSessionLogin {

    private static PreferenceSessionLogin instancePreference;
    private static Context baseContext;
    private static final String SHARED_PREF_NAME = "preference_session_login";

    private static final boolean STATUS_SESSION = false;


    public PreferenceSessionLogin(Context context){
        baseContext = context;
    }

    public static PreferenceSessionLogin getInstance(Context context){
        if (instancePreference == null){
            instancePreference = new PreferenceSessionLogin(context);
        }
        return instancePreference;

}



    public boolean getSessionLoginUser() {
        SharedPreferences sharedPreferences = baseContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(String.valueOf(STATUS_SESSION), false); // Gunakan getBoolean untuk membaca nilai boolean
    }



    public void setSessionLoginUser(boolean statusSession) {
        SharedPreferences sharedPreferences = baseContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(String.valueOf(STATUS_SESSION), statusSession); // Simpan sebagai boolean
        editor.apply();
    }

}
