package com.test.material.supitsara.materialnavigationtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class WriteReviewActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button submit;
    EditText mtitle;
    EditText mdetail;
    public String url = "http://10.70.80.249/android_connect/";
    ServiceAPI.InsertObject[] insertObjects = new ServiceAPI.InsertObject[0];
    ServiceAPI.UpdateObject[] updateObjects = new ServiceAPI.UpdateObject[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("booth_name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        submit = (Button) findViewById(R.id.button_submit);
        mtitle = (EditText) findViewById(R.id.title);
        mdetail = (EditText) findViewById(R.id.detail);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        if(getIntent().getBooleanExtra("wrote", false)) {
            mtitle.setText(getIntent().getStringExtra("myHeader"));
            mdetail.setText(getIntent().getStringExtra("myBody"));
            ratingBar.setRating(getIntent().getIntExtra("myStar", 0));
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mtitle.getText().toString().trim();
                String detail = mdetail.getText().toString().trim();
                if(checkSubmit(title,detail)) {
                    if(getIntent().getBooleanExtra("wrote", false)) {
                        Retrofit.Builder builder = new Retrofit.Builder();
                        builder.baseUrl(getString(R.string.url));
                        builder.addConverterFactory(GsonConverterFactory.create());
                        ServiceAPI serviceAPI = builder.build().create(ServiceAPI.class);
                        final ProgressDialog p = ProgressDialog.show(WriteReviewActivity.this, null, "Loading", true, false);
                        String user_id = getIntent().getStringExtra("user_id");
                        String booth_id = getIntent().getStringExtra("booth_id");
                        int star = (int) ratingBar.getRating();
                        title = mtitle.getText().toString().trim();
                        detail = mdetail.getText().toString().trim();
                        serviceAPI.update(user_id, booth_id, star, title, detail).enqueue(new Callback<ServiceAPI.UpdateObject[]>() {
                            @Override
                            public void onResponse(Response<ServiceAPI.UpdateObject[]> response, Retrofit retrofit) {
                                p.dismiss();
                                updateObjects = response.body();
                                Toast.makeText(getApplicationContext(), "Review success", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                p.dismiss();
                                Log.e("TEST", "Error : " + t.getMessage());
                                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Retrofit.Builder builder = new Retrofit.Builder();
                        builder.baseUrl(getString(R.string.url));
                        builder.addConverterFactory(GsonConverterFactory.create());
                        ServiceAPI serviceAPI = builder.build().create(ServiceAPI.class);
                        final ProgressDialog p = ProgressDialog.show(WriteReviewActivity.this, null, "Loading", true, false);
                        String user_id = getIntent().getStringExtra("user_id");
                        String booth_id = getIntent().getStringExtra("booth_id");
                        int star = (int) ratingBar.getRating();
                        title = mtitle.getText().toString().trim();
                        detail = mdetail.getText().toString().trim();
                        serviceAPI.insert(user_id, booth_id, star, title, detail).enqueue(new Callback<ServiceAPI.InsertObject[]>() {
                            @Override
                            public void onResponse(Response<ServiceAPI.InsertObject[]> response, Retrofit retrofit) {
                                p.dismiss();
                                insertObjects = response.body();
                                Toast.makeText(getApplicationContext(), "Review success", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                p.dismiss();
                                Log.e("TEST", "Error : " + t.getMessage());
                                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid title or detail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkSubmit(String title, String detail) {
        if(title.equalsIgnoreCase("") || detail.equalsIgnoreCase(""))
            return false;
        return true;
    }

}
