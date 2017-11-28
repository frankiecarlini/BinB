package com.example.frankie.binb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LoginAudioTest extends AppCompatActivity {
    TextView nome;
    TextView cognome;
    String n;
    String c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_audiotest);
        nome = (TextView)findViewById(R.id.name);
        cognome = (TextView)findViewById(R.id.surname);

        Button start = (Button)findViewById(R.id.crea_aud);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nome.getText().length()!=0 && cognome.getText().length()!=0) {

                    n = nome.getText().toString();
                    c = cognome.getText().toString();

                    Intent myIntent = new Intent(LoginAudioTest.this, AudioTest.class);
                    myIntent.putExtra("nome", n);
                    myIntent.putExtra("cognome", c);
                    LoginAudioTest.this.startActivity(myIntent);
                }
            }

        });
    }



}
