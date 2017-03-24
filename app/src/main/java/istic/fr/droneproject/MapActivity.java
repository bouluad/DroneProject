package istic.fr.droneproject;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    SupportMapFragment map;
    GoogleMap mGoogleMap;
    Marker myMarker;
    Marker markerChanged;
    ViewGroup view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera

        this.mGoogleMap = googleMap;

        LatLng lng = new LatLng(40.76793169992044, -73.98180484771729);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        myMarker = this.mGoogleMap.addMarker(new MarkerOptions()
                .position(lng)
                .title("I'm here"));

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Log.e("Map", "Map clicked");
                myMarker.remove();
                if (markerChanged != null)
                    markerChanged.remove();
                markerChanged = mGoogleMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title("I'm here now"));

                Log.e("Position Marker", point.toString());

            }
        });
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(lng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mGoogleMap.moveCamera(center);
        mGoogleMap.animateCamera(zoom);


   /* @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(myMarker)) {
            Toast.makeText(this,"position changed",Toast.LENGTH_SHORT).show();
            Intent i=new Intent(MapActivity.this,LoginActivity.class);
            startActivity(i);
        }
        return true;
    }*/
    }
}
