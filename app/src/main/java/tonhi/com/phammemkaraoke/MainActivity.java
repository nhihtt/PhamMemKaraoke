package tonhi.com.phammemkaraoke;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TabHost;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import tonhi.com.adapter.BaiHatAdapter;
import tonhi.com.model.BaiHat;

public class MainActivity extends AppCompatActivity {
    public static String DATABASE_NAME="Arirang.sqlite";
    public static SQLiteDatabase database=null;
    public static String TableName="ArirangSongList";
    String DB_PATH_SUFFIX="/databases/";
    ListView lvAll;
    BaiHatAdapter adapterAll;
    TabHost tabHost;
    ListView lvLove;
    BaiHatAdapter adapterLove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processCopy();
        setupTabhost();
        addControls();
        hienThiToanBoBaiHat();
        addevents();
    }


    private void addevents() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("tab1")){
                    hienThiToanBoBaiHat();
                }
                else{
                    hienThiBaiHatYeuThich();
                }
            }
        });
    }
    private void hienThiBaiHatYeuThich() {
        Cursor c=database.query(TableName,null,"YEUTHICH=?",new String[]{"1"},null,null,null);
        adapterLove.clear();
        while (c.moveToNext()){
            String ma=c.getString(0);
            String ten=c.getString(1);
            String casi=c.getString(3);
            int thich=c.getInt(5);
            BaiHat bh=new BaiHat(ma,ten,casi,thich);
            adapterAll.add(bh);
        }
        c.close();
    }

    private void setupTabhost() {
        tabHost=findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tab1=tabHost.newTabSpec("tab1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("All");
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2=tabHost.newTabSpec("tab2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("Love");
        tabHost.addTab(tab2);
    }

    private void hienThiToanBoBaiHat() {
        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor c=database.query(TableName,null,null,null,null,null,null);
        adapterAll.clear();
        while (c.moveToNext()){
            String ma=c.getString(0);
            String ten=c.getString(1);
            String casi=c.getString(3);
            int thich=c.getInt(5);
            BaiHat bh=new BaiHat(ma,ten,casi,thich);
            adapterAll.add(bh);
        }
        c.close();
    }

    private void addControls() {
        lvAll=findViewById(R.id.lvAll);
        adapterAll=new BaiHatAdapter(MainActivity.this,R.layout.item);
        lvAll.setAdapter(adapterAll);
        lvLove=findViewById(R.id.lvLove);
        adapterLove=new BaiHatAdapter(MainActivity.this,R.layout.item);
        lvAll.setAdapter(adapterLove);

    }

    private void processCopy(){
        try{
            File dbFile=getDatabasePath(DATABASE_NAME);
            if(!dbFile.exists()){
                copyDatabaseFromAsset();
            }
        }
        catch (Exception ex){
            Log.e("LOI",ex.toString());
        }
    }
    private String getDatabasePath(){
        return getApplicationInfo().dataDir+DB_PATH_SUFFIX+DATABASE_NAME;
    }
    private void copyDatabaseFromAsset() {
        try {
            InputStream myInput=getAssets().open(DATABASE_NAME);;
            String outFileName=getDatabasePath();
            File f=new File(getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            if(!f.exists()){
                f.mkdir();
            }
            OutputStream myOutput=new FileOutputStream(outFileName);
            byte []buffer=new byte[1024];
            int length;
            while ((length=myInput.read(buffer))>0){
                myOutput.write(buffer,0,length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception ex){
            Log.e("LOI",ex.toString());
        }
    }
}
