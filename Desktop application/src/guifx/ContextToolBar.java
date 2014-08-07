package guifx;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ContextToolBar extends ToolBar {
	private Label title;
	
	public ContextToolBar(String s, double prefWidth) {
		this(s,prefWidth,new Region[] {});		
	}
	
	public ContextToolBar(String s, double prefWidth, Region... items) {
		super(items);
		setPadding(new Insets(10));
		setPrefWidth(prefWidth);
		
		title = new Label(s);
		title.setFont(Font.font("",FontWeight.BOLD,20));
		getItems().add(0,title);		
		widthProperty().addListener(obs -> resizeItems());		
	}
	
	private final void resizeItems() {
		Optional<Double> option = getItems().stream().map(x -> { Region y = (Region) x; return y.prefWidth(y.getHeight()); }).max(Double::compare);
		final double maxWidth   = option.isPresent() ? Math.max(getPrefWidth(),option.get()) : getPrefWidth();
		getItems().stream().forEach(x -> ((Region) x).setPrefWidth(maxWidth));
	}
}
