package com.example.frankie.binb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.frankie.binb.MainActivity.LinearToDecibel;
import static com.example.frankie.binb.MainActivity.generateDXTone;
import static com.example.frankie.binb.MainActivity.generateSXTone;

public class BinauralB extends AppCompatActivity {

    int[] frequences;
    float[] loadRes;

    AudioTrack testBSX;
    AudioTrack testBDX;

    int dxB;
    int sxB;
    float volumeSX;
    float volumeDX;
    boolean test1=true;
    boolean resume=false;
    int bb1=0;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_bb);
        Intent myIntent= getIntent();
        loadRes= myIntent.getFloatArrayExtra("results");
        frequences= myIntent.getIntArrayExtra("frequences");

        menuBB();
    }

    public void menuBB(){
        ImageButton playBB = (ImageButton)findViewById(R.id.play_button);
        final ImageButton stopBB = (ImageButton)findViewById(R.id.stop_button);
        TextView textFD = (TextView)findViewById(R.id.tFD);
        TextView textFS = (TextView)findViewById(R.id.tFS);
        TextView textVD = (TextView)findViewById(R.id.tVD);
        TextView textVS = (TextView)findViewById(R.id.tVS);
        final Chronometer crono = (Chronometer) findViewById(R.id.chronometer1);
        crono.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String value = sharedPref.getString("example_list", "default value");
        String value2 = sharedPref.getString("durataBB","default value");
        int deltaF = Integer.parseInt(value);
        int durata = Integer.parseInt(value2);


        if(test1) {
            dxB = frequences[bb1];
            sxB = frequences[bb1] + deltaF;
        }
        if(!test1){
            dxB = frequences[bb1]+ deltaF;
            sxB = frequences[bb1];
        }

        volumeDX = loadRes[i];
        volumeSX = loadRes[i+1];
        textFD.setText("Freq. dx: " +dxB+ " Hz" );
        textFS.setText("Freq. sx: "+sxB+ " Hz" );
        textVD.setText("Volume dx:"+LinearToDecibel(volumeDX)+ " dB");
        textVS.setText("Volume sx" +LinearToDecibel(volumeSX)+" dB");
        testBSX = generateSXTone(sxB,durata*1000);
        testBDX = generateDXTone(dxB,durata*1000);

        stopBB.setVisibility(View.INVISIBLE);
        playBB.setVisibility(View.VISIBLE);

        playBB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testBDX.setVolume(volumeDX);
                testBSX.setVolume(volumeSX);
                testBDX.play();
                testBSX.play();
                crono.setVisibility(View.VISIBLE);
                crono.setBase(SystemClock.elapsedRealtime());
                crono.start();
                v.setVisibility(View.INVISIBLE);
                stopBB.setVisibility(View.VISIBLE);
            }

        });

        stopBB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resume =true;
                testBDX.stop();
                testBSX.stop();
                crono.stop();
                if(bb1<frequences.length-1){
                    bb1++;
                    i=i+4;
                    menuBB();
                }
                else {
                    if(test1) {
                        bb1 = 0;
                        i = 0;
                        test1 = false;
                        menuBB();
                    }
                    else{
                        Toast.makeText(getApplicationContext()," Test Completato ",Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(BinauralB.this, MainActivity.class);
                        BinauralB.this.startActivity(myIntent);
                    }
                }

            }

        });


    }

}
