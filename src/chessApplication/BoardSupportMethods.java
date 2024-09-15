package chessApplication;


import static chess.util.Action.PROMOTION;
import static chess.util.ConverterPieceView.convert;
import static chess.util.ConverterPieceView.viewPieceAtIndexes;
import static chessApplication.DragAndDropHandler.setOnDragDetected;
import static chessApplication.DragAndDropHandler.setOnDragDone;
import static chessApplication.DragAndDropHandler.setOnDragDropped;
import static chessApplication.DragAndDropHandler.setOnDragEntered;
import static chessApplication.DragAndDropHandler.setOnDragExited;
import static chessApplication.DragAndDropHandler.setOnDragOver;
import static javafx.scene.layout.GridPane.getColumnIndex;
import static javafx.scene.layout.GridPane.getRowIndex;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

import java.util.List;

import chess.Field;
import chess.Move;
import chess.util.Action;
import chessApplication.promotion.PromotionStage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class BoardSupportMethods {
	
   private static final int SIZE  = 48;
   private Color placement;
   private Move  translatedMove;
   private GridPane board;
   private MainController controller;
   private Label  viewField;
   private Field     modelField;


   public BoardSupportMethods(MainController controller) {
      this.controller = controller;
      this.board      = controller.board;
      setDimensions();
   }
   
   //метод, который заботится об изменении размера представления.
   private void setDimensions() {
      NumberBinding minOfWidthAndHeight = Bindings.min(
            controller.root.widthProperty().subtract(controller.bottom.prefWidthProperty()).divide(8),
            controller.root.heightProperty().divide(9));
      //установить выравнивание для верхних и боковых маркеров; 
      
      //установить выравнивание для доски
      List<Node> children = board.getChildren();
      for (Node c : children) {
          Region region = (Region) c;
          region.prefWidthProperty().bind(minOfWidthAndHeight);
          region.prefHeightProperty().bind(minOfWidthAndHeight);
      }
      //установите ширину обтекания для текстового узла.
      controller.evaluationMessages.wrappingWidthProperty().bind(controller.bottom.prefWidthProperty().subtract(20.0));
      //отрегулируйте размер шрифта, чтобы размер фигур увеличивался, когда доска становится больше, и уменьшался, когда доска становится меньше.
      minOfWidthAndHeight.addListener(new ChangeListener<Number>() {
    	    @Override
    	    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
    	        for (Node c : board.getChildren()) {
    	            ((Region) c).styleProperty().set("-fx-font-size:" + SIZE + ";");
    	        }
    	    }
    	});

   }


   //вспомогательная функция
   public Integer translate (Color c, Integer i) {
      return c == WHITE ? 7-i : i;
   };

   /*
    * устанавливает все фигуры на доске и регулирует маркеры вокруг доски.
 * Параметр color определяет цвет фигур в нижней части доски.
 * Белый = черные фигуры вверху доски, белые фигуры внизу. Черный = наоборот.
    */
   public void set(Color piecesAtBottom) {
      this.placement = piecesAtBottom;
      //расставляет фигуры или ничего, если поле пустое.
      List<Node> children = board.getChildren();
      for (int i = 0; i < children.size(); i++) {
          Node c = children.get(i);
          if (c instanceof Labeled) {
              Labeled labeled = (Labeled) c;
              labeled.setText(viewPieceAtIndexes(
                  translate(placement, getRowIndex(c)),
                  translate(placement, getColumnIndex(c))
              ).orElse(""));
          }
      }
   }

   //Функция, которая возвращает переведенный ход при передаче хода из вида в модель и наоборот. Эта функция необходима
   // потому что вид позволяет играть белыми внизу или вверху доски. Это приводит к зеркальному отражению ходов.
   public  Move translateMove (Move m) {
      if (placement == BLACK) return m;
      Move translatedMove = new Move(
            new Field(translate(placement, m.getStartX()), translate(placement, m.getStartY())), 
            new Field(translate(placement, m.getEndX()), translate(placement, m.getEndY())));
      translatedMove.setPiece(m.getPiece());
      return translatedMove;


   };

   /*
    * Параметр - это кнопка, которую вы хотите включить. Этот метод
 * обрабатывает включение/выключение других кнопок по мере необходимости.
    */
   public void handleButtons(Button b) {
      if (b == controller.start) {
         controller.start.setDisable(false);
         controller.resign.setDisable(true);
         controller.offerDraw.setDisable(true);
      }
      if (b == controller.resign || b == controller.offerDraw) {
         controller.start.setDisable(true);
         controller.resign.setDisable(false);
         controller.offerDraw.setDisable(false);
      }
   };


   //способ изменить цвет текста для активного игрока
   @FXML
   public void switchPlayer() {
      controller.setActivePlayer(controller.getActivePlayer() == WHITE ? BLACK : WHITE); 
   }

   /*
    * метод, который выполняет дополнительные действия, необходимые при рокировке, проходе и продвижении.
 * Для рокировки это размещение ладьи, а для прохода - удаление захваченной пешки.
 * Для продвижения отображается этап продвижения, так что пользователь может выбрать материал для продвижения.
    */
   public void executeAction(Enum<Action> action, Move move) {
	 //превращение содержит ход, сделанный пешкой в последнюю строку. Мы получаем поля продвижения модели и вида.
	   //Мы показываем этап продвижения пользователю, который может выбрать фигуру для продвижения.
      if (action == PROMOTION) {
         modelField = move.getEnd();
         translatedMove = translateMove(move);
         List<Node> children = board.getChildren();
         for (int i = 0; i < children.size(); i++) {
             Node c = children.get(i);
             if (getRowIndex(c) == translatedMove.getEndX() && getColumnIndex(c) == translatedMove.getEndY()) {
                 viewField = (Label) c;
                 break;
             }
         }
         PromotionStage promotionStage = new PromotionStage(controller, translatedMove.getColor());
         promotionStage.setSceneAndShow();
      }
   }

 //метод, который устанавливает фигуру превращения в представлении и модели.
  public void setPromotionPiece(String piece) {
      viewField.setText(piece);
      controller.getGame().setPromotionPiece(modelField, convert(piece));
   }

   public void clear() {
	   ObservableList<Node> children = board.getChildren();
	   for (Node c : children) {
	       if (c instanceof Labeled) {
	           ((Labeled) c).setText("");
	       }
	   }
      controller.evaluationMessages.setText("");
   }

   /*
    * устанавливает вид в положение "игра окончена".
 * Активирует кнопку Start.
    */
   public void endGame() {
      removeDragAndDrop();
      controller.getViewListeners().remove();
      controller.getModelListeners().remove();
      handleButtons(controller.start);
   }

   /*
    *метод устанавливает обработчики перетаскивания для
 * фрагментов.
    */
   public void setDragAndDrop() {
	   List<Node> children = board.getChildren();
	   for (Node c : children) {
	       if (c instanceof Label) {
	           setOnDragDetected((Label) c);
	           setOnDragEntered((Label) c);
	           setOnDragOver((Label) c);
	           setOnDragExited((Label) c);
	           setOnDragDropped((Label) c);
	           setOnDragDone((Label) c);
	       }
	   }
   }

   /*
    *  удаляет обработчики перетаскивания.
    */
   public void removeDragAndDrop() {
	   for (Node c : board.getChildren()) {
		    Labeled labeled = (Labeled) c;
		    labeled.setOnDragDetected(null);
		    labeled.setOnDragOver(null);
		    labeled.setOnDragExited(null);
		    labeled.setOnDragEntered(null);
		    labeled.setOnDragDropped(null);
		    labeled.setOnDragDone(null);
		}
   }



}
