package com.example.cafemoa;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CustomViewHolder> {

    private ArrayList<ReviewData> mList = null;
    private Activity context = null;


    public ReviewAdapter(Activity context, ArrayList<ReviewData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView reviewRate;
        protected TextView reviewUserid;
        protected TextView reviewTitle;
        protected TextView reviewReview;



        public CustomViewHolder(View view) {
            super(view);
            this.reviewRate = (TextView) view.findViewById(R.id.reviewRate);
            this.reviewUserid = (TextView) view.findViewById(R.id.reviewUserid);
            this.reviewTitle = (TextView) view.findViewById(R.id.reviewTitle);
            this.reviewReview = (TextView) view.findViewById(R.id.reviewReview);

        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reviewlist, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.reviewRate.setText(mList.get(position).getReview_rate());
        viewholder.reviewUserid.setText(mList.get(position).getReview_userID());
        viewholder.reviewTitle.setText(mList.get(position).getReview_title());
        viewholder.reviewReview.setText(mList.get(position).getReview_review());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}