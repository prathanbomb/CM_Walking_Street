package com.test.material.supitsara.materialnavigationtest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseFragment extends Fragment {

    private RecyclerView catListView;
    private String[] strListView;

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

        final List<CategoryItem> categoryItems = getCategory(strListView);

        //ArrayAdapter<String> objAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, strListView);

        CategoryAdapter adapter = new CategoryAdapter(getActivity(), categoryItems);
        catListView.setAdapter(adapter);

        return rootView;
    }

    public List<CategoryItem> getCategory(String[] strListView) {
        List<CategoryItem> items = new ArrayList<CategoryItem>();
        items.add(new CategoryItem(strListView[0], getResources().getDrawable(R.drawable.ic_category_arts)));
        items.add(new CategoryItem(strListView[1], getResources().getDrawable(R.drawable.ic_category_bags)));
        items.add(new CategoryItem(strListView[2], getResources().getDrawable(R.drawable.ic_category_cloths)));
        items.add(new CategoryItem(strListView[3], getResources().getDrawable(R.drawable.ic_category_decorations)));
        items.add(new CategoryItem(strListView[4], getResources().getDrawable(R.drawable.ic_category_foods)));
        items.add(new CategoryItem(strListView[5], getResources().getDrawable(R.drawable.ic_category_gifts)));
        items.add(new CategoryItem(strListView[6], getResources().getDrawable(R.drawable.ic_category_instruments)));
        items.add(new CategoryItem(strListView[7], getResources().getDrawable(R.drawable.ic_category_massage)));
        items.add(new CategoryItem(strListView[8], getResources().getDrawable(R.drawable.ic_category_shoes)));
        return items;
    }

}