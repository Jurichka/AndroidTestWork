package com.example.jurgen.androidtestworkas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ClubInfo extends Activity {
    private TextView txtName;
    private ImageView img;
    private View view;
    private final String TABS_TAG_1="tag1";
    private final String TABS_TAG_2="tag2";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_layout);
        view =getLayoutInflater().inflate(R.layout.info_club_tab, null);
        txtName=(TextView)view.findViewById(R.id.txtName);
        img=(ImageView)view.findViewById(R.id.img);
       getDate();
       tabInit();
    }
    private void getDate(){
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
    }
    private void tabInit(){
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
            tv.setText("sfkdhgkfhrthgkdfghk");
            return tv;
        }}
            return null;
        }
    };
}
