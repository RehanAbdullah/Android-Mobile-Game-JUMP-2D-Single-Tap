package com.example.rehanabdullah.jump.model;

/**
 * Created by rehanabdullah on 12/04/2016.
 */
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.widget.TextView;

import com.example.rehanabdullah.jump.R;

public class Highscore extends Activity {
    TextView t1;
    int score,highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("higher", MODE_PRIVATE);
        Editor editor = pref.edit();

        score=pref.getInt("score", 0);
        highscore=pref.getInt("highscore", 0);

        if(score>highscore)
        {
            editor.putInt("highscore", score);
            editor.commit();
        }
        highscore=pref.getInt("highscore", 0);

        t1=(TextView) findViewById(R.id.textView1);
        t1.setText("Highscore :"+highscore);
    }


}
