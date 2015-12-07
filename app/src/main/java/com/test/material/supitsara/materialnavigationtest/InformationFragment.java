package com.test.material.supitsara.materialnavigationtest;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {

    Context mContext;
    String mBoothName;
    String mThumbnailUrl;
    String mDetailString;
    String mLocationString;
    String Email;
    String Tel;
    double Rating;
    int review_count;
    Double mLatitude;
    Double mLongitude;
    GPSTracker gps;
    double cLatitude;
    double cLongitude;

    public InformationFragment(Context context, String boothName, String thumbnail, String detail, String location, String email, String tel, double rating, int review, double lat, double aLong) {
        mContext = context;
        mBoothName = boothName;
        mThumbnailUrl = thumbnail;
        mDetailString = detail;
        mLocationString = location;
        Email = email;
        Tel = tel;
        Rating = rating;
        review_count = review;
        mLatitude = lat;
        mLongitude = aLong;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_information, container, false);

        ImageView mThumbnail;
        TextView mRating;
        TextView mReview;
        TextView mDetail;
        TextView mLocation;
        TextView mEmail;
        TextView mTel;
        ImageView direction;
        ImageView email;
        ImageView call;
        ImageView mapImage;

        mThumbnail = (ImageView) v.findViewById(R.id.thumbnail);
        direction = (ImageView) v.findViewById(R.id.Direction);
        email = (ImageView) v.findViewById(R.id.emai_ic);
        call = (ImageView) v.findViewById(R.id.tel_ic);
        mRating = (TextView) v.findViewById(R.id.rating);
        mReview = (TextView) v.findViewById(R.id.review);
        mDetail = (TextView) v.findViewById(R.id.description);
        mLocation = (TextView) v.findViewById(R.id.location);
        mEmail = (TextView) v.findViewById(R.id.email);
        mTel = (TextView) v.findViewById(R.id.tel);
        mapImage = (ImageView) v.findViewById(R.id.map_image);

        gps = new GPSTracker(mContext);
        if (gps.canGetLocation()) {
            cLatitude = gps.getLatitude();
            cLongitude = gps.getLongitude();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        mRating.setText(String.valueOf(new DecimalFormat("#0.0").format(Rating)) + "/5.0");
        if(review_count>1)
            mReview.setText(String.valueOf(review_count) + " reviews");
        else
            mReview.setText(String.valueOf(review_count) + " review");
        Glide.with(mContext).load(mThumbnailUrl).into(mThumbnail);
        mDetail.setText(mDetailString);
        mLocation.setText(mLocationString);
        mEmail.setText(Email);
        mTel.setText(Tel);

        Glide.with(mContext).load("https://maps.googleapis.com/maps/api/staticmap?center=" + mLatitude + "," + mLongitude + "&zoom=16&size=400x200&scale=2&" + "markers=color:orange%7C" + mLatitude + "," + mLongitude + "&key=" + getString(R.string.google_maps_key)).into(mapImage);

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + cLatitude + "," + cLongitude + "&daddr=" + mLatitude + "," + mLongitude + "&mode=driving"));
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{Email});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Choose your email client"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+Tel));
                startActivity(intent);
            }
        });

        mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MapActivity.class);
                intent.putExtra("boothName", mBoothName);
                intent.putExtra("lat", mLatitude);
                intent.putExtra("long", mLongitude);
                startActivity(intent);
            }
        });

        return v;
    }
}
