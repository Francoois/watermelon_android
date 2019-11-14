package fr.android.watermelon.controller;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id") private int id;
    @SerializedName("email") private String email;
    @SerializedName("first_name") private String first_name;
    @SerializedName("last_name") private String last_name;
    @SerializedName("wallet_id") private int wallet_id;

    public User(String first_name, String last_name, String email, Byte is_admin, int wallet_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.wallet_id = wallet_id;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public int getWalletId() {
        return wallet_id;
    }

    public String toString() {
        return "email: " + email + "\n"
                + "first_name: " + first_name + "\n"
                + "last_name: " + last_name + "\n";
    }
}
