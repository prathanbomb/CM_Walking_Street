package com.test.material.supitsara.materialnavigationtest;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
    public String url = "http://10.70.80.249/android_connect/";
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

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(url);
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
                if(mWifi.isConnected()) {
                    Toast.makeText(mContext, "Review not found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Connection failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ListViewHolder holder, int position) {
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
        }

        @Override
        public int getItemCount() {
            return reviewObjects.length;
        }

    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        RatingBar ratingBar;
        TextView review_header;
        TextView review_body;
        TextView review_datetime;

        public ListViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.name);
            ratingBar = (RatingBar) itemView.findViewById(R.id.user_rate);
            review_header = (TextView) itemView.findViewById(R.id.review_header);
            review_body = (TextView) itemView.findViewById(R.id.review_body);
            review_datetime = (TextView) itemView.findViewById(R.id.review_datetime);
        }
    }

}