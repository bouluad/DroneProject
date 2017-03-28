package istic.fr.droneproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by bouluad on 22/03/17.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    private String idIntervention;

    public TabsPagerAdapter(FragmentManager fm, String idIntervention) {
        super(fm);
        this.idIntervention = idIntervention;
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Carte fragment activity
                return MapActivity.newInstance(idIntervention);
            case 1:
                // Album fragment activity
                return AlbumActivity.newInstance(idIntervention);
            case 2:
                // Videos fragment activity
                return new VideosActivity();
            case 3:
                // Moyens fragment activity
                return MoyensActivity.newInstance(idIntervention);
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
                return "Album";
            case 2:
                return "Vidéos";
            case 3:
                return "Moyens";
        }
        return null;
    }

}