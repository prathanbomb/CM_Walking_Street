package com.test.material.supitsara.materialnavigationtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by supitsara on 5/11/2558.
 */
public class LoginActivity extends AppCompatActivity{
    private Button mLogin;
    private EditText mUsername;
    private EditText mPassword;
    private TextView mRegister;
    private ImageView mLogo;
    private Context mContext;

    private String url = "http://10.70.80.249/android_connect/";

    private static final String MY_PREFS = "my_prefs";

    ServiceAPI.UserObject[] userObjects = new ServiceAPI.UserObject[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mContext = this;

        mLogo = (ImageView) findViewById(R.id.logo);
        mLogin = (Button) findViewById(R.id.button_login);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mRegister = (TextView) findViewById(R.id.register);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkLogin() {

        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(url);
        builder.addConverterFactory(GsonConverterFactory.create());
        ServiceAPI serviceAPI = builder.build().create(ServiceAPI.class);
        final ProgressDialog p = ProgressDialog.show(this, null, "Loading", true, false);
        serviceAPI.getUser(username, password).enqueue(new Callback<ServiceAPI.UserObject[]>() {
            @Override
            public void onResponse(Response<ServiceAPI.UserObject[]> response, Retrofit retrofit) {
                p.dismiss();
                userObjects = response.body();
                if (userObjects[0].success) {
                    Toast.makeText(getApplicationContext(), userObjects[0].message, Toast.LENGTH_SHORT).show();
                    setLogin(userObjects[0].fname+" "+userObjects[0].lname, userObjects[0].email,userObjects[0].id);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), userObjects[0].message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                p.dismiss();
                Log.e("TEST", "Error : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLogin(String fullname, String email, String id) {
        SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("session", true);
        editor.putString("fullname", fullname);
        editor.putString("email", email);
        editor.putString("id", id);
        editor.commit();
        boolean session = shared.getBoolean("session", false);
        Log.i("LOG_TAG", "session : " + session);
//        editor.remove("userKey");
//        editor.commit();
//        string = shared.getString("userKey", "not found!");
//        Log.i("LOG_TAG", "userKey : " + string);
    }
}
