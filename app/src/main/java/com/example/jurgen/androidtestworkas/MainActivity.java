package com.example.jurgen.androidtestworkas;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private boolean chek;
    private final DBhelper db = new DBhelper(this);
    private final int G1=0;
    private final int G2=1;
    private List<FootballTeam> list;
    private ListView listView;
    private final String TAG="TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        FootballTeamAdapter adapter = new FootballTeamAdapter(this, initData());
        listView.setAdapter(adapter);

    }
    private List<FootballTeam> initData() {
        list = new ArrayList<FootballTeam>();
        list.add(new FootballTeam(R.drawable.arsenal,1,"Arcenal"));
        list.add(new FootballTeam(R.drawable.chelsea,2,"Chelsea"));
        list.add(new FootballTeam(R.drawable.juventus,3,"Juventus"));
        list.add(new FootballTeam(R.drawable.realmadrid,4,"Real Madrid"));
        return list;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            menu.add(G1,0,0,"About");
            menu.add(G1,1,1,"Exit");
            menu.add(G2,2,1,"Add Team");
            menu.add(G2,3,1,"Delete Team");
            menu.add(G2,4,1,"Clear All");

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        menu.setGroupVisible(G2,chek);

        return super.onPrepareOptionsPanel(view, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item2:
                if(item.isChecked()==true){
                   item.setChecked(false);
                   chek=false;
                }else{item.setChecked(true);
                    chek=true; }
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FootballTeam ft=list.get(position);
        String s=String.valueOf(id);
        Log.d(TAG,s);
        Toast.makeText(this,ft.getName(),Toast.LENGTH_SHORT).show();
    }
}
