package com.haofan.monthly_budget_buddy;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.haofan.monthly_budget_buddy.db.AppDatabase;
import com.haofan.monthly_budget_buddy.entities.Item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreatItemActivity extends AppCompatActivity {

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

    String inote, iclass, isource, iamount;
    Date idate, itime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

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

            //db
            final AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                    AppDatabase.class, "BudgetBuddy").allowMainThreadQueries().build();

            //current datetime
            final Date curDatetime = new Date(System.currentTimeMillis());

            //Get intent
            Intent intent = getActivity().getIntent();
            final String iyear_month = intent.getStringExtra("Year_Month");

            //Creat expense page
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1){
                View rootView = inflater.inflate(R.layout.fragment_expense_tab, container, false);

                final String[] itype = new String[1];
                final String[] isource = new String[1];

                final EditText et_amount = (EditText)rootView.findViewById(R.id.et_amount);
                final EditText et_note = (EditText)rootView.findViewById(R.id.et_note);
                Button btn_create = (Button)rootView.findViewById(R.id.btn_create);


                //Set spinners
                final Spinner sp_type=(Spinner)rootView.findViewById(R.id.sp_type);
                final Spinner sp_src=(Spinner)rootView.findViewById(R.id.sp_src);

                //Adapter for sp_type
                final String[] Items_type = getResources().getStringArray(R.array.exp_type);
                final ArrayAdapter<String> adapter_type=new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, Items_type);
                adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_type.setAdapter(adapter_type);

                //Adapter for sp_src if Education selected
                String[] Items_src_edu = getResources().getStringArray(R.array.exp_src_edu);
                final ArrayAdapter<String> adapter_src_edu=new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, Items_src_edu);
                adapter_src_edu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //Adapter for sp_src if Housing selected
                String[] Items_src_house = getResources().getStringArray(R.array.exp_src_house);
                final ArrayAdapter<String> adapter_src_house = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, Items_src_house);
                adapter_src_house.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //Adapter for sp_src if Food selected
                String[] Items_src_food = getResources().getStringArray(R.array.exp_src_food);
                final ArrayAdapter<String> adapter_src_food=new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, Items_src_food);
                adapter_src_food.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //Adapter for sp_src if Transportation selected
                String[] Items_src_trans = getResources().getStringArray(R.array.exp_src_trans);
                final ArrayAdapter<String> adapter_src_trans=new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, Items_src_trans);
                adapter_src_trans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //Adapter for sp_src if Miscellaneous selected
                String[] Items_src_misc = getResources().getStringArray(R.array.exp_src_misc);
                final ArrayAdapter<String> adapter_src_misc=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Items_src_misc);
                adapter_src_misc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //Adapter for sp_src if Health selected
                String[] Items_src_heal = getResources().getStringArray(R.array.exp_src_heal);
                final ArrayAdapter<String> adapter_src_heal=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Items_src_heal);
                adapter_src_heal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {
                        //Set text size
                        ((TextView) parent.getChildAt(0)).setTextSize(20);

                        //Get selected value
                        itype[0] = parent.getSelectedItem().toString();

                        //When type selected, determine the adapter for sp_src
                        if (pos == 0){
                            sp_src.setAdapter(adapter_src_edu);
                        }else if (pos == 1){
                            sp_src.setAdapter(adapter_src_house);
                        }else if (pos == 2){
                            sp_src.setAdapter(adapter_src_food);
                        }else if (pos == 3){
                            sp_src.setAdapter(adapter_src_trans);
                        }else if (pos == 4){
                            sp_src.setAdapter(adapter_src_misc);
                        }else if (pos == 5){
                            sp_src.setAdapter(adapter_src_heal);
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Another interface callback
                    }
                });

                sp_src.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {
                        //Set text size
                        ((TextView) parent.getChildAt(0)).setTextSize(20);

                        //Get selected value
                        isource[0] = parent.getSelectedItem().toString();

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Another interface callback
                    }
                });

                //Create Button onClickListener
                btn_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Build Item
                        Item item = new Item();
                        item.setIdatetime(curDatetime);
                        item.setIyear_month(iyear_month);
                        item.setInote(et_note.getText().toString());
                        item.setIclass("expense");
                        item.setItype(itype[0]);
                        item.setIsource(isource[0]);
                        if (et_amount.getText().length()>0){
                            item.setIamount(Double.parseDouble(et_amount.getText().toString()));
                        }else {
                            item.setIamount(0.0);
                        }

                        db.itemDao().insert(item);

                        Toast.makeText(getActivity(), "Created successfully!", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);

                    }
                });

                return rootView;

                //Create Revenue page
            }else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2){
                View rootView = inflater.inflate(R.layout.fragment_revenue_tab, container, false);

                final String[] isource = new String[1];

                final EditText et_amount = (EditText)rootView.findViewById(R.id.et_amount);
                final EditText et_note = (EditText)rootView.findViewById(R.id.et_note);
                Button btn_create = (Button)rootView.findViewById(R.id.btn_create);

                //Set spinners
                final Spinner sp_src=(Spinner)rootView.findViewById(R.id.sp_src);

                //Adapter for sp_src
                String[] Items_src = getResources().getStringArray(R.array.rev_src);
                final ArrayAdapter<String> adapter_src = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, Items_src);
                adapter_src.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_src.setAdapter(adapter_src);

                //Spinner Item selected event
                sp_src.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {
                        //Set text size
                        ((TextView) parent.getChildAt(0)).setTextSize(20);

                        //Get selected value
                        isource[0] = parent.getSelectedItem().toString();

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Another interface callback
                    }
                });

                //Create Button onClickListener
                btn_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Build Item
                        Item item = new Item();
                        item.setIdatetime(curDatetime);
                        item.setIyear_month(iyear_month);
                        item.setInote(et_note.getText().toString());
                        item.setIclass("revenue");
                        item.setIsource(isource[0]);
                        if (et_amount.getText().length()>0){
                            item.setIamount(Double.parseDouble(et_amount.getText().toString()));
                        }else {
                            item.setIamount(0.0);
                        }

                        db.itemDao().insert(item);

                        Toast.makeText(getActivity(), "Created successfully!", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);

                    }
                });

                return rootView;

            }else {
                View rootView = inflater.inflate(R.layout.fragment_creat_item, container, false);
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

//            switch (position){
//                case 0:
//                    ExpenseTabFragment expenseTabFragment = new ExpenseTabFragment();
//                    return expenseTabFragment;
//
//
//            }
//
//            return null;

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    public static void ExpenseTabView(View rootView){



    }
}
