package com.fluidobjects.sdkchopeira;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fluidobjects.drafttapcontroller.DraftTapController;
import com.fluidobjects.drafttapcontroller.LogObj;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    int initialFator = 5000;
    String ip = "192.168.0.128";
    DraftTapController chopeira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        chopeira = new DraftTapController(getApplicationContext() ,ip, initialFator);
    }

    public void servir(View v) {
        EditText vol = findViewById(R.id.volume);
        int volumeProgramado=0;
        try {
            volumeProgramado = Integer.valueOf(vol.getText().toString());
        }catch (Exception e){
            print(e.getMessage());
        }

        if(volumeProgramado != 0){
            chopeira.openValve(volumeProgramado);
        }
    }

    public void calibrar(View v) {
        EditText vol = findViewById(R.id.volume);
        EditText medido = findViewById(R.id.medido);
        int volumeProgramado = 0;
        int volumeServido = 0;
        try {
            volumeProgramado = Integer.valueOf(vol.getText().toString());
            volumeServido = Integer.valueOf(medido.getText().toString());
        }catch (Exception e){
            print(e.getMessage());
        }
        if(volumeProgramado != 0 && volumeServido != 0){
            chopeira.calibratePulseFactor(volumeProgramado, volumeServido);
        }
    }

    public void teste(View v){
        for (LogObj l: chopeira.getLogs()) {
            print("Log: " + l.servedVolume);
        }
    }

    private void print(String text){
        Log.d("Main Activity", text);
    }
}