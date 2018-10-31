package com.example.yoshizumiapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TownNameListAdapter extends ArrayAdapter<TownData> {
    private int mResource;
    private List<TownData> list;
    private LayoutInflater inflater;

    public TownNameListAdapter(Context context, int resource, List<TownData> items){
        super(context,resource, items);

        this.mResource = resource;
        this.list = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;

        if(convertView != null){
            view = convertView;
        }else{
            view = inflater.inflate(mResource,null);
        }

        TownData td = list.get(position);

        TextView text = (TextView)view.findViewById(R.id.list_text);
        text.setText(String.valueOf(position) + td.getPrefecture() + td.getCityName());

        return view;
    }

    public void removeListAllData(){
        list.clear();
    }
}
