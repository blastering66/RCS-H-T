package id.tech.hsmsjackettoko;

/**
 * Created by RebelCreative-A1 on 07/01/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Dialog_Konfirmasi_Transaksi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar ac = getSupportActionBar();
//        ac.hide();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_konfirmasi_transaksi);

        final String transaction_id = getIntent().getStringExtra("tv_transaction_id");
        final String mason_id = getIntent().getStringExtra("tv_mason_id");
        final String qty = getIntent().getStringExtra("tv_qty");

        Button btn_positive = (Button)findViewById(R.id.btn_positive);
        Button btn_negative = (Button)findViewById(R.id.btn_negative);

        btn_positive.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(getApplicationContext(), Dialog_Input_Transaksi.class);
                intent.putExtra("tv_transaction_id", transaction_id);
                intent.putExtra("tv_mason_id", mason_id);
                intent.putExtra("tv_qty", qty);
                startActivity(intent);
                finish();
            }
        });

        btn_negative.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
