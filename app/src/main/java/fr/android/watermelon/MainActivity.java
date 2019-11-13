package fr.android.watermelon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.android.watermelon.controller.User;
import fr.android.watermelon.controller.retrofit.DataService;
import fr.android.watermelon.controller.retrofit.RetrofitClient;
import fr.android.watermelon.fragment.ATMFragment;
import fr.android.watermelon.fragment.CardsFragment;
import fr.android.watermelon.fragment.PayinsFragment;
import fr.android.watermelon.fragment.PayoutsFragment;
import fr.android.watermelon.fragment.ProfileFragment;
import fr.android.watermelon.fragment.TransfersFragment;
import fr.android.watermelon.fragment.WalletFragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar _toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout _drawer;
    @Nullable
    @BindView(R.id.userText)
    TextView _userName;
    @Nullable
    @BindView(R.id.nav_view)
    NavigationView _navigationView;

    DataService service = RetrofitClient.getRetrofitInstance().create(DataService.class);
    private String access_token;
    private int id;
    private Bundle savedInstanceState;

    public static boolean reloadMain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(_toolbar);
        this.savedInstanceState = savedInstanceState;

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, _drawer, _toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawer.addDrawerListener(toogle);
        toogle.syncState();

        setDefaults("access_token", null, this);

        if (savedInstanceState == null && access_token != null) {
         //   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            setWalletView();
        }

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (_drawer.isDrawerOpen(GravityCompat.START))
            _drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        _navigationView.setNavigationItemSelectedListener(this);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (reloadMain) {
            access_token = getDefaults("access_token", this);
            Call<List<User>> result = service.getUsers(access_token);
            updateUserData(result);
            setWalletView();
        }
        reloadMain = false;
    }

    public void updateUserData(Call<List<User>> result) {
        result.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("ALED", "KEY " + call.request());
                _userName.setText(response.body().get(0).getFirstName());
                id = response.body().get(0).getId();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });


    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Bundle bundle = new Bundle();
        bundle.putString("access_token", access_token);
        bundle.putInt("user_id", id);
        Fragment fragObj;

        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                fragObj = new ProfileFragment();
                fragObj.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragObj).commit();
                break;
            case R.id.nav_wallet:
                fragObj = new WalletFragment();
                fragObj.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragObj).commit();
                break;
            case R.id.nav_cards:
                fragObj = new CardsFragment();
                fragObj.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragObj).commit();
                break;
            case R.id.nav_payins:
                fragObj = new PayinsFragment();
                fragObj.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragObj).commit();
                break;
            case R.id.nav_payouts:
                fragObj = new PayoutsFragment();
                fragObj.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragObj).commit();
                break;
            case R.id.nav_transfers:
                fragObj = new TransfersFragment();
                fragObj.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragObj).commit();
                break;
            case R.id.nav_atm:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ATMFragment()).commit();
                break;
            case R.id.nav_signout:
                signOut();
                break;
            case R.id.nav_delete:
                new AlertDialog.Builder(this)
                        .setTitle("WARNING")
                        .setMessage(R.string.delete_msg)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Call<ResponseBody> r = service.deleteUsers(access_token, id);
                                readResponseBody(r);
                                signOut();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                break;
        }

        _drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean signOut() {
        access_token = null;
        setDefaults("acces_token", null, this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        return true;
    }

    public void readResponseBody(Call<ResponseBody> result) {
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.d("THROW", t + "");
            }
        });


    }

    public boolean setWalletView() {
        WalletFragment fragObj = new WalletFragment();
        Bundle bundle = new Bundle();
        bundle.putString("access_token", access_token);
        bundle.putInt("user_id", id);
        fragObj.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragObj).commit();
        _navigationView.setCheckedItem(R.id.nav_wallet);
        return true;
    }

}
