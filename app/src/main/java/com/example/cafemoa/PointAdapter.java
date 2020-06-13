package com.example.cafemoa;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PointAdapter  extends RecyclerView.Adapter<PointAdapter.CustomViewHolder> {

        private ArrayList<PointData> mList = null;
        private Activity context = null;


        public PointAdapter(Activity context, ArrayList<PointData> list1) {
            this.context = context;
            this.mList = list1;
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView name;
            protected TextView point;


            public CustomViewHolder(View view) {
                super(view);
                this.name = (TextView) view.findViewById(R.id.cafename);
                this.point = (TextView) view.findViewById(R.id.cafepoint);

            }
        }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pointlist, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }
        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

            viewholder.name.setText(mList.get(position).getCafe_name());
            viewholder.point.setText(mList.get(position).getCafe_point());


        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() : 0);
        }

    }


