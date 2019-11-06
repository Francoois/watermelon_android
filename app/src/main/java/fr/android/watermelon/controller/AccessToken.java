package fr.android.watermelon.controller;

import com.google.gson.annotations.SerializedName;

public class AccessToken {

    @SerializedName("access_token") private String access_token;

    public AccessToken(String access_token) {
        this.access_token = access_token;
    }

    public String toString() {
        return "access_token: " + access_token;
    }
}
