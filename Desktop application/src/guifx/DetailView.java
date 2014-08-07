package guifx;

import static javafx.collections.FXCollections.observableArrayList;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import entities.Batch;
import entities.ClientOrder;
import entities.Customization;

public class DetailView {
	public static final double	PREFERRED_WIDTH		= 850;
	public static final double	PREFERRED_HEIGHT	= 450;
	private static final String	backgroundStyle		= "linear-gradient(#f9f9f9,#e9e9e9)";

	private static final List<Pair<String,String>>	batchesCols = new ArrayList<>();
	private static final List<Pair<String,String>>	customsCols = new ArrayList<>();
	
	private ClientOrder			order;
	private Stage				primaryStage;
	private BorderPane			root;

	public DetailView(ClientOrder order) {
		this.order = order;
		primaryStage  = new Stage(StageStyle.DECORATED);
		Button close  = new Button("Fermer");
		
		HBox footer = new HBox(12,close);
		footer.setAlignment(Pos.CENTER_RIGHT); 
		
		root        = new BorderPane();
		root.setPadding(new Insets(17));
		root.setBackground(new Background(new BackgroundFill(Paint.valueOf(backgroundStyle),
				new CornerRadii(0,false),Insets.EMPTY)));
		root.setBottom(footer);
		root.setCenter(setCenter());
	
		//Event handling
		close .setOnAction(ev -> primaryStage.close());
		
		javafx.scene.Scene scene = new Scene(root,PREFERRED_WIDTH,PREFERRED_HEIGHT,Color.WHITESMOKE);
        primaryStage.setTitle(order.toString());
        primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	private Node setCenter() {
		Label batchesLabel = new Label("Lots commandés");
		Label customLabel  = new Label("Personnalisations");
		batchesLabel.setFont(Font.font("",FontWeight.BOLD,20));
		customLabel.setFont(Font.font("",FontWeight.BOLD,20));
		
		TableView<Batch>         batches = setBatchesTable();
		TableView<Customization> customs = setCustomizationTable(batches);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10));
		grid.add(batchesLabel,0,0);
		grid.add(batches,0,1);
		grid.add(customLabel,1,0);
		grid.add(customs,1,1);
		
		return grid;
	}

	private TableView<Batch> setBatchesTable() {
		TableView<Batch> batches = new TableView<>();
		for (Pair<String,String> pair : batchesCols) {
			TableColumn<Batch,String> col = new TableColumn<>(pair.getKey());
			col.setCellValueFactory(new PropertyValueFactory<>(pair.getValue()));
			col.setPrefWidth(Math.max(pair.getKey().length() * 8,15));
			batches.getColumns().add(col);
		}
		batches.setItems(observableArrayList(order.getBatches()));
		return batches;
	}

	private TableView<Customization> setCustomizationTable(TableView<Batch> batches) {
		TableView<Customization> customs = new TableView<>();
		for (Pair<String,String> pair : customsCols) {
			TableColumn<Customization,String> col = new TableColumn<>(pair.getKey());
			col.setCellValueFactory(new PropertyValueFactory<>(pair.getValue()));
			col.setPrefWidth(Math.max(pair.getKey().length() * 15,40));
			customs.getColumns().add(col);
		}
		if (!order.getBatches().isEmpty())
			customs.setItems(observableArrayList(order.getBatches().get(0).getCustomizations()));
		
		batches.getSelectionModel().selectedItemProperty().addListener(ev -> {
			Batch batch = batches.getSelectionModel().getSelectedItem();
			if (batch != null) 
				customs.setItems(observableArrayList(batch.getCustomizations()));
		});
		return customs;
	}

	static {
		batchesCols.add(new Pair<>("N° lot"                  ,"id"                    ));
		batchesCols.add(new Pair<>("Quantité"                ,"qt"                    ));
		batchesCols.add(new Pair<>("Référence biscuit"       ,"biscuitRef"            ));
		batchesCols.add(new Pair<>("Nombre de customisations","numberOfCustomizations"));
	}
	
	static {
		customsCols.add(new Pair<>("Mode"   ,"modeString"));
		customsCols.add(new Pair<>("Contenu","data"      ));
		customsCols.add(new Pair<>("Taille" ,"size"      ));
		customsCols.add(new Pair<>("x"      ,"x"         ));
		customsCols.add(new Pair<>("y"      ,"y"         ));
	}
}
