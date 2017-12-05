package com.aghazy.healthassistant.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.aghazy.healthassistant.R;
import com.aghazy.healthassistant.model.MainModel;


public class MainHolder extends RecyclerView.ViewHolder{

    private TextView mainTitleTextView, subTitleTextView;
    private ImageView mainImageView;

    public MainHolder(View itemView) {
        super(itemView);

        mainTitleTextView = (TextView) itemView.findViewById(R.id.mainTitleTextView);
        subTitleTextView = (TextView) itemView.findViewById(R.id.subTitleTextView);

        mainImageView = (ImageView) itemView.findViewById(R.id.mainImageView);
    }

    public void updateUI(MainModel model) {

        mainTitleTextView.setText(model.getMainTitle());
        subTitleTextView.setText(model.getSubTitle());

        mainImageView.setImageResource(mainImageView.getResources().getIdentifier(model.getImageUri(),
                null, mainImageView.getContext().getPackageName()));
    }
}
