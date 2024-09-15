package chessApplication;


import static chess.util.ConverterPieceView.getAllWhiteViewPieces;
import static chess.util.ConverterPieceView.getPathToImage;
import static javafx.scene.layout.GridPane.getColumnIndex;
import static javafx.scene.layout.GridPane.getRowIndex;
import static javafx.scene.paint.Color.AQUAMARINE;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

import chess.Field;
import chess.Move;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;


public final class DragAndDropHandler {

   private static final double SIZE_THRESHOLD   = 78.0;
   private static final int  OFFSET_SMALL_IMG = 14;
   private static final int  OFFSET_BIG_IMG   = 22;
   private static final Label TEMP_BG          = new Label();
   private static final Background AQUAMARINE_COLOR        = new Background(new BackgroundFill(AQUAMARINE, CornerRadii.EMPTY, Insets.EMPTY));
   private static final ObjectProperty<Move>  MOVE             = new SimpleObjectProperty<Move>();
   private static final ObservableBooleanValue MOVE_LEGAL       = new SimpleBooleanProperty(false);


   private DragAndDropHandler() {}


   /*
    * Обнаружено перетаскивание, запустите жест перетаскивания. Разрешите любой режим переноса.
 * Поместите фрагмент на панель перетаскивания.
 * Установите изображение dragView.
    */
   public static void setOnDragDetected(Label l) {
	   EventHandler<MouseEvent> onDragDetected = new EventHandler<MouseEvent>() {
	   @Override
	   public void handle(MouseEvent e) {
	   Dragboard db = l.startDragAndDrop(TransferMode.ANY);
	   if (l.getText() != "") {
	   ClipboardContent content = new ClipboardContent();
	   content.putString(l.getText());
	   int offset = l.getWidth() < SIZE_THRESHOLD ? OFFSET_SMALL_IMG : OFFSET_BIG_IMG;
	   db.setDragView(new Image(getPathToImage(l.getText(), l.getWidth())), offset, offset);
	   l.setText("");
	   db.setContent(content);
	   }
	   e.consume();
	   }
	   };
	   l.setOnDragDetected(onDragDetected);
	}

   /*
    * возвращает значение true, если метка, которую мы вводим, пуста или содержит фигуру противника
    */
   private static boolean emptyOrOpponent (Label l, DragEvent e) {
      if (l.getText() == "") return true;
      Color  attack  = getAllWhiteViewPieces().contains(e.getDragboard().getString()) ? WHITE : BLACK;
      Color  defense = getAllWhiteViewPieces().contains(l.getText()) ? WHITE : BLACK;
      return attack != defense;
   };

   /*
    * Данные перетаскиваются через целевой объект, принимаются только в том случае, если они не перетаскиваются с того же узла
 * и если они содержат строковые данные и если поле пусто или содержит врага.
    */
   public static void setOnDragOver(Label l) {
	    EventHandler<DragEvent> onDragOver = new EventHandler<DragEvent>() {
	        @Override
	        public void handle(DragEvent e) {
	            if (e.getGestureSource() != l &&
	                e.getDragboard().hasString() &&
	                emptyOrOpponent(l, e)) {
	                e.acceptTransferModes(TransferMode.MOVE);
	            }
	            e.consume();
	        }
	    };
	    l.setOnDragOver(onDragOver);
	}

   /*
    * Мышь перешла на следующий label, убрать графическую подсказку на label
    */
   public static void setOnDragExited(Label l) {
	   EventHandler<DragEvent> onDragExited = new EventHandler<DragEvent>() {
	   @Override
	   public void handle(DragEvent e) {
	   if (emptyOrOpponent(l, e)) {
	   l.setBackground(TEMP_BG.getBackground());
	   }
	   e.consume();
	   }
	   };
	   l.setOnDragExited(onDragExited);
	   }

   /*
    * перетаскивания на цели.
 * Показывает игроку, что это фактическая цель.
    */
   public static void setOnDragEntered(Label l){
	   EventHandler<DragEvent> onDragEntered = new EventHandler<DragEvent>() {
	   @Override
	   public void handle(DragEvent e) {
	   if (e.getDragboard().hasString() && emptyOrOpponent(l, e)) {
	   TEMP_BG.setBackground(l.getBackground());
	   if (e.getGestureSource() != l) l.setBackground(AQUAMARINE_COLOR);
	   }
	   e.consume();
	   }
	   };
	   l.setOnDragEntered(onDragEntered);
	   }

   /*
    * Метод, который создает перемещение для модели из перемещения, сделанного пользователем
    * в представлении.
    */
   private static Move convertViewToModel (Label l, DragEvent e) {
      Field start = new Field(getRowIndex(((Label) e.getGestureSource())), getColumnIndex(((Label) e.getGestureSource())));
      Field end   = new Field(getRowIndex(l), getColumnIndex(l));
      return new Move(start, end);
   };

   /*
    * Если в dragBoard есть строковые данные, прочитайте их и используйте. 
 * В модели будет воспроизведен Move и будет установлен moveLegal.
    */
   public static void setOnDragDropped(Label l) {
	   EventHandler<DragEvent> onDragDropped = new EventHandler<DragEvent>() {
	   @Override
	   public void handle(DragEvent e) {
	   ((SimpleBooleanProperty) MOVE_LEGAL).set(false);
	   if (e.getDragboard().hasString()) {
	   MOVE.set(convertViewToModel(l, e));
	   if (MOVE_LEGAL.get() == true) {
		   if (l.getText() != "") {
			   Sounds.captureStop();
			   Sounds.capture();
			   l.setBackground(TEMP_BG.getBackground());
			   l.setText(e.getDragboard().getString());
		   } else {
	   Sounds.move();
	   l.setBackground(TEMP_BG.getBackground());
	   l.setText(e.getDragboard().getString());
		   }
	   }
	 }
	   e.setDropCompleted(MOVE_LEGAL.get());
	   e.consume();
	  }
	 };
	   l.setOnDragDropped(onDragDropped);
	}

   /*
    * Жест перетаскивания закончился неудачно,
    * поместите фрагмент обратно на его начальное поле.
    */
   public static void setOnDragDone(Label l) {
	    EventHandler<DragEvent> onDragDone = new EventHandler<DragEvent>() {
	        @Override
	        public void handle(DragEvent e) {
	            if (e.getTransferMode() != TransferMode.MOVE) {
	                ((Labeled) e.getGestureSource()).setText(e.getDragboard().getString());
	            }
	            e.consume();
	        }
	    };
	    l.setOnDragDone(onDragDone);
	}


  
   public static ObjectProperty<Move> getMove() {
      return MOVE;
   }

   public static ObservableBooleanValue getMoveLegal() {
      return MOVE_LEGAL;
   }

   public static void  setMoveLegal(boolean value) {
      ((BooleanPropertyBase) MOVE_LEGAL).set(value);
   }
}

