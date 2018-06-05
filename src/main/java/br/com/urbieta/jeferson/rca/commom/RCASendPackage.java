package br.com.urbieta.jeferson.rca.commom;

import br.com.urbieta.jeferson.rca.enumeration.RCAUser;
import br.com.urbieta.jeferson.rca.utils.RCAUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class RCASendPackage extends Thread {

    private static final Logger logger = Logger.getLogger(RCASendPackage.class);

    private RCAUser rcaUser;

    private DatagramSocket serverSocket;

    private InetAddress iPAddress;

    private Integer port;

    private String menssagem;

    public RCASendPackage(RCAUser rcaUser, DatagramSocket serverSocket, Integer port, String menssagem) throws UnknownHostException {
        this.rcaUser = rcaUser;
        this.serverSocket = serverSocket;
        this.iPAddress = RCAUtils.getBroadcastAddress();
        this.port = port;
        this.menssagem = menssagem;
    }

    public RCASendPackage(RCAUser rcaUser, DatagramSocket serverSocket, InetAddress iPAddress, Integer port, String menssagem) {
        this.rcaUser = rcaUser;
        this.serverSocket = serverSocket;
        this.iPAddress = iPAddress;
        this.port = port;
        this.menssagem = menssagem;
    }

    public void run() {
        byte[] sendData = menssagem.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, iPAddress, port);
        try {
            serverSocket.send(sendPacket);
            logger.info(rcaUser.name() + " SENT: " + menssagem + " - BYTES: " + menssagem.getBytes().length);
        } catch (IOException e) {
            logger.error(e);
        }
    }

}