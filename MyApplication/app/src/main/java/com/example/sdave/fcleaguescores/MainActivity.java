package com.example.sdave.fcleaguescores;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.sdave.fcleaguescores.SlidingTabs.SlidingTabsColorsFragment;
import com.example.sdave.fcleaguescores.Fragments.FixturesFragment;
import com.example.sdave.fcleaguescores.Fragments.LeagueTableFragment;
import com.example.sdave.myapplication.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
    DBAdapter myDB;

    private static final int RESULT_SETTINGS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle(R.string.app_name);
        // set the content view
        setContentView(R.layout.activity_main);

        //setBehindContentView(R.layout.activity_main);
        //ListView lv = (ListView)findViewById(R.id.listview);
        openDB();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsColorsFragment fragment = new SlidingTabsColorsFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

        if (findViewById(R.id.sliding_menu_frame) == null) {
            setBehindContentView(R.layout.sliding_menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
            // show home as up so we can toggle
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            // add a dummy view
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.sliding_menu_frame, new SlidingListListener())
            .commit();

        SlidingMenu menu;
        menu = getSlidingMenu();
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        //menu.setMenu(R.layout.list);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);
        setSlidingActionBarEnabled(false);
        actionBar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + "1. Bundesliga" + "</font>"));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3f51b5")));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.menu_main, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            getSlidingMenu().toggle();
        }
        else if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
        }
        else if (id == R.id.action_refresh) {
            FixturesFragment f2 = (FixturesFragment) getSupportFragmentManager().findFragmentById(R.id.sample_content_fragment).getChildFragmentManager().getFragments().get(1);
            LeagueTableFragment f3 = (LeagueTableFragment) getSupportFragmentManager().findFragmentById(R.id.sample_content_fragment).getChildFragmentManager().getFragments().get(2);
            f2.refresh();
            f3.refresh();

        }
        return true;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDB = new DBAdapter(this);
        myDB.open();
    }

    private void closeDB() {
        myDB.close();

    }

    public void switchContent(final Fragment fragment, final String title) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.sample_content_fragment, fragment)
                .commit();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                getSlidingMenu().showContent();
            }
        }, 50);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + title.substring(0, title.length()-8) + "</font>"));
    }


}
