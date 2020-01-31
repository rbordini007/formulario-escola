package gui;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import java.util.Optional;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Funcionario;
import model.services.FuncionarioService;

public class CadastroFuncionarioController implements Initializable, DataChangeListener {

	private FuncionarioService service;
	
	private Image imgEdit = new Image(getClass().getResourceAsStream("/gui/css/edit2Icon.png"));
	private Image imgDelete = new Image(getClass().getResourceAsStream("/gui/css/delete2Icon.png"));
	
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
	private TableColumn<Funcionario, Funcionario> tableColumnEdit;
	@FXML
	private TableColumn<Funcionario, Funcionario> tableColumnRemove;
	@FXML
	private Button btNew;

	private ObservableList<Funcionario> obsList;

	public void setFuncionarioService(FuncionarioService service) {
		this.service = service;
	}	
	

	public void setImgEdit(Image imgEdit) {
		this.imgEdit = imgEdit;
	}



	@FXML
	private void onBtNewAction(ActionEvent event) {
		// System.out.println("onBtNew");
		Stage parentStage = Utils.currentStage(event);
		Funcionario obj = new Funcionario();
		createDialogForm(obj, "/gui/CadastroFuncionarioForm.fxml", parentStage);

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
		
		initEditButtons();
		initRemoveButtons();
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("O service estava nulo");
		}

		List<Funcionario> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewFuncionario.setItems(obsList);
	}

	private void createDialogForm(Funcionario obj, String nomeAbsoluto, Stage parentStage) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			Pane pane = loader.load();

			CadastroFuncionarioFormController controller = loader.getController();
			controller.setFuncionario(obj);
			controller.setFuncionarioService(new FuncionarioService());
			controller.subscribeDataChangeListeners(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com um novo Funcionario");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("Io Exceptions", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChange() {
		updateTableView();
	}

	private void initEditButtons() {
		//tableColumnEdit.setMinWidth(40.0);
		tableColumnEdit.setPrefWidth(50.0);
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue())); 
		tableColumnEdit.setCellFactory(param -> new TableCell<Funcionario, Funcionario>(){
			
			private final Button button = new Button("",new javafx.scene.image.ImageView(imgEdit));
			
			protected void updateItem(Funcionario obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/CadastroFuncionarioForm.fxml", Utils.currentStage(event)));
			}
			});
		}
	
	private void initRemoveButtons() {
		//tableColumnRemove.setMinWidth(40.0);
		tableColumnRemove.setPrefWidth(50.0);
        tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param -> new TableCell<Funcionario, Funcionario>() {
        	
            private final Button button = new Button("", new javafx.scene.image.ImageView(imgDelete)); 

    @Override
    protected void updateItem(Funcionario obj, boolean empty) {
        super.updateItem(obj, empty);

        if (obj == null) {
            setGraphic(null);
            return;
        } 

        setGraphic(button); 
        button.setOnAction(event -> removeEntity(obj));
                }

            
        }); 
    } 
	
	private void removeEntity(Funcionario obj) {
				Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Tem certeza que deseja deletar!");
				
				if (result.get() == ButtonType.OK) {
					if (service == null) {
						throw new IllegalStateException("service was null");
					}
					try {
						service.remove(obj);
						updateTableView();
					} catch (DbIntegrityException e) {
						Alerts.showAlert("Erro ao remover o objeto", null, e.getMessage(), AlertType.ERROR);
					}
				}
			}

}
