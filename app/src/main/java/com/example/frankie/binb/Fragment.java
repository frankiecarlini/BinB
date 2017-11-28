package com.example.frankie.binb;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class Fragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);
    }
}
