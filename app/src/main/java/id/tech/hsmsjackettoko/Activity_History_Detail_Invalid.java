package id.tech.hsmsjackettoko;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import id.tech.orm_sugar.SLite;

/**
 * Created by macbook on 2/15/16.
 */
public class Activity_History_Detail_Invalid extends AppCompatActivity {
    TextView tv_transaction_id, tv_mason_id;
    RadioButton radio_Sama, radio_Palsu;
    Button btn_submit;
    String transaction_id, mason_id, qty, status_kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history_detail_invalid);
        ActionBar ac = getSupportActionBar();
        ac.hide();

        qty = "0";

        ImageView img_Back = (ImageView) findViewById(R.id.btn_back);
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        transaction_id = getIntent().getStringExtra("tv_transaction_id");
        mason_id = getIntent().getStringExtra("tv_mason_id");

        tv_transaction_id = (TextView) findViewById(R.id.tv_transaction_id);
        tv_mason_id = (TextView) findViewById(R.id.tv_mason_id);
        radio_Sama = (RadioButton)findViewById(R.id.radio_sama);
        radio_Palsu = (RadioButton)findViewById(R.id.radio_palsu);
        btn_submit = (Button) findViewById(R.id.btn_submit);


        tv_transaction_id.setText(transaction_id);
        tv_mason_id.setText(mason_id);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cek pilihan jenis Invalidnya
                if(radio_Sama.isChecked()){
                    status_kategori = "Transaksi Sama";
                }else{
                    status_kategori = "Transaksi Palsu";
                }
                //eksekusi validasi sms
                new AsyncTask_Validasi().execute();
            }
        });

    }

    private class AsyncTask_Validasi extends AsyncTask<Void, Void, Boolean> {
        private DialogFragmentProgress pDialog;
        String cQty;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new DialogFragmentProgress();
            pDialog.show(getSupportFragmentManager(), "");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {

            }

            //Open Database lokal
            //set Confirm code 3 (INVALID) lalu update di db
            SQLiteDatabase db = SLite.openDatabase(getApplicationContext());
            ContentValues cv = new ContentValues();
            cv.put("confirmCode", "3");
            db.update("tbl_sms_mandor", cv, "trxId = ?", new String[]{transaction_id});
            db.close();

            //kirim sms invalid dengan formatnya
            String mesage = "YA2#" + transaction_id + "#" +  qty + "#" +status_kategori;
            Log.e("Invalid", mesage);
            return Public_Functions.sendSMS(mesage);
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            pDialog.dismiss();
            if (b) {
                Toast.makeText(getApplicationContext(), "Validasi akan kami proses.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Claim Gagal, Coba lagi nanti", Toast.LENGTH_LONG).show();
            }
            setResult(RESULT_OK);
            finish();
        }
    }
}
