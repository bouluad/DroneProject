package istic.fr.droneproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by bouluad on 22/03/17.
 */
public class MoyensActivity extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.moyens_fragment, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.textViewTestMoyens);
        textView.setText("Juste Test");
        return rootView;
    }
}
