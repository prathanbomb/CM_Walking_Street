package com.test.material.supitsara.materialnavigationtest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class PhotoViewerActivity extends AppCompatActivity {

    ViewPager viewPager;
    ServiceAPI.PhotoObject[] photoObjects = new ServiceAPI.PhotoObject[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("boothName"));

        viewPager = (ViewPager) findViewById(R.id.photo_pager);
        viewPager.setAdapter(new CustomPagerAdapter());

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(getString(R.string.url));
        builder.addConverterFactory(GsonConverterFactory.create());
        ServiceAPI serviceAPI = builder.build().create(ServiceAPI.class);
        serviceAPI.getPhoto(getIntent().getStringExtra("boothID")).enqueue(new Callback<ServiceAPI.PhotoObject[]>() {
            @Override
            public void onResponse(Response<ServiceAPI.PhotoObject[]> response, Retrofit retrofit) {
                photoObjects = response.body();
                viewPager.getAdapter().notifyDataSetChanged();
                viewPager.setCurrentItem(getIntent().getIntExtra("position", 0));
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("TEST", "Error : " + t.getMessage());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private class CustomPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return photoObjects.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = getLayoutInflater().inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            Glide.with(PhotoViewerActivity.this).load(photoObjects[position].imgUrl).into(imageView);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }


    }
}
