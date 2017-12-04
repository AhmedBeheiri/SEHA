package com.aghazy.healthassistant.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.aghazy.healthassistant.R;
import com.aghazy.healthassistant.holders.MainHolder;
import com.aghazy.healthassistant.model.MainModel;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainHolder>{

    private ArrayList<MainModel> mainList;

    public MainAdapter(ArrayList<MainModel> mainList) {
        this.mainList = mainList;
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MainHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_main, parent, false));
    }

    @Override
    public void onBindViewHolder(MainHolder holder, int position) {

        holder.updateUI(mainList.get(position));
    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }
}
