package com.technoxol.mandepos.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.technoxol.mandepos.R;
import com.technoxol.mandepos.Utils;

import java.util.List;

/**
 * Created by Jawad Zulqarnain on 7/10/2017.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Bitmap> imagesList;
    private Utils utils;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageItem;

        public MyViewHolder(View view) {
            super(view);

            imageItem = (ImageView) view.findViewById(R.id.imageHolder);
        }
    }


    public ImagesAdapter(Context mContext, List<Bitmap> imagesList) {
        this.mContext = mContext;
        this.imagesList = imagesList;
    }

    @Override
    public ImagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_image_item, parent, false);

        utils = new Utils(mContext);
        return new ImagesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImagesAdapter.MyViewHolder holder, final int position) {

        holder.imageItem.setImageBitmap(imagesList.get(position));
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

}
