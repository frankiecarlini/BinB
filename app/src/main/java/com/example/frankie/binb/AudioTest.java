package com.example.frankie.binb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import static com.example.frankie.binb.MainActivity.LinearToDecibel;
import static com.example.frankie.binb.MainActivity.creaFile;
import static com.example.frankie.binb.MainActivity.generateDXTone;
import static com.example.frankie.binb.MainActivity.generateSXTone;
import static com.example.frankie.binb.MainActivity.writeToFile;


public class AudioTest extends AppCompatActivity {

    String nome_file;
    String nome_file_freq;
    boolean fineStep1;
    float volume1=0.00f;
    float reset = 0.0f;
    int durata = 100000;
    int[] frequences;
    float[] results;
    float incr;
    int freq;
    int cont=0;
    int numFreq=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_audiotest);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Set<String> selections = preferences.getStringSet("multiList",null);
        String incremento = preferences.getString("incremento","default");
        incr = Float.parseFloat(incremento);
        incr = incr * 0.000000001f;

        try {
            String[] selected = selections.toArray(new String[]{});
            frequences = new int[selected.length];
            for (int i = 0; i < selected.length ; i++) {
                frequences[i] = Integer.parseInt(selected[i]);
            }
        }
        catch (NullPointerException e){

        }

        results = new float[frequences.length*4];
        ordina(frequences);

        Intent myIntent = getIntent();
        String n = myIntent.getStringExtra("nome");
        String c = myIntent.getStringExtra("cognome");
        freq = frequences[0];

        nome_file = n.toLowerCase().concat("_").concat(c.toLowerCase()).concat("_paz.txt");
        nome_file_freq = n.toLowerCase().concat("_").concat(c.toLowerCase()).concat("_freq.txt");

        creaFile(nome_file, getApplicationContext());
        creaFile(nome_file_freq, getApplicationContext());

        menuDXUP();

    }

    public void menuDXUP(){
        setContentView(R.layout.menu_audiotest);
        fineStep1 = true;
        volume1 = reset;
        final AudioTrack test = generateDXTone(freq,durata);
        final Chronometer crono = (Chronometer) findViewById(R.id.chronometer1);

        ImageButton play2 = (ImageButton)findViewById(R.id.play_button);
        final ImageButton next2 = (ImageButton)findViewById(R.id.stop_button);
        next2.setVisibility(View.INVISIBLE);

        TextView text1 = (TextView)findViewById(R.id.tt);
        text1.setText("Test : "+(cont+1));
        TextView text2 = (TextView)findViewById(R.id.tf);
        text2.setText("Frequenza : "+freq+ " Hz");
        TextView text3 = (TextView)findViewById(R.id.to);
        text3.setText( getString(R.string.right_ear) );
        TextView text4 = (TextView)findViewById(R.id.tv);
        text4.setText(getString(R.string.volume_up) );

        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                test.play();
                while(fineStep1){
                    volume1 = volume1 + incr;
                    test.setVolume(volume1);
                }
                test.stop();
            }
        });

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.start();
                crono.setBase(SystemClock.elapsedRealtime());
                crono.start();
                v.setVisibility(View.INVISIBLE);
                next2.setVisibility(View.VISIBLE);
            }


        });


        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fineStep1 = false;
                test.stop();
                crono.stop();
                t.interrupt();
                results[cont]=volume1;
                cont++;
                Toast.makeText(getApplicationContext(),"Db: "+LinearToDecibel(volume1)+" ",Toast.LENGTH_SHORT).show();
                menuSXUP();
            }


        });

    }


    public void menuSXUP(){
        setContentView(R.layout.menu_audiotest);

        TextView text1 = (TextView)findViewById(R.id.tt);
        text1.setText("Test : "+(cont+1));
        TextView text2 = (TextView)findViewById(R.id.tf);
        text2.setText("Frequenza : "+freq+ " Hz");
        TextView text3 = (TextView)findViewById(R.id.to);
        text3.setText(getString(R.string.left_ear));
        TextView text4 = (TextView)findViewById(R.id.tv);
        text4.setText(getString(R.string.volume_up));


        final AudioTrack test = generateSXTone(freq,durata);
        final Chronometer crono = (Chronometer) findViewById(R.id.chronometer1);

        volume1 = reset;
        fineStep1 = true;

        ImageButton play2 = (ImageButton)findViewById(R.id.play_button);
        final ImageButton next2 = (ImageButton)findViewById(R.id.stop_button);
        next2.setVisibility(View.INVISIBLE);


        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                test.play();
                while(fineStep1){
                    volume1 = volume1 + incr;
                    test.setVolume(volume1);
                }
                test.stop();
            }
        });

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.start();
                crono.setBase(SystemClock.elapsedRealtime());
                crono.start();
                v.setVisibility(View.INVISIBLE);
                next2.setVisibility(View.VISIBLE);
            }
        });

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fineStep1 = false;
                test.stop();
                crono.stop();
                t.interrupt();
                results[cont]=volume1;
                cont++;
                Toast.makeText(getApplicationContext(),"Db: "+LinearToDecibel(volume1)+" ",Toast.LENGTH_SHORT).show();
                menuDXDown();
            }
        });



    }


    public void menuDXDown(){
        setContentView(R.layout.menu_audiotest);

        final AudioTrack test = generateDXTone(freq,durata);
        volume1 = volume1 + 0.0006f;
        fineStep1 = true;

        TextView text1 = (TextView)findViewById(R.id.tt);
        text1.setText("Test : "+(cont+1));
        TextView text2 = (TextView)findViewById(R.id.tf);
        text2.setText("Frequenza : "+freq+ " Hz");
        TextView text3 = (TextView)findViewById(R.id.to);
        text3.setText(getString(R.string.right_ear));
        TextView text4 = (TextView)findViewById(R.id.tv);
        text4.setText(getString(R.string.volume_down));

        final Chronometer crono = (Chronometer) findViewById(R.id.chronometer1);

        ImageButton play2 = (ImageButton)findViewById(R.id.play_button);
        final ImageButton next2 = (ImageButton)findViewById(R.id.stop_button);
        next2.setVisibility(View.INVISIBLE);

        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                test.play();
                while(fineStep1){
                    volume1 = volume1 - incr;
                    test.setVolume(volume1);
                }
                test.stop();
            }
        });

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.start();
                crono.setBase(SystemClock.elapsedRealtime());
                crono.start();
                v.setVisibility(View.INVISIBLE);
                next2.setVisibility(View.VISIBLE);

            }


        });

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fineStep1 = false;
                test.stop();
                crono.start();
                t.interrupt();
                results[cont]=volume1;
                cont++;
                Toast.makeText(getApplicationContext(),"Db: "+LinearToDecibel(volume1)+" ",Toast.LENGTH_SHORT).show();
                menuSXDown();
            }


        });

    }


    public void menuSXDown(){

        setContentView(R.layout.menu_audiotest);

        TextView text1 = (TextView)findViewById(R.id.tt);
        text1.setText("Test : "+(cont+1));
        TextView text2 = (TextView)findViewById(R.id.tf);
        text2.setText("Frequenza : "+freq+ " Hz");
        TextView text3 = (TextView)findViewById(R.id.to);
        text3.setText(getString(R.string.left_ear));
        TextView text4 = (TextView)findViewById(R.id.tv);
        text4.setText(getString(R.string.volume_down));

        final Chronometer crono = (Chronometer) findViewById(R.id.chronometer1);

        ImageButton play2 = (ImageButton)findViewById(R.id.play_button);
        final ImageButton next2 = (ImageButton)findViewById(R.id.stop_button);
        next2.setVisibility(View.INVISIBLE);


        final AudioTrack test = generateSXTone(freq,durata);
        volume1 = volume1 + 0.0006f;
        fineStep1 = true;

        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                test.play();
                while(fineStep1){
                    volume1 = volume1 - incr;
                    test.setVolume(volume1);
                }
                test.stop();
            }
        });

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.start();
                crono.setBase(SystemClock.elapsedRealtime());
                crono.start();
                v.setVisibility(View.INVISIBLE);
                next2.setVisibility(View.VISIBLE);

            }


        });

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fineStep1 =false;
                test.stop();
                crono.stop();
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
                    String toWrite1="";
                    for(int i =0; i<frequences.length; i++){
                        toWrite1 = toWrite1+Integer.toString(frequences[i])+"\n";
                    }
                    String toWrite2 = "";
                    for(int i=0; i<results.length;i++){
                        toWrite2 = toWrite2+Float.toString(results[i])+"\n";
                    }

                    writeToFile(nome_file_freq,toWrite1,getApplicationContext());
                    writeToFile(nome_file,toWrite2,getApplicationContext());
                    Toast.makeText(getApplicationContext()," Test Completato ",Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(AudioTest.this, MainActivity.class);
                    AudioTest.this.startActivity(myIntent);
                }

            }


        });




    }


    public void ordina(int [] array) {

        for(int i = 0; i < array.length-1; i++) {
            int minimo = i;
            for(int j = i+1; j < array.length; j++) {

                if(array[minimo]>array[j]) {
                    minimo = j;
                }
            }

            if(minimo!=i) {
                int k = array[minimo];
                array[minimo]= array[i];
                array[i] = k;
            }
        }
    }



}
