package com.fluidobjects.sdkchopeira;

import android.os.StrictMode;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import static android.os.SystemClock.sleep;

public class ChopeiraSDK {

    private final ConectionTCP conn;
    private int volumeProgramado = 40;
    private int statusBatelada;
    private int volume;

    // REGISTRADORES CLP
    private int BATELADA_REG = 3000;
    private int STATUS_REG = 3003;
    private int VOLUME_REG = 3004;
    private int STATUS_VS_REG = 3005;
    private int MAX_VOL_REG = 3007;
    private int VAZAO_REG = 3008;
    private int MULT_FACTOR_REG = 3012;
    private int VOLUME_BARRIL_REG = 3014;
    private int PROBLEMA_VS_REG = 3015;

    private int batelada_valor;
    private int status_valor;
    private int volume_valor;
    private int status_vs_valor;
    private int max_vol_valor;
    private int vazao_valor;
    private int mult_factor_valor;
    private int volume_barril_valor;
    private int problema_vs_valor;
    String TAG = "CLP Manager";

    private boolean finalizaOp;

    public ChopeiraSDK(String ip) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        conn = new ConectionTCP(ip, 502);
        finalizaOp = false;
    }

    //É chamado para abrir a batelada
    public boolean open(int fator) {
        conn.writeRegisters(STATUS_REG,10);
        Log.d(TAG, "Abrindo Batelada!");
        int i = conn.readRegister(STATUS_REG);
        boolean aux = (i == 10 || i == 20);
        if (aux) {
            conn.writeRegisters(MULT_FACTOR_REG, fator);
            conn.writeRegisters(MAX_VOL_REG, 100); //seta o volume maximo
            conn.writeRegisters(BATELADA_REG, 4);
            conn.writeRegisters(BATELADA_REG, 1); //abre a batelada
            statusBatelada = 1;
            Log.d(TAG, "abriu batelada");
            volume = 0;
            return true;
        }
        return false;
    }


    public int monitorsCLP() {
        int volumeLido = conn.readRegister(VOLUME_REG);
        if (volume < volumeLido) volume = volumeLido;

        this.statusBatelada = conn.readRegister(BATELADA_REG);

        if (this.statusBatelada == 3) {
            Log.d(TAG, "Encerrou Batelada");
            conn.writeRegisters(BATELADA_REG, 4);
            this.statusBatelada = 4;
            return 1;
        }
        Log.d(TAG, "listening - Volume lido: " + volumeLido);
        sleep(100);
        return monitorsCLP();
    }


    public int getVolume() {
        return volume;
    }

    public boolean finalizou() {
        if (statusBatelada == 4) return true;
        return false;
    }

    // - - - - - - - - - - OPERADOR - - - - - - - - - - - //
    void openOp() {
        //inicializaEndRegistradores();
        batelada_valor = -1;
        status_valor = -1;
        volume_valor = -1;
        status_vs_valor = -1;
        max_vol_valor = -1;
        vazao_valor = -1;
        mult_factor_valor = -1;
        volume_barril_valor = -1;
        problema_vs_valor = -1;
    }

    boolean simulaServida() {
        if (conn.readRegister(STATUS_REG) == 10) {
            //master.writeRegisters(MULT_FACTOR_REG, 250);
            //master.writeRegisters(MAX_VOL_REG, volumeProgramado); //seta o volume maximo
            //master.writeRegisters(STATUS_REG, 20); //seta status para programado
            conn.writeRegisters(BATELADA_REG, 1); //abre a batelada
            statusBatelada = 1;
            Log.d(TAG, "abriu batelada");
            volume = 0;
            return true;
        }
        return false;
    }


    public int monitorsCLPOp() {
        if(finalizaOp){
            return 1;
        }
        batelada_valor = conn.readRegister(BATELADA_REG);
        status_valor = conn.readRegister(STATUS_REG);
        volume_valor = conn.readRegister(VOLUME_REG);
        status_vs_valor = conn.readRegister(STATUS_VS_REG);
        max_vol_valor = conn.readRegister(MAX_VOL_REG);
        vazao_valor = conn.readRegister(VAZAO_REG);
        mult_factor_valor = conn.readRegister(MULT_FACTOR_REG);
        volume_barril_valor = conn.readRegister(VOLUME_BARRIL_REG);
        problema_vs_valor = conn.readRegister(PROBLEMA_VS_REG);

        sleep(200);
        return monitorsCLPOp();
    }


    //Retorna o significado dos registradores
    public String getBatelaStr() { //status da servida
        int valor = batelada_valor;

        switch (valor) {
            case 0:
                return "Esperando (0)";
            case 1:
                return "Inicia servida (1)";
            case 2:
                return "Comando para fechar válvula (2)";
            case 3:
                return "Cliente terminou de se servir (3)";
            case 4:
                return "Servida finalizada (4)";
            default:
                return "Erro na leitura (" + valor + ")";
        }
    }

    public String getStatusStr() { //status da chopeira
        int valor = status_valor;

        switch (valor) {
            case 10:
                return "Parada (10)";
            case 20:
                return "Programada (20)";
            case 30:
                return "Torneira aberta (30)";
            case 40:
                return "Servida finalizada (40)";
            default:
                return "Erro na leitura (" + valor + ")";
        }
    }

    public String getVolumeStr() { //status da chopeira
        return String.valueOf(volume_valor);
    }

    public String getStatusValvulaStr() { //status da chopeira
        int valor = status_vs_valor;

        switch (valor) {
            case 0:
                return "Fechada (0)";
            case 1:
                return "Aberta (1)";
            default:
                return "Erro na leitura (" + valor + ")";
        }
    }

    public String getVazaoStr() {
        return String.valueOf(vazao_valor);
    }

    public String getMultFatorStr() {
        return String.valueOf(mult_factor_valor);
    }

    public String getVolumeBarrilStr() {
        return String.valueOf(volume_barril_valor);
    }

    public String getProblemaVsStr() {
        int valor = problema_vs_valor;

        switch (valor) {
            case 0:
                return "Normal (0)";
            case 1:
                return "Possível vazamento (1)";
            default:
                return "Erro na leitura (" + valor + ")";
        }
    }

    public void setFinalizaOp(boolean status){
        this.finalizaOp = status;
    }

    public void setMaxVol(int max){
        conn.writeRegisters(MAX_VOL_REG, max);
        Log.d("maximo", String.valueOf(max));
    }

    public void closeCon(){
        conn.closesCon();
    }

    public int getMaxVol(){
        return conn.readRegister(MAX_VOL_REG);
    }

}