package fr.android.watermelon.controller;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id") private int id;
    @SerializedName("email") private String email;
    @SerializedName("first_name") private String first_name;
    @SerializedName("last_name") private String last_name;
    @SerializedName("is_admin") private boolean is_admin;

    public User(String first_name, String last_name, String email, boolean is_admin) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.is_admin = is_admin;
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

    public String toString() {
        return "email: " + email + "\n"
                + "first_name: " + first_name + "\n"
                + "last_name: " + last_name + "\n"
                + "is_admin: " + is_admin + "\n";
    }
}
