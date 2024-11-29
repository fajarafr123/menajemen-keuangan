package com.example.apkmenajemenkeuangan.function;

import static com.example.apkmenajemenkeuangan.LoginActivity.SIGNAL_ERROR_AUTH;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apkmenajemenkeuangan.MainActivity;
import com.example.apkmenajemenkeuangan.network.BaseURL;
import com.example.apkmenajemenkeuangan.storage.PreferenceLogin;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFunction {
    Context context;

    BaseURL baseURL;

    public LoginFunction (Context context){
        this.context = context;
    }

    public String helloFunction(String message) {
        return message;
    }

    public Boolean validateLogin(String username, String pwd){
        Log.e("VALIDATE-LOGIN","PARAM:"+username + " -" + pwd);
        Boolean resultCheck = false;

        if (username.equals("")&& pwd.equals("")){
            resultCheck =false;
        }else {
            resultCheck = true;
        }

        return resultCheck;
    }
    public void executeLogin(String username, String password){
        Log.e("Fungsi Dijalankan", "Running, username: "+ username + " password " + password);
        baseURL= new BaseURL(context);


        StringRequest requestLogin = new StringRequest
                (
                        Request.Method.POST, "http://192.168.1.3/proyek_menajemenkeuangan/api/auth/login.php",
                        response -> {
                            try {
                                JSONObject object = new JSONObject(response);
                                Log.i("ERROR_EXCEPTIONLOGIN-1",object.toString());

                                boolean statusAuth = object.getBoolean("status");
                                String messageError = object.getString("message");

                                String getDataUser = object.getString("data user");
                                JSONObject objDataUser = new JSONObject(getDataUser);

                                String idUser = objDataUser.getString("id");
                                String nameOfuser = objDataUser.getString("name_user");
                                String emailuser = objDataUser.getString("email");
                                String usrName = objDataUser.getString("username");
                                String pwd = objDataUser.getString("password");

                                if(statusAuth){
                                    PreferenceLogin.getInstance(context).setSaveLoginUser(idUser,nameOfuser,emailuser,usrName,pwd);

                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);

                                }else {
                                    PreferenceLogin.getInstance(context).setsaveMessageError(messageError);
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(SIGNAL_ERROR_AUTH));
                                }
                            }catch (Exception e) {
                                Log.e("ERROR_EXCEPTIONLOGIN",e.toString());
                            }
                         },
                            error -> {
                                Log.e("ERROR_EXCEPTIONLOGIN",error.toString());
                            }
                )
        {
            @Override
            protected Map<String, String> getParams()  {
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("password",password);

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
