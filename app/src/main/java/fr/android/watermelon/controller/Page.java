package fr.android.watermelon.controller;

import com.google.gson.annotations.SerializedName;

public class Page {
    @SerializedName("page") private String page;

    public Page(String page) {
        this.page = page;
    }
}
