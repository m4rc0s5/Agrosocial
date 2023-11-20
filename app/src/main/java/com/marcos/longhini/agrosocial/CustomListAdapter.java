package com.marcos.longhini.agrosocial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private Map<String, List<Timeline>> _listDataChild;

    public CustomListAdapter(Context context, List<String> listDataHeader, Map<String, List<Timeline>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Timeline childItem = (Timeline) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_subgroup, null);
        }

        TextView txtChildName = convertView.findViewById(R.id.txtNome);
        TextView txtChildDt =  convertView.findViewById(R.id.txtDt);
        TextView txtChildMessage =  convertView.findViewById(R.id.txtMensagem);
        ImageView imgChildImage =  convertView.findViewById(R.id.imgFoto);

        txtChildName.setText(childItem.getNome());
        txtChildDt.setText(childItem.getDt());
        txtChildMessage.setText(childItem.getMensagem());
        String img64 = childItem.getImagem64();
        if (!img64.equals("null")) {
            Bitmap bmp = Ferramentas.decodeFromBase64(img64);
            imgChildImage.setImageBitmap(bmp);
        }

        if (childPosition % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#EEFFEE"));
        }

        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return _listDataChild.get(_listDataHeader.get(groupPosition)).size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.txtArea);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

/*
public class CustomListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private List<Timeline> _listDataChild;

    public CustomListAdapter(Context context, List<String> listDataHeader, List<Timeline> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Timeline childItem = (Timeline) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_subgroup, null);
        }

        TextView txtChildName = convertView.findViewById(R.id.txtNome);
        TextView txtChildDt =  convertView.findViewById(R.id.txtDt);
        TextView txtChildMessage =  convertView.findViewById(R.id.txtMensagem);
        ImageView imgChildImage =  convertView.findViewById(R.id.imgFoto);

        txtChildName.setText(childItem.getNome());
        txtChildDt.setText(childItem.getDt());
        txtChildMessage.setText(childItem.getMensagem());
        String img64 = childItem.getImagem64();

        return convertView;
    }

      @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return _listDataChild.get(childPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return _listDataChild.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.txtArea);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

 */