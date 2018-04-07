package com.haofan.monthly_budget_buddy;

import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.amitshekhar.DebugDB;
import com.haofan.monthly_budget_buddy.adapters.ItemsAdapter;
import com.haofan.monthly_budget_buddy.db.AppDatabase;
import com.haofan.monthly_budget_buddy.entities.Item;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.webkit.WebViewDatabase.getInstance;

public class MainActivity extends AppCompatActivity {

    TextView tv_date, tv_expense, tv_revenue, tv_budget, tv_used;
    Button btn_create;
    ListView lv_history;

    //db
    private AppDatabase db;

    int cur_year, cur_month, selected_year, selected_month, selected_day;

    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,
                "BudgetBuddy").build();

        //DEBUG
        /**
         * Reference: https://github.com/amitshekhariitbhu/Android-Debug-Database
         */
        Log.d("address", DebugDB.getAddressLog());

        Calendar calendar = Calendar.getInstance();

        // Get current year
        cur_year = calendar.get(Calendar.YEAR);
        // Get current month
        cur_month = calendar.get(Calendar.MONTH);

        tv_date.setText(cur_year + "/" + cur_month);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        tv_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to CertainItemsActivity
                Intent intent = new Intent(MainActivity.this, CertainItemsActivity.class);
                intent.putExtra("Focus_Tab", 0);
                intent.putExtra("Year_Month", tv_date.getText().toString());
                startActivity(intent);
            }
        });

        tv_revenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to CertainItemsActivity
                Intent intent = new Intent(MainActivity.this, CertainItemsActivity.class);
                intent.putExtra("Focus_Tab", 1);
                intent.putExtra("Year_Month", tv_date.getText().toString());
                startActivity(intent);
            }
        });

        db.itemDao().getAll().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable List<Item> items) {

                //TEST
                for (int i =0; i<items.size(); i++){
                    Log.d("item", String.valueOf(items.get(i).getIamount()));
                    System.out.println("item: " + String.valueOf(items.get(i).getIamount()));
                }

                itemsAdapter = new ItemsAdapter(MainActivity.this, items);
                lv_history.setAdapter(itemsAdapter);
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
    }

}

