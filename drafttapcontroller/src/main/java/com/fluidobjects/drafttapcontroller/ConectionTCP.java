package com.fluidobjects.drafttapcontroller;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleRegister;

import java.net.InetAddress;
import java.net.UnknownHostException;

class ConectionTCP {

    TCPMasterConnection con; //the connection
    /* Variables for storing the parameters */
    InetAddress addr; //the slave's address
    int port;



    ConectionTCP(String ip, int port){
        con = null;
        try {
            this.addr = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = port;
        con = new TCPMasterConnection(addr);
        con.setPort(port);
        con.setTimeout(5000);
        try {
            con.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Using jamod lib

    //Escreve um valor em um registrador
    void writeRegisters(int register, int value){
        try {
            SimpleRegister reg = new SimpleRegister(value);
            WriteSingleRegisterRequest write = new WriteSingleRegisterRequest(register, reg);
            ModbusTCPTransaction transaction = new ModbusTCPTransaction(con);
            transaction.setRequest(write);
            transaction.execute();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //Le o valor de um registrador
    int readRegister(int register){
        int valor = 0;
        try {
            ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(register, 1);
            request.setUnitID(1);
            ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) executeTransaction(con, request);
            valor = response.getRegisterValue(0);
            return valor;
        }catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    //Executa a transacao e recebe a resposta
    private static ModbusResponse executeTransaction(TCPMasterConnection connection,
                                                     ModbusRequest request)
            throws ModbusIOException, ModbusSlaveException, ModbusException {
        ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
        transaction.setRequest(request);
        transaction.execute();
        return transaction.getResponse();
    }

    void closesCon()
    {
        con.close();
    }



}
