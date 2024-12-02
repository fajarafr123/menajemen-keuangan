package com.example.apkmenajemenkeuangan.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceLogin {

    private static PreferenceLogin instancePreference;
    private static Context baseContext;
    private static final String SHARED_PREF_NAME = "LOGIN_PREF";
    private static final String MESSAGE_ERROR = "MESSAGE_ERROR";

    private static final String ID_USERAUTH = "id";
    private static final String PASSWORD_USERAUTH = "password";
        private static final String NAME_USERAUTH = "name";
    private static final String EMAIL_USERAUTH = "email";
    private static final String USERNAME_USERAUTH = "username";


    public PreferenceLogin(Context context){
        baseContext = context;
    }

    public static PreferenceLogin getInstance(Context context){
        if (instancePreference == null){
            instancePreference = new PreferenceLogin(context);
        }
        return instancePreference;

}

    public void setSaveLoginUser(String id,
                                 String password,
                                 String name,
                                 String email,
                                 String username){
        SharedPreferences.Editor editor = baseContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(ID_USERAUTH, id);
        editor.putString(NAME_USERAUTH, name);
        editor.putString(EMAIL_USERAUTH, email);
        editor.putString(USERNAME_USERAUTH, username);
        editor.putString(PASSWORD_USERAUTH, password);

        editor.apply();
    }

    public String getNameOfUser (){
        SharedPreferences sharedPreferences = baseContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(NAME_USERAUTH, null);
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
