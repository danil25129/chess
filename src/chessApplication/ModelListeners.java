package chessApplication;

import static chess.util.MoveValidator.ValidationResult.LEGAL_MOVE;
import static chessApplication.DragAndDropHandler.setMoveLegal;

import chess.Game;
import chess.Move;
import chess.util.Action;
import chess.util.GameEvaluator.EvaluationResult;
import chess.util.MoveValidator.ValidationResult;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;

public class ModelListeners {

   private Game     game;
   private BoardSupportMethods  boardHandler;
   private MainController   controller;
   private MapChangeListener<Enum<Action>, Move> extraAction;
   private ListChangeListener<EvaluationResult>  evaluation;
   private ListChangeListener<ValidationResult>  validation; 
   

   public ModelListeners(MainController controller) {
      this.controller   = controller;
      this.boardHandler = controller.getBoardHandler();
      this.game         = controller.getGame();
      controller.setModelListeners(this);
      this.set();
   }



   public void set() {
      //добавляет обработчик события для того, когда будет взята фигура.
      //добавляет обработчик события при превращении пешки
      game.getAction().addListener(extraAction = change -> {
         if (change.wasAdded()) {
            boardHandler.executeAction(change.getKey(), change.getValueAdded());
         };
      });
      //добавляет обработчик события для правильности хода
      game.getValidationResult().addListener(validation = change -> {
         change.next();
         if (change.wasAdded()) {
            if (change.getAddedSubList().get(0) == LEGAL_MOVE) {
               setMoveLegal(true);
            }
         }
      });
      //добавляет обработчик события для шаха, мата, и т.д.
      game.getEvaluationResult().addListener(evaluation = change -> {
         change.next();
         if (change.wasAdded()) {
            controller.handleEvaluationResult(change.getAddedSubList().get(0));
         }
      });
   }


   public void remove() {
      game.getAction().removeListener(extraAction);
      game.getEvaluationResult().removeListener(evaluation);
      game.getValidationResult().removeListener(validation);
   }
}
