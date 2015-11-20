package com.test.material.supitsara.materialnavigationtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    Button submit;
    String username, password, email, fname, lname;
    EditText uname, pword, rpword, mail, f, l, t;
    private String url = "http://10.70.80.249/android_connect/";
    ServiceAPI.RegisterObject[] registerObjects = new ServiceAPI.RegisterObject[0];
    private static final String MY_PREFS = "my_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        uname = (EditText) findViewById(R.id.username);
        pword = (EditText) findViewById(R.id.password);
        rpword = (EditText) findViewById(R.id.retype_password);
        mail = (EditText) findViewById(R.id.email);
        f = (EditText) findViewById(R.id.fname);
        l = (EditText) findViewById(R.id.lname);
        submit = (Button) findViewById(R.id.button_register);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = uname.getText().toString().trim();
                password = pword.getText().toString().trim();
                email = mail.getText().toString().trim();
                fname = f.getText().toString().trim();
                lname = l.getText().toString().trim();
                if(checkFullfill(username, password, email, fname, lname)) {
                    if(password.contentEquals(rpword.getText().toString().trim())) {
                        Retrofit.Builder builder = new Retrofit.Builder();
                        builder.baseUrl(getString(R.string.url));
                        builder.addConverterFactory(GsonConverterFactory.create());
                        ServiceAPI serviceAPI = builder.build().create(ServiceAPI.class);
                        //final ProgressDialog p = ProgressDialog.show(getApplicationContext(), null, "Loading", true, false);
                        serviceAPI.register(username, password, email, fname, lname).enqueue(new Callback<ServiceAPI.RegisterObject[]>() {
                            @Override
                            public void onResponse(Response<ServiceAPI.RegisterObject[]> response, Retrofit retrofit) {
                                //p.dismiss();
                                registerObjects = response.body();
                                if(!registerObjects[0].success) {
                                    Toast.makeText(getApplicationContext(), "Username is existed", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Registration success", Toast.LENGTH_SHORT).show();
                                    setLogin(registerObjects[0].fname+" "+registerObjects[0].lname, registerObjects[0].email,registerObjects[0].id);
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                //p.dismiss();
                                Log.e("TEST", "Error : " + t.getMessage());
                                Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Password and retype password must match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fulfill all field", Toast.LENGTH_SHORT).show();
                }
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

    private boolean checkFullfill(String username, String password, String email, String fname, String lname) {
        if(username.equalsIgnoreCase("") || password.equalsIgnoreCase("") || email.equalsIgnoreCase("") || fname.equalsIgnoreCase("") || lname.equalsIgnoreCase(""))
            return false;
        return true;
    }

}
