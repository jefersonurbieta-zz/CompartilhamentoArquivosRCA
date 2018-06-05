package br.com.urbieta.jeferson.rca.server;

import br.com.urbieta.jeferson.data.Preferences;
import org.apache.log4j.Logger;

import java.net.DatagramSocket;

public class RCAServer {

    private static final Logger logger = Logger.getLogger(RCAServer.class);

    private RCAServerReceive rcaServerReceive;

    public RCAServer(Preferences preferences, DatagramSocket serverSocket) {
        this.rcaServerReceive = new RCAServerReceive(serverSocket, preferences);
    }

    public void start() {
        rcaServerReceive.start();
        logger.info("Server RCA iniciado");
    }

    public void stop() {
        rcaServerReceive.setRunning(Boolean.FALSE);
        logger.info("Server RCA parado");
    }


}
