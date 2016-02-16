package id.tech.hsmsjackettoko;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import id.tech.orm_sugar.SLite;

public class Activity_Splashscreen extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        context = this;

        new AsyncTask_LoadData().execute();
    }

    private class AsyncTask_LoadData extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Thread.sleep(3000);

            }catch (Exception e){

            }

            SQLiteDatabase db = SLite.openDatabase(getApplicationContext());
            SLite.process_CreateIfExist_Table_Sms(db);
            db.close();

            Intent notif_service = new Intent(context, StartSMSService.class);
            notif_service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            notif_service.addFlags(Intent.FLAG_RECEIVER_NO_ABORT);
            //add FLAG_RECEIVER_FOREGROUND to force the intent in foreground
            notif_service.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            startService(notif_service);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            startActivity(new Intent(context, Activity_Home.class));
            finish();
        }
    }
}
