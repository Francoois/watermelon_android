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
import fr.android.watermelon.controller.Transfers;
import fr.android.watermelon.controller.User;
import fr.android.watermelon.controller.Wallet;
import fr.android.watermelon.controller.retrofit.DataService;
import fr.android.watermelon.controller.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransfersFragment extends Fragment {

    @BindView(R.id.transfers_wallet)
    EditText _emailText;
    @BindView(R.id.transfers_amount)
    EditText _amountText;
    @BindView(R.id.transfers_send)
    Button _transferBtn;
    @BindView(R.id.transfers_listView)
    ListView listView;


    DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);

    private String access_token;
    private int user_id;
    private int debited_wallet_id;
    private int credited_wallet_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transfers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());

        access_token = getArguments().getString("access_token");
        user_id = getArguments().getInt("user_id");

        Call<List<Transfers>> setup = service.getTransfers(access_token);
        getTransfersData(setup);
        Call<List<Wallet>> wallet_req = service.getWallets(access_token);
        debited_wallet_id = getWalletData(wallet_req);

       // Call<List<User>> credited_user = service.ge

        _transferBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Call<Transfers> result = service.postTransfers(access_token, debited_wallet_id, credited_wallet_id,(int)(Double.parseDouble(_amountText.getText().toString())*100));
                postTransfersData(result);
            }
        });
    }

    private void postTransfersData(Call<Transfers> result) {
        result.enqueue(new Callback<Transfers>() {
            @Override
            public void onResponse(Call<Transfers> call, Response<Transfers> response) {
                FragmentTransaction tr = getFragmentManager().beginTransaction();
                TransfersFragment frag = new TransfersFragment();
                Bundle bundle = new Bundle();
                bundle.putString("access_token", access_token);
                bundle.putInt("user_id", user_id);
                frag.setArguments(bundle);
                tr.replace(R.id.fragment_container, frag);
                tr.commit();
            }

            @Override
            public void onFailure(Call<Transfers> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });
    }

    private void getTransfersData(Call<List<Transfers>> result) {
        result.enqueue(new Callback<List<Transfers>>() {
            @Override
            public void onResponse(Call<List<Transfers>> call, Response<List<Transfers>> response) {
                if (response.body() != null) {
                    Collections.reverse(response.body());
                    final ArrayAdapter<Transfers> adapter = new ArrayAdapter<Transfers>(getActivity(), android.R.layout.simple_list_item_1, response.body());
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Transfers>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });
    }

    private int getWalletData(Call<List<Wallet>> result) {
        int[] wallet_id = {-1};
        result.enqueue(new Callback<List<Wallet>>() {
            @Override
            public void onResponse(Call<List<Wallet>> call, Response<List<Wallet>> response) {
                if (response.body() != null) {
                    wallet_id[0] = response.body().get(0).getWalletId();
                }
            }

            @Override
            public void onFailure(Call<List<Wallet>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });

        return wallet_id[0];
    }

    private void getWalletDataByEmail(Call<List<User>> result) {
        result.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body() != null) {
                   // debited_wallet_id = response.body().get(0).getWalletId();

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });
    }

}
