package com.example.frankie.binb;

import android.content.Intent;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.frankie.binb.MainActivity.LinearToDecibel;
import static com.example.frankie.binb.MainActivity.creaFile;
import static com.example.frankie.binb.MainActivity.generateDXTone;
import static com.example.frankie.binb.MainActivity.generateSXTone;
import static com.example.frankie.binb.MainActivity.readValue;
import static com.example.frankie.binb.MainActivity.writeToFile;

/**
 * Created by frankie on 08/11/17.
 */

public class Audiogramma extends AppCompatActivity {

    String nome_file;
    boolean fineStep1;
    float volume1=0.00f;
    int[] frequences = {125,250,500,1000,2000};
    float[] results = new float[frequences.length*4];
    int freq;
    int cont=0;
    int numFreq=0;
    CountDownTimer countdowntimer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent myIntent = getIntent(); //recuperare l'intent
        String n = myIntent.getStringExtra("nome");
        String c = myIntent.getStringExtra("cognome");
        freq = frequences[0];
        nome_file = n.toLowerCase().concat("_").concat(c.toLowerCase()).concat(".txt");
        creaFile(nome_file, getApplicationContext());
        Toast.makeText(getApplicationContext(), "Nome File: " + nome_file + " / ", Toast.LENGTH_SHORT).show();
        menuDXUP();
    }

    public void menuDXUP(){
        setContentView(R.layout.activity_main2);
        //freq1 = f[0]-10;
        fineStep1 = true;
        volume1 = 0.0f;
        final AudioTrack test = generateDXTone(freq,100000);

        ImageButton play2 = (ImageButton)findViewById(R.id.play_button);
        ImageButton next2 = (ImageButton)findViewById(R.id.stop_button);


        TextView text1 = (TextView)findViewById(R.id.tt);
        text1.setText("Test : "+(cont+1));
        TextView text2 = (TextView)findViewById(R.id.tf);
        text2.setText("Frequenza : "+freq+ " Hz");
        TextView text3 = (TextView)findViewById(R.id.to);
        text3.setText("Orecchio : destro" );
        TextView text4 = (TextView)findViewById(R.id.tv);
        text4.setText("Volume : crescente" );

        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                test.play();
                while(fineStep1){
                    volume1 = volume1 + 0.00000001f;
                    test.setVolume(volume1);
                }
                test.stop();
            }
        });

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.start();
            }


        });

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fineStep1 = false;
                test.stop();

                t.interrupt();
                results[cont]=volume1;
                cont++;
                Toast.makeText(getApplicationContext(),"Db: "+LinearToDecibel(volume1)+" ",Toast.LENGTH_SHORT).show();
                menuSXUP();
            }


        });

    }


    public void menuSXUP(){
        setContentView(R.layout.activity_main2);

        TextView text1 = (TextView)findViewById(R.id.tt);
        text1.setText("Test : "+(cont+1));
        TextView text2 = (TextView)findViewById(R.id.tf);
        text2.setText("Frequenza : "+freq+ " Hz");
        TextView text3 = (TextView)findViewById(R.id.to);
        text3.setText("Orecchio : sinistro" );
        TextView text4 = (TextView)findViewById(R.id.tv);
        text4.setText("Volume : crescente" );


        final AudioTrack test = generateSXTone(freq,100000);
        volume1 = 0.0f;
        fineStep1 = true;

        ImageButton play2 = (ImageButton)findViewById(R.id.play_button);
        ImageButton next2 = (ImageButton)findViewById(R.id.stop_button);

        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                test.play();
                while(fineStep1){
                    volume1 = volume1 + 0.00000001f;
                    test.setVolume(volume1);
                }
                test.stop();
            }
        });

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.start();
            }
        });

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fineStep1 = false;
                test.stop();

                t.interrupt();
                results[cont]=volume1;
                cont++;
                Toast.makeText(getApplicationContext(),"Db: "+LinearToDecibel(volume1)+" ",Toast.LENGTH_SHORT).show();
                menuDXDown();
            }
        });



    }


    public void menuDXDown(){
        setContentView(R.layout.activity_main2);

        final AudioTrack test = generateDXTone(freq,100000);
        volume1 = volume1 + 0.0006f;
        fineStep1 = true;

        TextView text1 = (TextView)findViewById(R.id.tt);
        text1.setText("Test : "+(cont+1));
        TextView text2 = (TextView)findViewById(R.id.tf);
        text2.setText("Frequenza : "+freq+ " Hz");
        TextView text3 = (TextView)findViewById(R.id.to);
        text3.setText("Orecchio : destro" );
        TextView text4 = (TextView)findViewById(R.id.tv);
        text4.setText("Volume : decrescente" );


        ImageButton play2 = (ImageButton)findViewById(R.id.play_button);
        ImageButton next2 = (ImageButton)findViewById(R.id.stop_button);

        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                test.play();
                while(fineStep1){
                    volume1 = volume1 - 0.00000001f;
                    test.setVolume(volume1);
                }
                test.stop();
            }
        });

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.start();
            }


        });

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fineStep1 = false;
                test.stop();

                t.interrupt();
                results[cont]=volume1;
                cont++;
                Toast.makeText(getApplicationContext(),"Db: "+LinearToDecibel(volume1)+" ",Toast.LENGTH_SHORT).show();
                menuSXDown();
            }


        });

    }


    public void menuSXDown(){

        setContentView(R.layout.activity_main2);

        TextView text1 = (TextView)findViewById(R.id.tt);
        text1.setText("Test : "+(cont+1));
        TextView text2 = (TextView)findViewById(R.id.tf);
        text2.setText("Frequenza : "+freq+ " Hz");
        TextView text3 = (TextView)findViewById(R.id.to);
        text3.setText("Orecchio : sinistro" );
        TextView text4 = (TextView)findViewById(R.id.tv);
        text4.setText("Volume : decrescente" );

        ImageButton play2 = (ImageButton)findViewById(R.id.play_button);
        ImageButton next2 = (ImageButton)findViewById(R.id.stop_button);


        final AudioTrack test = generateSXTone(freq,100000);
        volume1 = volume1 + 0.0006f;
        fineStep1 = true;

        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                test.play();
                while(fineStep1){
                    volume1 = volume1 - 0.00000001f;
                    test.setVolume(volume1);
                }
                test.stop();
            }
        });

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.start();
            }


        });

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fineStep1 =false;
                test.stop();
                t.interrupt();
                results[cont]=volume1;
                cont++;
                Toast.makeText(getApplicationContext(),"Db: "+LinearToDecibel(volume1)+" ",Toast.LENGTH_SHORT).show();

                numFreq = numFreq + 1;

                if(numFreq < frequences.length) {
                    freq = frequences[numFreq];

                    menuDXUP();
                }

                else {
                    String toWrite="";
                    for(int i=0; i<results.length;i++){
                        toWrite = toWrite+Float.toString(results[i])+"\n";
                    }

                    writeToFile(nome_file,toWrite,getApplicationContext());
                    //menuPaziente();
                    //menuRes(results);
                    Intent myIntent = new Intent(Audiogramma.this, Risultati.class);
                    myIntent.putExtra("results", results);
                    Audiogramma.this.startActivity(myIntent);
                }

            }


        });




    }




}
