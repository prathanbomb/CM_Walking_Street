package com.test.material.supitsara.materialnavigationtest;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {

    Context mContext;
    String mThumbnailUrl;
    String mDetailString;
    String mLocationString;
    String mEmail;
    String mTel;
    double mRating;
    int review_count;
    RecyclerView mRecyclerView;

    public InformationFragment(Context context, String thumbnail, String detail, String location, String email, String tel, double rating, int review) {
        mContext = context;
        mThumbnailUrl = thumbnail;
        mDetailString = detail;
        mLocationString = location;
        mEmail = email;
        mTel = tel;
        mRating = rating;
        review_count = review;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_information, container, false);
        // Initialize recycler view
        mRecyclerView = (RecyclerView) v.findViewById(R.id.booth_detail);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ListAdapter());
        return v;
    }

    class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ListViewHolder holder, int position) {
            holder.mRating.setText(String.valueOf(new DecimalFormat("#0.0").format(mRating)) + "/5.0");
            if(review_count>1)
                holder.mReview.setText(String.valueOf(review_count) + " reviews");
            else
                holder.mReview.setText(String.valueOf(review_count) + " review");
            Glide.with(mContext).load(mThumbnailUrl).into(holder.mThumbnail);
            holder.mDetail.setText(mDetailString);
            holder.mLocation.setText(mLocationString);
            holder.mEmail.setText(mEmail);
            holder.mTel.setText(mTel);
        }

        @Override
        public int getItemCount() {
            return 1;
        }

    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView mThumbnail;
        TextView mRating;
        TextView mReview;
        TextView mDetail;
        TextView mLocation;
        TextView mEmail;
        TextView mTel;

        public ListViewHolder(View itemView) {
            super(itemView);
            mThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            mRating = (TextView) itemView.findViewById(R.id.rating);
            mReview = (TextView) itemView.findViewById(R.id.review);
            mDetail = (TextView) itemView.findViewById(R.id.description);
            mLocation = (TextView) itemView.findViewById(R.id.location);
            mEmail = (TextView) itemView.findViewById(R.id.email);
            mTel = (TextView) itemView.findViewById(R.id.tel);
        }
    }

}
