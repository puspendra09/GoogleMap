package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    EditText et1;
    Button b1;
    LatLng l;
    private GoogleMap mMap;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 1);
        et1 = findViewById(R.id.et1);
        b1 = findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder g = new Geocoder(MapsActivity.this);
                try {
                    List<Address> Locations = g.getFromLocationName(et1.getText().toString(), 10);
                    Address address = Locations.get(0);
                    String title = address.getAddressLine(0) + "," + address.getAddressLine(1) + "," + address.getSubLocality() + "," + address.getCountryName();
                    Toast.makeText(MapsActivity.this, title, Toast.LENGTH_SHORT).show();
                    LatLng lng = new LatLng(address.getLatitude(), address.getLongitude());
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(lng, 15);
                    mMap.animateCamera(cu);
                    MarkerOptions mo = new MarkerOptions();
                    mo.title(title);
                    mo.position(lng);
                    mMap.addMarker(mo);
                    Toast.makeText(MapsActivity.this, title, Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 100, this);

    }
    @Override
    public void onLocationChanged(Location location) {
        l=new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions me = new MarkerOptions();
        mMap.addMarker(me);
        Toast.makeText(this, "Your GPS is now On", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.m1)
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if(item.getItemId()==R.id.m2)
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if(item.getItemId()==R.id.m3)
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if(item.getItemId()==R.id.m4)
            if (l==null)
            {
                Toast.makeText(this, "Your GPS is not Working Plz Wait...", Toast.LENGTH_SHORT).show();
            }
        else {
                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(l, 20);
                mMap.animateCamera(cu);
            }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
