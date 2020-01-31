package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Aluno;
import model.entities.Funcionario;
import model.exceptions.ValidationException;
import model.services.AlunoService;
import model.services.FuncionarioService;

public class CadastroAlunoFormController implements Initializable{
	
	private Aluno entity;
	
	private AlunoService service;
	
	private FuncionarioService funcionarioService;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtRg;
	
	@FXML
	private DatePicker dpDataNascminto;
	
	@FXML
	private TextField txtTelefone;
	
	@FXML
	private ComboBox<Funcionario> comboBoxFuncionario;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Label labelErrorNome;
	@FXML
	private Label labelErrorRg;	
	@FXML
	private Label labelErrorTelefone;
	@FXML
	private Label labelErrorNascimento;
	
	private ObservableList<Funcionario> obsList;	
	
	//================== setters ===================
	public void setAluno(Aluno entity) {
		this.entity = entity;
	}
	
	public void setServices(AlunoService service, FuncionarioService funcionarioService) {
		this.service = service;
		this.funcionarioService = funcionarioService;
	}
	
	public void subscribeDataChangeListeners(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}
	//==============================================
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		System.out.println("onBtSaveAction");
		if (entity == null) {
			throw new IllegalStateException("entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();			
			Utils.currentStage(event).close();			
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}		
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChange();
		}
	}
	
	private Aluno getFormData() {
		Aluno obj = new Aluno();
		ValidationException exception = new ValidationException("validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			txtNome.setPromptText("");
			exception.addError("nome", " O campo não pode estar Vazio");
		}
		obj.setNome(txtNome.getText());
		
		if (txtRg.getText() == null) {
			txtRg.setPromptText("");
			exception.addError("rg", " O campo não pode estar Vazio");
		}
		obj.setRg(txtRg.getText());
		
		if (dpDataNascminto.getValue() == null) {
			dpDataNascminto.setPromptText("");
			exception.addError("dataNascimento", " O campo não pode estar Vazio");
		}else {	
		Instant instant = Instant.from(dpDataNascminto.getValue().atStartOfDay(ZoneId.systemDefault()));		
		obj.setDataNascimento(Date.from(instant));
		}
		
		if (txtTelefone.getText() == null) {	
			txtTelefone.setPromptText("");
			exception.addError("telefone", " O campo não pode estar Vazio");
		}		
		obj.setTelefone(txtTelefone.getText());
		
		obj.setFuncionario(comboBoxFuncionario.getValue());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}	
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		System.out.println("onBtCancelAction");
		Utils.currentStage(event).close();
	}	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();		
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldMaxLength(txtTelefone, 10);
		Constraints.setTextFieldMaxLength(txtRg, 15);
		Utils.formatDatePicker(dpDataNascminto, "dd/MM/yyyy");
		initializeComboBoxFuncionario();
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtRg.setText(entity.getRg());
		txtTelefone.setText(entity.getTelefone());	
		if (entity.getDataNascimento() != null ) {
			dpDataNascminto.setValue(LocalDate.ofInstant(entity.getDataNascimento().toInstant(), ZoneId.systemDefault()));
		}
		if (entity.getFuncionario() == null) {
			comboBoxFuncionario.getSelectionModel().selectFirst();
		}else {
		comboBoxFuncionario.setValue(entity.getFuncionario());
		}		
	}	
	
	public void loadAssociatedObjects() {
		if (funcionarioService == null) {
			throw new IllegalStateException("funcionarioService was null");
		}
		List<Funcionario> list = funcionarioService.findAll();
		obsList = FXCollections.observableList(list);
		comboBoxFuncionario.setItems(obsList);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String>fields = errors.keySet();
		
		if (fields.contains("nome")) {
			labelErrorNome.setText(errors.get("nome"));
		}
		if (fields.contains("rg")) {
			labelErrorRg.setText(errors.get("rg"));
		}		
		if (fields.contains("telefone")) {
			labelErrorTelefone.setText(errors.get("telefone"));
		}		
		if (fields.contains("dataNascimento")) {
			labelErrorNascimento.setText(errors.get("dataNascimento"));
		}		
	}
	
	private void initializeComboBoxFuncionario() { 
		Callback<ListView<Funcionario>, ListCell<Funcionario>> factory = lv -> new ListCell<Funcionario>() {
			@Override      
			protected void updateItem(Funcionario item, boolean empty) { 
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome()); 
				}
			}; 
	 
	 comboBoxFuncionario.setCellFactory(factory);
	 comboBoxFuncionario.setButtonCell(factory.call(null));  
	 }
	

}
