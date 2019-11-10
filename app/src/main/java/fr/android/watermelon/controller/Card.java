package fr.android.watermelon.controller;

import com.google.gson.annotations.SerializedName;


public class Card {

    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("last_4")
    private String last_4;
    @SerializedName("brand")
    private String brand;
    @SerializedName("expired_at")
    private String expired_at;

    public Card(int id, int user_id, String last_4, String brand, String expired_at) {
        this.id = id;
        this.user_id = user_id;
        this.last_4 = last_4;
        this.brand = brand;
        this.expired_at = expired_at;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return user_id;
    }

    public String getLast4() {
        return last_4;
    }

    public String getBrand() {
        return brand;
    }

    public String getExpiredAt() {
        return expired_at;
    }

    @Override
    public String toString() {
        return "Brand: " + brand + "\nCard Number: ************" + last_4 + "\nValid Thru: " + expired_at;
    }

}
