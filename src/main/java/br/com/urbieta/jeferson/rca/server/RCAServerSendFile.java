package br.com.urbieta.jeferson.rca.server;

import br.com.urbieta.jeferson.data.Preferences;
import br.com.urbieta.jeferson.util.FileUtils;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class RCAServerSendFile extends Thread {

    private static final Logger logger = Logger.getLogger(RCAServerSendFile.class);

    private Preferences preferences;

    private Integer port;

    private String fileName;

    RCAServerSendFile(Preferences preferences, Integer port, String fileName) {
        this.preferences = preferences;
        this.port = port;
        this.fileName = fileName;
    }

    public void run() {
        logger.info("Iniciado Thread Server Send File");
        try {
            //Initialize Sockets
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socketClient = serverSocket.accept();

            //Specify the file
            File file = new File(FileUtils.pathFile(preferences.getDiretorioArquivos(), fileName));
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            //Get socket's output stream
            OutputStream os = socketClient.getOutputStream();

            //Read File Contents into contents array
            byte[] contents;
            long fileLength = file.length();
            long current = 0;

            long start = System.nanoTime();
            while (current != fileLength) {
                int size = 10000;
                if (fileLength - current >= size)
                    current += size;
                else {
                    size = (int) (fileLength - current);
                    current = fileLength;
                }
                contents = new byte[size];
                bufferedInputStream.read(contents, 0, size);
                os.write(contents);
                logger.info("Sending file ... " + (current * 100) / fileLength + "% complete!");
            }
            os.flush();
            //File transfer done. Close the socket connection!
            socketClient.close();
            serverSocket.close();
            logger.info("File sent succesfully!");
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}