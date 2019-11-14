package fr.android.watermelon.controller;

import com.google.gson.annotations.SerializedName;

public class Wallet {

    @SerializedName("wallet_id")
    private int wallet_id;
    @SerializedName("balance")
    private int balance;

    public Wallet(int wallet_id, int balance) {
        this.wallet_id = wallet_id;
        this.balance = balance;
    }

    public Double getBalance() {
        return balance/100.0;
    }

    public int getWalletId() {
        return wallet_id;
    }
}
