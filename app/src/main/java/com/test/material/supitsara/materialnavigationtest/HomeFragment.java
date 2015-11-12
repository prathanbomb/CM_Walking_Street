package com.test.material.supitsara.materialnavigationtest;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.alexvasilkov.foldablelayout.shading.GlanceFoldShading;
import com.bumptech.glide.Glide;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private View mListTouchInterceptor;
    private View mDetailsLayout;
    private UnfoldableView mUnfoldableView;
    public Context mContext;

    public HomeFragment(Context context) {
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.news);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ListAdapter());

        mListTouchInterceptor = v.findViewById(R.id.touch_interceptor_view);
        mListTouchInterceptor.setClickable(false);

        mDetailsLayout = v.findViewById(R.id.details_layout);
        mDetailsLayout.setVisibility(v.INVISIBLE);

        mUnfoldableView = (UnfoldableView) v.findViewById(R.id.unfoldable_view);

        Bitmap glance = BitmapFactory.decodeResource(getResources(), R.drawable.unfold_glance);
        mUnfoldableView.setFoldShading(new GlanceFoldShading(glance));

        mUnfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
                mDetailsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);
                mDetailsLayout.setVisibility(View.INVISIBLE);
            }
        });

        return v;
    }

    class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ListViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        public ListViewHolder(View itemView) {
            super(itemView);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.list_item_image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUnfoldableView.unfold(v, mDetailsLayout);
                }
            });
        }
    }

}
