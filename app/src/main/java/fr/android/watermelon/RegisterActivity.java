package fr.android.watermelon;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import fr.android.watermelon.controller.User;
import fr.android.watermelon.controller.retrofit.DataService;
import fr.android.watermelon.controller.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_first_name)
    EditText _firstNameText;
    @BindView(R.id.register_last_name)
    EditText _lastNameText;
    @BindView(R.id.register_email)
    EditText _emailText;
    @BindView(R.id.register_password1)
    EditText _password1Text;
    @BindView(R.id.register_password2)
    EditText _password2Text;
    @BindView(R.id.register_admin)
    Switch _adminSwitch;
    @BindView(R.id.signin_btn)
    Button _registerButton;

    DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        _registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Call<User> result = service.postUsers(_firstNameText.getText().toString(), _lastNameText.getText().toString(), _emailText.getText().toString(), _password1Text.getText().toString(), _adminSwitch.isChecked());
                usersResponse(result);
            }
        });
    }

    public void usersResponse(Call<User> result) {
        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("SUCCESS")
                        .setMessage("POST: " + response.body().toString())
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });
    }
}
