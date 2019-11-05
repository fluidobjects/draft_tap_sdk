package com.fluidobjects.sdkchopeira;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.os.SystemClock.sleep;
import com.fluidobjects.drafttapcontroller.DraftTapController;

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
        final TextView medido = findViewById(R.id.volumeMedido);
        final Handler mHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    chopeira.openValve(100);
                }catch (Exception e){
                    print(e.getMessage());
                }
            }}).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(chopeira.isServing){
                    sleep(200);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            int vol = chopeira.readVolume();
                            medido.setText(String.valueOf(vol));
                        }
                    });
                }
            }
        }).start();
    }

    public void servir250(View v) {
        final Handler mHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    chopeira.openValve(250);
                }catch (Exception e){
                    print(e.getMessage());
                }
            }}).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(chopeira.isServing){
                    sleep(200);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView medido = findViewById(R.id.volumeMedido);
                            if(Integer.getInteger((String) medido.getText()) != chopeira.readVolume()){
                                medido.setText(chopeira.readVolume());
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void servir450(View v) {
        final Handler mHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    chopeira.openValve(450);
                }catch (Exception e){
                    print(e.getMessage());
                }
            }}).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(chopeira.isServing){
                    sleep(200);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView medido = findViewById(R.id.volumeMedido);
                            if(Integer.getInteger((String) medido.getText()) != chopeira.readVolume()){
                                medido.setText(chopeira.readVolume());
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void calibrar(View v) {
        EditText servido = findViewById(R.id.servido);
        TextView medido = findViewById(R.id.volumeMedido);
        int volumeServido = 0;
        int volumeMedido = 0;
        try {
            volumeServido = Integer.valueOf(servido.getText().toString());
            volumeMedido = Integer.valueOf(medido.getText().toString());
        }catch (Exception e){
        }
        if(volumeServido != 0 && volumeMedido!=0){
            chopeira.calibratePulseFactor(volumeMedido, volumeServido);
        }
    }

    public void teste(View v){
        startActivity(new Intent(this, Logs.class));
    }

    private void print(String text){
        Log.d("Main Activity", text + "\n");
    }
}