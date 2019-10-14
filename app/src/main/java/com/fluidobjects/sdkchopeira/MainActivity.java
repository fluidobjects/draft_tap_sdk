package com.fluidobjects.sdkchopeira;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    Equipment clpManager;
    int volume;
    int fator=5000;
    String ip = "192.168.0.128";
    int volume_aux;
    boolean finaliza;
    String TAG = "Cerveja Activity";
    DraftTapController chopeira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        chopeira = new DraftTapController(ip);
    }

    public void servir(View v) {
        EditText vol = findViewById(R.id.volume);
        int volumeProgramado=0;
        try {
            volumeProgramado = Integer.valueOf(vol.getText().toString());
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
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
            Log.d(TAG, e.getMessage());
        }
        chopeira.calibratePulseFactor(volumeProgramado, volumeServido);
    }

}


    //pega informações registradas no clp manager e atualiza as a tela
//    public void atualizaInfoPedido() {
//        final Handler mHandler = new Handler();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (!finaliza) {
//                    //sleep(200);
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            volume_aux = volume;
//                            volume = equipment.getVolume(); //pega o volume registrado em tempo real
//                            finaliza = equipment.finalizou(); //verifica o status da batelada - Se 4(finalizado) -> termina o while
//
//                            if (volume != volume_aux) { //Verifica se houve alteração no volume
//                                Log.i(TAG, "terceira mensagem");
//                            }
//                        }
//                    });
//                }
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //TODO: call setTexts function here instead of setting texts
//                        Log.i(TAG, "quinta mensagem");
//                    }
//                });
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                            if(saldo_aux < 0) {
//                                saldo_aux = 0;
//                            }
//                        equipment.closeCon();
//                    }
//                }, 3000);
//            }
//        }).start();
//    }

