package br.com.urbieta.jeferson.rca.client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.urbieta.jeferson.data.Preferences;
import br.com.urbieta.jeferson.rca.RCAState;
import br.com.urbieta.jeferson.rca.commom.RCASendPackage;
import br.com.urbieta.jeferson.rca.data.Arquivo;
import br.com.urbieta.jeferson.rca.data.Cliente;
import br.com.urbieta.jeferson.rca.enumeration.RCAActions;
import br.com.urbieta.jeferson.rca.enumeration.RCAUser;
import br.com.urbieta.jeferson.rca.utils.RCAUtils;
import br.com.urbieta.jeferson.util.FileUtils;

public class RCAClient {
	
	private static final Logger logger = Logger.getLogger(RCAClient.class);

    private Preferences preferences;

    private DatagramSocket serverSocket;

    public RCAClient(Preferences preferences, DatagramSocket serverSocket) {
        this.preferences = preferences;
        this.serverSocket = serverSocket;
    }
    
    public void adicionarArquivosLocaisNaLista() throws UnknownHostException {
    	logger.info("Iniciado sicronização com diretori de arquivos");
    	boolean alterarCliente = true;
        Cliente cliente = RCAState.getClienteLocal();
        if(cliente == null) {
        	alterarCliente = false;
        	InetAddress myIPAddress = RCAUtils.getLocalHostLANAIP();
            cliente = new Cliente(myIPAddress.getHostAddress(), new ArrayList<Arquivo>());
            cliente.setClienteLocal(true);
        }
    	List<String> arquivos = FileUtils.listarNomesArquivosDiretorio(preferences.getDiretorioArquivos());
        for (String nomeArq : arquivos) {
            Arquivo arquivo = new Arquivo(nomeArq);
            arquivo.setBaixado(true);
            if(!cliente.getArquivos().contains(arquivo)) {
            	cliente.getArquivos().add(arquivo);
            }
        }
        if(alterarCliente) {
        	RCAState.alterarCliente(cliente);        	
        } else {
        	RCAState.addCliente(cliente);
        }
    }

    public void solicitarListaUsuario() throws IOException {
        String mensagem = RCAActions.FIND_USERS.name();
        new RCASendPackage(RCAUser.CLIENT, serverSocket, preferences.getPort(), mensagem).start();
    }

    public void solicitarListaArquivos() throws IOException {
        String mensagem = RCAActions.FIND_FILES.name();
        new RCASendPackage(RCAUser.CLIENT, serverSocket, preferences.getPort(), mensagem).start();
    }

    public void pesquiarArquivo(String nomeArquivo) throws IOException {
    	RCAState.limparArquivosPesquisa();
        String mensagem = RCAActions.FIND_FILE.name() + "#" + nomeArquivo;
        new RCASendPackage(RCAUser.CLIENT, serverSocket, preferences.getPort(), mensagem).start();
    }

    public void solicitarDownloadArquivo(Arquivo arquivo) throws IOException {
    	InetAddress inetAddress = InetAddress.getByName(arquivo.getIpCliente());
        String mensagem = RCAActions.SEND_FILE.name() + "#" + arquivo.getNome();
        new RCASendPackage(RCAUser.CLIENT, serverSocket, inetAddress, preferences.getPort(), mensagem).start();
    }

}