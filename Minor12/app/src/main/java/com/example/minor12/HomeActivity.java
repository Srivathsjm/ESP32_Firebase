package com.example.minor12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    Button btnLogout, mapsbutton;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    TextView name, battery, range, latTextView, stationTextView,error;
    String bat, rng, disp_name;
    LocationManager locationManager;
    int i=0;
    double latitude, longitude,dest_lat,dest_log,rang;
    double lat[]={12.92470576172706,13.011208184868835};
    double log[]={77.49857245397703, 77.55505746974616};
    float[] results = new float[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnLogout = findViewById(R.id.logout);
        name = findViewById(R.id.nm);
        battery = findViewById(R.id.bat);
        error=findViewById(R.id.err);
        range = findViewById(R.id.rang);
        mapsbutton = findViewById(R.id.mapsbtn);
        stationTextView = findViewById(R.id.station);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Log.d("UID",uid);
        getLocation();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference name_ref = database.getReference(uid).child("User");
        name_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                disp_name = snapshot.getValue(String.class);
                name.setText(disp_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference batt = database.getReference(uid).child("SOC");
        batt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bat = snapshot.getValue(String.class);
                rang=(double)Integer.valueOf(bat)/(double)100*450;
                battery.setText(bat);
                range.setText(String.valueOf(rang));
                Log.d("range",bat);
                Log.d("range1",String.valueOf(rang));
                System.out.println(rng);
                if(Integer.valueOf(bat)<=20) {
                    range.setTextColor(Color.RED);
                    battery.setTextColor(Color.RED);
                    error.setText("PLEASE RECHARGE AT THE EARLIEST");
                }
                else{
                    range.setTextColor(Color.BLACK);
                    battery.setTextColor(Color.BLACK);
                    error.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference range_ref = database.getReference(uid).child("Range");
        range_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                range.setText(String.valueOf(rang));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intToMain);
            }
        });
        mapsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMain = new Intent(HomeActivity.this, MapsActivity.class);
                intToMain.putExtra("lat",dest_lat);
                intToMain.putExtra("lon",dest_log);
                startActivity(intToMain);
            }
        });
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    float nearest_location(double c_lat,double c_log)
    {
        float temp=999999999;
        for(i=0;i<2;i++){
            Location.distanceBetween(c_lat,c_log,lat[i],log[i],results);
            if(results[0]<temp);
                temp=results[0];
                System.out.println(temp);
                dest_lat=lat[i];
                dest_log=log[i];
        }
        return(temp);
    }

    @Override
    public void onLocationChanged(Location location) {
        //stationTextView.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        Geocoder geocoder;
        List<Address> addresses;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                stationTextView.setText((nearest_location(latitude,longitude)/1000)+" KM");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(HomeActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

}