package br.com.urbieta.jeferson.rca.server;

import br.com.urbieta.jeferson.data.Preferences;
import br.com.urbieta.jeferson.rca.RCAState;
import br.com.urbieta.jeferson.rca.client.RCAClientReceiveFile;
import br.com.urbieta.jeferson.rca.commom.RCASendPackage;
import br.com.urbieta.jeferson.rca.data.Arquivo;
import br.com.urbieta.jeferson.rca.data.Cliente;
import br.com.urbieta.jeferson.rca.enumeration.RCAActions;
import br.com.urbieta.jeferson.rca.enumeration.RCAUser;
import br.com.urbieta.jeferson.rca.utils.RCAUtils;
import br.com.urbieta.jeferson.util.FileUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class RCAServerProcessRequisition extends Thread {

    private static final Logger logger = Logger.getLogger(RCAServerProcessRequisition.class);

    private DatagramSocket serverSocket;

    private Preferences preferences;

    private DatagramPacket receivePacket;

    RCAServerProcessRequisition(DatagramSocket serverSocket, Preferences preferences, DatagramPacket receivePacket) {
        this.serverSocket = serverSocket;
        this.preferences = preferences;
        this.receivePacket = receivePacket;
    }

    public void run() {
        try {
            String sentence = new String(receivePacket.getData());
            InetAddress addressIP = receivePacket.getAddress();
            Integer port = receivePacket.getPort();
            logger.info("SERVER RECEIVED: FROM: " + addressIP.getHostAddress() + ":" + port + " - DATA: " + sentence);
            RCAActions action = RCAUtils.retornarAcaoDaMensagem(sentence);
            switch (action) {
                case FIND_USERS:
                    enviarIdServidor(serverSocket, addressIP, preferences.getPort());
                    break;
                case LIST_USERS:
                    receberListaUsuario(addressIP);
                    break;
                case FIND_FILES:
                    enviarNomesArquivos(serverSocket, addressIP, preferences.getPort());
                    break;
                case LIST_FILES:
                    receberListaArquivo(sentence, addressIP);
                    break;
                case FIND_FILE:
                    responderPesquisarArquivo(sentence, serverSocket, addressIP, preferences.getPort());
                    break;
                case SEND_FILE:
                    enviarArquivoParaCliente(sentence, serverSocket, addressIP, preferences.getPort());
                    break;
                case HAVE_FILE:
                    receberInformacoesArquivoPesquisado(sentence, addressIP);
                    break;
                case UPLOAD_FILE:
                    iniciarRecebimentoArquivo(sentence, addressIP);
                    break;
            }
        } catch (IOException | NullPointerException e) {
            logger.error(e);
        }
    }

    private void enviarIdServidor(DatagramSocket serverSocket, InetAddress iPAddress, Integer port) throws UnknownHostException {
        InetAddress myIPAddressInLan = RCAUtils.getLocalHostLANAIP();
        if (myIPAddressInLan != null && iPAddress.getHostAddress().equals(myIPAddressInLan.getHostAddress())) {
            return;
        }
        InetAddress myIPAddress = myIPAddressInLan;
        if (myIPAddress == null) {
            myIPAddress = InetAddress.getLocalHost();
        }
        String mensagem = RCAActions.LIST_USERS.name() + "#" + myIPAddress.getHostAddress();
        new RCASendPackage(RCAUser.SERVER, serverSocket, iPAddress, port, mensagem).start();
    }

    private void enviarNomesArquivos(DatagramSocket serverSocket, InetAddress iPAddress, Integer port) throws UnknownHostException {
        InetAddress myIPAddressInLan = RCAUtils.getLocalHostLANAIP();
        if (myIPAddressInLan != null && iPAddress.getHostAddress().equals(myIPAddressInLan.getHostAddress())) {
            return;
        }
        List<String> arquivos = FileUtils.listarNomesArquivosDiretorio(preferences.getDiretorioArquivos());
        for (String nomeArquivo : arquivos) {
            String mensagem = RCAActions.LIST_FILES.name() + "#" + nomeArquivo;
            new RCASendPackage(RCAUser.SERVER, serverSocket, iPAddress, port, mensagem).start();
        }
    }

    private void receberListaUsuario(InetAddress iPAddress) {
        Cliente cliente = new Cliente(iPAddress.getHostAddress(), new ArrayList<Arquivo>());
        RCAState.addCliente(cliente);
    }

    private void receberListaArquivo(String mensagem, InetAddress addressIP) {
        String nomeArquivo = RCAUtils.retornarConteudoDaMensagem(mensagem);
        Arquivo arquivo = new Arquivo(nomeArquivo, addressIP.getHostAddress());
        RCAState.addArquivo(addressIP.getHostAddress(), arquivo);
    }

    private void responderPesquisarArquivo(String mensagem, DatagramSocket serverSocket, InetAddress addressIP, Integer port) {
        String nomeArquivoPesquisa = RCAUtils.retornarConteudoDaMensagem(mensagem);
        List<String> meusArquivos = FileUtils.listarNomesArquivosDiretorio(preferences.getDiretorioArquivos());
        for (String nomeArq : meusArquivos) {
            if (nomeArq.contains(nomeArquivoPesquisa)) {
                String resposta = RCAActions.HAVE_FILE.name() + "#" + nomeArq;
                new RCASendPackage(RCAUser.SERVER, serverSocket, addressIP, port, resposta).start();
            }
        }
    }

    private void enviarArquivoParaCliente(String mensagem, DatagramSocket serverSocket, InetAddress iPAddress, Integer port) {
        String nomeArquivo = RCAUtils.retornarConteudoDaMensagem(mensagem);
        new RCAServerSendFile(preferences, preferences.getPortFileTransfer(), nomeArquivo).start();
        String resposta = RCAActions.UPLOAD_FILE.name() + "#" + nomeArquivo + "#" + preferences.getPortFileTransfer();
        new RCASendPackage(RCAUser.SERVER, serverSocket, iPAddress, port, resposta).start();
    }
    
    private void receberInformacoesArquivoPesquisado(String mensagem, InetAddress iPAddress) {
        String[] informacoes = RCAUtils.separarMensagem(mensagem);
        Arquivo arquivo = new Arquivo(informacoes[1], iPAddress.getHostAddress());
        RCAState.addArquivosPesquisa(arquivo);
    }

    private void iniciarRecebimentoArquivo(String mensagem, InetAddress iPAddress) {
        String[] informacoes = RCAUtils.separarMensagem(mensagem);
        Integer portUpload = Integer.valueOf(informacoes[2].trim());
        new RCAClientReceiveFile(preferences, iPAddress, portUpload, informacoes[1].trim()).start();
    }

}