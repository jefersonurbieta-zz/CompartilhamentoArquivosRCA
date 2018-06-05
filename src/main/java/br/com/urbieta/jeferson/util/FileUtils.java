package br.com.urbieta.jeferson.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static boolean validarExistenciaDiretorio(String path) {
        File f = new File(path);
        return f.exists() && f.isDirectory();
    }

    public static List<String> listarNomesArquivosDiretorio(String path) {
        List<String> results = new ArrayList<>();
        File directory = new File(path);
        if (!directory.exists()) {
            throw new RuntimeException("Diretorio não encontrado!");
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return results;
    }

    public static String pathFile(String path, String fileName) {
        return path + File.separator + fileName;
    }

    public static BufferedInputStream prepararArquivoParaEnviar(String path, String fileName) throws FileNotFoundException {
        File file = new File(path + File.separator + fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        return new BufferedInputStream(fileInputStream);
    }

    public static BufferedOutputStream prepararArquivoParaReceber(String path, String fileName) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);
        return new BufferedOutputStream(fos);
    }
}
