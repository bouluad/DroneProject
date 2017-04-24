package istic.fr.droneproject.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import istic.fr.droneproject.R;

/**
 * Created by bouluad on 22/03/17.
 */
public class VideosActivity extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.videos_fragment, container, false);
    }
}
