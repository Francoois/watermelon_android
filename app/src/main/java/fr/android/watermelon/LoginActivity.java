package fr.android.watermelon;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.android.watermelon.atm_map.ATMMapsActivity;
import fr.android.watermelon.controller.AccessToken;
import fr.android.watermelon.controller.retrofit.DataService;
import fr.android.watermelon.controller.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.signin_email)
    EditText _emailText;
    @BindView(R.id.signin_password)
    EditText _passwordText;
    @BindView(R.id.signin_btn)
    Button _signinButton;
    @BindView(R.id.signup_link)
    TextView _signupLink;

    @BindView(R.id.atm_map_tv) TextView _gotoMapText;

    DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportActionBar().hide();


        _signinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Call<AccessToken> result = service.postLogin(_emailText.getText().toString(), _passwordText.getText().toString());
                loginResponse(result);
            }
        });


        _signupLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        _gotoMapText.setOnClickListener(
                (view) -> {
                    Intent intent = new Intent(LoginActivity.this, ATMMapsActivity.class);
                    startActivity(intent);
                }
        );

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void loginResponse(Call<AccessToken> result) {
        result.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.body() == null)
                    Toast.makeText(LoginActivity.this, "Something went wrong...Please try again!", Toast.LENGTH_SHORT).show();
                else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("SUCCESS")
                            .setMessage(response.body().toString())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });
    }


}
