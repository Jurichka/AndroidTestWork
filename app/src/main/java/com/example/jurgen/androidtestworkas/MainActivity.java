package com.example.jurgen.androidtestworkas;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

   private List<FootballTeamDaten> list;
    private ListView listView;
    private final String TAG = "TAG";
    private final Uri TEAM_URI = Uri.parse("content://com.example.jurgen.androidworkas/teams");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);
        //FloatingActionsMenu fab = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        FootballTeamAdapter adapter = new FootballTeamAdapter(this, initData());
        listView.setAdapter(adapter);

    }

    private List<FootballTeamDaten> initData() {
        int i = 4;
        list = new ArrayList<FootballTeamDaten>();
        Cursor c = getContentResolver().query(TEAM_URI, null, null, null, null);
        list.add(new FootballTeamDaten(R.drawable.arsenal, 1, "Arcenal", null));
        list.add(new FootballTeamDaten(R.drawable.chelsea, 2, "Chelsea", null));
        list.add(new FootballTeamDaten(R.drawable.juventus, 3, "Juventus", null));
        list.add(new FootballTeamDaten(R.drawable.realmadrid, 4, "Real Madrid", null));
        if (c.moveToFirst()) {
            do {
                i++;
                String name = c.getString(c.getColumnIndex("_name"));
                String logo = c.getString(c.getColumnIndex("_logo"));
                list.add(new FootballTeamDaten(0, i, name, logo));

            } while (c.moveToNext());
        } else {
            Log.d("TAG", "Пустая таблица!");
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 1, "About");
        menu.add(0, 1, 1, "Exit");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Toast.makeText(this, "Jurichka", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                finish();
                break;
    }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FootballTeamDaten ft = list.get(position);
        Intent intent = new Intent(this,ClubInfo.class);

        if(id<=4){
        String arg0=ft.getName();
        int arg1=ft.getImgId();
            startActivity(intent);
            Log.d("TAG", arg0+arg1);
        }else{
            String[] arg0={ft.getName(),ft.getPath()};
            Log.d("TAG", arg0[0]+" "+arg0[1]);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(list.get(info.position).getName());
            menu.add(0, 0, 0, "Delete");
            menu.add(0, 0, 0, "Update");
        }
//        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public void addNewTeam(View v){
                Intent intent = new Intent(this,NewTeam.class);
                startActivity(intent);
    }

}
