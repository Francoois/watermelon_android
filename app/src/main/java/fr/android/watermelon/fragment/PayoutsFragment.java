package fr.android.watermelon.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import fr.android.watermelon.R;
import fr.android.watermelon.controller.retrofit.DataService;
import fr.android.watermelon.controller.retrofit.RetrofitClient;

public class PayoutsFragment extends Fragment {


    DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);
    public static boolean reloadPayout = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, container);
        return inflater.inflate(R.layout.fragment_payouts, container, false);
    }
}
