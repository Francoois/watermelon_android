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

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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
import java.util.stream.Collectors;

import static java.lang.System.in;

public class WalletFragment extends Fragment {

    @Nullable
    @BindView(R.id.balance_wallet)
    TextView _balance_wallet;
    @Nullable
    @BindView(R.id.btc)
    TextView _balance_btc;


    private String access_token;
    private Double balance;

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
                balance = response.body().get(0).getBalance();
                new MyTask().execute();
            }

            @Override
            public void onFailure(Call<List<Wallet>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });


    } //"https://blockchain.info/tobtc?currency=USD&value="+balance

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
                InputStream in = new URL( "https://blockchain.info/tobtc?currency=USD&value="+balance ).openStream();
             //   System.out.println( IOUtils.toString( in ) );
                return IOUtils.toString( in );
            }catch (Exception e) {

            }
            finally {
                IOUtils.closeQuietly(in);
            }

            return "";
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
