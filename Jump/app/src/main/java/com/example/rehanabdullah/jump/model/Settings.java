package com.example.rehanabdullah.jump.model;

/**
 * Created by rehanabdullah on 12/04/2016.
 */
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//import android.view.Menu;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rehanabdullah.jump.R;

public class Settings extends Activity {
    CheckBox ch1;
    int volume;

    private SeekBar seekBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        seekBar = (SeekBar) findViewById(R.id.seekBar1); // seek bar
        textView = (TextView) findViewById(R.id.textView1);
        ch1 = (CheckBox) findViewById(R.id.checkBox1);

        // Initialize the textview with '0'
        textView.setText(seekBar.getProgress() + "/" + seekBar.getMax());
        seekBar.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                        progress = progresValue;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Display the value in textview
                        textView.setText(progress + "/" + seekBar.getMax());
                    }
                });


        SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
        Editor editor = pref.edit();
        volume = pref.getInt("volume", 0);

        if (volume == 1) {
            ch1.setChecked(true);
        }
    }

    public void volume(View v) {
        ch1 = (CheckBox) v;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
        Editor editor = pref.edit();
        if (ch1.isChecked()) {
            editor.putInt("volume", 1);
            editor.commit();
            Toast.makeText(this, "volume on", Toast.LENGTH_LONG).show();
        } else {
            editor.putInt("volume", 0);
            editor.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;

        //this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}

