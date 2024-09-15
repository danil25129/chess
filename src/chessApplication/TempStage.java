package chessApplication;

import static javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TempStage extends Stage {

	private final double width        = 400.0;
	private final double height       = 217.0;
	private boolean promotionSelected = true;	
	
	

	public TempStage() {
		this.addEventFilter(WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
	}
	
	

	public void setStage(ActionEvent event) {
		this.setMinWidth(width);
		this.setMinHeight(height); 
		this.setMaxWidth(width);
		this.setMaxHeight(height);
		if (this.getModality() == Modality.NONE) this.initModality(Modality.APPLICATION_MODAL);
		if (this.getOwner() == null) this.initOwner(((Node) event.getSource()).getScene().getWindow());
	}
	
	/*
	 * превращение не является событием действия. Таким образом, пользователь не может нажать кнопку, чтобы открыть
	 * окно превращения. Если пользователь закроет окно, не выбрав фигуру,
	 * игра будет ждать выбора от пользователя, который никогда не поступит. При
	 * использовании события window окно не закроется.
	 */
	private void closeWindowEvent(WindowEvent event) {
		if (!promotionSelected) event.consume();
	}
	
	/*
	 * центрирование second stage
	 */
	public void centerStage() {
		this.setX(this.getX() + this.getWidth()  / 2 - (width/2));
		this.setY(this.getY() + this.getHeight() / 2 - (height/2));  
	}
	
	
	public void setPromotionPieceSelected(boolean selected) {
		promotionSelected = selected;
	}
	
}
