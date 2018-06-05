package br.com.urbieta.jeferson.rca;

import br.com.urbieta.jeferson.rca.data.Arquivo;
import br.com.urbieta.jeferson.rca.data.Cliente;
import br.com.urbieta.jeferson.util.ApplicationUtils;

import java.util.ArrayList;
import java.util.List;

public class RCAState {

    private static List<Cliente> clientes = new ArrayList<Cliente>();

    private static List<Cliente> clientesPesquisa = new ArrayList<Cliente>();

    private static List<Arquivo> arquivosPesquisa = new ArrayList<Arquivo>();

    private static Boolean baixando = Boolean.FALSE;

    public static List<Arquivo> getArquivos(String cliente) {
        for (Cliente c : clientes) {
            if (c.getIp().equals(cliente)) {
                return c.getArquivos();
            }
        }
        return null;
    }

    public static void addArquivo(String ipCliente, Arquivo arquivo) {
        Cliente clienteEncontrado = null;
    	for (Cliente c : clientes) {
            if (c.getIp().equals(ipCliente)) {
                clienteEncontrado = c;
            }
        }
    	if(clienteEncontrado == null) {
    		clienteEncontrado = new Cliente(ipCliente, new ArrayList<Arquivo>());
    		addCliente(clienteEncontrado);
    	}
        if (ApplicationUtils.isNullOrEmpty(clienteEncontrado.getArquivos())) {
        	clienteEncontrado.setArquivos(new ArrayList<>());
        }
        verificarArquivoBaixado(arquivo);
        if(!clienteEncontrado.getArquivos().contains(arquivo)) {
        	clienteEncontrado.getArquivos().add(arquivo);
        }
    }
    
    public static void atualizarListagemArquivosAposDownload(String nomeArquivo) {
        Arquivo arq = new Arquivo(nomeArquivo);
        arq.setBaixado(Boolean.TRUE);
    	for (Cliente c : clientes) {
            if(c.getArquivos().contains(arq)) {
            	for(Arquivo arquivo : c.getArquivos()) {
            		if(arquivo.getNome().equals(arq.getNome())) {
            			arquivo.setBaixado(Boolean.TRUE);
            		}
            	}
            }
        	if (c.isClienteLocal()) {
        		arq.setIpCliente(c.getIp());
                c.getArquivos().add(arq);
            }
        }
    }


    public static List<Cliente> getClientes() {
        for (Cliente c : clientes) {
            if (c.isClienteLocal() && clientes.indexOf(c) != 0) {
                int index = clientes.indexOf(c);
                Cliente aux = clientes.get(0);
                clientes.set(0, c);
                clientes.set(index, aux);
            }
        }
        return clientes;
    }

    public static void addCliente(Cliente cliente) {
        if (!clientes.contains(cliente)) {
            clientes.add(cliente);
        }
    }
    
    public static Cliente getClienteLocal() {
        for (Cliente c : clientes) {
            if (c.isClienteLocal()) {
            	return c;
            }
        }
        return null;
    }

    public static void alterarCliente(Cliente cliente) {
        for (Cliente c : clientes) {
            if (c.getIp().equals(cliente.getIp())) {
                c.setArquivos(cliente.getArquivos());
                c.setClienteLocal(cliente.isClienteLocal());
            }
        }
    }

    public static List<Cliente> getClientesPesquisa() {
        return clientesPesquisa;
    }

    public static void addClientePesquisa(Cliente cliente) {
        if (!clientesPesquisa.contains(cliente)) {
            clientesPesquisa.add(cliente);
        }
    }

    public static void alterarClientePesquisa(Cliente cliente) {
        for (Cliente c : clientesPesquisa) {
            if (c.getIp().equals(cliente.getIp())) {
                c.setArquivos(cliente.getArquivos());
                c.setClienteLocal(cliente.isClienteLocal());
            }
        }
    }

    public static void addArquivosPesquisa(Arquivo arquivo) {
    	verificarArquivoBaixado(arquivo);
    	arquivosPesquisa.add(arquivo);
    }

    public static List<Arquivo> getArquivosPesquisa() {
        return arquivosPesquisa;
    }
    
    public static void limparArquivosPesquisa() {
        arquivosPesquisa = new ArrayList<Arquivo>();
    }

    public static Boolean getBaixando() {
        return baixando;
    }

    public static void setBaixando(Boolean baixando) {
        RCAState.baixando = baixando;
    }
    
    private static List<String> getNomesArquivosClienteLocal() {
        List<String> arquivosLocais = new ArrayList<String>();
        Cliente clienteLocal = null;
    	for (Cliente c : clientes) {
            if (c.isClienteLocal()) {
            	clienteLocal = c;
            	break;
            }
        }
    	if(clienteLocal != null) {
    		for(Arquivo arquivo : clienteLocal.getArquivos()) {
    			arquivosLocais.add(arquivo.getNome());
    		}
    	}
        return arquivosLocais;
    }
    
    private static void verificarArquivoBaixado(Arquivo arquivo) {
    	List<String> nomesArquivosLocais = getNomesArquivosClienteLocal();
    	if(nomesArquivosLocais.contains(arquivo.getNome().trim())) {
    		arquivo.setBaixado(Boolean.TRUE);
    	}
    }
}
