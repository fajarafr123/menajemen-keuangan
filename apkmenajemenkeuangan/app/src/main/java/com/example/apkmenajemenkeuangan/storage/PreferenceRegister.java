package com.example.apkmenajemenkeuangan.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceRegister {

    private static PreferenceRegister instancePreference;
    private static Context baseContext;
    private static final String SHARED_PREF_NAME = "preference_register";
    private static final String MESSAGE_ERROR = "MESSAGE_ERROR";
    private static final String EMAIL_USERAUTH = "email";
    private static final String PASSWORD_USERAUTH = "password";




    public PreferenceRegister(Context context){
        baseContext = context;
    }

    public static PreferenceRegister getInstance(Context context){
        if (instancePreference == null){
            instancePreference = new PreferenceRegister(context);
        }
        return instancePreference;

}
    public void setSaveRegisterUser(String password, String email){
        SharedPreferences.Editor editor = baseContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(EMAIL_USERAUTH, email);
        editor.putString(PASSWORD_USERAUTH, password);

        editor.apply();
    }
    public String getEmailRegist (){
        SharedPreferences sharedPreferences = baseContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(EMAIL_USERAUTH, null);
    }
    public String getPasswordRegist (){
        SharedPreferences sharedPreferences = baseContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PASSWORD_USERAUTH, null);
    }

    public void setsaveMessageError(String msgError) {
        SharedPreferences.Editor editor = baseContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(MESSAGE_ERROR, msgError);
        editor.apply();
    }
    public String getSaveMessageError(){
        SharedPreferences sharedPreferences = baseContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(MESSAGE_ERROR, null);
    }
}
