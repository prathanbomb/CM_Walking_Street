package com.test.material.supitsara.materialnavigationtest;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.test.material.supitsara.materialnavigationtest.Tour;
import com.test.material.supitsara.materialnavigationtest.Search;

import com.test.material.supitsara.materialnavigationtest.TourDao;
import com.test.material.supitsara.materialnavigationtest.SearchDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig tourDaoConfig;
    private final DaoConfig searchDaoConfig;

    private final TourDao tourDao;
    private final SearchDao searchDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        tourDaoConfig = daoConfigMap.get(TourDao.class).clone();
        tourDaoConfig.initIdentityScope(type);

        searchDaoConfig = daoConfigMap.get(SearchDao.class).clone();
        searchDaoConfig.initIdentityScope(type);

        tourDao = new TourDao(tourDaoConfig, this);
        searchDao = new SearchDao(searchDaoConfig, this);

        registerDao(Tour.class, tourDao);
        registerDao(Search.class, searchDao);
    }
    
    public void clear() {
        tourDaoConfig.getIdentityScope().clear();
        searchDaoConfig.getIdentityScope().clear();
    }

    public TourDao getTourDao() {
        return tourDao;
    }

    public SearchDao getSearchDao() {
        return searchDao;
    }

}