package com.test.material.supitsara.materialnavigationtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by supitsara on 19/9/2558.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<CategoryItem> mData;
    private LayoutInflater inflater;
    private Context context;
    public CategoryAdapter(Context context, List<CategoryItem> data) {
        mData = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_row, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setClickable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.textView.setText(mData.get(position).getText());
        viewHolder.textView2.setText("("+String.valueOf(mData.get(position).getCount())+")");
        Glide.with(context).load(mData.get(position).getDrawable()).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public TextView textView2;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "comfortaa_bold.ttf");
            textView = (TextView) itemView.findViewById(R.id.item_name);
            textView2 = (TextView) itemView.findViewById(R.id.item_count);
            textView.setTypeface(typeface);
            textView2.setTypeface(typeface);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Intent intent;
            //Toast.makeText(context, "Menu item selected -> " + mData.get(getAdapterPosition()).getText(), Toast.LENGTH_SHORT).show();
            intent =  new Intent(context, BoothListActivity.class);
            intent.putExtra("categoryName", mData.get(getAdapterPosition()).getText());
            context.startActivity(intent);
        }
    }

}
