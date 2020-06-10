package com.example.myBicycleMap.Adapter;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myBicycleMap.R;
import com.example.myBicycleMap.model.Fix;

import java.util.ArrayList;

public class FixAdapter extends RecyclerView.Adapter<FixAdapter.FixViewHolder>{
    private ArrayList<Fix> fList;

    public void addItem(Fix fix){
        fList.add(fix);
        Log.i("size", fList.size()+"");
        notifyDataSetChanged();
    }

    public class FixViewHolder extends RecyclerView.ViewHolder{
        protected TextView name;
        protected TextView distance;

        public FixViewHolder(View view){
            super(view);
            this.name = view.findViewById(R.id.name_listitem);
            this.distance = view.findViewById(R.id.address_listitem);
        }
    }

    public FixAdapter(ArrayList<Fix> list){
        this.fList = list;
    }

    @NonNull
    @Override
    public FixViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        FixViewHolder viewHolder = new FixViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FixViewHolder holder, int position) {
       holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        holder.distance.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        holder.name.setGravity(Gravity.CENTER);
        holder.distance.setGravity(Gravity.CENTER);

        holder.name.setText(fList.get(position).getName());
        holder.distance.setText(fList.get(position).getDistance());
    }

    @Override
    public int getItemCount() {
        return (null != fList ? fList.size() :0);
    }

}
