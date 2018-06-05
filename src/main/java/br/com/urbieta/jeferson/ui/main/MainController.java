package br.com.urbieta.jeferson.ui.main;

import br.com.urbieta.jeferson.rca.RCAApplication;
import br.com.urbieta.jeferson.rca.RCAState;
import br.com.urbieta.jeferson.rca.data.Arquivo;
import br.com.urbieta.jeferson.rca.data.Cliente;
import br.com.urbieta.jeferson.util.AlertUtils;
import br.com.urbieta.jeferson.util.ApplicationUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final Logger logger = Logger.getLogger(MainController.class);

    private RCAApplication rcaApplication;

    private Timeline timelineAtualizacaoTela;
    
    private Timeline timelineAtualizacaoClientes;

    @FXML
    private StackPane rootPane;

    @FXML
    private ListView<Cliente> listaClientes;

    @FXML
    private TableView<Arquivo> arquivosCliente;

    @FXML
    private TableColumn<Arquivo, String> nomeArquivoCol;

    @FXML
    private TableColumn<Arquivo, String> baixadoCol;

    @FXML
    private TextField pesquisaTxt;

    @FXML
    private Button pesquisaBtn;

    @FXML
    private Button baixarBtn;

    @FXML
    private TableView<Arquivo> pesquisaTbl;

    @FXML
    private TableColumn<Arquivo, String> pesquisaColNome;

    @FXML
    private TableColumn<Arquivo, String> pesquisaColProprietario;

    @FXML
    private TableColumn<Arquivo, String> pesquisaColBaixado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            rcaApplication = new RCAApplication();
            rcaApplication.start();
            rcaApplication.solicitarListaUsuario();
            rcaApplication.solicitarListaArquivos();
            rcaApplication.adicionarArquivosLocaisNaLista();
            atualizarDados();
            adicionarListener();
            initCol();
            initRotinaDeAtualizacaoUI();
            initRotinaDeAtualizacaoClientes();
            listaClientes.getSelectionModel().selectFirst();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void initCol() {
        nomeArquivoCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        baixadoCol.setCellValueFactory(new PropertyValueFactory<>("baixadoLbn"));
        pesquisaColNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        pesquisaColProprietario.setCellValueFactory(new PropertyValueFactory<>("ipCliente"));
        pesquisaColBaixado.setCellValueFactory(new PropertyValueFactory<>("baixadoLbn"));
    }

    private void initRotinaDeAtualizacaoUI() {
        timelineAtualizacaoTela = new Timeline(new KeyFrame(Duration.seconds(5), ev -> {
            atualizarDados();
        }));
        timelineAtualizacaoTela.setCycleCount(Animation.INDEFINITE);
        timelineAtualizacaoTela.play();
    }
    
    private void initRotinaDeAtualizacaoClientes() {
    	timelineAtualizacaoClientes = new Timeline(new KeyFrame(Duration.seconds(30), ev -> {
            try {
				rcaApplication.solicitarListaUsuario();
				rcaApplication.solicitarListaArquivos();
	            rcaApplication.adicionarArquivosLocaisNaLista();
            } catch (IOException e) {
            	logger.error(e);
			}
        }));
    	timelineAtualizacaoClientes.setCycleCount(Animation.INDEFINITE);
    	timelineAtualizacaoClientes.play();
    }

    private void atualizarDados() {
        ObservableList<Cliente> observableClientesList = FXCollections.observableArrayList(RCAState.getClientes());
        listaClientes.setItems(observableClientesList);
        ObservableList<Arquivo> observableArquivosList = FXCollections.observableArrayList(RCAState.getArquivosPesquisa());
        pesquisaTbl.setItems(observableArquivosList);
        ObservableList<Cliente> clientesSelecionados = listaClientes.getSelectionModel().getSelectedItems();
        if (clientesSelecionados.size() > 0) {
            for (int i = 0; i < arquivosCliente.getItems().size(); i++) {
                arquivosCliente.getItems().clear();
            }
            clientesSelecionados.forEach((cliente) -> {
                ObservableList<Arquivo> observableList = FXCollections.observableArrayList(RCAState.getArquivos(cliente.getIp()));
                arquivosCliente.setItems(observableList);
            });
        }
        baixarBtn.setDisable(RCAState.getBaixando());
    }

    private void adicionarListener() {
        listaClientes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Cliente>() {
            @Override
            public void changed(ObservableValue<? extends Cliente> observable, Cliente oldValue, Cliente newValue) {
                ObservableList<Arquivo> observableList;
                if (newValue != null) {
                    observableList = FXCollections.observableArrayList(RCAState.getArquivos(newValue.getIp()));
                } else {
                    observableList = FXCollections.observableArrayList(RCAState.getArquivos(oldValue.getIp()));
                }
                arquivosCliente.setItems(observableList);
            }
        });
        pesquisaBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    if (ApplicationUtils.isNullOrBlank(pesquisaTxt.getText())) {
                        AlertUtils.showErrorMessage("Digite algo para pesquisar!", "");
                        return;
                    }
                    rcaApplication.pesquiarArquivo(pesquisaTxt.getText());
                } catch (IOException e1) {
                    AlertUtils.showErrorMessage("Ocorreu um erro ao tentar pesquiar o arquivo",
                            "Por favor tente mais tarde.");
                    logger.error(e1);
                }
            }
        });
        baixarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    ObservableList<Arquivo> arquivosSelecionados = pesquisaTbl.getSelectionModel().getSelectedItems();
                    if (arquivosSelecionados.size() == 0) {
                        AlertUtils.showErrorMessage("Selecione algum arquivo para baixar!", "");
                        return;
                    }
                    arquivosSelecionados.forEach((arquivo) -> {
                        try {
                            rcaApplication.baixarArquivo(arquivo);
                        } catch (IOException e1) {
                            AlertUtils.showErrorMessage("Ocorreu um erro ao tentar pesquiar o arquivo", "Por favor tente mais tarde.");
                            logger.error(e1);
                        }
                        AlertUtils.showSimpleAlert("Seu pedido de download foi encaminhado!", "Em alguns instantes o arquivo estará em seu diretorio.");
                        return;
                    });
                } catch (Exception e1) {
                    AlertUtils.showErrorMessage("Ocorreu um erro ao tentar pesquiar o arquivo", "Por favor tente mais tarde.");
                    logger.error(e1);
                }
            }
        });
    }

    @FXML
    private void handleMenuClose(ActionEvent event) {
        logger.info("Aplicação encerrada");
        rcaApplication.stop();
        timelineAtualizacaoTela.stop();
        timelineAtualizacaoClientes.stop();
        System.exit(0);
    }

    @FXML
    private void handleAboutMenu(ActionEvent event) {
        ApplicationUtils.loadWindow(getClass().getResource("/fxml/about.fxml"), "About Me", null);
    }

    @FXML
    private void handleMenuSettings(ActionEvent event) {
        ApplicationUtils.loadWindow(getClass().getResource("/fxml/settings.fxml"), "Settings", null);
    }

}
