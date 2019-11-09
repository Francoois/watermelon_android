package fr.android.watermelon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
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
import fr.android.watermelon.fragment.PayinsFragment;
import fr.android.watermelon.fragment.PayoutsFragment;
import fr.android.watermelon.fragment.ProfileFragment;
import fr.android.watermelon.fragment.TransfersFragment;
import fr.android.watermelon.fragment.WalletFragment;
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

    public static boolean reloadMain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(_toolbar);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, _drawer, _toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawer.addDrawerListener(toogle);
        toogle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment()).commit();
            _navigationView.setCheckedItem(R.id.nav_wallet);
        }

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(_drawer.isDrawerOpen(GravityCompat.START))
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

        if(reloadMain) {
            String access_token = getDefaults("access_token", this);
            Toast.makeText(this, access_token, Toast.LENGTH_SHORT).show();
            Call<List<User>> result = service.getUsers(access_token);
            updateUserData(result);
        }
        reloadMain = false;
    }

    public void updateUserData(Call<List<User>> result) {
        result.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("ALED","KEY "+call.request());
                Toast.makeText(MainActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, response.body().get(0).getFirstName(), Toast.LENGTH_SHORT).show();
                _userName.setText(response.body().get(0).getFirstName());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            //    Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_wallet:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment()).commit();
                break;
            case R.id.nav_payins:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PayinsFragment()).commit();
                break;
            case R.id.nav_payouts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PayoutsFragment()).commit();
                break;
            case R.id.nav_transfers:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TransfersFragment()).commit();
                break;
            case R.id.nav_atm:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ATMFragment()).commit();
                break;
            case R.id.nav_signout:
                Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_delete:
                Toast.makeText(this, "Delete Account", Toast.LENGTH_SHORT).show();
                break;
        }

        _drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
