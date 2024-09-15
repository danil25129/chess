package chessApplication;

import static chessApplication.DragAndDropHandler.getMove;
import static chessApplication.DragAndDropHandler.getMoveLegal;

import chess.Move;
import javafx.beans.value.ChangeListener;

public class MoveListeners {

   private MainController controller;
   private BoardSupportMethods   boardHandler;
   private Move     modelMove;
   private ChangeListener<Move>  move;
   private ChangeListener<Boolean>    moveLegal;

   public MoveListeners(MainController controller) {
      this.controller   = controller;
      this.boardHandler = controller.getBoardHandler();
   }



   public void set() {
	      /*
	       * установливает прослушиватель на ход от пользователя. Переводит ход (для учета
 * игры белыми или черными в нижней части доски). Преобразует ход
 * в ход для модели и позволяет модели воспроизвести этот ход.
	       */
	      getMove().addListener(move = (ObservableValue, oldValue, newValue) -> {
	         Move translatedMove = boardHandler.translateMove(newValue);
	         modelMove = controller.getGame().viewToModel(translatedMove);
	         controller.getGame().play(modelMove);
	      });

	      /*
	       * мы хотим переключать игроков, когда DragAndDrop был успешным и подтвержден моделью.
	       */
	      getMoveLegal().addListener(moveLegal = (ObservableValue, oldValue, newValue) -> {
	         if (newValue == true && !controller.getGame().isPromotion(modelMove)) boardHandler.switchPlayer();
	      });
	   }

	   public void remove() {
	      getMove().removeListener(move);
	      getMoveLegal().removeListener(moveLegal);
	   }
}
