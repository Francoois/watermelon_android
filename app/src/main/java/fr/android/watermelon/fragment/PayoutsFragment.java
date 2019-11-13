package fr.android.watermelon.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.android.watermelon.R;
import fr.android.watermelon.controller.PayOuts;
import fr.android.watermelon.controller.Wallet;
import fr.android.watermelon.controller.retrofit.DataService;
import fr.android.watermelon.controller.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayoutsFragment extends Fragment {

    @BindView(R.id.payouts_wallet)
    EditText _walletText;
    @BindView(R.id.payouts_amount)
    EditText _amountText;
    @BindView(R.id.payouts_send)
    Button _payInBtn;
    @BindView(R.id.payouts_listView)
    ListView listView;


    DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);

    private String access_token;
    private int user_id;
    private int wallet_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payouts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());

        access_token = getArguments().getString("access_token");
        user_id = getArguments().getInt("user_id");

        Call<List<PayOuts>> setup = service.getPayOuts(access_token);
        getPayOutsData(setup);
        Call<List<Wallet>> wallet_req = service.getWallets(access_token);
        getWalletData(wallet_req);

        _payInBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Call<PayOuts> result = service.postPayOuts(access_token, wallet_id,(int)(Double.parseDouble(_amountText.getText().toString())*100));
                postPayOutsData(result);
            }
        });
    }

    private void postPayOutsData(Call<PayOuts> result) {
        result.enqueue(new Callback<PayOuts>() {
            @Override
            public void onResponse(Call<PayOuts> call, Response<PayOuts> response) {
                FragmentTransaction tr = getFragmentManager().beginTransaction();
                PayoutsFragment frag = new PayoutsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("access_token", access_token);
                bundle.putInt("user_id", user_id);
                frag.setArguments(bundle);
                tr.replace(R.id.fragment_container, frag);
                tr.commit();
            }

            @Override
            public void onFailure(Call<PayOuts> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });
    }

    private void getPayOutsData(Call<List<PayOuts>> result) {
        result.enqueue(new Callback<List<PayOuts>>() {
            @Override
            public void onResponse(Call<List<PayOuts>> call, Response<List<PayOuts>> response) {
                Log.d("PayOuts", call.request().toString());
                Log.d("PayOuts", response.message());
                if (response.body() != null) {
                    Collections.reverse(response.body());
                    final ArrayAdapter<PayOuts> adapter = new ArrayAdapter<PayOuts>(getActivity(), android.R.layout.simple_list_item_1, response.body());
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<PayOuts>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });
    }

    private void getWalletData(Call<List<Wallet>> result) {
        result.enqueue(new Callback<List<Wallet>>() {
            @Override
            public void onResponse(Call<List<Wallet>> call, Response<List<Wallet>> response) {
                if (response.body() != null) {
                    wallet_id = response.body().get(0).getWalletId();
                }
            }

            @Override
            public void onFailure(Call<List<Wallet>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });
    }

}
