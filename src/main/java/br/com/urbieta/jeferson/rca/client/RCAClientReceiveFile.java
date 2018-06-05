package br.com.urbieta.jeferson.rca.client;

import br.com.urbieta.jeferson.data.Preferences;
import br.com.urbieta.jeferson.rca.RCAState;
import br.com.urbieta.jeferson.util.FileUtils;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;


public class RCAClientReceiveFile extends Thread {

    private static final Logger logger = Logger.getLogger(RCAClientReceiveFile.class);

    private Preferences preferences;

    private InetAddress inetAddress;

    private Integer port;

    private String fileName;

    public RCAClientReceiveFile(Preferences preferences, InetAddress inetAddress, Integer port, String fileName) {
        this.preferences = preferences;
        this.inetAddress = inetAddress;
        this.port = port;
        this.fileName = fileName;
    }

    public void run() {
        logger.info("Iniciado Thread Client Receive File");
        RCAState.setBaixando(Boolean.TRUE);
        try {
            //Initialize socket
            Socket socket;
            socket = new Socket(inetAddress, port);
            byte[] contents = new byte[10000];

            //Initialize the FileOutputStream to the output file's full path.
            BufferedOutputStream bos = FileUtils.prepararArquivoParaReceber(preferences.getDiretorioArquivos(), fileName);
            InputStream is = socket.getInputStream();

            //No of bytes read in one read() call
            int bytesRead = 0;

            while ((bytesRead = is.read(contents)) != -1)
            	bos.write(contents, 0, bytesRead);

            bos.flush();
            socket.close();
            RCAState.atualizarListagemArquivosAposDownload(fileName);
            RCAState.limparArquivosPesquisa();
            logger.info("File saved successfully!");
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        RCAState.setBaixando(Boolean.FALSE);
    }
}