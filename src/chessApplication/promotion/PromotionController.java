package chessApplication.promotion;

import static javafx.scene.layout.BorderStrokeStyle.SOLID;
import static javafx.scene.layout.CornerRadii.EMPTY;
import static javafx.scene.paint.Color.PINK;
import static javafx.scene.paint.Color.TRANSPARENT;


import java.util.ArrayList;
import java.util.List;
import chessApplication.TempStage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PromotionController {

	public StringProperty piece = new SimpleStringProperty("");
	private Border borderInvisible = new Border (new BorderStroke(TRANSPARENT, SOLID, EMPTY, null));
	private Border borderVisible   = new Border (new BorderStroke(PINK, SOLID, EMPTY, null));
	
	@FXML VBox   root;
	@FXML HBox   pieces;
	@FXML Button selectButton;
	
	

	@FXML
	private void initialize() {
		for (Node child : pieces.getChildren()) {
		    ((Labeled) child).setBorder(borderInvisible);
		}
	}

	@FXML
	private void selectPiece(MouseEvent event) {
	    for (Node child : pieces.getChildren()) {
	        ((Labeled) child).setBorder(borderInvisible);
	    }
	    ((Label) event.getSource()).setBorder(borderVisible);
	}
	
	@FXML
	private void selectButtonAction(ActionEvent event) {
		((SimpleStringProperty) piece).set("");
		List<Node> selectedPiece = new ArrayList<>();
		for (Node c : pieces.getChildren()) {
		if (((Labeled) c).getBorder() == borderVisible) {
		selectedPiece.add(c);
		}
	}
		if (! selectedPiece.isEmpty()) {
			piece.set(((Labeled) selectedPiece.get(0)).getText());
			((TempStage) selectButton.getScene().getWindow()).setPromotionPieceSelected(true);
			((TempStage) selectButton.getScene().getWindow()).close();
		}
		
	}
}
