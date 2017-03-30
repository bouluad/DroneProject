package istic.fr.droneproject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import istic.fr.droneproject.model.Intervention;

public class CodisMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_INTERVENTION = "intervention";
    private Intervention intervention;
    private GoogleMap googleMap;
    private String adresseCourante;
    private LatLng positionCourante;
    private Button btnValider;
    private ValiderPositionListener mListener;

    public CodisMapFragment() {
        // Required empty public constructor
    }

    public static CodisMapFragment newInstance(Intervention intervention) {
        CodisMapFragment fragment = new CodisMapFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_INTERVENTION, intervention);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            intervention = getArguments().getParcelable(ARG_INTERVENTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.codis_map_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.codis_map);
        map.getMapAsync(this);


        final EditText adresseInput = (EditText) view.findViewById(R.id.codis_map_adresse);
        adresseInput.requestFocus();

        final Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        Button btnChercher = (Button) view.findViewById(R.id.codis_map_btn_chercher);
        btnChercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (googleMap != null) {
                    adresseCourante = adresseInput.getText().toString();
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(adresseCourante, 1);
                        if (addresses.size() > 0) {
                            Address address = addresses.get(0);
                            double lat = address.getLatitude();
                            double lng = address.getLongitude();

                            LatLng latLng = new LatLng(lat, lng);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            positionCourante = latLng;
                            googleMap.clear();
                            MarkerOptions marker = new MarkerOptions();
                            marker.position(latLng);
                            googleMap.addMarker(marker);
                            btnValider.setEnabled(true);

                        } else {
                            Toast.makeText(getContext(), "Aucun résultat pour cette adresse", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e("CodisMapFragment", e.toString());
                    }
                } else {
                    Toast.makeText(getContext(), "La carte n'est pas prête. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnValider = (Button) view.findViewById(R.id.codis_map_btn_valider);
        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.validerPosition(positionCourante, adresseCourante);
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                positionCourante = latLng;
                googleMap.clear();
                MarkerOptions marker = new MarkerOptions();
                marker.position(latLng);
                googleMap.addMarker(marker);
                btnValider.setEnabled(true);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ValiderPositionListener) {
            mListener = (ValiderPositionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ValiderPositionListener {
        void validerPosition(LatLng position, String adresse);
    }
}
