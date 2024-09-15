package chessApplication.endedbyuser;

import static chess.util.GameEvaluator.EvaluationResult.RESIGN;
import static javafx.scene.paint.Color.WHITE;

import java.io.IOException;

import chess.util.GameEvaluator.EvaluationResult;
import chessApplication.MainController;
import chessApplication.TempStage;
import chessApplication.Sounds;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class AdditionStage {

	private MainController controller;
	private TempStage  secondStage;
	private EvaluationResult result;
	private AdditionStageController endedByUserController;
	private String activePlayer;
	private String   otherPlayer;
	

	public AdditionStage(MainController controller, EvaluationResult result) {
		this.controller   = controller;
		this.secondStage  = controller.getSecondStage();
		//Можно сдаться, а можно и ничью
		this.result       = result;
		this.activePlayer = controller.getActivePlayer() == WHITE ? "Player White" : "Player Black";
		this.otherPlayer  = controller.getActivePlayer() == WHITE ? "Player Black" : "Player White"; 
	}
	
	
	//METHODS
	public void setSceneAndShow() {
		secondStage.setTitle("");
		String endMessage       = result == RESIGN ? activePlayer+" сдались."+"\n"+otherPlayer+" победил." : "Согласились на ничью";
		String acceptButtonText = result == RESIGN ? "Сдаться" : "Принять";
		String cancelButtonText = result == RESIGN ? "Играть дальше" : "Отклонить";
		String confirmationText = result == RESIGN ? activePlayer+"\n"+" вы уверены что хотите сдаться?" : activePlayer+" предлагают ничью."+"\n"+otherPlayer+" вы принимаете предложение?";
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AdditionStage.fxml"));
			Scene endedByUserScene = new Scene(loader.load());
			secondStage.setScene(endedByUserScene);
			endedByUserController = loader.getController();
			//Прослушиватель для "пользователь принимает ничью или уверен, что хочет сдаться.
			endedByUserController.getEndedByUser().addListener((ChangeListener<Boolean>) (ObservableValue, oldValue, newValue) -> {
				if (newValue == true) {
					if (result == RESIGN) {
						Sounds.resign();
					}
					else Sounds.draw();
				}
				controller.evaluationMessages.setText(endMessage);
				controller.getBoardHandler().endGame();
			});
			endedByUserController.acceptButton.setText(acceptButtonText);
			endedByUserController.cancelButton.setText(cancelButtonText);
			endedByUserController.message.setText(confirmationText);
		} catch (IOException e) {
			e.printStackTrace();
		}
		secondStage.centerStage();
		secondStage.show();
	}
}
