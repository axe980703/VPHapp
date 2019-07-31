package com.uzflsoft.vphapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import static com.uzflsoft.vphapp.MainActivity.*;


public class ExListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;
    private int vph_position;

    public ExListAdapter(Context context, int vph_position) {
        this.context = context;
        this.vph_position = vph_position;
        listDataHeader = new LinkedList<>();
        for(String s: tables[vph_position].getElements().keySet())  // maybe not correct order
            this.listDataHeader.add(s);
        this.listHashMap = tables[vph_position].getElements();

    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  inflater.inflate(R.layout.middle_exlistview_group, null);

        }
        TextView lbListHeader = (TextView)convertView.findViewById(R.id.mid_exlist_group);
        lbListHeader.setTypeface(null, Typeface.BOLD);
        lbListHeader.setText(headerTitle);
        return convertView;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.middle_exlistview_item, null);
        }

        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        checkBox.setText(childText);
        final int major_position = getMajorPosition(groupPosition, childPosition);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    tables[vph_position].setBox(major_position, true);
                }
                else {
                    tables[vph_position].setBox(major_position, false);
                }


            }
        });

        checkBox.setChecked(tables[vph_position].getBox(major_position));

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private int getMajorPosition(int group, int child) {
        int sum = 0;
        int table_groups[] = getGroups();
        for(int i = 0; i < group; i++)
            sum += table_groups[i];
        sum += child;
        return sum;
    }


    private int[] getGroups() {
        int sums[] = new int[listHashMap.size()], index = 0;
        List list;
        for(String name: listDataHeader) {
            list = listHashMap.get(name);
            for(int i = 0; i < list.size(); i++)
                sums[index]++;
            index++;
        }
        return sums;
    }


}

