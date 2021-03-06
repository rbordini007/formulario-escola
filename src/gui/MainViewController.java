package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.services.AlunoService;
import model.services.FuncionarioService;
import model.services.InstituicaoService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemCadastroInstituicao;
	
	@FXML
	private MenuItem menuItemCadastroFuncionario;
	
	@FXML
	private MenuItem menuItemFichaMatricula;
	
	@FXML
	private MenuItem menuItemFichaSaude;
	
	@FXML
	private MenuItem menuItemTurma1;
	
	@FXML
	private MenuItem menuItemTurma2;
	
	@FXML
	private MenuItem menuItemTurma3;
	
	@FXML
	private MenuItem menuItemTurma4;
	
	@FXML
	private MenuItem menuItemFormularios;
	
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	private ImageView imageViewPricipal;	

	public void setImageViewPricipal(ImageView imageViewPricipal) {
		this.imageViewPricipal = imageViewPricipal;
	}

	@FXML
	public void onMenuItemCadastroFuncionario() {
		//System.out.println("menuItemCadastroFuncionario");
		loadView("/gui/CadastroFuncionario.fxml", (CadastroFuncionarioController controller) -> {
			controller.setFuncionarioService(new FuncionarioService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemCadastroInstitucao() {
		System.out.println("menuItemCadastroInstituicao");
		loadView("/gui/CadastroInstituicao.fxml", (CadastroInstituicaoController controller) -> {
			controller.setInstituicaoService(new InstituicaoService());
			controller.updateTableView();
		});
		
	}
	
	@FXML
	public void menuItemFichaMatricula() {
		System.out.println("menuItemFichaMatricula");
		loadView("/gui/CadastroAluno.fxml", (CadastroAlunoController controller) -> {
			controller.setAlunoService(new AlunoService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void menuItemFichaSaude() {
		System.out.println("menuItemFichaSaude");
		loadView("/gui/CadastroFichaSaude.fxml", (CadastroFichaSaudeController controller)-> {
			
		});
	}
	
	@FXML
	public void menuItemTurma1() {
		System.out.println("menuItemTurma1");
	}
	
	@FXML
	public void menuItemTurma2() {
		System.out.println("menuItemTurma2");
	}
	
	@FXML
	public void menuItemTurma3() {
		System.out.println("menuItemTurma3");
	}
	
	@FXML
	public void menuItemTurma4() {
		System.out.println("menuItemTurma4");
	}
	
	@FXML
	public void menuItemFormularios() {
		System.out.println("menuItemFormularios");
		
	}
	
	@FXML
	public void menuItemSobre() {
		System.out.println("menuItemSobre");
		loadView("/gui/Sobre.fxml", x -> {});
	}
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		
	}
	
	
	private synchronized <T> void loadView(String AbsolutName, Consumer<T>initializingAction) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(AbsolutName));
			VBox newVBox = loader.load();
						
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
						
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);			
			
		} catch (IOException e) {
			Alerts.showAlert("Io Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}		
	}
	
}
