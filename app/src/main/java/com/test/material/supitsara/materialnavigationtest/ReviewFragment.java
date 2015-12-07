package com.test.material.supitsara.materialnavigationtest;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.text.DecimalFormat;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

    public boolean wrote = false;
    public String id;
    private static final String MY_PREFS = "my_prefs";
    RecyclerView mRecyclerView;
    Context mContext;
    public String booth_id;
    public String booth_name;
    public int myStar;
    public String myHeader;
    public String myBody;
    ServiceAPI.ReviewObject[] reviewObjects = new ServiceAPI.ReviewObject[0];

    public ReviewFragment(Context context, String boothID, String boothname) {
        mContext = context;
        booth_id = boothID;
        booth_name = boothname;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_review, container, false);
        // Initialize recycler view
        mRecyclerView = (RecyclerView) v.findViewById(R.id.booth_review);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ListAdapter());
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);

        if(checkLogin()) {
            fab = (FloatingActionButton) v.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences shared = mContext.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
                    id = shared.getString("id", "id not found");
                    Intent intent;
                    if(!wrote) {
                        intent = new Intent(mContext, WriteReviewActivity.class);
                        intent.putExtra("wrote", false);
                    }
                    else {
                        intent = new Intent(mContext, WriteReviewActivity.class);
                        intent.putExtra("wrote", true);
                        intent.putExtra("myStar", myStar);
                        intent.putExtra("myHeader", myHeader);
                        intent.putExtra("myBody", myBody);
                    }
                    intent.putExtra("user_id", id);
                    intent.putExtra("booth_id", booth_id);
                    intent.putExtra("booth_name", booth_name);
                    startActivity(intent);
                }
            });
        } else {
            fab.hide();
        }

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(getString(R.string.url));
        builder.addConverterFactory(GsonConverterFactory.create());
        ServiceAPI serviceAPI = builder.build().create(ServiceAPI.class);
        //final ProgressDialog p = ProgressDialog.show(mContext, null, "Loading", true, false);
        serviceAPI.getReview(booth_id).enqueue(new Callback<ServiceAPI.ReviewObject[]>() {
            @Override
            public void onResponse(Response<ServiceAPI.ReviewObject[]> response, Retrofit retrofit) {
                //p.dismiss();
                reviewObjects = response.body();
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                //p.dismiss();
                Log.e("TEST", "Error : " + t.getMessage());
                ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWifi.isConnected()) {
                    Toast.makeText(mContext, "Connection fail", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "No connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final SwipeRefreshLayout swipeRefresh =
                (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // download data
                Retrofit.Builder builder = new Retrofit.Builder();
                builder.baseUrl(getString(R.string.url));
                builder.addConverterFactory(GsonConverterFactory.create());
                ServiceAPI serviceAPI = builder.build().create(ServiceAPI.class);
                //final ProgressDialog p = ProgressDialog.show(mContext, null, "Loading", true, false);
                serviceAPI.getReview(booth_id).enqueue(new Callback<ServiceAPI.ReviewObject[]>() {
                    @Override
                    public void onResponse(Response<ServiceAPI.ReviewObject[]> response, Retrofit retrofit) {
                        //p.dismiss();
                        reviewObjects = response.body();
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        //p.dismiss();
                        Log.e("TEST", "Error : " + t.getMessage());
                        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (mWifi.isConnected()) {
                            Toast.makeText(mContext, "Review not found", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Connection failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // refresh recycler view
                mRecyclerView.getAdapter().notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });

        return v;
    }

    private boolean checkLogin() {
        SharedPreferences shared = mContext.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        boolean login = shared.getBoolean("session", false);
        return login;
    }

    class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final ListViewHolder holder, int position) {
            SharedPreferences shared = mContext.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
            id = shared.getString("id", "id not found");
            if(id.equalsIgnoreCase(reviewObjects[position].user_id)) {
                wrote = true;
                myStar = reviewObjects[position].rating;
                myHeader = reviewObjects[position].review_header;
                myBody = reviewObjects[position].review_body;
            }
            holder.username.setText(reviewObjects[position].fullname);
            holder.ratingBar.setRating(reviewObjects[position].rating);
            holder.review_header.setText("\"" + reviewObjects[position].review_header + "\"");
            holder.review_body.setText(reviewObjects[position].review_body);
            holder.review_datetime.setText(reviewObjects[position].review_datetime);
            Glide.with(mContext).load(reviewObjects[position].profile_img).asBitmap().centerCrop().placeholder(R.drawable.avatar_gray).into(new BitmapImageViewTarget(holder.imageView){
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCornerRadius(90);
                    circularBitmapDrawable.setAntiAlias(true);
                    holder.imageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        @Override
        public int getItemCount() {
            return reviewObjects.length;
        }

    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView username;
        RatingBar ratingBar;
        TextView review_header;
        TextView review_body;
        TextView review_datetime;

        public ListViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.profile_img);
            username = (TextView) itemView.findViewById(R.id.name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.user_rate);
            review_header = (TextView) itemView.findViewById(R.id.review_header);
            review_body = (TextView) itemView.findViewById(R.id.review_body);
            review_datetime = (TextView) itemView.findViewById(R.id.review_datetime);
        }
    }

}
