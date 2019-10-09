package com.fluidobjects.sdkchopeira;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    int fator = 5000;
    String ip = "192.168.0.128";
    int volume_aux = 100;
    int timeout = 1;
    boolean finaliza;
    int volume;
    DraftTapController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new DraftTapController(ip, volume_aux, timeout, fator);
    }

    public void clicou(View view) {
        controller.openValve();
//        Log.i("TELA", "Abriu valvula");
    }

    public void calibrar(View v){
        controller.calibratePulseFactor(100,controller.readVolume());
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

