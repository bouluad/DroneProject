package istic.fr.droneproject.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import istic.fr.droneproject.R;
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

    private List<Validation> validations;
    private ValidationRecyclerAdapter adapter;
    private Validation tmpValidation;

    public CodisValidationFragment() {

    }

    public static CodisValidationFragment newInstance() {
        return new CodisValidationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedStateInstance) {
        View validationView = inflater.inflate(R.layout.codis_validation, container, false);

        final RecyclerView validationsRecycler = (RecyclerView) validationView.findViewById(R.id.codis_list_validation);
        validationsRecycler.setLayoutManager(new LinearLayoutManager(validationView.getContext()));

        validations = new ArrayList<>();
        ValidationRecyclerAdapter.ValidClickListener validListener = new ValidationRecyclerAdapter.ValidClickListener() {
            @Override
            public void clickValidation(Validation validation) {
                tmpValidation = validation;
                if (validation.vehicule.position == null) {
                    confirmerPopUp(EtatVehicule.PARKING);
                } else {
                    confirmerPopUp(EtatVehicule.ENGAGE);
                }
            }
        };
        ValidationRecyclerAdapter.RefusClickListener refusListener = new ValidationRecyclerAdapter.RefusClickListener() {
            @Override
            public void clickRefus(Validation validation) {
                tmpValidation = validation;
                confirmerPopUp(EtatVehicule.ANNULE);
            }
        };
        adapter = new ValidationRecyclerAdapter(validations, R.layout.codis_validation_item, refusListener, validListener);
        validationsRecycler.setAdapter(adapter);
        chargerValidations();
        return validationView;
    }

    public void chargerValidations() {
        final List<Intervention> interventions = new ArrayList<>();
        InterventionService service = InterventionServiceCentral.getInstance();
        service.getListeInterventions(new Callback<List<Intervention>>() {
            @Override
            public void onResponse(Call<List<Intervention>> call, Response<List<Intervention>> response) {
                interventions.clear();
                interventions.addAll(response.body());
                validations.clear();
                for (Intervention i : interventions) {
                    if (i.vehicules != null) {
                        for (Vehicule v : i.vehicules) {
                            if (v.etat == EtatVehicule.DEMANDE) {
                                validations.add(new Validation(v, i));
                            }
                        }
                    }
                }
                Log.d("CodisValidationFragment", "Nombre de véhicules à valider : " + validations.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Intervention>> call, Throwable t) {
                //DO NOTHING
                Log.e("CodisValidationFragment", t.toString());
            }
        });
    }

    public void confirmerPopUp(final EtatVehicule nouvelEtat) {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getContext());
        String text;
        if (nouvelEtat == EtatVehicule.ANNULE) {
            text = "Confirmer le refus ?";
        } else {
            text = "Confirmer la validation ?";
        }
        confirmBuilder.setMessage(text).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (nouvelEtat == EtatVehicule.ANNULE) {
                    tmpValidation.vehicule.annuler();
                } else if (nouvelEtat == EtatVehicule.ENGAGE) {
                    //Le vehicule a deja une position mais est toujours demande
                    tmpValidation.vehicule.engager(tmpValidation.vehicule.position);
                } else if (nouvelEtat == EtatVehicule.PARKING) {
                    tmpValidation.vehicule.parking();
                }

                InterventionServiceCentral.getInstance().updateIntervention(tmpValidation.intervention, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        chargerValidations();
                        Log.d("TestCodisValidationActi", "Demande traitée");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("TestCodisValidationActi", "modification KO");
                    }
                });
                dialog.cancel();
            }
        }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setCancelable(false).show();
    }
}
