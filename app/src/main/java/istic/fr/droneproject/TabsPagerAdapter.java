package istic.fr.droneproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by bouluad on 22/03/17.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Carte fragment activity
                return new CarteActivity();
            case 1:
                // Album fragment activity
                return new AlbumActivity();
            case 2:
                // Videos fragment activity
                return new VideosActivity();
            case 3:
                // Moyens fragment activity
                return new MoyensActivity();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

}