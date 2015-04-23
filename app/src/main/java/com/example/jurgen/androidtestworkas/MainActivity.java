package com.example.jurgen.androidtestworkas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

   private List<FootballTeamDaten> list;
    private ListView listView;
    private final String TAG = "TAG";
    private final Uri TEAM_URI = Uri.parse("content://com.example.jurgen.androidworkas/teams");
    private FootballTeamAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);
        adapter= new FootballTeamAdapter(this, initData());
        listView.setAdapter(adapter);

    }

    private List<FootballTeamDaten> initData() {
        int i = 4;
        list = new ArrayList<FootballTeamDaten>();
        list.add(new FootballTeamDaten(R.drawable.arsenal, 1, "Arcenal"));
        list.add(new FootballTeamDaten(R.drawable.chelsea, 2, "Chelsea"));
        list.add(new FootballTeamDaten(R.drawable.juventus, 3, "Juventus"));
        list.add(new FootballTeamDaten(R.drawable.realmadrid, 4, "Real Madrid"));

        Cursor c = getContentResolver().query(TEAM_URI, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                i++;
                int id = c.getInt(c.getColumnIndex("_id"));
                String name = c.getString(c.getColumnIndex("_name"));
                String logo = c.getString(c.getColumnIndex("_logo"));
                list.add(new FootballTeamDaten(id, i, name, logo));

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
                Toast.makeText(this, "Jakob test app", Toast.LENGTH_SHORT).show();
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
        int value=ft.getId();
        if(id<=3){
             String arg0=ft.getName();
             int arg1=ft.getImgId();
             intent.putExtra("id",value);
             intent.putExtra("name",arg0);
             intent.putExtra("logo",arg1);
             startActivity(intent);

        }else{
            String[] arg0={ft.getName(),ft.getPath()};
            intent.putExtra("name",arg0);
            intent.putExtra("id",value);
            startActivity(intent);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(list.get(info.position).getName());
            menu.setHeaderIcon(R.drawable.info);
            menu.add(0, 0, 0, "Delete");
            menu.add(0, 1, 0, "Update");

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

         switch(item.getItemId()){
            case 0:
                if(list.get(info.position).getId()>4){
                int id=list.get(info.position).getImgId();
//                Log.d("TAG","Удалена запись: id список: "+list.get(info.position).getId()+" idDB: "+id +"  "+list.get(info.position).getName());
                list.remove(info.position);
                adapter.notifyDataSetChanged();
                Uri uri = ContentUris.withAppendedId(TEAM_URI,id);
                getContentResolver().delete(uri, null, null);}
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false)
                            .setTitle("ERROR")
                            .setMessage("нельзя удалить єту команду, она нравится автору =)")
                            .setIcon(android.R.drawable.ic_dialog_info);
                    builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //finish();
                        }
                    });
                    builder.create();
                    builder.show();
                }
               break;
            case 1:
//                Log.d("TAG","Позиция елемента "+info.position+" "+list.get(info.position).getName());
                break;
        }
        return true;
    }

    public void addNewTeam(View v){
                Intent intent = new Intent(this,NewTeam.class);
                startActivity(intent);
    }
    public void ubdateListView(View v){
//       for (FootballTeamDaten ft : list) {
//           Log.d("TAG","Позиция елемента imgID: "+ft.getImgId()+" id: "+ft.getId()+" Name: "+ft.getName());
//
//            }

    }
}
