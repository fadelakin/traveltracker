package com.fisheradelakin.traveltracker;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener, MemoryDialogFragment.Listener {

    private static final String TAG = "MainActivity";
    private static final String MEMORY_DIALOG_TAG = "MemoryDialog";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private HashMap<String, Memory> mMemories = new HashMap<>();
    private MemoriesDataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDataSource = new MemoriesDataSource(this);

        addGoogleAPIClient();
    }

    private void addGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setInfoWindowAdapter(new MarkerAdapter(getLayoutInflater(), mMemories));
        List<Memory> memories = mDataSource.getAllMememories();
        Log.d(TAG, "Memories are " + memories);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // use geocoding to get some information about coordinates
        Geocoder geocoder = new Geocoder(this);
        List<Address> matches = null;
        try {
            matches = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address bestMatch = (matches.isEmpty()) ? null : matches.get(0);
        int maxLine = bestMatch.getMaxAddressLineIndex();

        Memory memory = new Memory();
        memory.setCity(bestMatch.getAddressLine(maxLine - 1));
        memory.setCountry(bestMatch.getAddressLine(maxLine));
        memory.setLatitude(latLng.latitude);
        memory.setLongitude(latLng.longitude);

        MemoryDialogFragment.newInstance(memory).show(getFragmentManager(), MEMORY_DIALOG_TAG); // custom dialog


    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Log.d(TAG, "Latitude: " + latitude + ", longitude: " + longitude);
    }

    @Override
    public void onSaveClicked(Memory memory) {
        // add marker for where user clicked
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(memory.getLatitude(), memory.getLongitude())));

        mMemories.put(marker.getId(), memory);
        mDataSource.createMemory(memory);
    }

    @Override
    public void onCancelClicked(Memory memory) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
