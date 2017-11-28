package com.example.frankie.binb;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class Settings extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new Fragment())
                .commit();
    }
}
