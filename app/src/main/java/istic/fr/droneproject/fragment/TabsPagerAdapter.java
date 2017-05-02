package istic.fr.droneproject.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import istic.fr.droneproject.activities.AlbumActivity;
import istic.fr.droneproject.activities.MapActivity;
import istic.fr.droneproject.activities.MoyensActivity;
import istic.fr.droneproject.activities.VideosActivity;

/**
 * Created by bouluad on 22/03/17.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    private String idIntervention;

    public TabsPagerAdapter(FragmentManager fm, String idIntervention) {
        super(fm);
        this.idIntervention = idIntervention;
    }
    MapActivity mapActivity = null;
    MoyensActivity moyensActivity = null;
    AlbumActivity albumActivity = null;
    VideosActivity videosActivity = null;
    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Carte fragment activity
                mapActivity = MapActivity.newInstance(idIntervention);
                return mapActivity;
            case 1:

                // Moyens fragment activity
                moyensActivity = MoyensActivity.newInstance(idIntervention);
            return moyensActivity;
            case 2:
                // Album fragment activity
                albumActivity = AlbumActivity.newInstance(idIntervention);
            return albumActivity;
            case 3:
                // Videos fragment activity
                videosActivity = new VideosActivity().newInstance(idIntervention);
            return videosActivity;
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Carte";
            case 1:
                return "Moyens";
            case 2:
                return "Points d'intérêt";
            case 3:
                return "Vidéos";
        }
        return null;
    }

    public void refreshTab(int position) {
        switch (position) {
            case 0:
                mapActivity.SynchroniserIntervention();
                break;
            case 1:
                moyensActivity.chargerIntervention();
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}