package com.haofan.monthly_budget_buddy;

import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import com.haofan.monthly_budget_buddy.adapters.ItemsAdapter;
import com.haofan.monthly_budget_buddy.db.AppDatabase;
import com.haofan.monthly_budget_buddy.entities.Item;

import java.util.List;

public class CertainItemsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certain_items);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        //get intent
        Intent intent = CertainItemsActivity.this.getIntent();

        //Set certain tab
        mViewPager.setCurrentItem(intent.getIntExtra("Focus_Tab", 0));

        //Set title as selected date
        toolbar.setTitle(intent.getStringExtra("Year_Month"));


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            //Get intent
            Intent intent = getActivity().getIntent();
            final String iyear_month = intent.getStringExtra("Year_Month");

            AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class,
                    "BudgetBuddy").build();

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                View rootView = inflater.inflate(R.layout.fragment_view_expenses_tab, container, false);

                final ListView lv_history = (ListView) rootView.findViewById(R.id.lv_history);

                db.itemDao().getAllExpenses(iyear_month).observe(this, new Observer<List<Item>>() {
                    @Override
                    public void onChanged(@Nullable List<Item> items) {

                        ItemsAdapter itemsAdapter = new ItemsAdapter(getActivity(), items);
                        lv_history.setAdapter(itemsAdapter);
                    }
                });

                return rootView;

            }else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2){
                View rootView = inflater.inflate(R.layout.fragment_view_revenues_tab, container, false);

                final ListView lv_history = (ListView) rootView.findViewById(R.id.lv_history);

                db.itemDao().getAllRevenues(iyear_month).observe(this, new Observer<List<Item>>() {
                    @Override
                    public void onChanged(@Nullable List<Item> items) {

                        ItemsAdapter itemsAdapter = new ItemsAdapter(getActivity(), items);
                        lv_history.setAdapter(itemsAdapter);
                    }
                });

                return rootView;

            }else{
                View rootView = inflater.inflate(R.layout.fragment_certain_items, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }

        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
