package com.example.jurgen.androidtestworkas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FootballTeamAdapter extends BaseAdapter {
    List<FootballTeam> list;
    private LayoutInflater inflaters;
    public FootballTeamAdapter(Context context,List<FootballTeam> list) {
        this.list=list;
        inflaters = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        FootballTeam ft = getFT(position);
        if (view == null) {
            int v = R.layout.item_layout;
            view = inflaters.inflate(v, parent, false);
        }
        ImageView img = (ImageView) view.findViewById(R.id.imgView);
        TextView text = (TextView)view.findViewById(R.id.textView);

        img.setImageResource(ft.getImgId());
        text.setText(ft.getId()+". "+ft.getName());
        return view;
    }
    private FootballTeam getFT(int position) {
        return (FootballTeam) getItem(position);
    }
}
