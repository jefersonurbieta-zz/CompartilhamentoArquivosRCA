package br.com.urbieta.jeferson.rca.data;

import java.util.Objects;

public class Arquivo {

    private String nome;

    private Boolean baixado;

    private String ipCliente;

    public Arquivo(String nome) {
        this.nome = nome;
    }

    public Arquivo(String nome, String ipCliente) {
        this.nome = nome;
        this.ipCliente = ipCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getBaixado() {
        return baixado;
    }

    public void setBaixado(Boolean baixado) {
        this.baixado = baixado;
    }

    public String getIpCliente() {
        return ipCliente;
    }

    public void setIpCliente(String ipCliente) {
        this.ipCliente = ipCliente;
    }

    public String getBaixadoLbn() {
		if(baixado != null && baixado) {
			return "Sim";
		}
    	return "Não";
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arquivo arquivo = (Arquivo) o;
        return Objects.equals(nome, arquivo.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}
