package com.fluidobjects.drafttapcontroller;

import android.os.StrictMode;
import android.util.Log;

import static android.os.SystemClock.sleep;

class Equipment {

    private ConectionTCP conn;
    private int volumeProgramado;
    private int statusBatelada;
    private int volume;

    // REGISTRADORES CLP
    private int BATELADA_REG = 3000;
    private int STATUS_REG = 3003;
    private int VOLUME_REG = 3004;
    private int STATUS_VALVULA_REG = 3005;
    private int MAX_VOL_REG = 3007;
    private int VAZAO_REG = 3008;
    private int MULT_FACTOR_REG = 3012;
    private int VOLUME_BARRIL_REG = 3014;
    private int PROBLEMA_VS_REG = 3015;

    String TAG = "CLP Manager";

    private boolean finalizaOp;

    Equipment(String ip)throws Exception{
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        finalizaOp = false;
        try{
            conn = new ConectionTCP(ip, 502);
            if(conn.readRegister(BATELADA_REG)==3)
                conn.writeRegisters(BATELADA_REG, 4);
        }catch (Exception e){
            throw new Exception("Could not open connection with device ip: "+ip);
        }

    }

    //É chamado para abrir a batelada
    boolean open(int fator, int volProgramado) throws Exception{
        boolean isConnected = conn.con.isConnected();
        Log.d(TAG, "Abrindo Batelada!" + "Conected?!" + isConnected);
        int status = 0;
        try{
            status = conn.readRegister(STATUS_REG);
        }catch (Exception e){
            throw new Exception("Could not read status register of the equipment");
        }

        boolean aux = (status == 10 || status == 20);
        if (aux) {
            try{
                conn.writeRegisters(MULT_FACTOR_REG, fator);
                conn.writeRegisters(MAX_VOL_REG, volProgramado); //seta o volume maximo
                conn.writeRegisters(BATELADA_REG, 1); //abre a batelada
            }catch (Exception e){
                throw new Exception("Could not write registers of the equipment");
            }
            statusBatelada = 1;
            Log.d(TAG, "abriu batelada");
            volume = 0;
            return true;
        }
        return false;
    }


    int monitorsVolume() throws Exception{
        try{
            this.statusBatelada = conn.readRegister(BATELADA_REG);
        }catch (Exception e){
            throw new Exception("Could not read valve status register");
        }
        while (this.statusBatelada != 3) {
            int volumeLido = 0;
            try{
                volumeLido = conn.readRegister(VOLUME_REG);
            }catch (Exception e){
                throw new Exception("Could not read volume register");
            }
            if (volume < volumeLido) {
                volume = volumeLido;
                Log.d(TAG, "listening - Volume lido: " + volumeLido);
            }
            try{
                this.statusBatelada = conn.readRegister(BATELADA_REG);
            }catch (Exception e){
                throw new Exception("Could not read valve status register");
            }
        }
        Log.d(TAG, "Encerrou Batelada");
        try{
            conn.writeRegisters(BATELADA_REG, 4);
        }catch (Exception e){
            throw new Exception("Could not write valve status register");
        }
        this.statusBatelada = 4;
        conn.closesCon();
        return volume;
    }

    int getVolume() {
        return volume;
    }

    boolean isServing() {
        if (statusBatelada == 4) return true;
        return false;
    }

    void closeCon() {
        conn.con.isConnected();
        conn.closesCon();
        conn.con.isConnected();
    }

    int getMaxVol()throws Exception{
        try{
            return conn.readRegister(MAX_VOL_REG);
        }catch (Exception e){
            throw new Exception("Could not read maximum volume register of the equipment");
        }
    }
}
