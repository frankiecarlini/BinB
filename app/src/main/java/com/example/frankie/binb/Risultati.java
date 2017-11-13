package com.example.frankie.binb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import static com.example.frankie.binb.MainActivity.LinearToDecibel;

/**
 * Created by frankie on 09/11/17.
 */

public class Risultati extends AppCompatActivity {
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_res);
        tableLayout=(TableLayout)findViewById(R.id.tableLayout);
        ImageButton home = (ImageButton)findViewById(R.id.res_home);
        ImageButton bb = (ImageButton)findViewById(R.id.res_bb);
        Intent myIntent = getIntent(); //recuperare l'intent
        final float [] r= myIntent.getFloatArrayExtra("results");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Risultati.this, MainActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                Risultati.this.startActivity(myIntent);
            }

        });

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Risultati.this, BinauralB.class);
                myIntent.putExtra("results", r); //Optional parameters
                Risultati.this.startActivity(myIntent);
            }

        });



        menuRes(r);

    }

    public void menuRes(float[]r){
        String[]ris= new String[r.length];
        int freq = 125;
        String orecchio = "destro";
        for (int i=0;i<r.length;i++){
            ris[i] = Float.toString(LinearToDecibel(r[i])) ;

            if(i==4 || i==8 || i==12 || i==16)
                freq=freq*2;
            if(i%2==0)
                orecchio="destro";
            if(i%2!=0)
                orecchio="sinistro";

            View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item,null,false);
            TextView test  = (TextView) tableRow.findViewById(R.id.res_test);
            TextView frequenza  = (TextView) tableRow.findViewById(R.id.res_freq);
            TextView ear  = (TextView) tableRow.findViewById(R.id.res_ear);
            TextView db  = (TextView) tableRow.findViewById(R.id.res_dB);

            test.setText("" + (i + 1));
            frequenza.setText(freq+"hz");
            ear.setText(orecchio);
            db.setText(ris[i]);
            tableLayout.addView(tableRow);

        }

    }
}
