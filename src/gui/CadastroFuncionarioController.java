package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Funcionario;
import model.services.FuncionarioService;

public class CadastroFuncionarioController implements Initializable{
	
	private FuncionarioService service;
	
	@FXML
	private TableView<Funcionario> tableViewFuncionario;
	
	@FXML
	private TableColumn<Funcionario, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnNome;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnRg;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnCpf;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnTelefone;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Funcionario> obsList;
	
	public void setFuncionarioService(FuncionarioService service) {
		this.service = service;
	}
	
	
	@FXML
	private void onBtNew(ActionEvent event) {
		System.out.println("onBtNew");
	}	
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ininiciandoNodes();
		
	}

	private void ininiciandoNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnRg.setCellValueFactory(new PropertyValueFactory<>("rg"));
		tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFuncionario.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("O service estava nulo");
		}
		
		List<Funcionario>list =  service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewFuncionario.setItems(obsList);
	}

}
