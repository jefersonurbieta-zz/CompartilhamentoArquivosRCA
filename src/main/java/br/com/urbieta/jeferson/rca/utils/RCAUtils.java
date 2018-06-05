package br.com.urbieta.jeferson.rca.utils;

import br.com.urbieta.jeferson.rca.enumeration.RCAActions;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class RCAUtils {

    public static InetAddress getBroadcastAddress() throws UnknownHostException {
        return InetAddress.getByName("255.255.255.255");
    }

    public static RCAActions retornarAcaoDaMensagem(String mensagem) {
        if (mensagem.contains("#")) {
            String[] msg = mensagem.split("#");
            return RCAActions.valueOf(msg[0].trim().toUpperCase());
        }
        return RCAActions.valueOf(mensagem.trim().toUpperCase());
    }

    public static String retornarConteudoDaMensagem(String mensagem) {
        if (mensagem.contains("#")) {
            String[] msg = mensagem.split("#");
            return msg[1].trim();
        }
        return mensagem;
    }

    public static String[] separarMensagem(String mensagem) {
        if (mensagem.contains("#")) {
            return mensagem.split("#");
        }
        String[] msg = new String[1];
        msg[0] = mensagem;
        return msg;
    }

    public static InetAddress getLocalHostLANAIP() throws UnknownHostException {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 15151);
            return socket.getLocalAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}
