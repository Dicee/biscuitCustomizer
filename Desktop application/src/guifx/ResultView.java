package guifx;

import static javafx.collections.FXCollections.observableArrayList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import entities.ClientOrder;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import model.DBManager;

public class ResultView extends TableView<ClientOrder>{
	private static final List<Pair<String,String>>	cols     = new ArrayList<>();
	static {
		cols.add(new Pair<>("N° commande"   ,"id"                   ));
		cols.add(new Pair<>("N° client"     ,"ownerId"              ));
		cols.add(new Pair<>("Client"        ,"name"                 ));
		cols.add(new Pair<>("Date"          ,"formattedCreationDate"));
		cols.add(new Pair<>("Etat"          ,"state"                ));
		cols.add(new Pair<>("Nombre de lots","numberOfBatches"      ));
	}
	
	private List<ClientOrder>						orders;
	private ObservableList<Predicate<ClientOrder>> 	filters;	
	private DBManager								manager = new DBManager();
	
	public ResultView(double prefWidth) throws SQLException {
		manager.connect();
		this.orders  = manager.getAllOrders();
		this.filters = observableArrayList();
		int size     = cols.size();
		
		filters.addListener((ListChangeListener.Change<? extends Predicate<ClientOrder>> change) -> applyFilters());
		
		for (Pair<String,String> pair : cols) {
			TableColumn<ClientOrder,String> col = new TableColumn<>(pair.getKey());
			col.setCellValueFactory(new PropertyValueFactory<>(pair.getValue()));
			col.setPrefWidth(prefWidth/size);
			getColumns().add(col);
		}
		setItems(observableArrayList(orders));
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	public void applyFilters() {
		Predicate<ClientOrder> p = co -> true;
		for (Predicate<ClientOrder> pred : filters) p = p.and(pred);
		setItems(observableArrayList(orders.stream().filter(p).collect(Collectors.toList())));
	}

	public ObservableList<Predicate<ClientOrder>> getFilters() {
		return filters;
	}
}
