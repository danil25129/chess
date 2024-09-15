package chess;

import static chess.util.Action.PROMOTION;
import static chess.util.MoveValidator.ValidationResult.LEGAL_MOVE;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.util.Action;
import chess.util.GameEvaluator;
import chess.util.GameEvaluator.EvaluationResult;
import chess.util.MoveValidator;
import chess.util.MoveValidator.ValidationResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;

public class Game {

   private Board board;
   private Player white;
   private Player  black;
   private Move  lastMove;
   private Player activePlayer; 
   
   private ObservableMap<Enum<Action>, Move> action                = FXCollections.observableMap(new HashMap<>());
   private ObservableList<ValidationResult> validationResult      = FXCollections.observableList(new ArrayList<>()); 
   private ObservableList<EvaluationResult> evaluationResult      = FXCollections.observableList(new ArrayList<>());
 
   public Game (Board board, Player player1, Player player2) {
      this.board   = board;
      white        = player1.getColor() == WHITE ? player1 : player2;
      black        = player1.getColor() == BLACK ? player1 : player2;
      activePlayer = white; 

   }

   //METHODS
   public void play(Move move) {
      validateMove(this, move);
      if (validationResult.get(validationResult.size() -1) == LEGAL_MOVE) {
         executeMove(move);
         evaluateGame(move);
         lastMove = move;
      }
   }

   private void executeMove(Move move) {
      execute(move);
      hasMovedPawn(move);
      promotion(move);
      setActivePlayer(move.getColor() == WHITE ? black : white);
   }

   //Оценивает игру и добавьте эту оценку в ObservableList.
   private void evaluateGame(Move m) {
      evaluationResult.add(GameEvaluator.FinalEvaluation(this, m));
   };

   //проверяет правильность перемещения и добавляет результат в ObservableList.

   private void validateMove(Game g, Move m) {
      validationResult.add(MoveValidator.finalResult(g, m));

   };

   /*
    * возвращает true если пути фигуры ничего не преграждает
    */
   
   
   
   public static boolean pathUnobstructed(Game g, Move m) {
      Piece piece = m.getPiece();
      //Кони могут прыгать, а у пешек нет пути, потому что они могут перемещаться только по одному полю
      //Если их путь заблокирован, пешка делает ход, который является некорректным для пешки.
      if (piece instanceof Knight ||  piece instanceof Pawn) return true;
      //определение направления движения
      int oneRow = m.getEndX() - m.getStartX();
      int oneColumn = m.getEndY() - m.getStartY();
      if (oneRow != 0) oneRow /= Math.abs(oneRow); //oneRow,oneColumn = 1 or 0 or -1
      if (oneColumn != 0) oneColumn /= Math.abs(oneColumn); 
      //перемещение по одному полю за раз в направлении движения
      Field path = g.getField(m.getStartX() + oneRow, m.getStartY() + oneColumn);
      while (path != m.getEnd()) {
         if (path.piecePresent()) return false;
         path = g.getField(path.getX() + oneRow, path.getY() + oneColumn); 
      }
      return true;
   };



   /*
    * Определяет местоположение королей и возвращает Map, где цвет является ключевым, а 
    * поле, содержащее короля, является значением.
    */ 
      public static Map<Color, Field> searchForKings(Game g) {
	   
	   Field[][] board= g.getBoardState();
	   Map<Color, Field> map = new HashMap<>();
	   for (int i = 0; i < board.length; i++) {
		   for (int j = 0; j < board.length; j++) {
			   if (board[i][j].piecePresent() && board[i][j].getPiece().get() instanceof King) {
				   map.put(board[i][j].getPieceColor().get(), board[i][j]);
			   }
		   }
	   }
	   
	   
      return map;
   };
   
   
   /*
    * возвращает список, содержащий все поля с фигурой заданного цвета
    */  
   
   private static List<Field> allOfColor(Game g, Color c) {
	   
	   Field[][] board= g.getBoardState();
	   List<Field> list = new ArrayList<>();
	   for (int i = 0; i < board.length; i++) {
		   for (int j = 0; j < board.length; j++) {
			   if (board[i][j].getPieceColor().isPresent() && board[i][j].getPieceColor().get() == c) {
				   list.add(board[i][j]);
			   }
		   }
	   }
	   
	   
      return list;
   };
   
   /*
    * возвращает список, содержащий все ходы, которые может сделать противник, чтобы атаковать данное поле.
    */
			   
   public static List<Move> attackingMoves(Game g, Field fieldUnderAttack) {
      Move testMove = new Move(null, fieldUnderAttack);
      List<Field> listFieldColor= allOfColor(g, fieldUnderAttack.getPieceColor().get() == WHITE ? BLACK : WHITE);
      List<Move> attackMoves = new ArrayList<>();
      
      for(Field fieldColor: listFieldColor) {
    	  testMove.setStart(fieldColor);
    	  testMove.setPiece(fieldColor.getPiece().get());
    	  if (testMove.getPiece().checkMove(g, testMove) && pathUnobstructed(g, testMove)) {
    		  attackMoves.add(testMove);
    	  }
      }
      
      return attackMoves;
   }
   /*
    * проверка, не находится ли сам игрок под шахом, сделав ход. Возвращает значение true, если
    * король не под шахом.
    */
   public static boolean kingSave(Game g, Move m) {
      //делаем ход
      Optional<Piece> enemy = g.rewindableMove(m);
      //проверяем, в безопасности ли король
      boolean kingSave = m.getColor() == WHITE ? attackingMoves(g, searchForKings(g).get(WHITE)).isEmpty() : 
               attackingMoves(g, searchForKings(g).get(BLACK)).isEmpty();
            //отменяем ход		
            g.rewindMove(m, enemy);
            return kingSave;
   };

   
   /*
    * returns true if the move results in a check on the opponent's king.
    */
   public boolean checkOnOpponent(Move m) {
      return !attackingMoves(this, m.getColor() == Color.WHITE ?
            searchForKings(this).get(BLACK) :
               searchForKings(this).get(WHITE)).isEmpty();
   };

   /*
    * устанавливает значение true при первом перемещении фигуры.
    * Это необходимо только для ладей, пешек и королей.
    */
   private void hasMovedPawn(Move m) {
      if (!m.getPiece().hasMoved() && m.getPiece() instanceof Pawn) m.getPiece().setHasMoved(true);		
   };

   /*
    *возвращает все возможные поля, в которые могут перемещаться фигуры заданного цвета
    */
   private List<Field> allPositionsFor(Game g, Color c) {
	   Field[][] board= g.getBoardState();
	   List<Field> list = new ArrayList<>();
	   for (int i = 0; i < board.length; i++) {
		   for (int j = 0; j < board.length; j++) {
			   if (!board[i][j].piecePresent() || 
					   (board[i][j].piecePresent() && board[i][j].getPiece().get().getColor() == 
					   (c == Color.WHITE ? Color.BLACK: Color.WHITE))) {
				   list.add(board[i][j]);
			   }
		   }
	   }
	   
      return list;
   };


   /*
    * возвращает все ходы, которые может сделать фигура на данном поле, в виде списка со
 * всеми полями, которые пусты или на которых есть противник
    */
   private List<Move> allMovesForPiece(Field f, List<Field> l) {
      Move testMove = new Move(f, null);
      List<Move> accessedMoves = new ArrayList<>();
      
      for(Field fields: l) {
    	  testMove.setEnd(fields);
    	  if (f.getPiece().isPresent() && f.getPiece().get().checkMove(this, testMove) 
    			  && pathUnobstructed(this, testMove) && kingSave(this, testMove)) {
    		  accessedMoves.add(testMove);
    	  }
      }
      
      return accessedMoves;
   };
   
   
   
   /*
    * возвращает список, содержащий все ходы, которые может сделать противник.
    */
   private List<Move> allMovesForOpponent(Move m) {
	 //allMovesForPiece возвращает допустимые ходы = ход, при котором kingSave() true, поэтому, если есть
	   //ходы, то это не мат.
	      List<Field> listFieldColor= allOfColor(this, m.getColor() == WHITE ? BLACK : WHITE);
	      List<Move> accessedEnemyMoves = new ArrayList<>();
	      
	      for(Field fieldColor: listFieldColor) {
	    	  accessedEnemyMoves.addAll(allMovesForPiece(fieldColor, allPositionsFor(this, m.getColor() == WHITE ? BLACK : WHITE)));
	    	  }
	      
	      return accessedEnemyMoves;
   };

   /*
    * возвращает true если мат
    */
   public boolean checkmate(Move m) {
      return allMovesForOpponent(m).isEmpty() && checkOnOpponent(m);
   };

   /*
    * возвращает true если пат
    */
   public boolean stalemate(Move m) {
      return allMovesForOpponent(m).isEmpty() && !checkOnOpponent(m);
   };

   /*
    * возвращает true если превращение пешки
    */
   public boolean isPromotion(Move m){
      return m.getPiece() instanceof Pawn && ((m.getColor() == WHITE && m.getEndX() == 7)
                  || (m.getColor() == BLACK && m.getEndX() == 0));
   };

   /*
    * проверяет если пешка находится на позиции превращения 
    * and помещает действие в ObservableMap action.
    */
   private void promotion(Move m) {
      if (isPromotion(m)) action.put(PROMOTION, m);
   };

 

   /*
    * размещает фигуру превращения на своем поле и оценивает новую ситуацию.
    */
   public void setPromotionPiece(Field field, Piece piece) {
      field.setPiece(piece);
      evaluateGame(new Move(field, field));
   }

   /*
    * метод, используемый для получения фактического перемещения в модели. 
    */
   public Move viewToModel(Move move) {
      Move modelMove = new Move(getField(move.getStartX(), move.getStartY()), getField(move.getEndX(), move.getEndY()));
      modelMove.setPiece(getPiece(move.getStartX(), move.getStartY()).get());
      return modelMove;
   }

   /*
    * возвращает optional taken piece. Этот метод в сочетании с
 * rewindMove выполняет ход, а затем отменяет этот ход.
    */
   private Optional<Piece> rewindableMove(Move move) {
      move.getStart().setPiece(null);
      Optional<Piece> takenPiece = move.getEnd().getPiece();
      move.getEnd().setPiece(move.getPiece());
      return takenPiece;
   }
   /*
    * отменяет перемещение, выполненное методом rewindableMove.
    */
   private void rewindMove(Move move, Optional<Piece> takenPiece) {
      move.getStart().setPiece(move.getPiece());
      move.getEnd().setPiece(takenPiece.orElse(null));

   }

   /*
    * выполняет ход
    */
   private void execute(Move move) {
      move.getStart().setPiece(null);
      move.getEnd().setPiece(move.getPiece());
   }

   public Optional<Piece> getPiece(int x, int y){
      return getBoardState()[x][y].getPiece();
   }

   public Field getField(int x, int y) {
      return getBoardState()[x][y];
   }

   public Player getActivePlayer() {
      return activePlayer;	
   }

   public void setActivePlayer(Player player) {
      activePlayer = player;
   }


   public Field[][] getBoardState() {
      return board.getBoardState();
   }


   public Move getLastMove() {
      return lastMove;
   }



   public  ObservableMap<Enum<Action>, Move> getAction() {
      return action;
   }

   public ObservableList<EvaluationResult> getEvaluationResult() {
      return evaluationResult;
   }

   public ObservableList<ValidationResult> getValidationResult(){
      return validationResult;
   }
}
