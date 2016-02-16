package id.tech.hsmsjackettoko;

/**
 * Created by RebelCreative-A1 on 15/01/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import id.tech.orm_sugar.SLite;

public class RecyclerAdapter_Home extends RecyclerView.Adapter<RecyclerAdapter_Home.ViewHolder>{
    private Activity activity_adapter;
    private Context context_adapter;

    public RecyclerAdapter_Home(Activity activity_adapter, Context context_adapter) {
        this.activity_adapter = activity_adapter;
        this.context_adapter = context_adapter;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SQLiteDatabase db = SLite.openDatabase(context_adapter);

        final Cursor c = db.query("tbl_sms_inbox", new String[] {"viewed"}
                ,null, null, null, null,null, null);
        int total_inbox_unread = 0;

        while(c.moveToNext()){
//            jika pending maka masukan ke data
            if(c.getString(0).equals("0")){
                total_inbox_unread++;
            }

        }

        final Cursor c2 = db.query("tbl_sms_news", new String[] {"viewed"}
                ,null, null, null, null,null, null);
        int total_new_tidak_terbaca = 0;

        while(c2.moveToNext()){
//            jika pending maka masukan ke data
            if(c2.getString(0).equals("0")){
                total_new_tidak_terbaca = total_new_tidak_terbaca + 1;
            }

        }

        if(position == 0){
            holder.tv_total_unread_sms.setText(String.valueOf(total_inbox_unread));
        }else if(position == 2){
            holder.tv_total_unread_sms.setText(String.valueOf(total_new_tidak_terbaca));
        }else{
            holder.tv_total_unread_sms.setVisibility(View.GONE);
        }

        switch (position){
            case 0:
                holder.img.setImageResource(R.drawable.img_item_inbox);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context_adapter, Activity_Inbox.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context_adapter.startActivity(intent);
                    }
                });
                break;
            case 1:
                holder.img.setImageResource(R.drawable.img_item_history);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context_adapter, Activity_History_Parent.class);
                        Intent intent = new Intent(context_adapter, Activity_History_Tabpager.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context_adapter.startActivity(intent);
                    }
                });
                break;
            case 2:
                holder.img.setImageResource(R.drawable.img_item_news);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context_adapter, Activity_News.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context_adapter.startActivity(intent);
                    }
                });
                break;

        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, null);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView tv_total_unread_sms;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.img);
            tv_total_unread_sms = (TextView)itemView.findViewById(R.id.tv_total_unread_sms);
        }
    }
}
