package com.example.pingponggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Boolean audioState;
    ImageButton btnSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControl();
    }

    private void addControl() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        btnSound = findViewById(R.id.btnSound);
        sharedPreferences = getSharedPreferences("my_pref", 0);
        audioState = sharedPreferences.getBoolean("audioState", true);
        if (audioState)
        {
            btnSound.setImageResource(R.drawable.audio_on);
        }else
        {
            btnSound.setImageResource(R.drawable.audio_off);
        }
    }

    public void playGame(View view) {
        GameView gameView = new GameView(this);
        setContentView(gameView);
    }

    public void audioGame(View view) {
        if (audioState)
        {
            audioState = false;
            btnSound.setImageResource(R.drawable.audio_off);
        }else
        {
            audioState = true;
            btnSound.setImageResource(R.drawable.audio_on);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("audioState", audioState);
        editor.commit();
    }
}