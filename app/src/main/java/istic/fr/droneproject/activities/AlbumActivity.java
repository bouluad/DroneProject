package istic.fr.droneproject.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.adapter.AlbumPhotoAdapter;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Photo;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bouluad on 22/03/17.
 */
public class AlbumActivity extends Fragment implements OnMapReadyCallback {
    private static final String ARG_ID = "idIntervention";

    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.album_fragment, container, false);
    }

    @Override
    public void onPause() {
        Fragment fragment = (getChildFragmentManager().findFragmentById(R.id.photo_map));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        final FragmentManager fragManager = this.getFragmentManager();
        final Fragment fragment = fragManager.findFragmentById(R.id.photo_map);
        if (fragment != null) {
            fragManager.beginTransaction().remove(fragment).commit();
        }
    }

    public static AlbumActivity newInstance(String idIntervention) {
        AlbumActivity fragment = new AlbumActivity();
        Bundle args = new Bundle();
        args.putString(ARG_ID, idIntervention);
        fragment.setArguments(args);
        return fragment;
    }
}
