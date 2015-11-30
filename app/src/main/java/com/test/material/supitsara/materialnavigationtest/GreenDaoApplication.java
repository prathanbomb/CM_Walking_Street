package com.test.material.supitsara.materialnavigationtest;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.test.material.supitsara.materialnavigationtest.DaoMaster;
import com.test.material.supitsara.materialnavigationtest.DaoSession;

import com.test.material.supitsara.materialnavigationtest.SearchDao;

/**
 * Created by supitsara on 23/11/2558.
 */
public class GreenDaoApplication extends Application {

    DaoSession mDaoSession;
    SearchDao searchDao;
    TourDao tourDao;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "MyGreenDao.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        searchDao.createTable(db, true);
        tourDao.createTable(db, true);
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

}
