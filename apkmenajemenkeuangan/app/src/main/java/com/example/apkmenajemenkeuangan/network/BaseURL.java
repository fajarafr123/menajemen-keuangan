package com.example.apkmenajemenkeuangan.network;

import android.content.Context;

public class BaseURL {
    Context context;

    String BASE_URL_SERVER = "http://192.168.1.11/";
    String PACKAGE_URL = "proyek_menajemenkeuangan/api/";

    public BaseURL (Context context){
        this.context = context;
    }

    public String apiLogin(){
        String LoginAPI = BASE_URL_SERVER + PACKAGE_URL + "auth/login.php";
        return LoginAPI;
    }
    public String apiRegister(){
        String RegisterAPI = BASE_URL_SERVER + PACKAGE_URL + "regist/register.php";
        return RegisterAPI;
    }
}
