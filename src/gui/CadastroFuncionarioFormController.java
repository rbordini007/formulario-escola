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
import model.entities.Funcionario;
import model.exceptions.ValidationException;
import model.services.FuncionarioService;

public class CadastroFuncionarioFormController implements Initializable {
	
	private Funcionario entity;
	
	private FuncionarioService service;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<DataChangeListener>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtRg;
	@FXML
	private TextField txtCpf;
	@FXML
	private TextField txtTelefone;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancel;
	@FXML
	private Label labelErrorNome;
	@FXML
	private Label labelErrorRg;
	@FXML
	private Label labelErrorCpf;
	@FXML
	private Label labelErrorTelefone;
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		//System.out.println("onBtSaveAction");
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
	
	private Funcionario getFormData() {
		Funcionario obj = new Funcionario();
		ValidationException exception = new ValidationException("Validation error");
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtNome.getText() == null) {
			exception.addError("nome", "O campo não pode estar Vazio");
		}
		obj.setNome(txtNome.getText());
		
		if (txtRg.getText() == null) {
			exception.addError("rg", "O campo não pode estar Vazio");
		}
		obj.setRg(txtRg.getText());
		
		if (txtCpf.getText() == null) {
			exception.addError("cpf", "O campo não pode estar Vazio");
		}
		obj.setCpf(txtCpf.getText());
		
		if (txtTelefone.getText() == null) {
			exception.addError("telefone", "O campo não pode estar Vazio");
		}
		obj.setTelefone(txtTelefone.getText());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	public void onBtCancelAction(ActionEvent event) {
		//System.out.println("OnBtCancelAction");
		Utils.currentStage(event).close();
	}
	
	
	
	
	//============= Setters ==================//
	public void setFuncionario(Funcionario entity) {
		this.entity = entity;
	}

	public void setFuncionarioService(FuncionarioService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListeners(DataChangeListener listener) {
		this.dataChangeListener.add(listener);
	}

	//=========================================//

	private void notifyDataChangeListeners() {
		dataChangeListener.forEach((listener) -> {
			listener.onDataChange();
		});
	}


	@Override
	public void initialize(URL url, ResourceBundle rb) {
			inititializeNodes();	
	}

	private void inititializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 60);
		Constraints.setTextFieldMaxLength(txtRg, 12);
		Constraints.setTextFieldMaxLength(txtCpf, 14);
		Constraints.setTextFieldMaxLength(txtTelefone, 10);		
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtRg.setText(entity.getRg());
		txtCpf.setText(entity.getCpf());
		txtTelefone.setText(entity.getTelefone());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String>fields = errors.keySet();
		
		if (fields.contains("nome")) {
			labelErrorNome.setText(errors.get("nome"));
		}
		if (fields.contains("rg")) {
			labelErrorRg.setText(errors.get("rg"));
		}
		if (fields.contains("cpf")) {
			labelErrorCpf.setText(errors.get("cpf"));
		}
		if (fields.contains("telefone")) {
			labelErrorTelefone.setText(errors.get("telefone"));
		}
	}

}
