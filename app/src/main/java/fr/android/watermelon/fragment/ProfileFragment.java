package fr.android.watermelon.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.android.watermelon.MainActivity;
import fr.android.watermelon.R;
import fr.android.watermelon.controller.User;
import fr.android.watermelon.controller.retrofit.DataService;
import fr.android.watermelon.controller.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static fr.android.watermelon.MainActivity.reloadMain;

public class ProfileFragment extends Fragment {

    @Nullable
    @BindView(R.id.profile_first_name)
    EditText _firstNameText;
    @Nullable
    @BindView(R.id.profile_last_name)
    EditText _lastNameText;
    @Nullable
    @BindView(R.id.profile_email)
    EditText _emailText;
    @Nullable
    @BindView(R.id.profile_password1)
    EditText _password1Text;
    @Nullable
    @BindView(R.id.profile_password2)
    EditText _password2Text;
    @Nullable
    @BindView(R.id.update_btn)
    Button _updateButton;

    private int id;
    private String access_token;

    DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);
    public static boolean reloadProfile = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());

        access_token = getArguments().getString("access_token");

        Call<List<User>> setup = service.getUsers(access_token);
        setupUserData(setup);

        _updateButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Call<User> result = service.putUsers(access_token, id, _firstNameText.getText().toString(), _lastNameText.getText().toString(), _emailText.getText().toString(), _password1Text.getText().toString());
                updateUserData(result);
            }
        });
    }

    public void updateUserData(Call<User> result) {
        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                _firstNameText.setText(response.body().getFirstName());
                _lastNameText.setText(response.body().getLastName());
                _emailText.setText(response.body().getEmail());
                reloadMain = true;
                ((MainActivity) getActivity()).onResume();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });


    }

    public void setupUserData(Call<List<User>> result) {
        result.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                id = response.body().get(0).getId();
                _firstNameText.setText(response.body().get(0).getFirstName());
                _lastNameText.setText(response.body().get(0).getLastName());
                _emailText.setText(response.body().get(0).getEmail());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });


    }

}
