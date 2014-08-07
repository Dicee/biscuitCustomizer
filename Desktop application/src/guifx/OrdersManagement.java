package guifx;

import static entities.ClientOrder.CREATED_STATE;
import static entities.ClientOrder.ONGOING_STATE;
import static entities.ClientOrder.TO_DELIVER_STATE;
import entities.Batch;
import entities.ClientOrder;
import entities.User;
import guifx.components.ComboBoxFilter;
import guifx.components.IntegerConstraintField;
import guifx.components.IntegerValueFilter;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import org.controlsfx.dialog.Dialogs;

import svg.SVGBlueprinter;

public class OrdersManagement extends Application {
	public static final double			PREFERRED_WIDTH		= 1100;
	public static final double			PREFERRED_HEIGHT	= 600;

	private static final List<String>	states				= Arrays.asList(CREATED_STATE,ONGOING_STATE,TO_DELIVER_STATE);

	private File                        currentDir = new File("C:/Users/" + System.getProperty("user.name") + "/Desktop/");
	private ResultView					resultView;

	@Override
    public void start(Stage primaryStage) throws SQLException {
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        
        BorderPane root = new BorderPane();
        root.setLeft(setContextToolbar());
        root.setCenter(setCenter());
        root.setTop(setMenuBar());
        
        Scene scene = new Scene(root, PREFERRED_WIDTH, PREFERRED_HEIGHT);
        primaryStage.setTitle("Interface de gestion des commandes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	private Node setCenter() throws SQLException {
		resultView = new ResultView(3.4*PREFERRED_WIDTH/4.5);
        resultView.setPrefHeight(PREFERRED_HEIGHT);
		return resultView;
	}
	
	private ContextToolBar setContextToolbar() {
		IntegerValueFilter     refOrderFilter    = new IntegerValueFilter("N° commande","ID");
		IntegerValueFilter     refCustomerFilter = new IntegerValueFilter("N° client","OWNER_ID");
		ComboBoxFilter<String> stateFilterBox    = new ComboBoxFilter<>("Etat","state",states);
		CheckBox               nameCheckBox      = new CheckBox("Nom-prénom");
		TextField              customerName      = new TextField();
		
		refOrderFilter   .field.setValue(1);
		refCustomerFilter.field.setValue(1);
		stateFilterBox.comboBox.getSelectionModel().select(0);
		
		GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(3);
		grid.add(refOrderFilter.checkBox,0,0);
		grid.add(refOrderFilter.field,1,0);
		grid.add(refCustomerFilter.checkBox,0,1);
		grid.add(refCustomerFilter.field,1,1);
		grid.add(stateFilterBox.checkBox,0,2);
		grid.add(stateFilterBox.comboBox,1,2);
		grid.add(nameCheckBox,0,3);
		grid.add(customerName,1,3);
		
		//Handle filter events
		final Predicate<ClientOrder> orderFilter = co -> refOrderFilter   .field.getValue() == co.getId();
		final Predicate<ClientOrder> ownerFilter = co -> refCustomerFilter.field.getValue() == co.getOwner().getId();
		final Predicate<ClientOrder> stateFilter = co -> stateFilterBox.comboBox.getSelectionModel().getSelectedItem().equals(co.getState());
		final Predicate<ClientOrder> nameFilter  = co -> {
			String start = Arrays.asList(customerName.getText().split("\\s+")).stream().reduce((s1,s2) -> s1 + s2).get().toLowerCase();
			User   owner = co.getOwner();
			String name  = (owner.getNom() + owner.getPrenom()).toLowerCase();
			return name.startsWith(start);
		};
		
		final Consumer<Pair<CheckBox,Predicate<ClientOrder>>> activateFilter = pair -> {
			if (!pair.getKey().isSelected())
				pair.getKey().setSelected(true); 
			else
				applyFilter(pair.getValue());
		};
		
		setAction(refCustomerFilter,ownerFilter);
		setAction(refOrderFilter   ,orderFilter);
		setAction(stateFilterBox.checkBox,stateFilter);
		setAction(nameCheckBox,nameFilter);
		stateFilterBox.comboBox.setOnAction(ev -> activateFilter.accept(new Pair<>(stateFilterBox.checkBox,stateFilter)));
		customerName           .setOnAction(ev -> activateFilter.accept(new Pair<>(nameCheckBox,nameFilter)));
		
		Label actionLabel = new Label("Actions");
		actionLabel.setFont(Font.font("",FontWeight.BOLD,20));
		
		Button detail = new Button("Voir le détail");
		detail.setOnAction(ev -> { 
			ClientOrder order = resultView.getSelectionModel().getSelectedItem();
			if (order != null) new DetailView(order); 
		});
		
		Button print = new Button("Imprimer la sélection");
		print.setOnAction(ev -> {
			File f = chooseFile("Tous les fichiers",true,"*.*");
			if (f != null) 
				createOrdersDirs(f);
		});
		
		ContextToolBar ct = new ContextToolBar("Filtres",PREFERRED_WIDTH / 4.5,grid,new Separator(),actionLabel,detail,print);
		ct.setOrientation(Orientation.VERTICAL);
		return ct;
	}
	
	private void createOrdersDirs(File f) {
		boolean created = mkdir(f);
		if (created) {
			List<ClientOrder> orders = resultView.getSelectionModel().getSelectedItems().stream().
					filter(co -> co.getState().equals(ClientOrder.ONGOING_STATE)).collect(Collectors.toList());
			for (ClientOrder order : orders) {
				User owner     = order.getOwner();
				File orderDir  = new File(String.format("%s/order_%d_%s_%s",
						f.getAbsolutePath(),order.getId(),owner.getNom(),owner.getPrenom()));
				boolean exists = mkdir(orderDir);
				if (exists) {
					int i = 0;
					for (Batch batch : order.getBatches()) {
						try {
							SVGBlueprinter.getBluePrint(batch,String.format("%s/batch_%d.svg",orderDir.getAbsolutePath(),i++));
						} catch (Throwable t) {
							Dialogs.create().owner(resultView).
			                 	title("Erreur").
			                 	masthead("Une erreur est survenue").
			                 	showException(t);
						} 
					}
				}
			}
		}
	}
	
	private boolean mkdir(File f) {
		if (!f.exists()) {
			boolean created = f.mkdir();
			if (!created) 
				Dialogs.create().owner(resultView).
					title("Erreur").
					masthead("Une erreur s'est produite").
					message("Impossible de créer les dossiers").
					showError();
		} 
		
		if (f.exists() && !f.isDirectory())
			Dialogs.create().owner(resultView).
				title("Erreur").
				masthead("Une erreur s'est produite").
				message(f.getAbsolutePath() + "n'est pas un dossier").
				showError();
		return f.exists() && f.isDirectory();
	}
	
	private File chooseFile(String filterName, boolean save, String... extensions) {
        FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(currentDir);
			
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(filterName,extensions));
		File selectedFile = save ? chooser.showSaveDialog(null) : chooser.showOpenDialog(null);
        currentDir        = selectedFile == null ? currentDir : selectedFile.getParentFile();
        return selectedFile;
    }
	
	private final void applyFilter(Predicate<ClientOrder> p) {
		ObservableList<Predicate<ClientOrder>> filters = resultView.getFilters();
		if (filters.contains(p)) resultView.applyFilters();
		else                     filters.add(p);
	};
	
	private void setAction(CheckBox cb, Predicate<ClientOrder> filter) {
		cb.selectedProperty().addListener(ev -> {
			if (cb.isSelected())
				applyFilter(filter);
			else
				resultView.getFilters().remove(filter);
		});
	}
	
	private void setAction(IntegerValueFilter filter, final Predicate<ClientOrder> pred) {
		filter.checkBox.selectedProperty().addListener(ev -> {
			if (filter.isSelected()) {
				int n = filter.field.getValue();
				if (n != IntegerConstraintField.ERROR_RETURN) applyFilter(pred);
			} else 
				resultView.getFilters().remove(pred);
		});
		filter.field.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER)
				if (!filter.isSelected()) 
					filter.checkBox.setSelected(true);
				else if (filter.field.getValue() != IntegerConstraintField.ERROR_RETURN) 
					applyFilter(pred);
		});
	}
	
	private MenuBar setMenuBar() {
        MenuBar menuBar 	= new MenuBar();
        Menu 	menuFile 	= new Menu("Fichier");
        Menu 	menuEdit 	= new Menu("Editer");
        Menu 	menuHelp 	= new Menu("Aide");
        
        MenuItem quit;
        
        menuFile.getItems().add(quit = new MenuItem("Quitter"));
        quit.setAccelerator(new KeyCharacterCombination("Q",KeyCharacterCombination.CONTROL_DOWN));
        
        quit.setOnAction(ev -> System.exit(0));
        menuBar.getMenus().addAll(menuFile,menuEdit,menuHelp);
        
        return menuBar;
	}

    public static void main(String[] args) {
        launch(args);
    }
}

