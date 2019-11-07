package fr.android.watermelon;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.widget.EditText;


import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar _toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout _drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(_toolbar);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, _drawer, _toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawer.addDrawerListener(toogle);
        toogle.syncState();

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
        return true;
    }
}
