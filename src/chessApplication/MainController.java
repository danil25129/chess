package chessApplication;

import static chess.util.GameEvaluator.EvaluationResult.OFFER_DRAW;
import static chess.util.GameEvaluator.EvaluationResult.RESIGN;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

import chess.Board;
import chess.Game;
import chess.Player;
import chess.util.GameEvaluator.EvaluationResult;
import chessApplication.endedbyuser.AdditionStage;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainController extends Application{

	private TempStage  secondStage      = new TempStage();
	private Color activePlayer     = WHITE;
	private BoardSupportMethods boardHandler; 
	private Game  game;
	private MoveListeners viewListeners;
	private ModelListeners modelListeners;

		@FXML
	    public Button offerDraw;

	    @FXML
	    public Text evaluationMessages;

	    @FXML
	    public BorderPane root;

	    @FXML
	    public Button start;

	    @FXML
	    public VBox bottom;

	    @FXML
	    public Button resign;

	    @FXML
	    public GridPane board;


	public MainController() {}
	
	
	@FXML
	private void initialize() {
		boardHandler  = new BoardSupportMethods(this);
		boardHandler.set(BLACK);
		viewListeners = new MoveListeners(this);
	}

	public void handleEvaluationResult(EvaluationResult result) {
		String color = activePlayer == WHITE ? "Black" :"White";
		String draw  = "\n"+" Игра закончилась в ничью.";
		if (getActivePlayer() == WHITE) {
		}
		if (getActivePlayer() == WHITE) {
		}
		String checkmate = "\n"+ color +" wins.";
		AdditionStage endedByUserStage;
		switch (result) {
		case CHECK:
			evaluationMessages.setText(result.toString());
			boardHandler.handleButtons(offerDraw);
			break;
		case CHECKMATE:
			Sounds.checkmate();
			evaluationMessages.setText(result.toString()+ checkmate);
			boardHandler.endGame();
			break;
		case STALEMATE:
		   Sounds.stalemate();
			evaluationMessages.setText(result.toString()+ draw);
			boardHandler.endGame();
			break;
		case RESIGN:
			endedByUserStage = new AdditionStage(this, result);
			endedByUserStage.setSceneAndShow();
			break;
		case OFFER_DRAW:
			endedByUserStage = new AdditionStage(this, result);
			endedByUserStage.setSceneAndShow();
			break;
		case NORMAL_GAME_SITUATION: 
			evaluationMessages.setText("");
			boardHandler.handleButtons(offerDraw);
			break;
		default:
			break;
		}
	};
	
	
	//способ отображения окна параметров запуска.
	@FXML
	private void startButtonAction() {
		this.boardHandler.handleButtons(offerDraw);
		//очищает доску и начинает новую игру
		this.boardHandler.clear();
		Player white = new Player("", WHITE);
		Player black = new Player("", BLACK);
		Game game = new Game(new Board(), white, black);
		game.setActivePlayer(white);
		this.setActivePlayer(WHITE);
		//передает модель в контроллер
		this.setGame(game);
		//установите доску и фигуры на видном месте.
		this.boardHandler.set(WHITE);
		//установите обработчики событий для жеста перетаскивания
		this.boardHandler.setDragAndDrop();
		//установка прослушивателей на наблюдаемые значения из модели и представления.
		new ModelListeners(this);
		this.getViewListeners().set();

	}

	@FXML
	private void resignButtonAction() {
		handleEvaluationResult(RESIGN);
	}

	@FXML
	private void offerDrawButtonAction() {
		handleEvaluationResult(OFFER_DRAW);

	}


	@Override
	public void start(Stage stage) throws Exception {
		stage.getIcons().add(new Image(getClass().getResource("../resources/images/icon.png").toString()));
		stage.setTitle("ChessGame");
		root = FXMLLoader.load(getClass().getResource("StartInterface.fxml"));
		Scene scene = new Scene(root, 860, 490);
		stage.setMinWidth(860);
		stage.setMinHeight(525);
		stage.setScene(scene);
		stage.show();
		//При первом воспроизведении аудиоклипы воспроизводятся с задержкой. Этой ошибки можно избежать, если воспроизвести клип без звука здесь.
		Sounds.lagWorkAround();
	}


	public static void main(String[] args) {
		launch(args);
	}

	public TempStage getSecondStage() {
		return secondStage;
	}


	public BoardSupportMethods getBoardHandler() {
		return boardHandler;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public Color getActivePlayer() {
		return activePlayer;
	}

	public void setActivePlayer(Color color) {
		this.activePlayer = color;
	}
	
	public MoveListeners getViewListeners() {
		return viewListeners;
	}
	
	public ModelListeners getModelListeners() {
		return modelListeners;
	}

	public void setModelListeners(ModelListeners modelListeners) {
		this.modelListeners = modelListeners;
	}
}
