package fr.android.watermelon.controller;

public class PayIns extends Pay {

    public PayIns(int id, int wallet_id, int amount) {
        super(id, wallet_id, amount);
    }

    @Override
    public String toString() {
        return "+" + amount/100.0 +" $";
    }
}
