package istic.fr.droneproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import istic.fr.droneproject.adapter.ValidationRecyclerAdapter;
import istic.fr.droneproject.model.EtatVehicule;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Validation;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.InterventionService;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodisValidationFragment extends Fragment {

    List<Validation> validations;
    ValidationRecyclerAdapter adapter;
    EtatVehicule tmpEtat;
    Validation tmpValidation;

    public CodisValidationFragment(){

    }

    public static CodisValidationFragment newInstance(){
        CodisValidationFragment fragment = new CodisValidationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedStateInstance){
        View validationView = inflater.inflate(R.layout.codis_validation, container, false);

        final RecyclerView validationsRecycler = (RecyclerView) validationView.findViewById(R.id.codis_list_validation);
        validationsRecycler.setLayoutManager(new LinearLayoutManager(validationView.getContext()));

        validations = new ArrayList<>();
        ValidationRecyclerAdapter.ValidClickListener validListener = new ValidationRecyclerAdapter.ValidClickListener() {
            @Override
            public void clickValidation(Validation validation) {
                if (validation.vehicule.position == null){
                    tmpEtat = EtatVehicule.PARKING;
                }else{
                    tmpEtat = EtatVehicule.ENGAGE;
                }
                tmpValidation = validation;
                confirmerPopUp(savedStateInstance);
            }
        };
        ValidationRecyclerAdapter.RefusClickListener refusListener = new ValidationRecyclerAdapter.RefusClickListener() {
            @Override
            public void clickRefus(Validation validation) {
                tmpEtat = EtatVehicule.ANNULE;
                tmpValidation = validation;
                confirmerPopUp(savedStateInstance);
            }
        };
        adapter = new ValidationRecyclerAdapter(validations,R.layout.codis_validation_item,refusListener,validListener);
        validationsRecycler.setAdapter(adapter);
        chargerValidations();
        return validationView;
    }

    public void chargerValidations(){
        final List<Intervention> interventions = new ArrayList<>();
        InterventionService service = InterventionServiceCentral.getInstance();
        service.getListeInterventions(new Callback<List<Intervention>>() {
            @Override
            public void onResponse(Call<List<Intervention>> call, Response<List<Intervention>> response) {
                interventions.clear();
                interventions.addAll(response.body());
                validations.clear();
                for (Intervention i:interventions){
                    if (i.vehicules!= null) {
                        for (Vehicule v : i.vehicules) {
                            if (v.etat == EtatVehicule.DEMANDE) {
                                validations.add(new Validation(v, i));
                            }
                        }
                    }
                }
                Log.e("CodisValidationFragment","Nombre de véhicules à valider : "+validations.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Intervention>> call, Throwable t) {
                //DO NOTHING
                Log.e("CodisValidationFragment", t.toString());
            }
        });
    }

    public void confirmerPopUp(Bundle savedInstanceState) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getContext());
        confirmBuilder.setTitle("Confirmation");
        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        final View popupLayout = inflater.inflate(R.layout.codis_validation_popup, null);
        confirmBuilder.setView(popupLayout);
        final AlertDialog alert = confirmBuilder.create();
        TextView confirmerText = (TextView) popupLayout.findViewById(R.id.codis_validation_text);
        if (tmpEtat.equals(EtatVehicule.ANNULE)){
            confirmerText.setText("Refuser ?");
        }else{
            confirmerText.setText("Accepter ?");
        }
        Button confirmerButton = (Button) popupLayout.findViewById(R.id.codis_validation_button);
        confirmerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmpValidation.vehicule.etat = tmpEtat;
                tmpValidation.vehicule.heureEngagement = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());
                InterventionService service = InterventionServiceCentral.getInstance();
                service.updateIntervention(tmpValidation.intervention, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        alert.dismiss();
                        chargerValidations();
                        Log.e("TestCodisValidationActi","modification OK");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("TestCodisValidationActi","modification KO");
                    }
                });
            }
        });
        alert.show();
    }
}
