package id.tech.hsmsjackettoko;

/**
 * Created by RebelCreative-A1 on 07/01/2016.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Dialog_Input_Transaksi extends AppCompatActivity {
    private EditText ed_Trans_Id, ed_Qty;
    private String mTransId, mQty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar ac = getSupportActionBar();
//        ac.hide();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_input_transaksi_sack);

        ed_Trans_Id = (EditText) findViewById(R.id.ed_transaksi_id);
        ed_Qty = (EditText) findViewById(R.id.ed_qty);

        final String transaction_id = getIntent().getStringExtra("tv_transaction_id");
        final String mason_id = getIntent().getStringExtra("tv_mason_id");
        final String qty = getIntent().getStringExtra("tv_qty");

        ed_Trans_Id.setText(transaction_id);

        Button btn_positive = (Button) findViewById(R.id.btn_positive);
        Button btn_negative = (Button) findViewById(R.id.btn_negative);

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTransId = ed_Trans_Id.getText().toString();
                mQty = ed_Qty.getText().toString();

                if (mTransId.equals("") || mTransId.isEmpty()) {
                    ed_Trans_Id.setError("Harap di isi Trasaksi Id");
                } else if (mQty.equals("") || mQty.isEmpty()) {
                    ed_Qty.setError("Harap ini kuantitas pembelian");
                } else {
//                    new AsyncTask_Konfirmasi().execute();
                    Toast.makeText(getApplicationContext(), "YA#" + "Retailer Code" + "#" + mason_id + "#" + mQty, Toast.LENGTH_LONG).show();

                }
            }
        });

        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private class AsyncTask_Konfirmasi extends AsyncTask<Void, Void, Boolean> {
        private DialogFragmentProgress pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new DialogFragmentProgress();
            pDialog.show(getSupportFragmentManager(), "");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String message = "YA#" + mTransId + "#" + mQty;
            return Public_Functions.sendSMS(message);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            pDialog.dismiss();
            if (aBoolean) {
                Toast.makeText(getApplicationContext(), "Konfirmasi berhasil dikirim", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);

            } else {
                Toast.makeText(getApplicationContext(), "Konfirmasi gagal, coba lagi", Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED);
            }
            finish();

        }
    }
}
