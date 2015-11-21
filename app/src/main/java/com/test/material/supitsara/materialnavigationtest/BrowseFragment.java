package com.test.material.supitsara.materialnavigationtest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment {

    private RecyclerView catListView;
    private String[] strListView;
    ServiceAPI.CountObject[] countObjects = new ServiceAPI.CountObject[0];
    public int art = 0;
    public int bag = 0;
    public int clothes = 0;
    public int decoration = 0;
    public int food = 0;
    public int gift = 0;
    public int instrument = 0;
    public int massage = 0;
    public int shoe = 0;
    public int others = 0;

    public BrowseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_browse, container, false);

        catListView = (RecyclerView) rootView.findViewById(R.id.catList);

        strListView = getResources().getStringArray(R.array.my_data_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        catListView.setLayoutManager(layoutManager);
        catListView.setHasFixedSize(true);

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(getString(R.string.url));
        builder.addConverterFactory(GsonConverterFactory.create());
        ServiceAPI serviceAPI = builder.build().create(ServiceAPI.class);
        serviceAPI.getBoothCount().enqueue(new Callback<ServiceAPI.CountObject[]>() {
            @Override
            public void onResponse(Response<ServiceAPI.CountObject[]> response, Retrofit retrofit) {
                countObjects = response.body();
                for (int i = 0; i < countObjects.length; i++) {
                    switch (countObjects[i].catID) {
                        case "00001":
                            decoration = countObjects[i].count;
                            break;
                        case "00002":
                            clothes = countObjects[i].count;
                            break;
                        case "00003":
                            bag = countObjects[i].count;
                            break;
                        case "00004":
                            shoe = countObjects[i].count;
                            break;
                        case "00005":
                            art = countObjects[i].count;
                            break;
                        case "00006":
                            food = countObjects[i].count;
                            break;
                        case "00007":
                            gift = countObjects[i].count;
                            break;
                        case "00008":
                            instrument = countObjects[i].count;
                            break;
                        case "00009":
                            massage = countObjects[i].count;
                            break;
                        case "00010":
                            others = countObjects[i].count;
                            break;
                    }
                }
                final List<CategoryItem> categoryItems = getCategory(strListView);
                CategoryAdapter adapter = new CategoryAdapter(getActivity(), categoryItems);
                catListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("TEST", "Error : " + t.getMessage());
            }
        });

        return rootView;
    }

    public List<CategoryItem> getCategory(String[] strListView) {
        List<CategoryItem> items = new ArrayList<CategoryItem>();
        items.add(new CategoryItem(strListView[0], R.drawable.art, art));
        items.add(new CategoryItem(strListView[1], R.drawable.bag, bag));
        items.add(new CategoryItem(strListView[2], R.drawable.clothes, clothes));
        items.add(new CategoryItem(strListView[3], R.drawable.decoration, decoration));
        items.add(new CategoryItem(strListView[4], R.drawable.food, food));
        items.add(new CategoryItem(strListView[5], R.drawable.gift, gift));
        items.add(new CategoryItem(strListView[6], R.drawable.instrument, instrument));
        items.add(new CategoryItem(strListView[7], R.drawable.massage, massage));
        items.add(new CategoryItem(strListView[8], R.drawable.shoe, shoe));
        items.add(new CategoryItem(strListView[9], R.drawable.others, others));
        return items;
    }

}
