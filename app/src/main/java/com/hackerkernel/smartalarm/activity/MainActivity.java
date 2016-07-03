package com.hackerkernel.smartalarm.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hackerkernel.smartalarm.R;
import com.hackerkernel.smartalarm.adapter.ViewPagerAdapter;
import com.hackerkernel.smartalarm.fragments.AlarmFragment;
import com.hackerkernel.smartalarm.fragments.InstructionFragment;
import com.hackerkernel.smartalarm.fragments.SettingsFragment;
import com.hackerkernel.smartalarm.fragments.StatisticsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private int[] tabIcons = {
            R.drawable.ic_alarm_white,
            R.drawable.ic_statistics_white,
            R.drawable.ic_settings_white,
            R.drawable.ic_instruction_white
    };
    private TabLayout tab;
    //member varaible
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Bind(R.id.nav_view) NavigationView mNaviagtionView;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.app_name));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tab = (TabLayout) findViewById(R.id.tabs);
        if (tab != null) {
            tab.setupWithViewPager(viewPager);
        }

        setupTabIcons();

        initSideMenu();
    }

    private void setupTabIcons() {
        if (tab != null){
            tab.getTabAt(0).setIcon(tabIcons[0]);
            tab.getTabAt(1).setIcon(tabIcons[1]);
            tab.getTabAt(2).setIcon(tabIcons[2]);
            tab.getTabAt(3).setIcon(tabIcons[3]);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(new AlarmFragment(),"Alarm");
        adapter.add(new StatisticsFragment(),"Statistics");
        adapter.add(new SettingsFragment(),"Settings");
        adapter.add(new InstructionFragment(),"Instruction");
        viewPager.setAdapter(adapter);
    }

    /*
     * Method to instanciate Side Drawer
     * */
    private void initSideMenu() {
        //instanciate ActionbarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar, R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        //When drawer menu items are clicked
        mNaviagtionView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){

                }
                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
