package com.haofan.monthly_budget_buddy.adapters;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haofan.monthly_budget_buddy.EditItemActivity;
import com.haofan.monthly_budget_buddy.MainActivity;
import com.haofan.monthly_budget_buddy.R;
import com.haofan.monthly_budget_buddy.entities.Item;

import java.util.List;

public class ItemsAdapter extends BaseAdapter {

    private Context context;
    private List<Item> items;


    public ItemsAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.model_items, parent,false);
        }

        TextView tv_itype = (TextView) convertView.findViewById(R.id.tv_itype);
        TextView tv_isource = (TextView) convertView.findViewById(R.id.tv_isource);
        TextView tv_iamount = (TextView) convertView.findViewById(R.id.tv_iamount);
        TextView tv_connector = (TextView) convertView.findViewById(R.id.tv_connector);

        final Item item = (Item)this.getItem(position);

        //////////
        System.out.println("adapter_items "+item.getIamount());

        if (item.getIclass().equalsIgnoreCase("revenue")){
            tv_itype.setVisibility(View.GONE);
            tv_connector.setVisibility(View.GONE);

            //Set tv_isource wider in dp units
            final float scale = context.getResources().getDisplayMetrics().density;
            int pixels = (int) (800 * scale + 0.5f);
            tv_isource.setWidth(pixels);
        }

        tv_itype.setText(item.getItype());
        tv_isource.setText(item.getIsource());

        if (item.getIclass().equalsIgnoreCase("revenue")){
            tv_iamount.setText("+ " + String.valueOf(item.getIamount()));
            tv_iamount.setTextColor(convertView.getResources().getColor(R.color.colorGreen));
        }else if(item.getIclass().equalsIgnoreCase("expense") ){
            tv_iamount.setText("- " + String.valueOf(item.getIamount()));
            tv_iamount.setTextColor(convertView.getResources().getColor(R.color.colorRed));
        }


        //Click event for each item
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OPEN Update info
                editItem(item);
            }
        });

        return convertView;
    }

    public void editItem(Item item){

        //build Intent to EditItemActivity
        Intent intent = new Intent(context, EditItemActivity.class);
        intent.putExtra("iid", item.getIid());
        intent.putExtra("idatetime", item.getIdatetime().getTime());
        intent.putExtra("iyear_month", item.getIyear_month());
        intent.putExtra("inote", item.getInote());
        intent.putExtra("iclass", item.getIclass());
        intent.putExtra("itype", item.getItype());
        intent.putExtra("isource", item.getIsource());
        intent.putExtra("iamount", item.getIamount());

        context.startActivity(intent);
    }
}
