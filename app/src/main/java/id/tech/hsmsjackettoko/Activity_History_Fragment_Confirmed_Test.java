package id.tech.hsmsjackettoko;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.tech.orm_sugar.SLite;

/**
 * Created by macbook on 2/7/16.
 */
public class Activity_History_Fragment_Confirmed_Test extends Fragment {
    private RecyclerView rv, rv_yesterday, rv_else;
    private RecyclerView.LayoutManager layoutManager, layoutManager_yesterday, layoutManager_else;
    private RecyclerView.Adapter adapter, adapter_yesterday, adapter_else;
    private List<RowData_History> data, data_yesterday, data_else;
    private ToggleButton btn_today, btn_yesterday, btn_else;
    Context context;
    private SharedPreferences sh;

    public Activity_History_Fragment_Confirmed_Test() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_rv_history_test, null);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv_yesterday = (RecyclerView) view.findViewById(R.id.rv_yesterday);
        rv_else = (RecyclerView) view.findViewById(R.id.rv_else);

        btn_today = (ToggleButton) view.findViewById(R.id.btn_today);
        btn_yesterday = (ToggleButton) view.findViewById(R.id.btn_yesterday);
        btn_else = (ToggleButton) view.findViewById(R.id.btn_else);

        data = new ArrayList<>();
        data_yesterday = new ArrayList<>();
        data_else = new ArrayList<>();

//        data.add(new RowData_History("1", "SHMGG00092", "10", "1", "13 Feb 2015"));
//        data.add(new RowData_History("2", "SHMGG00092", "12", "1", "13 Feb 2015"));
//        data_yesterday.add(new RowData_History("1", "SHMGG00092", "10", "1", "12 Feb 2015"));
//        data_yesterday.add(new RowData_History("2", "SHMGG00092", "12", "1", "12 Feb 2015"));
//        data_yesterday.add(new RowData_History("3", "SHMGG00092", "250", "1", "12 Feb 2015"));
//        data_yesterday.add(new RowData_History("4", "SHMGG00092", "100", "1", "12 Feb 2015"));
//        data_yesterday.add(new RowData_History("3", "SHMGG00092", "250", "1", "12 Feb 2015"));
//        data_yesterday.add(new RowData_History("4", "SHMGG00092", "100", "1", "12 Feb 2015"));
//        data_else.add(new RowData_History("1", "SHMGG00092", "10", "1", "01 Feb 2015"));
//        data_else.add(new RowData_History("2", "SHMGG00092", "12", "1", "02 Feb 2015"));
//        data_else.add(new RowData_History("3", "SHMGG00092", "250", "1", "03 Feb 2015"));
//        data_else.add(new RowData_History("4", "SHMGG00092", "100", "1", "04 Feb 2015"));
//        data_else.add(new RowData_History("1", "SHMGG00092", "10", "1", "01 Feb 2015"));
//        data_else.add(new RowData_History("2", "SHMGG00092", "12", "1", "02 Feb 2015"));
//        data_else.add(new RowData_History("3", "SHMGG00092", "250", "1", "03 Feb 2015"));
//        data_else.add(new RowData_History("4", "SHMGG00092", "100", "1", "04 Feb 2015"));

        context = getActivity().getApplicationContext();

        sh = context.getSharedPreferences("sh_sms", Context.MODE_PRIVATE);

        SQLiteDatabase db = SLite.openDatabase(context);
//        final Cursor c = db.rawQuery("SELECT * FROM tbl_sms",new String[] {} );
        final Cursor c = db.query("tbl_sms_mandor", new String[]{"trxId", "masonId", "qty", "confirmCode", "dateReceived"}
                , null, null, null, null, "dateReceived DESC", null);

        while (c.moveToNext()) {
//            jika pending maka masukan ke data
            String date_current = c.getString(4);
            String date_sh = sh.getString("tgl_now", "-");

            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
            String date_now = df.format(Calendar.getInstance().getTime());

            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

            Calendar ctoday = Calendar.getInstance();

            Calendar c2 = Calendar.getInstance();
            c2.setTime(new Date(date_current)); // your date

            boolean isYesterday = false;
            boolean isToday = false;
            if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                isYesterday = true;
            }else if(ctoday.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && ctoday.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                isToday = true;
            }
            if (isToday) {
                if (c.getString(3).equals("2")) {
                    data.add(new RowData_History(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
                }
            }else if(isYesterday){
                if (c.getString(3).equals("2")) {
                    data_yesterday.add(new RowData_History(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
                }
            } else{
                if (c.getString(3).equals("2")) {
                    data_else.add(new RowData_History(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
                }
            }


        }

        Log.e("Query Berhasil", "");
        c.close();
        db.close();

        layoutManager = new LinearLayoutManager(context, 1, false);
        adapter = new RecyclerAdapter_History(context, data);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        layoutManager_yesterday = new LinearLayoutManager(context, 1, false);
        adapter_yesterday = new RecyclerAdapter_History(context, data_yesterday);
        rv_yesterday.setLayoutManager(layoutManager_yesterday);
        rv_yesterday.setAdapter(adapter_yesterday);

        layoutManager_else = new LinearLayoutManager(context, 1, false);
        adapter_else = new RecyclerAdapter_History(context, data_else);
        rv_else.setLayoutManager(layoutManager_else);
        rv_else.setAdapter(adapter_else);

        CompoundButton.OnCheckedChangeListener listener_Today = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rv.setVisibility(View.VISIBLE);
                } else {
                    rv.setVisibility(View.GONE);
                }
            }
        };

        CompoundButton.OnCheckedChangeListener listener_Yesterday = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rv_yesterday.setVisibility(View.VISIBLE);
                } else {
                    rv_yesterday.setVisibility(View.GONE);
                }
            }
        };

        CompoundButton.OnCheckedChangeListener listener_Else = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rv_else.setVisibility(View.VISIBLE);
                } else {
                    rv_else.setVisibility(View.GONE);
                }
            }
        };

        btn_today.setOnCheckedChangeListener(listener_Today);
        btn_yesterday.setOnCheckedChangeListener(listener_Yesterday);
        btn_else.setOnCheckedChangeListener(listener_Else);
//        if(btn_today.isChecked()){
//            rv.setVisibility(View.VISIBLE);
//        }else{
//            rv.setVisibility(View.GONE);
//        }
//
//        if(btn_yesterday.isChecked()){
//            rv_yesterday.setVisibility(View.VISIBLE);
//        }else{
//            rv_yesterday.setVisibility(View.GONE);
//        }
//
//        if(btn_else.isChecked()){
//            rv_else.setVisibility(View.VISIBLE);
//        }else{
//            rv_else.setVisibility(View.GONE);
//        }

        return view;
    }
}
