package com.svs.myprojects.googlemapdirections;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by snehalsutar on 1/28/16.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionsInterface {

    static LatLng CURRENT;// = new LatLng(53.558, 9.927);
    static final LatLng RJT = new LatLng(41.9166844, -88.2753049);
    static final LatLng SUPER8 = new LatLng(41.9185011, -88.2976685);
    private GoogleMap mGoogleMap;
    MapFragment mMapFragment;
    String LOG_TAG = "SVS";
    Context mContext;
    Polyline lineWalking, lineDriving, lineBicycle;
    Polyline line = null;
//    Object mContext;

    private static final NumberFormat nf = new DecimalFormat("##.########");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mContext = MapsActivity.this;
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", 53.558);
        double lng = intent.getDoubleExtra("lng", 9.927);
        CURRENT = new LatLng(lat, lng);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        googleMap.addMarker(new MarkerOptions().position(CURRENT)
                .title("Current Location"));

        googleMap.addMarker(new MarkerOptions()
                .position(SUPER8)
                .title("Super 8 Motel"));

        googleMap.addMarker(new MarkerOptions()
                .position(RJT)
                .title("RJT Office")
                .snippet("RJT Android is cool")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_launcher)));


        // Move the camera instantly to hamburg with a zoom of 15.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(RJT, 15));


        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(RJT)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom 21 is max
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(65)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.addCircle(new CircleOptions()
                .center(RJT)
                .radius(300)
                .strokeColor(Color.rgb(127, 127, 229))
                .fillColor((Color.argb(150, 230, 230, 250))));

        googleMap.addPolyline(new PolylineOptions()
                .color(Color.rgb(127, 127, 229))
                .add(RJT)
                .add(SUPER8));


    }

    private String convertToString(LatLng latlng) {
        return latlng.latitude + "," + latlng.longitude;
    }


    @Override
    public void plotPoints(ArrayList<LatLng> latLngArrayList) {

        PolylineOptions polylineOptions = new PolylineOptions();


        if (latLngArrayList != null) {
            for (int i = 0; i < latLngArrayList.size(); i++) {
                LatLng latLng = latLngArrayList.get(i);
                Log.i(LOG_TAG, "Latitude: " + latLng.latitude + " Lng: " + latLng.longitude);
            }

            // Create polyline options with existing LatLng ArrayList
            polylineOptions.addAll(latLngArrayList);
            polylineOptions
                    .width(10)
                    .color(Color.RED);

            // Adding multiple points in map using polyline and arraylist
            line = mGoogleMap.addPolyline(polylineOptions);

        }

    }

    public void draw_bicycle_path(View view) {
        if (line != null) {
            line.remove();
        }
        GetDirectionsInAsyncTask getDirectionsInAsyncTask = new GetDirectionsInAsyncTask((DirectionsInterface) mContext);
        getDirectionsInAsyncTask.execute(convertToString(RJT), convertToString(SUPER8), TravelModes.BICYCLING);
    }
    public void draw_walking_path(View view) {
        if (line != null) {
            line.remove();
        }
        GetDirectionsInAsyncTask getDirectionsInAsyncTask = new GetDirectionsInAsyncTask((DirectionsInterface) mContext);
        getDirectionsInAsyncTask.execute(convertToString(RJT), convertToString(SUPER8), TravelModes.WALKING);
    }
    public void draw_driving_path(View view) {
        if (line != null) {
            line.remove();
        }
        GetDirectionsInAsyncTask getDirectionsInAsyncTask = new GetDirectionsInAsyncTask((DirectionsInterface) mContext);
        getDirectionsInAsyncTask.execute(convertToString(RJT), convertToString(SUPER8), TravelModes.DRIVING);
    }
}
