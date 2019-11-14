package fr.android.watermelon.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.android.watermelon.R;
import fr.android.watermelon.controller.Wallet;
import fr.android.watermelon.controller.retrofit.DataService;
import fr.android.watermelon.controller.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Scanner;

public class WalletFragment extends Fragment {

    @Nullable
    @BindView(R.id.balance_wallet)
    TextView _balance_wallet;
    @Nullable
    @BindView(R.id.btc)
    TextView _balance_btc;


    private String access_token;
    private int balance;

    DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());

        access_token = getArguments().getString("access_token");



        Call<List<Wallet>> wallet_list = service.getWallets(access_token);
        getBalance(wallet_list);

    }

    public void getBalance(Call<List<Wallet>> result) {
        result.enqueue(new Callback<List<Wallet>>() {
            @Override
            public void onResponse(Call<List<Wallet>> call, Response<List<Wallet>> response) {
                _balance_wallet.setText(response.body().get(0).getBalance() + " $");
                balance = (response.body().get(0).getBalance()).intValue();
                new MyTask().execute();
            }

            @Override
            public void onFailure(Call<List<Wallet>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });


    }

    public class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Do something like display a progress bar
        }

        @Override
        protected String doInBackground(Void... param) {
            String out = "";

            try {
                out = new Scanner(new URL("https://blockchain.info/tobtc?currency=USD&value="+balance).openStream(), "UTF-8").useDelimiter("\\A").next();
            } catch (Exception e) {

            }
            Log.d("TAAAG", out+"|\n "+ balance);
            return out;
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            _balance_btc.setText(result+" BTC");
            // Do things like hide the progress bar or change a TextView
        }


    }
}
