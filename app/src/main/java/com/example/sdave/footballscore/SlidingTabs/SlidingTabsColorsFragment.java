/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.sdave.footballscore.SlidingTabs;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sdave.footballscore.Objects.League;
import com.example.sdave.footballscore.Fragments.FixturesFragment;
import com.example.sdave.footballscore.Fragments.LeagueTableFragment;
import com.example.sdave.footballscore.Fragments.TeamsFragment;
import com.example.sdave.footballscore.R;

import java.util.ArrayList;
import java.util.List;

public class SlidingTabsColorsFragment extends Fragment {
    League league = null;

    static class SamplePagerItem {
        private final CharSequence mTitle;
        private final int mIndicatorColor;
        private final int mDividerColor;

        SamplePagerItem(CharSequence title, int indicatorColor, int dividerColor) {
            mTitle = title;
            mIndicatorColor = indicatorColor;
            mDividerColor = dividerColor;
        }

        Fragment createTeamsFragment(League league) {
            return TeamsFragment.newInstance(league);
        }

        Fragment createFixturesFragment(League league) {
            return FixturesFragment.newInstance(league);
        }

        Fragment createLeaguetableFragment(League league) {
            return LeagueTableFragment.newInstance(league);
        }

        CharSequence getTitle() {
            return mTitle;
        }

        int getIndicatorColor() {
            return mIndicatorColor;
        }

        int getDividerColor() {
            return mDividerColor;
        }
    }

    static final String LOG_TAG = "SlidingTabsColorsFragment";

    private SlidingTabLayout mSlidingTabLayout;

    private ViewPager mViewPager;

    private List<SamplePagerItem> mTabs = new ArrayList<SamplePagerItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if(args != null){
            league = (League)getArguments().getSerializable("league");
        }

        mTabs.add(new SamplePagerItem(
                "Teams", // Title
                Color.WHITE, // Indicator color
                Color.TRANSPARENT // Divider color
        ));

        mTabs.add(new SamplePagerItem(
                "Fixtures",
                Color.WHITE,
                Color.TRANSPARENT
        ));

        mTabs.add(new SamplePagerItem(
                "Standings",
                Color.WHITE,
                Color.TRANSPARENT
        ));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(getChildFragmentManager()));

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.primary));

        mSlidingTabLayout.setViewPager(mViewPager);

        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }

            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }

        });
    }

    class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    //Fragement for Teams Tab
                    return mTabs.get(i).createTeamsFragment(league);
                case 1:
                    //Fragment for Fixtures Tab
                    return mTabs.get(i).createFixturesFragment(league);
                case 2:
                    //Fragment for League Table Tab
                    return mTabs.get(i).createLeaguetableFragment(league);
            }
            return null;
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).getTitle();
        }

    }

}