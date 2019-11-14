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
import fr.android.watermelon.controller.Card;
import fr.android.watermelon.controller.Pay;
import fr.android.watermelon.controller.PayIns;
import fr.android.watermelon.controller.Wallet;
import fr.android.watermelon.controller.retrofit.DataService;
import fr.android.watermelon.controller.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayinsFragment extends Fragment {

    EditText _walletText;
    @BindView(R.id.payins_amount)
    EditText _amountText;
    @BindView(R.id.payins_send)
    Button _payInBtn;
    @BindView(R.id.payins_listView)
    ListView listView;


    DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);

    private String access_token;
    private int user_id;
    private int wallet_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payins, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());

        access_token = getArguments().getString("access_token");
        user_id = getArguments().getInt("user_id");

        Call<List<PayIns>> setup = service.getPayIns(access_token);
        getPayInsData(setup);
        Call<List<Wallet>> wallet_req = service.getWallets(access_token);
        getWalletData(wallet_req);


        _payInBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Call<PayIns> result = service.postPayIns(access_token, wallet_id/*Integer.parseInt(_walletText.getText().toString())*/,(int)(Double.parseDouble(_amountText.getText().toString())*100));
                postPayInsData(result);
            }
        });
    }

    private void postPayInsData(Call<PayIns> result) {
        result.enqueue(new Callback<PayIns>() {
            @Override
            public void onResponse(Call<PayIns> call, Response<PayIns> response) {
                FragmentTransaction tr = getFragmentManager().beginTransaction();
                PayinsFragment frag = new PayinsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("access_token", access_token);
                bundle.putInt("user_id", user_id);
                frag.setArguments(bundle);
                tr.replace(R.id.fragment_container, frag);
                tr.commit();
            }

            @Override
            public void onFailure(Call<PayIns> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });
    }

    private void getPayInsData(Call<List<PayIns>> result) {
        result.enqueue(new Callback<List<PayIns>>() {
            @Override
            public void onResponse(Call<List<PayIns>> call, Response<List<PayIns>> response) {
                Log.d("PAYINS", call.request().toString());
                Log.d("PAYINS", response.message());
                if (response.body() != null) {
                    Collections.reverse(response.body());
                    final ArrayAdapter<PayIns> adapter = new ArrayAdapter<PayIns>(getActivity(), android.R.layout.simple_list_item_1, response.body());
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<PayIns>> call, Throwable t) {
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
