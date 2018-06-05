package br.com.urbieta.jeferson.rca.data;

import java.util.List;
import java.util.Objects;

public class Cliente {

    private String ip;

    private List<Arquivo> arquivos;

    private boolean clienteLocal = false;

    public Cliente(String ip, List<Arquivo> arquivos) {
        this.ip = ip;
        this.arquivos = arquivos;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<Arquivo> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<Arquivo> arquivos) {
        this.arquivos = arquivos;
    }

    public boolean isClienteLocal() {
        return clienteLocal;
    }

    public void setClienteLocal(boolean clienteLocal) {
        this.clienteLocal = clienteLocal;
    }

    @Override
    public String toString() {
        return ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(ip, cliente.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }
}
