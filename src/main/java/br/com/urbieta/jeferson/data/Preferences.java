package br.com.urbieta.jeferson.data;

public class Preferences {

    String username;

    Integer port;
    
    Integer portFileTransfer;

    String diretorioArquivos;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getPortFileTransfer() {
		return portFileTransfer;
	}

	public void setPortFileTransfer(Integer portFileTransfer) {
		this.portFileTransfer = portFileTransfer;
	}

	public String getDiretorioArquivos() {
        return diretorioArquivos;
    }

    public void setDiretorioArquivos(String diretorioArquivos) {
        this.diretorioArquivos = diretorioArquivos;
    }
}
