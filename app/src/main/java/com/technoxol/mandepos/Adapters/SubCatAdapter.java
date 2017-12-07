package com.technoxol.mandepos.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technoxol.mandepos.HttpService;
import com.technoxol.mandepos.MainActivity;
import com.technoxol.mandepos.R;
import com.technoxol.mandepos.SharedPrefUtils;
import com.technoxol.mandepos.Utils;
import com.technoxol.mandepos.models.SubCatInfo;

import java.util.List;

/**
 * Created by Jawad Zulqarnain on 7/10/2017.
 */

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.MyViewHolder> {

    private Context mContext;
    private List<SubCatInfo> subCatInfoList;
    private Utils utils;
    private HttpService httpService;
    private SharedPrefUtils sharedPrefUtils;
    private String id, salesType;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView catTitle;
        public LinearLayout catLayout;

        public MyViewHolder(View view) {
            super(view);
            catTitle = (TextView) view.findViewById(R.id.catTitle);
            catLayout = (LinearLayout) view.findViewById(R.id.catLayout);
        }
    }


    public SubCatAdapter(Context mContext, List<SubCatInfo> subCatInfoList, String id, String salesType) {
        this.mContext = mContext;
        this.subCatInfoList = subCatInfoList;
        this.salesType = salesType;
        this.id = id;
        httpService = new HttpService(mContext);

    }

    @Override
    public SubCatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_category_item, parent, false);

        utils = new Utils(mContext);
        return new SubCatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SubCatAdapter.MyViewHolder holder, final int position) {
        final SubCatInfo subCatInfo = subCatInfoList.get(position);
        holder.catTitle.setText(subCatInfo.getCategory_title());
        holder.catLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id",subCatInfo.getCategory_id());
                bundle.putString("sub_id",id);
                bundle.putString("sub_name",subCatInfo.getCategory_title());
                bundle.putString("type",salesType);
                utils.startNewActivity(MainActivity.class,bundle,true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subCatInfoList.size();
    }

}
