package com.example.myBicycleMap.Adapter;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myBicycleMap.R;
import com.example.myBicycleMap.model.Cafe;

import java.util.ArrayList;

public class CafeAdapter extends RecyclerView.Adapter<CafeAdapter.CafeViewHolder>{
    private ArrayList<Cafe> cList;

    public void addItem(Cafe cafe){
        cList.add(cafe);
        notifyDataSetChanged();
    }

    public class CafeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView name;
        protected TextView distance;

        public CafeViewHolder(View view){
            super(view);
            this.name = view.findViewById(R.id.name_listitem);
            this.distance = view.findViewById(R.id.address_listitem);
        }
    }

    @NonNull
    @Override
    public CafeAdapter.CafeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        CafeViewHolder viewHolder = new CafeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CafeAdapter.CafeViewHolder holder, int position) {
        holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        holder.distance.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        holder.name.setGravity(Gravity.CENTER);
        holder.distance.setGravity(Gravity.CENTER);

        holder.name.setText(cList.get(position).getName());
        holder.distance.setText(cList.get(position).getDistance());

    }

    @Override
    public int getItemCount() {
        return (null != cList ? cList.size() :0);
    }




}
