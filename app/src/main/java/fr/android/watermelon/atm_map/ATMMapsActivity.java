package fr.android.watermelon.atm_map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import fr.android.watermelon.R;
//import android.support.design.widget.Snackbar;


// LocationListener : Interface avec les fonctions pour gérer les MAJ de position
public class ATMMapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Marker lastPositionMarker;

    // Provides access to the system location services https://developer.android.com/reference/kotlin/android/location/LocationManager
    private LocationManager locationManager = null;
    private String provider;

    //private boolean geoLocPermit = false;
    private boolean geoLocRequest = false;

    private final int PERMISSION_REQUEST_LOC = 0;
    private final int GPS_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // GEOLOC : ETAPE 1/3 : Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(hasGeoLocPermission()){
            @SuppressLint("MissingPermission") Location loc = locationManager.getLastKnownLocation(provider);
            updateCurrentLocation(loc);
        }
    }

    private boolean hasGeoLocPermission(){
        return (
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Si la permission a été donnée par le USER
        if ( hasGeoLocPermission() ) {

            // XXX : si le GPS est desactivé, on lance un intent implicit pour demander à l'utilisateur de l'activer. Tout ça on peut le supprimer, pas important
            // if GPS is not enabled, ask the use to enable it
            if (!locationManager.isProviderEnabled(provider)) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                // Answer in onActivityResult
                startActivityForResult(intent, GPS_REQUEST_CODE);
            } else
                requestLocationUpdates(); //GEOLOC : ETAPE 2/3 : s'inscrire

            return;
        }

        if (!geoLocRequest) {// if it ever resumes and we've already requested the permits, we won'textView ask twice
            geoLocRequest = true;

            // ask for permissions; this is asynchronous https://developer.android.com/reference/android/support/v4/app/ActivityCompat#requestpermissions
            // Continues in `onRequestPermissionsResult`
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOC);
        }

    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();

        if (hasGeoLocPermission())
            locationManager.removeUpdates(this);

        return;
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        Toast.makeText(this, "Request location updates", Toast.LENGTH_LONG).show();
        locationManager.requestLocationUpdates(provider, 10, 1, this); // GEOLOC : ETAPE 2/3 : je m'inscris => this
    }

    @SuppressLint("MissingPermission")
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == GPS_REQUEST_CODE && resultCode == 0){

            if(provider != null){
                Log.v("GPS", " Location providers: "+provider);
                //Start searching for location and update the location text when update available.
                requestLocationUpdates();
            }else{
                finish();
            }
        }
    }

    // Traiter les réponses aux demandes de permission
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.println(Log.DEBUG, "TAGG", "Got PERMISSION REQUEST RESULT !!!");

        if (requestCode == PERMISSION_REQUEST_LOC) {
            // Request for geolocation permit
            if (grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Request location updates
                Snackbar.make(getWindow().getDecorView(), R.string.loc_permitted,
                        Snackbar.LENGTH_SHORT)
                        .show();
                //geoLocPermit = true;
                requestLocationUpdates();

            } else
                // Permission request was denied. quit the activity
                finish();
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    /**
     * Called when the location has changed.
     *
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {

        Toast.makeText(this, "Location update", Toast.LENGTH_LONG);
        updateCurrentLocation(location);
    }

    private void updateCurrentLocation(Location location){
        if(lastPositionMarker!=null) lastPositionMarker.remove();
        if (locationManager != null && hasGeoLocPermission() && location != null) {

            LatLng coord = new LatLng(location.getLatitude(), location.getLongitude());

            lastPositionMarker = mMap.addMarker(new MarkerOptions().position(coord).title("Last position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));
        }
    }

    /**
     * This callback will never be invoked and providers can be considers as always in the
     * {@link LocationProvider#AVAILABLE} state.
     *
     * @param provider
     * @param status
     * @param extras
     * @deprecated This callback will never be invoked.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // DEPRECATED
    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
