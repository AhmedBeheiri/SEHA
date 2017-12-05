package com.aghazy.healthassistant.model;



public class MainModel {

    private String mainTitle;
    private String subTitle;
    private String imageUri;

    public MainModel(String mainTitle, String subTitle, String imageUri) {

        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
        this.imageUri = imageUri;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getImageUri() {
        return "drawable/" + imageUri;
    }
}
