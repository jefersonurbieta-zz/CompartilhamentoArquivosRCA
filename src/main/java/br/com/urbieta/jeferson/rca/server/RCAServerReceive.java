package br.com.urbieta.jeferson.rca.server;

import br.com.urbieta.jeferson.data.Preferences;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class RCAServerReceive extends Thread {

    private static final Logger logger = Logger.getLogger(RCAServerReceive.class);

    private DatagramSocket serverSocket;

    private Preferences preferences;

    private Boolean running = Boolean.TRUE;

    RCAServerReceive(DatagramSocket serverSocket, Preferences preferences) {
        this.serverSocket = serverSocket;
        this.preferences = preferences;
    }

    public void run() {
        logger.info("Iniciado Thread Server Receive");
        try {
            while (running) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                new RCAServerProcessRequisition(serverSocket, preferences, receivePacket).start();
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }
}