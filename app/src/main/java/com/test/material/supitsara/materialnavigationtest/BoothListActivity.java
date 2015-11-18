package com.test.material.supitsara.materialnavigationtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class BoothListActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    // url to get all products list
    private static String urlEmulator = "http://10.0.2.2/android_connect/";
    private static String urlDevice = "http://10.70.80.249/android_connect/";
    private static String url = urlDevice;
    private static String categoryID;
    private String categoryName;

    public double latitude;
    public double longitude;

    public String mSelected = "Popular";


    public static String[] sort_menu = {"Popular","Name","Near by"};

    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;

    ServiceAPI.BoothObject[] boothObjects = new ServiceAPI.BoothObject[0];

    // GPSTracker class
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booth_list);
        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");
//        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //textView.setText(categoryName);
        //getSupportActionBar().setTitle(categoryName);

        /*Bundle args = new Bundle();
        args.putCharSequence("categoryName", categoryName);

        BoothListFragment boothListFragment = new BoothListFragment();
        boothListFragment.setArguments(args);
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, boothListFragment);
        transaction.commit();*/

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.boothList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ListAdapter());

        // Initialize progressbar
        //progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        //progressBar.setVisibility(View.VISIBLE);

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(getString(R.string.url));
        builder.addConverterFactory(GsonConverterFactory.create());
        final ServiceAPI serviceAPI = builder.build().create(ServiceAPI.class);

        if(categoryName.equalsIgnoreCase("Decoration")) {
            categoryID = "00001";
        } else if(categoryName.equalsIgnoreCase("Cloth")) {
            categoryID = "00002";
        } else if(categoryName.equalsIgnoreCase("Bag")) {
            categoryID = "00003";
        } else if(categoryName.equalsIgnoreCase("Shoe")) {
            categoryID = "00004";
        } else if(categoryName.equalsIgnoreCase("Art")) {
            categoryID = "00005";
        } else if(categoryName.equalsIgnoreCase("Food")) {
            categoryID = "00006";
        } else if(categoryName.equalsIgnoreCase("Gift")) {
            categoryID = "00007";
        } else if(categoryName.equalsIgnoreCase("Instrument")) {
            categoryID = "00008";
        } else if(categoryName.equalsIgnoreCase("Massage")) {
            categoryID = "00009";
        }
        loadData(serviceAPI,categoryID, mSelected);

        final SwipeRefreshLayout swipeRefresh =
                (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // download data
                loadData(serviceAPI,categoryID, mSelected);

                // refresh recycler view
                mRecyclerView.getAdapter().notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
                Log.d("TEST", "refresh");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void loadData(ServiceAPI serviceAPI, String categoryID, String sortBy) {
        final ProgressDialog p = ProgressDialog.show(this, null, "Loading", true, false);
        gps = new GPSTracker(BoothListActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        serviceAPI.sendData(categoryID, sortBy, latitude, longitude).enqueue(new Callback<ServiceAPI.BoothObject[]>() {
            @Override
            public void onResponse(Response<ServiceAPI.BoothObject[]> response, Retrofit retrofit) {
                p.dismiss();
                boothObjects = response.body();
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                p.dismiss();
                Log.e("TEST", "Error : " + t.getMessage());
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if(mWifi.isConnected()) {
                    Toast.makeText(BoothListActivity.this, "Booth not found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BoothListActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_booth_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        int position = 0;

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(BoothListActivity.this);
            builder.setTitle("Sort by");
            if(mSelected.equalsIgnoreCase(sort_menu[0])) {
                position = 0;
            } else if(mSelected.equalsIgnoreCase(sort_menu[1])) {
                position = 1;
            } else if (mSelected.equalsIgnoreCase(sort_menu[2])) {
                position = 2;
            }
            builder.setSingleChoiceItems(sort_menu, position, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mSelected = sort_menu[which];
                }
            });
            builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Retrofit.Builder builder = new Retrofit.Builder();
                    builder.baseUrl(getString(R.string.url));
                    builder.addConverterFactory(GsonConverterFactory.create());
                    final ServiceAPI serviceAPI = builder.build().create(ServiceAPI.class);
                    loadData(serviceAPI,categoryID, mSelected);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", null);
            builder.create();

            // สุดท้ายอย่าลืม show() ด้วย
            builder.show();
            return true;
        } else if(id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false));
        }

        @Override
        public void onBindViewHolder(ListViewHolder holder, int position) {
            holder.name.setText(boothObjects[position].boothName);
            holder.description.setText(boothObjects[position].boothDescription);
            holder.distance.setText(String.valueOf(new DecimalFormat("#0.00").format(boothObjects[position].distance)) + " km");
            holder.rating.setText(String.valueOf(new DecimalFormat("#0.0").format(boothObjects[position].rating)));
            holder.review.setText(String.valueOf(boothObjects[position].review));
            Glide.with(BoothListActivity.this).load(boothObjects[position].thumbnailUrl).into(holder.thumbnail);
            Glide.with(BoothListActivity.this).load(R.drawable.road_sign).into(holder.road_icon);
        }

        @Override
        public int getItemCount() {
            return boothObjects.length;
        }

    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView description;
        public TextView distance;
        public TextView rating;
        public TextView review;
        public ImageView thumbnail;
        public ImageView road_icon;

        public ListViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.booth_name);
            description = (TextView) itemView.findViewById(R.id.booth_description);
            rating = (TextView) itemView.findViewById(R.id.rating);
            review = (TextView) itemView.findViewById(R.id.review);
            thumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            distance = (TextView) itemView.findViewById(R.id.distance);
            road_icon = (ImageView) itemView.findViewById(R.id.road_icon);
            itemView.setOnClickListener(this);
            thumbnail.setOnClickListener(this);
            description.setOnClickListener(this);
            name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Intent intent;
            int viewId = v.getId();
            int position = getAdapterPosition();
            //Toast.makeText(BoothListActivity.this, boothObjects[position].boothName, Toast.LENGTH_SHORT).show();
            intent =  new Intent(getApplication(), BoothDetailActivity.class);
            intent.putExtra("boothID", boothObjects[position].boothID);
            intent.putExtra("boothName", boothObjects[position].boothName);
            intent.putExtra("thumbnail", boothObjects[position].thumbnailUrl);
            intent.putExtra("detail", boothObjects[position].boothDetail);
            intent.putExtra("location", boothObjects[position].boothLoc);
            intent.putExtra("email", boothObjects[position].boothEmail);
            intent.putExtra("tel", boothObjects[position].boothTel);
            intent.putExtra("rating", boothObjects[position].rating);
            intent.putExtra("review", boothObjects[position].review);
            intent.putExtra("lat", boothObjects[position].boothLat);
            intent.putExtra("long", boothObjects[position].boothLong);
            startActivity(intent);
        }
    }

}
