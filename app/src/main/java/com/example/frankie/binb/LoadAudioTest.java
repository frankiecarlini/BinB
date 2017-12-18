package com.example.frankie.binb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import static com.example.frankie.binb.MainActivity.numFreq;
import static com.example.frankie.binb.MainActivity.readFreq;
import static com.example.frankie.binb.MainActivity.readValues;


public class LoadAudioTest extends AppCompatActivity {
    TextView nome;
    TextView cognome;
    String nome_file;
    String nome_file_freq;
    int numF;
    int[] frequences;
    float[] loadRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_audiotest);
        final Button read = (Button)findViewById(R.id.startbb);

        nome = (TextView)findViewById(R.id.name);
        cognome = (TextView)findViewById(R.id.surname);

        List<String> results = new ArrayList<>();
        File[] files = new File("/data/data/com.example.frankie.binb/files/").listFiles(new FilenameFilter() {
            @Override public boolean accept(File dir, String name) { return name.endsWith("paz.txt"); } });

        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName().replaceAll("_paz.txt","");
                name = name.replaceAll("_"," ");
                results.add(name);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, results);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);


        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner sItems = (Spinner) findViewById(R.id.spinner);
                nome_file = sItems.getSelectedItem().toString().replaceAll(" ","_").concat("_paz.txt");
                nome_file_freq = sItems.getSelectedItem().toString().replaceAll(" ","_").concat("_freq.txt");

                numF = numFreq(nome_file_freq,getApplicationContext());
                frequences = readFreq(nome_file_freq, numF ,getApplicationContext());
                loadRes = readValues(nome_file, numF , getApplicationContext());

                Intent myIntent = new Intent(LoadAudioTest.this, BinauralBeat.class);
                myIntent.putExtra("results", loadRes);
                myIntent.putExtra("frequences",frequences);
                LoadAudioTest.this.startActivity(myIntent);


            }

        });

    }
}
