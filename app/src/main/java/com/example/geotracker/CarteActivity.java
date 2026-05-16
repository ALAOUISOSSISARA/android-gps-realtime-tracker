package com.example.geotracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class CarteActivity extends AppCompatActivity {

    private static final String URL_POINTS =
            "http://10.0.2.2/geotrack/listerPoints.php";

    private MapView mapView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialiser OSMDroid
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_carte);

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        chargerPoints();
    }

    private void chargerPoints() {
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                URL_POINTS,
                null,
                response -> {
                    try {
                        JSONArray points = response.getJSONArray("points");

                        for (int i = 0; i < points.length(); i++) {
                            JSONObject p  = points.getJSONObject(i);
                            double lat    = p.getDouble("latitude");
                            double lon    = p.getDouble("longitude");
                            String date   = p.getString("captured_at");
                            String device = p.getString("device_id");

                            GeoPoint position = new GeoPoint(lat, lon);

                            Marker marker = new Marker(mapView);
                            marker.setPosition(position);
                            marker.setTitle("📍 " + date);
                            marker.setSnippet("Appareil : " + device);
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                            mapView.getOverlays().add(marker);

                            // Centrer sur le dernier point
                            if (i == points.length() - 1) {
                                mapView.getController().setCenter(position);
                            }
                        }

                        mapView.invalidate();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        requestQueue.add(req);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}