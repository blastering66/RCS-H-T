package id.tech.hsmsjackettoko;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import id.tech.orm_sugar.SLite;

/**
 * Created by macbook on 2/10/16.
 */
public class Activity_News extends AppCompatActivity {
    RecyclerView rv;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    List<RowData_Sms> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ActionBar ac = getSupportActionBar();
        ac.hide();

        ImageView img_Back = (ImageView)findViewById(R.id.btn_back);
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv = (RecyclerView)findViewById(R.id.rv);

        data = new ArrayList<>();

        SQLiteDatabase db = SLite.openDatabase(getApplicationContext());
        //        final Cursor c = db.rawQuery("SELECT * FROM tbl_sms",new String[] {} );
        final Cursor c = db.query("tbl_sms_news", new String[] {"senderId", "bodyMessage", "dateReceived","viewed", "id"}
                ,null, null, null, null,"dateReceived DESC", null);

        while(c.moveToNext()){
            data.add(new RowData_Sms(c.getString(4), c.getString(0),c.getString(1),c.getString(2), c.getString(3)));
            Log.e("QUERY RESULT = ", c.getString(0) + c.getString(1) + c.getString(2) + c.getString(3));
        }

        Log.e("Query Berhasil", "");
        c.close();
        db.close();

        layoutManager = new LinearLayoutManager(getApplicationContext(), 1, false);
        adapter = new RecyclerAdapter_SMS_News(getApplicationContext(), data);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }


}
