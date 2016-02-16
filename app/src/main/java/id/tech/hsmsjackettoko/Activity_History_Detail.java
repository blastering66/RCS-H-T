package id.tech.hsmsjackettoko;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import id.tech.orm_sugar.SLite;

/**
 * Created by macbook on 2/7/16.
 */
public class Activity_History_Detail extends AppCompatActivity {
    TextView tv_transaction_id, tv_mason_id;
    EditText tv_qty;
    Button btn_Valid, btn_InValid, btn_submit;
    EditText ed_Ket;
    String transaction_id, mason_id, qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history_detail);
        ActionBar ac = getSupportActionBar();
        ac.hide();

        ImageView img_Back = (ImageView) findViewById(R.id.btn_back);
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_transaction_id = (TextView) findViewById(R.id.tv_transaction_id);
        tv_mason_id = (TextView) findViewById(R.id.tv_mason_id);
        tv_qty = (EditText) findViewById(R.id.tv_qty);
        btn_Valid = (Button) findViewById(R.id.btn_valid);
        btn_InValid = (Button) findViewById(R.id.btn_tdk_valid);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        ed_Ket = (EditText) findViewById(R.id.ed_ket);

        transaction_id = getIntent().getStringExtra("tv_transaction_id");
        mason_id = getIntent().getStringExtra("tv_mason_id");
        qty = getIntent().getStringExtra("tv_qty");

        tv_transaction_id.setText(transaction_id);
        tv_mason_id.setText(mason_id);
//        tv_qty.setText(qty);

        btn_InValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_History_Detail_Invalid.class);
                intent.putExtra("tv_transaction_id", transaction_id);
                intent.putExtra("tv_mason_id",mason_id);
                startActivityForResult(intent, 112);

            }
        });

        btn_Valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String qty_Valid = tv_qty.getText().toString();

                if (qty_Valid.equals("") || qty_Valid.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Harap Isi Quantity", Toast.LENGTH_LONG).show();

                } else {
                    new AsyncTask_Validasi().execute();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            finish();
        }
    }

    private class AsyncTask_Validasi extends AsyncTask<Void, Void, Boolean> {
        private DialogFragmentProgress pDialog;
        String cQty;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new DialogFragmentProgress();
            pDialog.show(getSupportFragmentManager(), "");
            cQty = tv_qty.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {

            }
            SQLiteDatabase db = SLite.openDatabase(getApplicationContext());
            ContentValues cv = new ContentValues();
            cv.put("qty", cQty);
            db.update("tbl_sms_mandor", cv, "trxId = ?", new String[]{transaction_id});
            db.close();
            String mesage = "YA2#" + transaction_id + "#" + cQty + "#Valid";
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
            finish();
        }
    }

}
