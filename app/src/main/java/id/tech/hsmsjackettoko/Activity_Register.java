package id.tech.hsmsjackettoko;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by RebelCreative-A1 on 15/01/2016.
 */
public class Activity_Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView img_Back = (ImageView)findViewById(R.id.btn_back);
        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ActionBar ac = getSupportActionBar();
        ac.hide();

    }

}
