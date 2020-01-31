package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
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
import model.entities.FichaSaude;
import model.entities.Aluno;
import model.services.FichaSaudeService;
import model.services.AlunoService;

public class CadastroFichaSaudeController implements Initializable, DataChangeListener {

	private Image imgEdit = new Image(getClass().getResourceAsStream("/gui/css/edit2Icon.png"));
	private Image imgDelete = new Image(getClass().getResourceAsStream("/gui/css/delete2Icon.png"));

	private FichaSaudeService service;

	@FXML
	private TableView<FichaSaude> tableViewFichaSaude;
	@FXML
	private TableColumn<FichaSaude, Integer> tableColumnId;
	@FXML
	private TableColumn<FichaSaude, String> tableColumnNome;
	@FXML
	private TableColumn<FichaSaude, String> tableColumnRg;
	@FXML
	private TableColumn<FichaSaude, String> tableColumnTelefone;
	@FXML
	private TableColumn<FichaSaude, Date> tableColumnNascimento;
	@FXML
	private TableColumn<FichaSaude, Aluno> tableColumnAluno;
	@FXML
	private TableColumn<FichaSaude, FichaSaude> tableColumnEDIT;
	@FXML
	private TableColumn<FichaSaude, FichaSaude> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<FichaSaude> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		System.out.println("onBtNewActionFichaSaude");
		Stage parentStage = Utils.currentStage(event);
		FichaSaude obj = new FichaSaude();
		createDialogForm(obj, "/gui/CadastroFichaSaudeForm.fxml", parentStage);
	}

	// ================ setters ====================== //
	public void setFichaSaudeService(FichaSaudeService service) {
		this.service = service;
	}

	// ===============================================//

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//initializeNodes();
	}

	public void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnRg.setCellValueFactory(new PropertyValueFactory<>("rg"));
		tableColumnNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
		Utils.formatTableColumnDate(tableColumnNascimento, "dd/MM/yyyy");
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnAluno.setCellValueFactory(new PropertyValueFactory<>("funcionario"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFichaSaude.prefHeightProperty().bind(stage.heightProperty());
//
//		initEditButtons();
//		initRemoveButtons();
	}

	public void updateTableView() {
//		if (service == null) {
//			throw new IllegalStateException("Service was null");
//		}
//
//		List<FichaSaude> list = service.findAll();
//		obsList = FXCollections.observableArrayList(list);
//		tableViewFichaSaude.setItems(obsList);
	}

	private synchronized void createDialogForm(FichaSaude obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

//			CadastroFichaSaudeFormController controller = loader.getController();
//			controller.setFichaSaude(obj);
//			controller.setServices(new FichaSaudeService(), new AlunoService());
//			controller.loadAssociatedObjects();
//			controller.subscribeDataChangeListeners(this);
//			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do FichaSaude");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChange() {
		updateTableView();
	}

//	private void initEditButtons() {
//		// tableColumnEdit.setMinWidth(40.0);
//		tableColumnEDIT.setPrefWidth(50.0);
//		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
//		tableColumnEDIT.setCellFactory(param -> new TableCell<FichaSaude, FichaSaude>() {
//
//			private final Button button = new Button("", new javafx.scene.image.ImageView(imgEdit));
//
//			protected void updateItem(FichaSaude obj, boolean empty) {
//				super.updateItem(obj, empty);
//				if (obj == null) {
//					setGraphic(null);
//					return;
//				}
//
//				setGraphic(button);
//				button.setOnAction(
//						event -> createDialogForm(obj, "/gui/CadastroFichaSaudeForm.fxml", Utils.currentStage(event)));
//			}
//		});
//	}
//
//	private void initRemoveButtons() {
//		tableColumnREMOVE.setPrefWidth(50.0);
//		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
//		tableColumnREMOVE.setCellFactory(param -> new TableCell<FichaSaude, FichaSaude>() {
//
//			private final Button button = new Button("", new javafx.scene.image.ImageView(imgDelete));
//
//			@Override
//			protected void updateItem(FichaSaude obj, boolean empty) {
//				super.updateItem(obj, empty);
//
//				if (obj == null) {
//					setGraphic(null);
//					return;
//				}
//
//				setGraphic(button);
//				button.setOnAction(event -> removeEntity(obj));
//			}
//		});
//	}

//	private void removeEntity(FichaSaude obj) {
//		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Tem certeza que deseja deletar!");
//
//		if (result.get() == ButtonType.OK) {
//			if (service == null) {
//				throw new IllegalStateException("service was null");
//			}
//			try {
//				service.remove(obj);
//				updateTableView();
//			} catch (DbIntegrityException e) {
//				Alerts.showAlert("Erro ao remover o objeto", null, e.getMessage(), AlertType.ERROR);
//			}
//		}
//	}
}
