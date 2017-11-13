package com.example.frankie.binb;

import android.content.Context;
import android.content.Intent;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by frankie on 08/11/17.
 */

public class Paziente extends AppCompatActivity {
    TextView nome;
    TextView cognome;
    String nome_file;
    String n;
    String c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pazienti);
        nome = (TextView)findViewById(R.id.name);
        cognome = (TextView)findViewById(R.id.surname);

        Button start = (Button)findViewById(R.id.crea_aud);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nome.getText().length()!=0 && cognome.getText().length()!=0) {

                    n = nome.getText().toString();
                    c = cognome.getText().toString();

                    Intent myIntent = new Intent(Paziente.this, Audiogramma.class);
                    myIntent.putExtra("nome", n); //Optional parameters
                    myIntent.putExtra("cognome", c);
                    Paziente.this.startActivity(myIntent);
                }
            }

        });
    }



}
