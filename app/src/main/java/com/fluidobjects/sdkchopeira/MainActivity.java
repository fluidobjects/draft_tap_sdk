package com.fluidobjects.sdkchopeira;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    ChopeiraSDK clpManager;
    int volume;
    int fator=5000;
    String ip = "192.168.0.128";
    float saldo_aux;
    int volume_aux;
    boolean finaliza;
    String TAG = "Cerveja Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void preparesCLP(View v) throws JSONException {
        clpManager = new ChopeiraSDK(ip);
        if (clpManager.open(fator)) { //inicializa os registradores necessários
            volume = 0;
//            custo = (float) 0.0;
            Log.i(TAG, "abriu");
            Log.i(TAG, "primeira mensagem");
//            linha1 = findViewById(R.id.linha1);
//            linha2 = findViewById(R.id.linha2);
//            linha1.setText("Olá " + user.getNome() + ". Seu saldo é de R$" + String.format("%.2f", user.getSaldo()));
//            linha2.setText("Pode se servir");
            setMaxVolume();
            Log.i("volumemax", "setou");
            monitoraBatelada(); //monitora as informações da batelada
        } else {
            Toast.makeText(getApplicationContext(), "Falha na comunicação com o CLP", Toast.LENGTH_LONG).show();
            Log.i(TAG, " Falha comunicação CLP");
            volume = 0;
//            custo = (float) 0.0;
//            atualizaClienteDrupal();
        }
    }

    private void setMaxVolume(){
        float max = 500.0f;
        if(max > 500){
            max = 500;
        }
        clpManager.setMaxVol((int) max);
        Log.i("max vol", String.valueOf(max));
    }

    public void monitoraBatelada() { //abre uma nova thread para escutar os registradores do clp
        Thread t = new Thread(new Runnable() {
            public void run() {
                clpManager.monitorsCLP();
            }
        });
        t.start();
        atualizaInfoPedido(); //atualiza as informações na tela do aplicativo
    }

    //pega informações registradas no clp manager e atualiza as a tela
    public void atualizaInfoPedido() {
        final Handler mHandler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (!finaliza) {
                    //sleep(200);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            volume_aux = volume;
                            volume = clpManager.getVolume(); //pega o volume registrado em tempo real
                            finaliza = clpManager.finalizou(); //verifica o status da batelada - Se 4(finalizado) -> termina o while

                            if (volume != volume_aux) { //Verifica se houve alteração no volume
//                                linha2 =(TextView) findViewById(R.id.linha2);
//                                linha2.setText("Serviu "+volume+"ml, valor R$ "+ getValorStr());
                                Log.i(TAG, "terceira mensagem");

                            }
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        saldo_aux = user.getSaldo();
//                        custo = (cerveja.getValor()/100)*volume;
//                        //double roundOff = Math.round(custo * 100.0) / 100.0;
//                        saldo_aux = (saldo_aux - custo);
//                        if (saldo_aux < 0 ) {
//                            custo = custo + saldo_aux;
//                            saldo_aux = 0;
//                        }

                        //TODO: call setTexts function here instead of setting texts
                        //atualiza a interface
//                        linha1 =(TextView) findViewById(R.id.linha1);
//                        linha1.setText(user.getNome()+", você serviu "+ volume +"ml, valor R$"+ String.format("%.2f", custo));
//                        linha2 =(TextView) findViewById(R.id.linha2);
//                        linha2.setText("O saldo do seu cartão agora é de R$"+ String.format("%.2f", saldo_aux));

//                        user.setSaldo(saldo_aux);
                        Log.i(TAG, "quinta mensagem");
//                        Log.i(TAG, "valores" + String.valueOf(custo) +","+ String.valueOf(saldo_aux)+","+ String.valueOf(volume));
//                        passouCartao = false;
                    }
                });

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            if(saldo_aux < 0) {
                                saldo_aux = 0;
                            }
                        clpManager.closeCon();
//                        linha1 =(TextView) findViewById(R.id.linha1);
//                        linha1.setText("Aproxime o cartão");
//                        linha2 =(TextView) findViewById(R.id.linha2);
//                        linha2.setText("Espere aparecer o seu nome e saldo para servir");
//                        Log.i(TAG, "quarta mensagem");

                    }
                }, 3000);

            }
        }).start();

    }

}
