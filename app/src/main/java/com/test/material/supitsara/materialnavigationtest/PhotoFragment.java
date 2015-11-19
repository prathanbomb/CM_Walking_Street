package com.test.material.supitsara.materialnavigationtest;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
public class PhotoFragment extends Fragment {

    Context mContext;
    RecyclerView mRecyclerView;
    String booth_id;
    ServiceAPI.PhotoObject[] photoObjects = new ServiceAPI.PhotoObject[0];

    public PhotoFragment(Context context, String boothID) {
        mContext = context;
        booth_id = boothID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photo, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.gridview);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(new ListAdapter());

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(getString(R.string.url));
        builder.addConverterFactory(GsonConverterFactory.create());
        ServiceAPI serviceAPI = builder.build().create(ServiceAPI.class);
        //final ProgressDialog p = ProgressDialog.show(mContext, null, "Loading", true, false);
        serviceAPI.getPhoto(booth_id).enqueue(new Callback<ServiceAPI.PhotoObject[]>() {
            @Override
            public void onResponse(Response<ServiceAPI.PhotoObject[]> response, Retrofit retrofit) {
                photoObjects = response.body();
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("TEST", "Error : " + t.getMessage());
                ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWifi.isConnected()) {
                    Toast.makeText(mContext, "No photo", Toast.LENGTH_SHORT).show();
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
            return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ListViewHolder holder, int position) {
            Glide.with(mContext).load(photoObjects[position].imgUrl).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return photoObjects.length;
        }

    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ListViewHolder(View itemView) {
            super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.grid_item);
        }

    }

}
