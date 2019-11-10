package fr.android.watermelon.controller;

public class PayOuts extends Pay {

    public PayOuts(int id, int wallet_id, int amount) {
        super(id, wallet_id, amount);
    }

    @Override
    public String toString() {
        return "-" + amount/100.0 +" $";
    }
}
