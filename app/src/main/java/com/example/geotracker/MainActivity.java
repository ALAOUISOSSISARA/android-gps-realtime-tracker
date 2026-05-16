package com.example.geotracker;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_PERMISSION = 101;

    // 🔧 Remplace par l'IP de ton PC (ipconfig dans cmd)
    private static final String URL_ENREGISTRER =
            "http://192.168.1.111/geotrack/enregistrer.php";

    private TextView tvLatitude, tvLongitude, tvStatut;
    private RequestQueue requestQueue;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatitude  = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        tvStatut    = findViewById(R.id.tvStatut);
        Button btnVoirCarte = findViewById(R.id.btnVoirCarte);

        requestQueue    = Volley.newRequestQueue(getApplicationContext());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        btnVoirCarte.setOnClickListener(v ->
                startActivity(new Intent(this, CarteActivity.class)));

        demanderPermissionGps();
    }

    private void demanderPermissionGps() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    CODE_PERMISSION);
        } else {
            demarrerSuiviGps();
        }
    }

    @SuppressLint("MissingPermission")
    private void demarrerSuiviGps() {
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                60000,  // 60 secondes
                150,    // 150 mètres
                new LocationListener() {

                    @Override
                    public void onLocationChanged(@NonNull Location loc) {
                        double lat = loc.getLatitude();
                        double lon = loc.getLongitude();
                        double alt = loc.getAltitude();
                        float  acc = loc.getAccuracy();

                        tvLatitude.setText("Latitude : " + lat);
                        tvLongitude.setText("Longitude : " + lon);
                        tvStatut.setText("Précision : " + acc + " m | Alt : " + alt + " m");

                        Toast.makeText(getApplicationContext(),
                                " Lat: " + lat + " | Lon: " + lon,
                                Toast.LENGTH_LONG).show();

                        envoyerPosition(lat, lon);
                    }

                    @Override
                    public void onProviderEnabled(@NonNull String provider) {
                        Toast.makeText(getApplicationContext(),
                                "GPS activé ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {
                        Toast.makeText(getApplicationContext(),
                                "GPS désactivé ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                }
        );
    }

    private void envoyerPosition(final double lat, final double lon) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_ENREGISTRER,
                response -> tvStatut.setText(" Position envoyée au serveur"),
                error -> Toast.makeText(getApplicationContext(),
                        " Erreur réseau : " + error.getMessage(),
                        Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SimpleDateFormat sdf =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                params.put("latitude",    String.valueOf(lat));
                params.put("longitude",   String.valueOf(lon));
                params.put("captured_at", sdf.format(new Date()));
                params.put("device_id",   obtenirIdentifiantAppareil());
                return params;
            }
        };

        requestQueue.add(request);
    }

    private String obtenirIdentifiantAppareil() {
        String androidId = Settings.Secure.getString(
                getContentResolver(), Settings.Secure.ANDROID_ID);
        if (androidId != null && !androidId.isEmpty()) return androidId;
        return "INCONNU_" + System.currentTimeMillis();
    }

    @Override
    public void onRequestPermissionsResult(int code,
                                           @NonNull String[] perms, @NonNull int[] results) {
        super.onRequestPermissionsResult(code, perms, results);
        if (code == CODE_PERMISSION && results.length > 0
                && results[0] == PackageManager.PERMISSION_GRANTED) {
            demarrerSuiviGps();
        } else {
            Toast.makeText(this,
                    " Permission GPS refusée",
                    Toast.LENGTH_LONG).show();
        }
    }
}