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
import android.widget.Spinner;
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
import fr.android.watermelon.controller.retrofit.DataService;
import fr.android.watermelon.controller.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardsFragment extends Fragment {

    @BindView(R.id.spinner_brand)
    Spinner _brandSpinner;
    @BindView(R.id.last_4)
    EditText _last4;
    @BindView(R.id.expired_at)
    EditText _expiredAtText;
    @BindView(R.id.card_send)
    Button _cardBtn;
    @BindView(R.id.cards_listView)
    ListView listView;

    DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);

    private String access_token;
    private int user_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());

        access_token = getArguments().getString("access_token");
        user_id = getArguments().getInt("user_id");

        Call<List<Card>> setup = service.getCards(access_token);
        getCardsData(setup);

        _cardBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String last4 = _last4.getText().toString().substring(_last4.getText().toString().length() - 4, _last4.getText().toString().length());
                Call<Card> result = service.postCards(access_token, user_id, last4, _brandSpinner.getSelectedItem().toString(), _expiredAtText.getText().toString());
                postCardsData(result);
            }
        });
    }

    private void postCardsData(Call<Card> result) {
        result.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                FragmentTransaction tr = getFragmentManager().beginTransaction();
                CardsFragment frag = new CardsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("access_token", access_token);
                bundle.putInt("user_id", user_id);
                frag.setArguments(bundle);
                tr.replace(R.id.fragment_container, frag);
                tr.commit();
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });
    }

    private void getCardsData(Call<List<Card>> result) {
        result.enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {

                if (response.body() != null) {
                    Collections.reverse(response.body());
                    final ArrayAdapter<Card> adapter = new ArrayAdapter<Card>(getActivity(), android.R.layout.simple_list_item_1, response.body());
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });


    }


}
