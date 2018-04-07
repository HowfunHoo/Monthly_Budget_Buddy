package com.haofan.monthly_budget_buddy;

import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.haofan.monthly_budget_buddy.adapters.ItemsAdapter;
import com.haofan.monthly_budget_buddy.db.AppDatabase;
import com.haofan.monthly_budget_buddy.entities.DateConverter;
import com.haofan.monthly_budget_buddy.entities.Item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditItemActivity extends AppCompatActivity {

    EditText et_amount, et_note;
    TextView tv_title_type, tv_datetime;
    Spinner sp_type, sp_src;
    Button btn_update, btn_delete;

    //db
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        //GET DATA FROM INTENT
        Intent intent = getIntent();
        final int iid = intent.getIntExtra("iid",0);
        final Date idatetime = new Date(intent.getLongExtra("idatetime", 0));
        final String iyear_month = intent.getStringExtra("iyear_month");
        final String inote = intent.getStringExtra("inote");
        final String iclass = intent.getStringExtra("iclass");
        final String[] itype = {intent.getStringExtra("itype")};
        final String isource = intent.getStringExtra("isource");
        double iamount = intent.getDoubleExtra("iamount",0);

        //LOG TEST
        System.out.println("iid " + String.valueOf(iid));

        //set db
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,
                "BudgetBuddy").allowMainThreadQueries().build();

        et_amount = (EditText) findViewById(R.id.et_amount);
        et_note = (EditText) findViewById(R.id.et_note);
        tv_title_type = (TextView)findViewById(R.id.title_type);
        tv_datetime = (TextView)findViewById(R.id.tv_datetime);
        sp_type = (Spinner)findViewById(R.id.sp_type);
        sp_src = (Spinner)findViewById(R.id.sp_src);
        btn_update = (Button)findViewById(R.id.btn_update);
        btn_delete = (Button)findViewById(R.id.btn_delete);

        //format datetime
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String datetime = simpleDateFormat.format(idatetime);
        tv_datetime.setText(String.format("%s%s", tv_datetime.getText(), datetime));

        et_note.setText(inote);
        et_amount.setText(String.valueOf(iamount));

        final String[] type = new String[1];
        final String[] source = new String[1];

        //If the item is a revenue
        if (iclass.equalsIgnoreCase("revenue")){
            et_amount.setTextColor(getResources().getColor(R.color.colorGreen));
            et_amount.setHintTextColor(getResources().getColor(R.color.colorGreen));
            tv_title_type.setVisibility(View.GONE);
            sp_type.setVisibility(View.GONE);

            //Adapter for sp_src
            String[] Items_src = getResources().getStringArray(R.array.rev_src);
            final ArrayAdapter<String> adapter_src = new ArrayAdapter<String>(EditItemActivity.this,
                    android.R.layout.simple_spinner_item, Items_src);
            adapter_src.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_src.setAdapter(adapter_src);
            sp_src.setSelection(adapter_src.getPosition(isource));

            //Spinner Item selected event
            sp_src.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    //Set text size
                    ((TextView) parent.getChildAt(0)).setTextSize(20);

                    //Get selected value
                    source[0] = parent.getSelectedItem().toString();

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });

            //If the item is an expense
        }else {
            //Adapter for sp_type
            String[] Items_type = getResources().getStringArray(R.array.exp_type);
            final ArrayAdapter<String> adapter_type=new ArrayAdapter<String>(EditItemActivity.this, android.R.layout.simple_spinner_item, Items_type);
            adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_type.setAdapter(adapter_type);
            sp_type.setSelection(adapter_type.getPosition(itype[0]));

            //Adapter for sp_src if Education selected
            String[] Items_src_edu = getResources().getStringArray(R.array.exp_src_edu);
            final ArrayAdapter<String> adapter_src_edu=new ArrayAdapter<String>(EditItemActivity.this,
                    android.R.layout.simple_spinner_item, Items_src_edu);
            adapter_src_edu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Adapter for sp_src if Housing selected
            String[] Items_src_house = getResources().getStringArray(R.array.exp_src_house);
            final ArrayAdapter<String> adapter_src_house = new ArrayAdapter<String>(EditItemActivity.this,
                    android.R.layout.simple_spinner_item, Items_src_house);
            adapter_src_house.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Adapter for sp_src if Food selected
            String[] Items_src_food = getResources().getStringArray(R.array.exp_src_food);
            final ArrayAdapter<String> adapter_src_food=new ArrayAdapter<String>(EditItemActivity.this,
                    android.R.layout.simple_spinner_item, Items_src_food);
            adapter_src_food.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Adapter for sp_src if Transportation selected
            String[] Items_src_trans = getResources().getStringArray(R.array.exp_src_trans);
            final ArrayAdapter<String> adapter_src_trans=new ArrayAdapter<String>(EditItemActivity.this,
                    android.R.layout.simple_spinner_item, Items_src_trans);
            adapter_src_trans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Adapter for sp_src if Miscellaneous selected
            String[] Items_src_misc = getResources().getStringArray(R.array.exp_src_misc);
            final ArrayAdapter<String> adapter_src_misc=new ArrayAdapter<String>(EditItemActivity.this,
                    android.R.layout.simple_spinner_item, Items_src_misc);
            adapter_src_misc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Adapter for sp_src if Health selected
            String[] Items_src_heal = getResources().getStringArray(R.array.exp_src_heal);
            final ArrayAdapter<String> adapter_src_heal=new ArrayAdapter<String>(EditItemActivity.this,
                    android.R.layout.simple_spinner_item, Items_src_heal);
            adapter_src_heal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    //Set text size
                    ((TextView) parent.getChildAt(0)).setTextSize(20);

                    //Get selected value
                    type[0] = parent.getSelectedItem().toString();

                    //When type selected, determine the adapter for sp_src
                    if (pos == 0){
                        sp_src.setAdapter(adapter_src_edu);
                        sp_src.setSelection(adapter_src_edu.getPosition(isource));
                    }else if (pos == 1){
                        sp_src.setAdapter(adapter_src_house);
                        sp_src.setSelection(adapter_src_house.getPosition(isource));
                    }else if (pos == 2){
                        sp_src.setAdapter(adapter_src_food);
                        sp_src.setSelection(adapter_src_food.getPosition(isource));
                    }else if (pos == 3){
                        sp_src.setAdapter(adapter_src_trans);
                        sp_src.setSelection(adapter_src_trans.getPosition(isource));
                    }else if (pos == 4){
                        sp_src.setAdapter(adapter_src_misc);
                        sp_src.setSelection(adapter_src_misc.getPosition(isource));
                    }else if (pos == 5){
                        sp_src.setAdapter(adapter_src_heal);
                        sp_src.setSelection(adapter_src_heal.getPosition(isource));
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
                    source[0] = parent.getSelectedItem().toString();

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });


        }

        //DELETE ITEM
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.itemDao().deleteById(iid);
                Toast.makeText(EditItemActivity.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                Intent i =new Intent(EditItemActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        //Update item
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Item item = new Item();
                item.setIid(iid);
                item.setIdatetime(idatetime);
                item.setIyear_month(iyear_month);
                item.setInote(et_note.getText().toString());
                item.setIclass(iclass);
                item.setItype(type[0]);
                item.setIsource(source[0]);
                item.setIamount(Double.parseDouble(et_amount.getText().toString()));

                db.itemDao().update(item);

                Toast.makeText(EditItemActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                Intent i =new Intent(EditItemActivity.this, MainActivity.class);
                startActivity(i);

            }
        });



    }
}
