package com.example.frankie.binb;

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

import static com.example.frankie.binb.MainActivity.LinearToDecibel;
import static com.example.frankie.binb.MainActivity.generateDXTone;
import static com.example.frankie.binb.MainActivity.generateSXTone;

/**
 * Created by frankie on 09/11/17.
 */

public class BinauralB extends AppCompatActivity {

    int[] frequences = {125,250,500,1000,2000};
    float[] loadRes = new float[frequences.length*4];


    AudioTrack testBSX;
    AudioTrack testBDX;

    int dxB;
    int sxB;
    float volumeSX;
    float volumeDX;
    boolean test1=true;
    int bb1=0;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_bb);
        Intent myIntent= getIntent();
        loadRes= myIntent.getFloatArrayExtra("results");

        menuBB(loadRes);
        //menuBB2(loadRes);
    }

    public void menuBB(final float [] loadR){
        ImageButton playBB = (ImageButton)findViewById(R.id.play_button);
        ImageButton stopBB = (ImageButton)findViewById(R.id.stop_button);
        TextView textFD = (TextView)findViewById(R.id.tFD);
        TextView textFS = (TextView)findViewById(R.id.tFS);
        TextView textVD = (TextView)findViewById(R.id.tVD);
        TextView textVS = (TextView)findViewById(R.id.tVS);

        if(test1) {
            dxB = frequences[bb1];
            sxB = frequences[bb1] + 10;
        }
        if(!test1){
            dxB = frequences[bb1]+ 10;
            sxB = frequences[bb1];
        }
        volumeDX = loadR[i]+0.0001f;
        volumeSX = loadR[i+1]+0.0001f;
        textFD.setText("Freq. dx: " +dxB+ " Hz" );
        textFS.setText("Freq. sx: "+sxB+ " Hz" );
        textVD.setText("Volume dx:"+LinearToDecibel(volumeDX)+ " dB");
        textVS.setText("Volume sx" +LinearToDecibel(volumeSX)+" dB");
        testBSX = generateSXTone(sxB,60000);
        testBDX = generateDXTone(dxB,60000);

        playBB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testBDX.setVolume(volumeDX);
                testBSX.setVolume(volumeSX);
                testBDX.play();
                testBSX.play();
                //Toast.makeText(getApplicationContext(),"Db: "+LinearToDecibel(loadR[2]+0.0001f)+" ",Toast.LENGTH_SHORT).show();

            }

        });

        stopBB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testBDX.stop();
                testBSX.stop();
                if(bb1<frequences.length-1){
                    bb1++;
                    i=i+4;
                    menuBB(loadR);
                }
                else {
                    if(test1) {
                        bb1 = 0;
                        i = 0;
                        test1 = false;
                        menuBB(loadR);
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
