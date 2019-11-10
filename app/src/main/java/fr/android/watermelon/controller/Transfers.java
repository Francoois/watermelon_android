package fr.android.watermelon.controller;

import com.google.gson.annotations.SerializedName;

public class Transfers {

    @SerializedName("id")
    private int id;
    @SerializedName("debited_wallet_id")
    private int debited_wallet_id;
    @SerializedName("debited_wallet_id")
    private int credited_wallet_id;
    @SerializedName("amount")
    private int amount;

    public Transfers(int debited_wallet_id, int credited_wallet_id, int amount) {
        this.id = id;
        this.debited_wallet_id = debited_wallet_id;
        this.credited_wallet_id = credited_wallet_id;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Debited Wallet: " + debited_wallet_id + "\nCredited Wallet: " + credited_wallet_id + "\nAmount: " +amount;
    }

}
