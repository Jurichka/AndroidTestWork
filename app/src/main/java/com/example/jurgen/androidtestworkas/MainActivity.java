package com.example.jurgen.androidtestworkas;

import android.content.Context;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private List<FootballTeam> list;
    private ListView listView;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int i = R.drawable.arsenal;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView) findViewById(R.id.listView);
        FootballTeamAdapter adapter = new FootballTeamAdapter(context, initData());
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
