package com.rd.pageindicatorview.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter2 extends RecyclerView.Adapter<HomeAdapter2.ColoredHolder> {

    private List<Integer> colorList;

    HomeAdapter2() {
        this.colorList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ColoredHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ColoredHolder.newInstance(parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ColoredHolder holder, int position) {
        holder.setColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    void setData(@Nullable List<Integer> list) {
        this.colorList.clear();
        if (list != null && !list.isEmpty()) {
            this.colorList.addAll(list);
        }

        notifyDataSetChanged();
    }

    static class ColoredHolder extends RecyclerView.ViewHolder {
        public ColoredHolder(View view) {
            super(view);
        }

        public void setColor(@ColorRes int color) {
            itemView.setBackgroundColor(itemView.getResources().getColor(color));
        }

        public static ColoredHolder newInstance(Context context) {
            View view = new View(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(params);
            return new ColoredHolder(view);
        }
    }
}