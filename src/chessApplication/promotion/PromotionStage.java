package chessApplication.promotion;

import static chess.util.ConverterPieceView.getViewPieceFromFEN;
import static javafx.scene.paint.Color.WHITE;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import chessApplication.MainController;
import chessApplication.TempStage;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Color;

public class PromotionStage {

	private MainController  controller;
	private TempStage secondStage;
	private Color       color;

	public PromotionStage(MainController controller, Color color) {
		this.controller  = controller;
		this.secondStage = controller.getSecondStage();
		this.color       = color;
	}
	

	public void setSceneAndShow() {
		secondStage.setTitle("Превращение фигуры");
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PromotionStage.fxml"));
			Scene promotionScene = new Scene(loader.load());
			secondStage.setScene(promotionScene);
			PromotionController promotionController = loader.getController();
			List<String> promotionPieces = color == WHITE ? 
					Arrays.asList(getViewPieceFromFEN("N"),	getViewPieceFromFEN("B"), getViewPieceFromFEN("R"), getViewPieceFromFEN("Q")) 
					: Arrays.asList(getViewPieceFromFEN("n"), getViewPieceFromFEN("b"), getViewPieceFromFEN("r"), getViewPieceFromFEN("q"));
					for (int i=0; i<4 ; i++) ((Labeled) promotionController.pieces.getChildren().get(i)).setText(promotionPieces.get(i));
					promotionController.piece.addListener((ChangeListener<String>) (ObservableValue, oldValue, newValue) -> {
						if (newValue != "") {
							controller.getBoardHandler().setPromotionPiece(newValue);
							controller.getBoardHandler().switchPlayer();
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
		secondStage.centerStage();
		//игрок не может закрыть окно пока не выбрал фигуру
		secondStage.setPromotionPieceSelected(false);
		secondStage.show();
	}
}
