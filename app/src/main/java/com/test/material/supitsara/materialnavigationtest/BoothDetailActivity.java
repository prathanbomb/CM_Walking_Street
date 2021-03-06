package com.test.material.supitsara.materialnavigationtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

public class BoothDetailActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static final String MY_PREFS = "my_prefs";

    GreenDaoApplication greenDaoApplication;
    DaoSession daoSession;
    TourDao tourDao;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booth_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        greenDaoApplication = (GreenDaoApplication) getApplication();
        daoSession = greenDaoApplication.getDaoSession();

        Intent intent = getIntent();
        String boothName = intent.getStringExtra("boothName");
        getSupportActionBar().setTitle(boothName);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_booth_detail, menu);

        SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        tourDao = daoSession.getTourDao();
        if ((tourDao.queryBuilder().where((TourDao.Properties.UserID.eq(shared.getString("id", "00000"))),(TourDao.Properties.BoothID.eq(getIntent().getStringExtra("boothID")))).build().list().size())==0)
            this.menu.getItem(0).setIcon(R.drawable.ic_favorite_border_white_36dp);
        else
            this.menu.getItem(0).setIcon(R.drawable.ic_favorite_white_36dp);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                if ((tourDao.queryBuilder().where((TourDao.Properties.UserID.eq(shared.getString("id", "00000"))),(TourDao.Properties.BoothID.eq(getIntent().getStringExtra("boothID")))).build().list().size())==0) {
                    tourDao.insert(new Tour(null, shared.getString("id", "00000"), getIntent().getStringExtra("boothID"), getIntent().getDoubleExtra("lat", 0), getIntent().getDoubleExtra("long", 0)));
                    menu.getItem(0).setIcon(R.drawable.ic_favorite_white_36dp);
                    Toast.makeText(BoothDetailActivity.this, "Added this booth to tour", Toast.LENGTH_SHORT).show();
                } else {
                    tourDao.queryBuilder().where((TourDao.Properties.UserID.eq(shared.getString("id", "00000"))),(TourDao.Properties.BoothID.eq(getIntent().getStringExtra("boothID")))).buildDelete().executeDeleteWithoutDetachingEntities();
                    this.menu.getItem(0).setIcon(R.drawable.ic_favorite_border_white_36dp);
                    Toast.makeText(BoothDetailActivity.this, "Deleted this booth from tour", Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Intent intent = getIntent();
            Fragment fragment;
            if(position==0) {
                fragment = new InformationFragment(getApplicationContext(), intent.getStringExtra("boothName"),intent.getStringExtra("thumbnail"), intent.getStringExtra("detail"), intent.getStringExtra("location"), intent.getStringExtra("email"), intent.getStringExtra("tel"), intent.getDoubleExtra("rating", 0.0), intent.getIntExtra("review", 0), intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("long", 0.0));
            } else if(position==1) {
                fragment = new ReviewFragment(getApplicationContext(), intent.getStringExtra("boothID"), intent.getStringExtra("boothName"));
            } else {
                fragment = new PhotoFragment(getApplicationContext(), intent.getStringExtra("boothID"), intent.getStringExtra("boothName"));
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Detail";
                case 1:
                    return "Review ("+getIntent().getIntExtra("review",0)+")";
                case 2:
                    return "Photo";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_booth_detail, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
