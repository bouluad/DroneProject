package istic.fr.droneproject.activities;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.MapView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import istic.fr.droneproject.R;

/**
 * Created by bouluad on 22/03/17.
 */
public class VideosActivity extends android.support.v4.app.Fragment {

    private static final String ARG_ID = "idIntervention";
    private ImageView video;
    private Button start;
    private Button pause;

    Handler handler = new Handler();


    private Boolean aStart = true;
    private String idIntervention;
    private Runnable task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.videos_fragment, container, false);


        video = (ImageView) RootView.findViewById(R.id.video_videoview);
        start = (Button) RootView.findViewById(R.id.button_start);
        pause = (Button) RootView.findViewById(R.id.button_pause);
        return RootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            idIntervention = getArguments().getString(ARG_ID);

        }


        task = new Runnable() {
            public void run() {
                Picasso.with(getContext())
                        .load("http://148.60.11.238/projet/"+idIntervention+"/VideoDrone.jpeg")
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .error(R.drawable.image_not_found)
                        .into(video);
                handler.postDelayed(task, 2000);
            }
        };


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.postDelayed(task, 0);

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.removeCallbacks(task);

            }
        });


    }

    public static VideosActivity newInstance(String idIntervention) {
        VideosActivity fragment = new VideosActivity();
        Bundle args = new Bundle();
        args.putString(ARG_ID, idIntervention);
        fragment.setArguments(args);
        return fragment;
    }


}
