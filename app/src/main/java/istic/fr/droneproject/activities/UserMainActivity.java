package istic.fr.droneproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import istic.fr.droneproject.R;
import istic.fr.droneproject.fragment.TabsPagerAdapter;

/**
 * Created by bouluad on 22/03/17.
 */
public class UserMainActivity extends AppCompatActivity implements ActionBar.TabListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private TabsPagerAdapter mSectionsPagerAdapter;
    private static final String ARG_ID = "idIntervention";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String idIntervention;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main_activity);

        Intent intent = getIntent();
        idIntervention = intent.getStringExtra("idIntervention");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), this.idIntervention);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //.setSelectedNavigationItem(position);
                Log.e("UserMainActivity ","position: "+position);
                mSectionsPagerAdapter.refreshTab(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String title = (String) item.getTitle();
        if(title.equals("Rafraiche")){

            Intent refresh= new Intent(this,this.getClass());
            refresh.putExtra(ARG_ID, this.idIntervention);
            startActivity(refresh);


        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        Log.e("UserMainActivity","onTabSelected");

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        Log.e("UserMainActivity","onTabUnselected");

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        Log.e("UserMainActivity","onTabReselected");

    }
}