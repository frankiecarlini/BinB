package com.example.frankie.binb;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.media.SoundPool;
import android.media.AudioManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static java.lang.Math.log10;


public class MainActivity extends AppCompatActivity  {

    float volume1 = 0.00f;
    boolean fineStep1=true;
    Thread t;
    TextView nome;
    TextView cognome;
    String nome_file;


    int freq;
    int numFreq=0;
    int[] frequences = {125,250,500,1000,2000};
    int cont=0;
    float[] results = new float[frequences.length*4];
    float[] loadRes = new float[frequences.length*4];
    int bb1=0;
    int dxB;
    int sxB;

    SoundPool pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    int count = (int)(44100.0 * 2.0 * (6000 / 1000.0)) & ~1; //529200

    AudioTrack testDXUp;
    AudioTrack testSXUp;

    AudioTrack testBB;
    AudioTrack testBSX;
    AudioTrack testBDX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_iniziale);

        ImageButton nuovo = (ImageButton)findViewById(R.id.new_audio);
        ImageButton carica = (ImageButton) findViewById(R.id.load_audio);
        ImageButton bb = (ImageButton)findViewById(R.id.bb_test);
        nuovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Paziente.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);

            }

        });

        carica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Paziente2.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);            }

        });

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Paziente3.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);            }

        });

    }


    public static void creaFile(String nomefile,Context context){
        File path = context.getFilesDir();
        File file = new File(path,nomefile);
    }
    //per la memoria esterna: File path = context.getExternalFilesDir(null);
    public static void writeToFile(String nomefile , String data, Context context) {
        //path /data/data/com.example.frankie.binb/files/

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(nomefile, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(String nomefile ,Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(nomefile);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static float[] readValue(String nomefile ,Context context) {
        //path /data/data/com.example.frankie.binb/files/

        float[] res = new float[20];
        int i=0;

        try {
            InputStream inputStream = context.openFileInput(nomefile);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    //stringBuilder.append(receiveString);
                    res[i]= Float.parseFloat(receiveString);
                    i++;
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return res;
    }


    private AudioTrack generateBB(double freqHz, double freqHz2, int durationMs)
    {
        int count = (int)(44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        //Toast.makeText(getApplicationContext(),"Count: "+count,Toast.LENGTH_SHORT).show();
        short[] samples = new short[count];
        for(int i = 0; i < count; i += 2){
            short sample1 = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            short sample2 = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz2)) * 0x7FFF);
            samples[i + 0] = sample1;
            samples[i + 1] = sample2;

        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);

        track.write(samples, 0, count);
        return track;
    }

    //AudioTrack (int streamType,int sampleRateInHz,int channelConfig,int audioFormat,
    //int bufferSizeInBytes,int mode,int sessionId)
    private AudioTrack generateTone(double freqHz, int durationMs)
    {
        int count = (int)(44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];

        for(int i = 0; i < count; i += 2){
            short sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            samples[i + 0] = sample;
            samples[i + 1] = sample;
        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);

        track.write(samples, 0, count);
        return track;
    }

    public static AudioTrack generateDXTone(double freqHz, int durationMs)
    {
        int count = (int)(44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];

        for(int i = 0; i < count; i += 2){
            short sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            samples[i + 1] = sample;
        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);

        track.write(samples, 0, count);
        return track;
    }

    public static AudioTrack generateSXTone(double freqHz, int durationMs)
    {
        int count = (int)(44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];

        for(int i = 0; i < count; i += 2){
            short sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            samples[i + 0] = sample;
        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);

        track.write(samples, 0, count);
        return track;
    }

    public static float LinearToDecibel(float linear)
    {
        double db;
        if (linear != 0.0f)
            db = 20.0f * log10(linear);
        else
            db = -144.0f;  // effectively minus infinity
        return (float)db;
    }



}
