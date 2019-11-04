package com.fluidobjects.sdkchopeira;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fluidobjects.drafttapcontroller.DraftTapController;
import com.fluidobjects.drafttapcontroller.DraftTapLog;
import com.fluidobjects.drafttapcontroller.LogObj;

import org.json.JSONException;

import java.net.InetAddress;
import java.net.NetworkInterface;

public class MainActivity extends AppCompatActivity {
    String ip = "192.168.0.128";
    DraftTapController chopeira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try {
            chopeira = new DraftTapController(getApplicationContext() ,ip);
        }catch (Exception e){
            print(e.getMessage());
        }

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
            try {
                chopeira.openValve(volumeProgramado);

            }catch (Exception e){
                print(e.getMessage());
            }
        }
    }

    public void servir100(View v) {
        try {
            chopeira.openValve(100);
        }catch (Exception e){
            print(e.getMessage());
        }
    }
    public void servir250(View v) {
        try {
            chopeira.openValve(250);
        }catch (Exception e){
            print(e.getMessage());
        }
    }

    public void servir450(View v) {
        try {
            chopeira.openValve(450);
        }catch (Exception e){
            print(e.getMessage());
        }
    }

    public void calibrar(View v) {
        EditText medido = findViewById(R.id.medido);
        int volumeServido = 0;
        try {
            volumeServido = Integer.valueOf(medido.getText().toString());
        }catch (Exception e){
        }
        if(volumeServido != 0){
            chopeira.calibratePulseFactor(volumeServido);
        }
    }

    public void teste(View v){
        startActivity(new Intent(this, Logs.class));
    }

    private void print(String text){
        Log.d("Main Activity", text + "\n");
    }
}