package com.example.jurgen.androidtestworkas;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;


public class NewTeam extends Activity implements View.OnClickListener {
    private ImageView img;
    private EditText txtNameNewTeam,txtURL;
    private Bitmap image;
    private Button btnSave,btnAdd,btnDownload;
    private int newWidth = 200;
    private int newHeight = 200;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath="",fname;
    private final Uri TEAM_URI = Uri.parse("content://com.example.jurgen.androidworkas/teams");
    private final String KEY_LOGO = "_logo";
    private final String KEY_NAME = "_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_team);
        img= (ImageView)findViewById(R.id.imageView);
        btnSave=(Button)findViewById(R.id.button);
        btnDownload=(Button)findViewById(R.id.button2);
        btnAdd=(Button)findViewById(R.id.button3);
        btnSave.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
        txtNameNewTeam=(EditText)findViewById(R.id.txtNameNewTeam);
        txtURL=(EditText)findViewById(R.id.URL);
        image= BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcherr) ;
        image = Bitmap.createScaledBitmap(image, newHeight, newWidth, true);
        img.setImageBitmap(image);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
        case R.id.button3:
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
        break;
            case R.id.button:
                String _name=txtNameNewTeam.getText().toString();

                if(_name.equals("")||selectedImagePath==""){
                    Toast.makeText(this,"Заполните поля",Toast.LENGTH_SHORT).show();
                }else{
                    ContentValues cv = new ContentValues();
                    cv.put(KEY_LOGO,selectedImagePath);
                    cv.put(KEY_NAME,_name);
                    Uri newUri = getContentResolver().insert(TEAM_URI, cv);
                    Toast.makeText(this,"Добавлена новая команда",Toast.LENGTH_SHORT).show();
                    }
                break;
        case R.id.button2:
            String url = txtURL.getText().toString();
            new DownloadImage().execute(url);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                //MEDIA GALLERY
                //путь к картинке и назначение картинки на кнопку
                selectedImagePath = getPath(selectedImageUri);
                image= BitmapFactory.decodeFile(selectedImagePath) ;
                image = Bitmap.createScaledBitmap(image, newHeight, newWidth, true);
                img.setImageBitmap(image);
            }
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }

    @Override
    protected void onDestroy() {
        Cursor c = getContentResolver().query(TEAM_URI, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Log.d("TAG",
                        "ID: " + c.getInt(c.getColumnIndex("_id")) + " Img: "
                                + c.getString(c.getColumnIndex("_logo")) + " Name: "
                                + c.getString(c.getColumnIndex("_name")));

            } while (c.moveToNext());
        } else {
            Log.d("TAG", "Пустая таблица!");
        }
        super.onDestroy();
    }
    class DownloadImage extends AsyncTask<String, String, Void> {
        public int progress=0;

        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(getApplicationContext(), "ВьІполняется",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), "Завершено", Toast.LENGTH_LONG)
                    .show();
            image= BitmapFactory.decodeFile("/mnt/sdcard/A_TEST/"+fname) ;
            image = Bitmap.createScaledBitmap(image, newHeight, newWidth, true);
            selectedImagePath="/mnt/sdcard/A_TEST/"+fname;
            img.setImageBitmap(image);
        }

        protected void onProgressUpdate(String... values) {
            int myNum=0;
            try {
                myNum = java.lang.Integer.parseInt(values[0]);
//                MainActivity.txt.setText(""+myNum+"%");
//                MainActivity.progres.setProgress(myNum);
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }

        }


//        private String setFileName() {
//            Date date = new Date();
//
//            String s = String.valueOf(date.getHours())
//                    + String.valueOf(date.getMinutes())
//                    + String.valueOf(date.getSeconds());
//            return s;
//        }

        protected Void doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);
                String baseName = FilenameUtils.getBaseName(arg0[0]);
                String extension = FilenameUtils.getExtension(arg0[0]);
                Log.d("TAG",baseName+"------"+extension);
			/* making a directory in sdcard */

                String sdCard = Environment.getExternalStorageDirectory()
                        .toString();
                File myDir = new File(sdCard, "A_TEST");

			/* if specified not exist create new */
                if (!myDir.exists()) {
                    myDir.mkdir();

                    Log.w("", "inside mkdir");
                }

			/* checks the file and if it already exist delete */
                fname =baseName+"."+extension;
                File file = new File(myDir, fname);
                if (file.exists())
                    file.delete();

			/* Open a connection */
                URLConnection ucon = url.openConnection();

                InputStream inputStream = null;
                HttpURLConnection httpConn = (HttpURLConnection) ucon;
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = httpConn.getInputStream();
                }

                FileOutputStream fos = new FileOutputStream(file);

                int totalSize = httpConn.getContentLength();
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                long total = 0;
                int lenghtOfFile = ucon.getContentLength();

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    total += bufferLength;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    fos.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    Log.i("progres", "downloadedSize:" + downloadedSize
                            + "totalSize:" + totalSize);
                }

                fos.close();


            } catch (IOException io) {
                io.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
