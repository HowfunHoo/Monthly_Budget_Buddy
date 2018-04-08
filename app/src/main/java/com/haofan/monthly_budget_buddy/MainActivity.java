package com.haofan.monthly_budget_buddy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Observable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.amitshekhar.DebugDB;
import com.haofan.monthly_budget_buddy.adapters.ItemsAdapter;
import com.haofan.monthly_budget_buddy.db.AppDatabase;
import com.haofan.monthly_budget_buddy.entities.Item;
import com.haofan.monthly_budget_buddy.entities.MonthlyStatus;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.webkit.WebViewDatabase.getInstance;

public class MainActivity extends AppCompatActivity {

    TextView tv_date, tv_expense, tv_revenue, tv_used, tv_budget;
    Button btn_create;
    ListView lv_history;
    LinearLayout ll_expense, ll_revenue;

    //db
    private AppDatabase db;

    int cur_year, cur_month;

    ItemsAdapter itemsAdapter;

    MonthYearPicker monthYearPicker;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,
                "BudgetBuddy").build();

        //DEBUG
        /************************************************************************************************
         *    Title: amitshekhariitbhu/Android-Debug-Database
         *    Author: amitshekhariitbhu
         *    Date: 2018
         *    Code version: 1.0
         *    Availability: https://github.com/amitshekhariitbhu/Android-Debug-Database
         *
         *************************************************************************************************/
        Log.d("address", DebugDB.getAddressLog());


        Calendar calendar = Calendar.getInstance();
        // Get current year
        cur_year = calendar.get(Calendar.YEAR);
        // Get current month
        cur_month = calendar.get(Calendar.MONTH);
//        // Get current day
//        cur_day = calendar.get(Calendar.DAY_OF_MONTH);

        //Set the text of tv_date as the current date
//        tv_date.setText(cur_year + "/" + cur_month);

        final int[] mid = new int[1];

        sharedPreferences = getSharedPreferences("selected_date", 0);
        tv_date.setText(sharedPreferences.getString("selected_date", cur_year + "/" + cur_month+1));

//        if (sharedPreferences.getString("selected_date", cur_year + "/" + cur_month) != null)

        /************************************************************************************************
         *    Title: developersuru/android-month-year-picker
         *    Author: developersuru
         *    Date: 2014
         *    Code version: 1.0
         *    Availability: https://github.com/developersuru/android-month-year-picker/tree/master/mypDemo
         *
         *************************************************************************************************/
        monthYearPicker = new MonthYearPicker(MainActivity.this);

        monthYearPicker.build(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_date.setText(monthYearPicker.getSelectedYear()+ "/" +monthYearPicker.getSelectedMonthName());

                //save selected date
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_date", tv_date.getText().toString());
                editor.apply();

                //Query results again
                //new thread
                Thread thread = new Thread(){
                    public void run() {
                        final MonthlyStatus m = db.monthlyStatusDao().findByYearAndMonth(tv_date.getText().toString());

                        if (m != null) {
                            mid[0] = m.getMid();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_expense.setText(String.valueOf(m.getMexpense()));
                                    tv_revenue.setText(String.valueOf(m.getMrevenue()));
                                    tv_budget.setText(String.valueOf(m.getMbudget()));
                                    tv_used.setText(String.valueOf(m.getMused()));
                                }
                            });

                        }

                        db.itemDao().findByYearAndMonth(tv_date.getText().toString()).observe(MainActivity.this, new Observer<List<Item>>() {
                            @Override
                            public void onChanged(@Nullable List<Item> items) {

                                //TEST
                                for (int i =0; i<items.size(); i++){
                                    Log.d("item", String.valueOf(items.get(i).getIamount()));
                                    System.out.println("item: " + String.valueOf(items.get(i).getIamount()));
                                }

                                itemsAdapter = new ItemsAdapter(MainActivity.this, items);
                                lv_history.setAdapter(itemsAdapter);

                                double total_expense = 0.0;
                                double total_revenue = 0.0;
                                double budget = 0.0;
                                double used = 0.0;

                                //Calculate monthly expense and monthly revenue
                                for (int i=0; i<items.size(); i++){
                                    if (items.get(i).getIclass().equalsIgnoreCase("expense")){
                                        total_expense += items.get(i).getIamount();
                                    }else {
                                        total_revenue += items.get(i).getIamount();
                                    }
                                }

                                //Display monthly expense and monthly revenue
                                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                tv_expense.setText(decimalFormat.format(total_expense));
                                tv_revenue.setText(decimalFormat.format(total_revenue));

                                //Calculate the percentage of expended budget
                                budget = Double.parseDouble(tv_budget.getText().toString());
                                if (budget!=0){
                                    used = (total_expense - total_revenue)/budget;
                                }else {
                                    used = 0;
                                }

                                NumberFormat numberFormat = NumberFormat.getPercentInstance();
                                numberFormat.setMinimumFractionDigits(0);
                                tv_used.setText(numberFormat.format(used));

                                //Build a monthlyStatus
                                final MonthlyStatus monthlyStatus = new MonthlyStatus();
                                monthlyStatus.setMyear_month(tv_date.getText().toString());
                                monthlyStatus.setMbudget(budget);
                                monthlyStatus.setMexpense(total_expense);
                                monthlyStatus.setMrevenue(total_revenue);
                                monthlyStatus.setMused(used);

                                Thread thread = new Thread(){
                                    public void run() {
                                        //search for a MonthlyStatus of the selected year and month
                                        MonthlyStatus m2 = db.monthlyStatusDao().findByYearAndMonth(tv_date.getText().toString());
                                        if (m2 == null){
                                            //insert a new MonthlyStatus
                                            monthlyStatus.setMbudget(0);
                                            db.monthlyStatusDao().insert(monthlyStatus);
                                        }else {
                                            monthlyStatus.setMid(mid[0]);
                                            //update a new MonthlyStatus
                                            db.monthlyStatusDao().update(monthlyStatus);

                                        }

                                    }
                                };
                                thread.start();

                            }
                        });

                    }
                };
                thread.start();

            }
        }, null);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //show monthYearPicker
                monthYearPicker.show();

            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to CreateItemActivity
                Intent intent = new Intent(MainActivity.this, CreatItemActivity.class);
                intent.putExtra("Year_Month", tv_date.getText().toString());
                startActivity(intent);
            }
        });

        ll_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to CertainItemsActivity
                Intent intent = new Intent(MainActivity.this, CertainItemsActivity.class);
                intent.putExtra("Focus_Tab", 0);
                intent.putExtra("Year_Month", tv_date.getText().toString());
                startActivity(intent);
            }
        });

        ll_revenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to CertainItemsActivity
                Intent intent = new Intent(MainActivity.this, CertainItemsActivity.class);
                intent.putExtra("Focus_Tab", 1);
                intent.putExtra("Year_Month", tv_date.getText().toString());
                startActivity(intent);
            }
        });

        //new thread
        Thread thread = new Thread(){
            public void run() {

                MonthlyStatus m = db.monthlyStatusDao().findByYearAndMonth(tv_date.getText().toString());

                if (m != null) {
                    mid[0] = m.getMid();
                    tv_expense.setText(String.valueOf(m.getMexpense()));
                    tv_revenue.setText(String.valueOf(m.getMrevenue()));
                    tv_budget.setText(String.valueOf(m.getMbudget()));
                    tv_used.setText(String.valueOf(m.getMused()));
                }

                db.itemDao().findByYearAndMonth(tv_date.getText().toString()).observe(MainActivity.this, new Observer<List<Item>>() {
                    @Override
                    public void onChanged(@Nullable List<Item> items) {

                        //TEST
                        for (int i =0; i<items.size(); i++){
                            Log.d("item", String.valueOf(items.get(i).getIamount()));
                            System.out.println("item: " + String.valueOf(items.get(i).getIamount()));
                        }

                        itemsAdapter = new ItemsAdapter(MainActivity.this, items);
                        lv_history.setAdapter(itemsAdapter);

                        double total_expense = 0.0;
                        double total_revenue = 0.0;
                        double budget = 0.0;
                        double used = 0.0;

                        //Calculate monthly expense and monthly revenue
                        for (int i=0; i<items.size(); i++){
                            if (items.get(i).getIclass().equalsIgnoreCase("expense")){
                                total_expense += items.get(i).getIamount();
                            }else {
                                total_revenue += items.get(i).getIamount();
                            }
                        }

                        //Display monthly expense and monthly revenue
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        tv_expense.setText(decimalFormat.format(total_expense));
                        tv_revenue.setText(decimalFormat.format(total_revenue));

                        //Calculate the percentage of expended budget
                        budget = Double.parseDouble(tv_budget.getText().toString());
                        if (budget!=0){
                            used = (total_expense - total_revenue)/budget;
                        }else {
                            used = 0;
                        }


                        NumberFormat numberFormat = NumberFormat.getPercentInstance();
                        numberFormat.setMinimumFractionDigits(0);
                        tv_used.setText(numberFormat.format(used));

                        //Build a monthlyStatus
                        final MonthlyStatus monthlyStatus = new MonthlyStatus();
                        monthlyStatus.setMyear_month(tv_date.getText().toString());
                        monthlyStatus.setMbudget(budget);
                        monthlyStatus.setMexpense(total_expense);
                        monthlyStatus.setMrevenue(total_revenue);
                        monthlyStatus.setMused(used);

                        Thread thread = new Thread(){
                            public void run() {
                                //search for a MonthlyStatus of the selected year and month
                                MonthlyStatus m2 = db.monthlyStatusDao().findByYearAndMonth(tv_date.getText().toString());
                                if (m2 == null){
                                    //insert a new MonthlyStatus
                                    db.monthlyStatusDao().insert(monthlyStatus);
                                }else {
                                    monthlyStatus.setMid(mid[0]);
                                    //update a new MonthlyStatus
                                    db.monthlyStatusDao().update(monthlyStatus);

                                }

                            }
                        };
                        thread.start();

                    }
                });

            }
        };
        thread.start();

        //Clicke event for tv_budget
        tv_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Edit budget: ");
                final EditText editText = new EditText(MainActivity.this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER  | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                builder.setView(editText);

                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_budget.setText(editText.getText().toString());

                        double budget = 0.0;
                        double used = 0.0;

                        //Calculate the percentage of expended budget
                        budget = Double.parseDouble(tv_budget.getText().toString());
                        used = (Double.parseDouble(tv_expense.getText().toString()) - Double.parseDouble(tv_revenue.getText().toString()))/budget;

                        NumberFormat numberFormat = NumberFormat.getPercentInstance();
                        numberFormat.setMinimumFractionDigits(0);
                        tv_used.setText(numberFormat.format(used));

                        //Build a monthlyStatus
                        final MonthlyStatus monthlyStatus = new MonthlyStatus();
                        monthlyStatus.setMyear_month(tv_date.getText().toString());
                        monthlyStatus.setMbudget(budget);
                        monthlyStatus.setMexpense(Double.parseDouble(tv_expense.getText().toString()));
                        monthlyStatus.setMrevenue(Double.parseDouble(tv_revenue.getText().toString()));
                        monthlyStatus.setMused(used);

                        Thread thread = new Thread(){
                            public void run() {

                                //search for a MonthlyStatus of the selected year and month
                                MonthlyStatus m2 = db.monthlyStatusDao().findByYearAndMonth(tv_date.getText().toString());
                                if (m2 == null){
                                    //insert a new MonthlyStatus
                                    db.monthlyStatusDao().insert(monthlyStatus);
                                }else {
                                    monthlyStatus.setMid(mid[0]);
                                    //update a new MonthlyStatus
                                    db.monthlyStatusDao().update(monthlyStatus);
                                }

                            }
                        };
                        thread.start();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

    }

    /**
     * Initialize the views
     */
    private void initView() {

        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_expense = (TextView) findViewById(R.id.tv_expense);
        tv_revenue = (TextView) findViewById(R.id.tv_revenue);
        tv_budget = (TextView) findViewById(R.id.tv_budget);
        tv_used = (TextView) findViewById(R.id.tv_used);
        btn_create = (Button) findViewById(R.id.btn_create);
        lv_history = (ListView) findViewById(R.id.lv_history);
        ll_expense = (LinearLayout)findViewById(R.id.ll_expense);
        ll_revenue = (LinearLayout)findViewById(R.id.ll_revenue);

    }

}

