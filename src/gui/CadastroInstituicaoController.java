package gui;

import java.io.IOException;
import java.net.URL;
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
import model.entities.Instituicao;
import model.services.InstituicaoService;

public class CadastroInstituicaoController implements Initializable, DataChangeListener {

	private InstituicaoService service;

	private Image imgEdit = new Image(getClass().getResourceAsStream("/gui/css/edit2Icon.png"));
	private Image imgDelete = new Image(getClass().getResourceAsStream("/gui/css/delete2Icon.png"));

	@FXML
	private TableView<Instituicao> tableViewInstituicao;
	@FXML
	private TableColumn<Instituicao, Integer> tableColumnId;
	@FXML
	private TableColumn<Instituicao, String> tableColumnNome;
	@FXML
	private TableColumn<Instituicao, String> tableColumnEmail;
	@FXML
	private TableColumn<Instituicao, String> tableColumnTelefone;
	@FXML
	private TableColumn<Instituicao, Instituicao> tableColumnEdit;
	@FXML
	private TableColumn<Instituicao, Instituicao> tableColumnRemove;
	@FXML
	private Button btNew;

	private ObservableList<Instituicao> obsList;

	// ========== setters ==============
	public void setInstituicaoService(InstituicaoService service) {
		this.service = service;
	}

	// ==================================

	@FXML
	public void onBtNewAction(ActionEvent event) {
		System.out.println("onBtNewAction");
		Stage parentStage = Utils.currentStage(event);
		Instituicao obj = new Instituicao();
		createDialogForm(obj, "/gui/CadastroInstituicaoForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewInstituicao.prefHeightProperty().bind(stage.heightProperty());

		initEditButtons();
		initRemoveButtons();
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("this service was null");
		}
		List<Instituicao> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewInstituicao.setItems(obsList);
	}

	private void createDialogForm(Instituicao obj, String nomeAbsoluto, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			Pane pane = loader.load();

			CadastroInstituicaoFormController controller = loader.getController();
			controller.setInstituicao(obj);
			controller.setInstituicaoService(new InstituicaoService());
			controller.subscribeDataChangeListeners(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com uma Nova Instituição");
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
		// tableColumnEdit.setMinWidth(40.0);
		tableColumnEdit.setPrefWidth(50.0);
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Instituicao, Instituicao>() {

			private final Button button = new Button("", new javafx.scene.image.ImageView(imgEdit));

			protected void updateItem(Instituicao obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);

					return;
				}

				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/CadastroInstituicaoForm.fxml", Utils.currentStage(event)));
			}
		});

	}

	private void initRemoveButtons() {
		// tableColumnRemove.setMinWidth(40.0);
		tableColumnRemove.setPrefWidth(50.0);
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Instituicao, Instituicao>() {

			private final Button button = new Button("", new javafx.scene.image.ImageView(imgDelete));

			@Override
			protected void updateItem(Instituicao obj, boolean empty) {
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

	private void removeEntity(Instituicao obj) {
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
