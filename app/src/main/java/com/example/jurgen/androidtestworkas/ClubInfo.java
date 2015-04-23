package com.example.jurgen.androidtestworkas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ClubInfo extends Activity implements AdapterView.OnItemSelectedListener {
    private TextView txtName,FullName,Nickname,Founder,Groundt,Capacity,Owner;
    private EditText etFullName,etNickname,etFounder,etGroundt,etCapacity,etOwner;
    private ImageView img;
    private Cursor c;
    private View view,v;
    private String [] arg;
    private final String TABS_TAG_1="tag1";
    private final String TABS_TAG_2="tag2";
    private final Uri INFO_URI = Uri.parse("content://com.example.jurgen.androidworkas/info");
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_layout);
        view =getLayoutInflater().inflate(R.layout.info_club_tab, null);
        txtName=(TextView)view.findViewById(R.id.txtName);
        img=(ImageView)view.findViewById(R.id.img);

        FullName=(TextView)view.findViewById(R.id.txtFullname2);
        Nickname=(TextView)view.findViewById(R.id.txtNickname2);
        Founder=(TextView)view.findViewById(R.id.txtFounded2);
        Groundt=(TextView)view.findViewById(R.id.txtGround2);
        Capacity=(TextView)view.findViewById(R.id.txtCapacity2);
        Owner=(TextView)view.findViewById(R.id.txtOwner2);



        Intent intent=getIntent();
        if(intent.getIntExtra("id",-1)>4){
            String[]value= intent.getStringArrayExtra("name");
            txtName.setText(value[0]);
            img.setImageBitmap(BitmapFactory.decodeFile(value[1]));
        }else{
            String name=intent.getStringExtra("name");
            int i=intent.getIntExtra("logo", 0);
            txtName.setText(name);
            img.setImageResource(i);
        }

        tabInit();
        getArgDB();
    }
      void tabInit(){
        TabHost tabHost =(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec;

        tabSpec=tabHost.newTabSpec(TABS_TAG_1);
        tabSpec.setIndicator("Info");
        tabSpec.setContent(TabFactory);
        tabHost.addTab(tabSpec);

        tabSpec=tabHost.newTabSpec(TABS_TAG_2);
        tabSpec.setIndicator("Team");
        tabSpec.setContent(TabFactory);
        tabHost.addTab(tabSpec);
    }
       TabHost.TabContentFactory TabFactory = new TabHost.TabContentFactory() {
        @Override
        public View createTabContent(String tag) {
        if(tag==TABS_TAG_1){
            return  view;
        }else {if(tag==TABS_TAG_2){
            TextView tv= new TextView(getApplicationContext());
            tv.setText("Список футболистов где-то здеся =)");
            return tv;
        }}
            return null;
        }
    };
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    //NEW
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                conekt();
                view= getLayoutInflater().inflate(R.layout.add_item_info,null);
                etFullName=(EditText)view.findViewById(R.id.txtFullname2_2);
                etNickname=(EditText)view.findViewById(R.id.txtNickname2_2);
                etFounder=(EditText)view.findViewById(R.id.txtFounded2_2);
                etGroundt=(EditText)view.findViewById(R.id.txtGround2_2);
                etCapacity=(EditText)view.findViewById(R.id.txtCapacity2_2);
                etOwner=(EditText)view.findViewById(R.id.txtOwner2_2);
                if (c.moveToFirst()){
                    etFullName.setText(c.getString(c.getColumnIndex("arg00")));
                    etNickname.setText(c.getString(c.getColumnIndex("arg01")));
                    etFounder.setText(c.getString(c.getColumnIndex("arg02")));
                    etGroundt.setText(c.getString(c.getColumnIndex("arg03")));
                    etCapacity.setText(c.getString(c.getColumnIndex("arg04")));
                    etOwner.setText(c.getString(c.getColumnIndex("arg05")));
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true)
                        .setTitle("Information")
                        .setIcon(android.R.drawable.ic_dialog_info);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datInsert();
                        dialog.dismiss();
                        //finish();
                    }
                }).setView(view);
                builder.create();
                builder.show();

            break;
           }
        return true;
    }
    public void datInsert(){
        ContentValues cv = new ContentValues();
        String[] arg0 = new String[6];
        arg0[0] = etFullName.getText().toString();
        FullName.setText(arg0[0]);
        arg0[1] = etNickname.getText().toString();
        Nickname.setText(arg0[1]);
        arg0[2] = etFounder.getText().toString();
        Founder.setText(arg0[2]);
        arg0[3] = etGroundt.getText().toString();
        Groundt.setText(arg0[3]);
        arg0[4] = etCapacity.getText().toString();
        Capacity.setText(arg0[4]);
        arg0[5] = etOwner.getText().toString();
        Owner.setText(arg0[5]);

        int i = 0;
        cv.put("name", txtName.getText().toString());
        do {
            cv.put("arg0" + i, arg0[i]);
            i++;
        } while (i <= 5);

        if(c.getCount()==0){
              Uri newUri = getContentResolver().insert(INFO_URI, cv);
        }else {
            String arg = txtName.getText().toString();
            String[] st = new String[]{arg};
            int cnt = getContentResolver().update(INFO_URI, cv, "name=?", st);
            Log.d("TAG", "update, count = " + cnt);
        }


    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

private void conekt(){
    String arg = txtName.getText().toString();
    String[] st = new String[]{arg};
    Uri uri = ContentUris.withAppendedId(INFO_URI, 1);
    c = getContentResolver().query(uri, null, "name=?", st, null);
}
    private void getArgDB(){
        try {
            conekt();
            if (c.moveToFirst()) {
                do {
                    int id = c.getInt(c.getColumnIndex("_id"));
                    String name = c.getString(c.getColumnIndex("name"));
                    String arg00 = c.getString(c.getColumnIndex("arg00"));
                    String arg01 = c.getString(c.getColumnIndex("arg01"));
                    String arg02 = c.getString(c.getColumnIndex("arg02"));
                    String arg03 = c.getString(c.getColumnIndex("arg03"));
                    String arg04 = c.getString(c.getColumnIndex("arg04"));
                    String arg05 = c.getString(c.getColumnIndex("arg05"));
                    Log.d("TAG", "ID " + id + " NAME " + name + "  0| " + arg00 + " 1| " + arg01 + "  2| " + arg02 + "  3| " + arg03 + "  4| " + arg04 + "  5| " + arg05 + "   ");
                    FullName.setText(arg00);
                    Nickname.setText(arg01);
                    Founder.setText(arg02);
                    Groundt.setText(arg03);
                    Capacity.setText(arg04);
                    Owner.setText(arg05);
                } while (c.moveToNext());
                } else {
                Log.d("TAG", "Пустая таблица!");
            }
        }catch (NullPointerException e){e.toString();}

    }

}
