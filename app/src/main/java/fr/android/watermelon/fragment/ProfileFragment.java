package fr.android.watermelon.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.android.watermelon.R;
import fr.android.watermelon.controller.User;
import fr.android.watermelon.controller.retrofit.DataService;
import fr.android.watermelon.controller.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);
    public static boolean reloadProfile = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, container);

        _updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Call<User> result = service.putUsers(_firstNameText.getText().toString(), _lastNameText.getText().toString(), _emailText.getText().toString(), _password1Text.getText().toString(), false);
                updateUserData(result);
            }
        });

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    public void updateUserData(Call<User> result) {
        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("ALED","KEY "+call.request());
                Toast.makeText(getActivity(), response.body().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), response.body().getFirstName(), Toast.LENGTH_SHORT).show();
                _firstNameText.setText(response.body().getFirstName());
                _lastNameText.setText(response.body().getLastName());
                _emailText.setText(response.body().getEmail());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t, Toast.LENGTH_SHORT).show();
                //    Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });


    }
}
