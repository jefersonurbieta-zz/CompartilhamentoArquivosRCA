package br.com.urbieta.jeferson.rca;

import br.com.urbieta.jeferson.rca.data.Arquivo;
import br.com.urbieta.jeferson.data.Preferences;
import br.com.urbieta.jeferson.rca.client.RCAClient;
import br.com.urbieta.jeferson.rca.server.RCAServer;
import br.com.urbieta.jeferson.util.PreferencesUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class RCAApplication {

    private static final Logger logger = Logger.getLogger(RCAApplication.class);

    private RCAServer rcaServer;

    private RCAClient rcaClient;

    private DatagramSocket serverSocket;

    public void start() {
        try {
            Preferences preferences = PreferencesUtils.getPreferences();
            serverSocket = new DatagramSocket(preferences.getPort());
            rcaServer = new RCAServer(preferences, serverSocket);
            rcaServer.start();
            rcaClient = new RCAClient(preferences, serverSocket);
            logger.info("Aplicação RCA iniciada");
        } catch (SocketException e) {
            logger.error(e);
        }
    }

    public void adicionarArquivosLocaisNaLista() throws UnknownHostException {
        rcaClient.adicionarArquivosLocaisNaLista();
    }

    public void solicitarListaUsuario() throws IOException {
        rcaClient.solicitarListaUsuario();
    }

    public void solicitarListaArquivos() throws IOException {
        rcaClient.solicitarListaArquivos();
    }

    public void baixarArquivo(Arquivo arquivo) throws IOException {
    	rcaClient.solicitarDownloadArquivo(arquivo);
    }

    public void pesquiarArquivo(String nomeArquivo) throws IOException {
        rcaClient.pesquiarArquivo(nomeArquivo);
    }
    
    public void stop() {
        rcaServer.stop();
        serverSocket.close();
    }
}
