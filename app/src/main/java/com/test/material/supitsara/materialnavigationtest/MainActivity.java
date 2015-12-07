package com.test.material.supitsara.materialnavigationtest;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import com.test.material.supitsara.materialnavigationtest.DaoSession;
import com.test.material.supitsara.materialnavigationtest.Search;
import com.test.material.supitsara.materialnavigationtest.SearchDao;

import java.util.List;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private String[] mPlanetTitle = {"Home", "Browse", "Search", "Tour"};
    private ImageView mImageView;
    private MaterialSearchView materialSearchView;

    GreenDaoApplication greenDaoApplication;
    DaoSession daoSession;
    SearchDao searchDao;
    String[] mDataset;

    Intent intent;

    private static final String MY_PREFS = "my_prefs";
    ServiceAPI.UserObject[] userObjects = new ServiceAPI.UserObject[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);

        greenDaoApplication = (GreenDaoApplication) getApplication();
        daoSession = greenDaoApplication.getDaoSession();

        materialSearchView = (MaterialSearchView) findViewById(R.id.search_view);

        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDao = daoSession.getSearchDao();
                Query query1 = searchDao.queryBuilder().where(SearchDao.Properties.Keyword.eq(query)).build();
                int c = query1.list().size();
                if (c == 0)
                    searchDao.insert(new Search(null, shared.getString("id", "00000"), query));
                intent = new Intent(getApplication(), SearchResultActivity.class);
                intent.putExtra("keyword", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                initSearchData();
                materialSearchView.setSuggestions(mDataset);
                return false;
            }
        });

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        mNavigationDrawerFragment.context = getApplicationContext();

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer
        mNavigationDrawerFragment.setUserData(shared.getString("fullname", "Anonymous"), shared.getString("email", "prathanbomb@gmail.com"), shared.getString("profile_img", "R.drawable.avatar_gray"));

    }

    private void initSearchData() {
        searchDao = daoSession.getSearchDao();
        final SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        List<Search> searches = searchDao.queryBuilder().where(SearchDao.Properties.UserID.eq(shared.getString("id", "00000"))).build().list();
        int size = searches.size();
        mDataset = new String[size];
        for (int i = 0; i < searches.size(); i++) {
            mDataset[i] = searches.get(i).getKeyword();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //Toast.makeText(this, "Menu item selected -> " + mPlanetTitle[position], Toast.LENGTH_SHORT).show();
        if(position==0) {
            HomeFragment homeFragment = new HomeFragment(getApplicationContext());
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, homeFragment);
            transaction.commit();
        }
        else if(position==1) {
            BrowseFragment browseFragment = new BrowseFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, browseFragment);
            transaction.commit();
        } else if(position==2) {
            TourFragment tourFragment = new TourFragment(getApplicationContext());
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, tourFragment);
            transaction.commit();
        } else if(position==3) {
            SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = shared.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else {
            if (materialSearchView.isSearchOpen())
                materialSearchView.closeSearch();
            else
                mNavigationDrawerFragment.openDrawer();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            MenuItem menuItem = menu.findItem(R.id.action_search);
            materialSearchView.setMenuItem(menuItem);

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
