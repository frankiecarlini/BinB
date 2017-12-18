package com.example.frankie.binb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.Math.log10;


public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_iniziale);

        ImageButton nuovo = (ImageButton)findViewById(R.id.new_audio);
        ImageButton bb = (ImageButton)findViewById(R.id.bb_test);


        nuovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    Set<String> selections = preferences.getStringSet("multiList", null);
                    selections.toArray(new String[]{});
                    Intent myIntent = new Intent(MainActivity.this, LoginAudioTest.class);
                    MainActivity.this.startActivity(myIntent);
                }
                catch (NullPointerException n){
                    Toast.makeText(getApplicationContext(),"Seleziona le frequenze",Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(MainActivity.this, Settings.class);
                    MainActivity.this.startActivity(myIntent);
                }



            }

        });

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (countPazienti()!=0) {
                    Intent myIntent = new Intent(MainActivity.this, LoadAudioTest.class);
                    MainActivity.this.startActivity(myIntent);
                }
                else
                    Toast.makeText(getApplicationContext(),"Nessun paziente nel database",Toast.LENGTH_LONG).show();

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id)
        {
            case R.id.n_ad:
                Intent myIntent = new Intent(MainActivity.this, Settings.class);
                MainActivity.this.startActivity(myIntent);
        }
        return false;
    }

    public static void creaFile(String nomefile, Context context){
        File path = context.getFilesDir();
        File file = new File(path,nomefile);
    }
    public static void writeToFile(String nomefile , String data, Context context) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(nomefile, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public static int numFreq(String nomefile ,Context context) {
        int i=0;

        try {
            InputStream inputStream = context.openFileInput(nomefile);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                while ( (bufferedReader.readLine()) != null ) {
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


        return i;
    }


    public static int[] readFreq(String nomefile ,int numFreq, Context context) {

        int[] freq = new int[numFreq];
        int i=0;

        try {
            InputStream inputStream = context.openFileInput(nomefile);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    freq[i]=Integer.parseInt(receiveString);
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


        return freq;
    }

    public static float[] readValues(String nomefile, int numFreq, Context context) {

        float[] res = new float[numFreq*4];
        int i=0;

        try {
            InputStream inputStream = context.openFileInput(nomefile);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    res[i]=Float.parseFloat(receiveString);
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

        short[] samples = new short[count];
        for(int i = 0; i < count; i += 2){
            short sample1 = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            short sample2 = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz2)) * 0x7FFF);
            samples[i] = sample1;
            samples[i + 1] = sample2;

        }
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8), AudioTrack.MODE_STATIC);

        track.write(samples, 0, count);
        return track;
    }

    private AudioTrack generateTone(double freqHz, int durationMs)
    {
        int count = (int)(44100.0 * 2.0 * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];

        for(int i = 0; i < count; i += 2){
            short sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            samples[i] = sample;
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
            samples[i] = sample;
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
            db = -144.0f;
        return (float)db;
    }

    private int countPazienti(){
        File[] files;
        try {
            files = new File("/data/data/com.example.frankie.binb/files/").listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith("paz.txt");
                }
            });
            return files.length;
        }
        catch (NullPointerException n){
            return 0;
        }
    }



}
