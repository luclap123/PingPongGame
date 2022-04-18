package com.example.pingponggame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    TextView txtPoint, txtHighest;
    SharedPreferences sharedPreferences;
    ImageView imgHighest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        txtPoint = findViewById(R.id.txtPoint);
        txtHighest = findViewById(R.id.txtHighest);
        imgHighest = findViewById(R.id.imgHighest);

        int points = getIntent().getExtras().getInt("points");
        txtPoint.setText(""+points);
        sharedPreferences = getSharedPreferences("my_pref", 0);
        int highest = sharedPreferences.getInt("highest", 0);
        if (points > highest)
        {
            imgHighest.setVisibility(View.INVISIBLE);
            highest = points;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highest", highest);
            editor.commit();
        }
        txtHighest.setText(""+highest);
    }

    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        finish();
    }
}
