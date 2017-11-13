package com.example.frankie.binb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static com.example.frankie.binb.MainActivity.LinearToDecibel;
import static com.example.frankie.binb.MainActivity.readValue;


public class Paziente2 extends AppCompatActivity{
    TextView nome;
    TextView cognome;


    float[] loadRes = new float[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pazienti2);
        nome = (TextView)findViewById(R.id.name);
        cognome = (TextView)findViewById(R.id.surname);

        Button read = (Button)findViewById(R.id.read);

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nome.getText().length()!=0 && cognome.getText().length()!=0) {
                    String n = nome.getText().toString().toLowerCase();
                    String c = cognome.getText().toString().toLowerCase();

                    String nome_file = n.concat("_").concat(c).concat(".txt");
                    loadRes = readValue(nome_file,getApplicationContext());

                    Intent myIntent = new Intent(Paziente2.this, Risultati.class);
                    myIntent.putExtra("results", loadRes);
                    Paziente2.this.startActivity(myIntent);

                }
            }

        });

    }

}
