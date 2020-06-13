package com.example.cafemoa;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

//카페리스트 어댑터

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {

    private ArrayList<ListData> mList = null;
    private Activity context = null;


    public ListAdapter(Activity context, ArrayList<ListData> list1) {
        this.context = context;
        this.mList = list1;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView address;


        public CustomViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.cafename);
            this.address = (TextView) view.findViewById(R.id.cafeaddress);

        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cafe_list2, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.name.setText(mList.get(position).getCafe_name());
        viewholder.address.setText(mList.get(position).getCafe_address());


    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
