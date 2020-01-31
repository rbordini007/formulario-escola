package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Instituicao;
import model.exceptions.ValidationException;
import model.services.InstituicaoService;

public class CadastroInstituicaoFormController implements Initializable{
	
	private Instituicao entity;
	
	private InstituicaoService service;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<DataChangeListener>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtTelefone;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	@FXML
	private Label labelErrorNome;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorTelefone;
	
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
	
	private Instituicao getFormData() {
		Instituicao obj = new Instituicao();
		ValidationException exception = new ValidationException("validation error");
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtNome.getText() == null) {
			txtNome.setPromptText("");
			exception.addError("nome", " O campo não pode estar Vazio");
		}
		obj.setNome(txtNome.getText());
		
		if (txtEmail.getText() == null) {
			txtEmail.setPromptText("");
			exception.addError("email", " O campo não pode estar Vazio");
		}
		obj.setEmail(txtEmail.getText());
		
		if (txtTelefone.getText() == null) {
			txtTelefone.setPromptText("");
			exception.addError("telefone", " O campo não pode estar Vazio");
		}		
		obj.setTelefone(txtTelefone.getText());	
		
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
	
	//================== setters ===================
	public void setInstituicao(Instituicao entity) {
		this.entity = entity;
	}
	
	public void setInstituicaoService(InstituicaoService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListeners(DataChangeListener listener) {
		this.dataChangeListener.add(listener);
	}
	//==============================================
	
	private void notifyDataChangeListeners() {
		dataChangeListener.forEach((listener) -> {
			listener.onDataChange();
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();		
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldMaxLength(txtEmail, 70);
		Constraints.setTextFieldMaxLength(txtTelefone, 10);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtEmail.setText(entity.getEmail());
		txtTelefone.setText(entity.getTelefone());		
	}	
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String>fields = errors.keySet();
		
		if (fields.contains("nome")) {
			labelErrorNome.setText(errors.get("nome"));
		}
		if (fields.contains("email")) {
			labelErrorEmail.setText(errors.get("email"));
		}		
		if (fields.contains("telefone")) {
			labelErrorTelefone.setText(errors.get("telefone"));
		}		
	}
}
