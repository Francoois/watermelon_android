package fr.android.watermelon.controller;

import com.google.gson.annotations.SerializedName;

public class Pay {

    @SerializedName("id")
    protected int id;
    @SerializedName("wallet_id")
    protected int wallet_id;
    @SerializedName("amount")
    protected int amount;

    public Pay(int id, int wallet_id, int amount) {
        this.id = id;
        this.wallet_id = wallet_id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getWalletId() {
        return  wallet_id;
    }

    public int getAmount() {
        return amount;
    }

}
