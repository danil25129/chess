package chess.util;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Queen;
import chess.pieces.Rook;
import javafx.util.Pair;

public final class ConverterPieceView {

   private static final double  SIZE_THRESHOLD = 78.0;
   private static final int SMALL_IMAGE    = 2;
   private static final int BIG_IMAGE      = 3;
   private static final Map<Piece, List<String>> FEN_MAP        = new HashMap<Piece, List<String>>();
   private static final Map<List<Pair<Integer, Integer>>,Pair<String, Piece>> PIECES = new HashMap<>();

   private ConverterPieceView() {}

   static {
      //белые
      FEN_MAP.put(new King  (WHITE), Arrays.asList("K","♔", ConverterPieceView.class.getClassLoader().getResource("resources/images/normal/whiteKing.png").toString(),
            ConverterPieceView.class.getClassLoader().getResource("resources/images/big/whiteKing2.png").toString()));
      FEN_MAP.put(new Queen (WHITE), Arrays.asList("Q","♕", ConverterPieceView.class.getClassLoader().getResource("resources/images/normal/whiteQueen.png").toString(),
            ConverterPieceView.class.getClassLoader().getResource("resources/images/big/whiteQueen2.png").toString()));
      FEN_MAP.put(new Rook  (WHITE), Arrays.asList("R","♖", ConverterPieceView.class.getClassLoader().getResource("resources/images/normal/whiteRook.png").toString(),
            ConverterPieceView.class.getClassLoader().getResource("resources/images/big/whiteRook2.png").toString()));
      FEN_MAP.put(new Bishop(WHITE), Arrays.asList("B","♗", ConverterPieceView.class.getClassLoader().getResource("resources/images/normal/whiteBishop.png").toString(),
            ConverterPieceView.class.getClassLoader().getResource("resources/images/big/whiteBishop2.png").toString()));
      FEN_MAP.put(new Knight(WHITE), Arrays.asList("N","♘", ConverterPieceView.class.getClassLoader().getResource("resources/images/normal/whiteKnight.png").toString(),
            ConverterPieceView.class.getClassLoader().getResource("resources/images/big/whiteKnight2.png").toString()));
      FEN_MAP.put(new Pawn  (WHITE), Arrays.asList("P","♙", ConverterPieceView.class.getClassLoader().getResource("resources/images/normal/whitePawn.png").toString(),
            ConverterPieceView.class.getClassLoader().getResource("resources/images/big/whitePawn2.png").toString()));
      //черные
      FEN_MAP.put(new King  (BLACK), Arrays.asList("k","♚", ConverterPieceView.class.getClassLoader().getResource("resources/images/normal/blackKing.png").toString(), 
            ConverterPieceView.class.getClassLoader().getResource("resources/images/big/blackKing2.png").toString()));
      FEN_MAP.put(new Queen (BLACK), Arrays.asList("q","♛", ConverterPieceView.class.getClassLoader().getResource("resources/images/normal/blackQueen.png").toString(),
            ConverterPieceView.class.getClassLoader().getResource("resources/images/big/blackQueen2.png").toString()));
      FEN_MAP.put(new Rook  (BLACK), Arrays.asList("r","♜", ConverterPieceView.class.getClassLoader().getResource("resources/images/normal/blackRook.png").toString(),
            ConverterPieceView.class.getClassLoader().getResource("resources/images/big/blackRook2.png").toString()));
      FEN_MAP.put(new Bishop(BLACK), Arrays.asList("b","♝", ConverterPieceView.class.getClassLoader().getResource("resources/images/normal/blackBishop.png").toString(),
            ConverterPieceView.class.getClassLoader().getResource("resources/images/big/blackBishop2.png").toString()));
      FEN_MAP.put(new Knight(BLACK), Arrays.asList("n","♞", ConverterPieceView.class.getClassLoader().getResource("resources/images/normal/blackKnight.png").toString(),
            ConverterPieceView.class.getClassLoader().getResource("resources/images/big/blackKnight2.png").toString()));
      FEN_MAP.put(new Pawn  (BLACK), Arrays.asList("p","♟", ConverterPieceView.class.getClassLoader().getResource("resources/images/normal/blackPawn.png").toString(),
            ConverterPieceView.class.getClassLoader().getResource("resources/images/big/blackPawn2.png").toString()));
   }

   static {
      PIECES.put(new ArrayList<>(Arrays.asList(new Pair<>(0,3))), new Pair<>("♔", new King(WHITE)));
      PIECES.put(new ArrayList<>(Arrays.asList(new Pair<>(7,3))), new Pair<>("♚", new King(BLACK)));
      PIECES.put(new ArrayList<>(Arrays.asList(new Pair<>(0,4))), new Pair<>("♕", new Queen(WHITE)));
      PIECES.put(new ArrayList<>(Arrays.asList(new Pair<>(7,4))), new Pair<>("♛", new Queen(BLACK)));
      PIECES.put(new ArrayList<>(Arrays.asList(new Pair<>(0,0), new Pair<>(0,7))), new Pair<>("♖", new Rook(WHITE)));
      PIECES.put(new ArrayList<>(Arrays.asList(new Pair<>(7,0), new Pair<>(7,7))), new Pair<>("♜", new Rook(BLACK)));
      PIECES.put(new ArrayList<>(Arrays.asList(new Pair<>(0,2), new Pair<>(0,5))), new Pair<>("♗", new Bishop(WHITE)));
      PIECES.put(new ArrayList<>(Arrays.asList(new Pair<>(7,2), new Pair<>(7,5))), new Pair<>("♝", new Bishop(BLACK)));
      PIECES.put(new ArrayList<>(Arrays.asList(new Pair<>(0,1), new Pair<>(0,6))), new Pair<>("♘", new Knight(WHITE)));
      PIECES.put(new ArrayList<>(Arrays.asList(new Pair<>(7,1), new Pair<>(7,6))), new Pair<>("♞", new Knight(BLACK)));
   }

   /*
    * Вспомогательный метод. Учитывая индекс строки и столбца, возвращает поток, содержащий пару, которая имеет соответствующий фрагмент представления в качестве ключа и
* соответствующий фрагмент модели в качестве значения.
    */
   private static Stream<Pair<String, Piece>> getViewModelPair(Integer rowIndex, Integer columnIndex) {
	   return PIECES.entrySet()
	   .stream()
	   .filter(entryset -> entryset.getKey().contains(new Pair<>(rowIndex, columnIndex)))
	   .map(entryset -> entryset.getValue());
	   }

   /*
    * возвращает необязательное значение, содержащее символ элемента, который будет использоваться в представлении, или значение без значения, учитывая индекс строки и столбца.

    */
   public static  Optional<String> viewPieceAtIndexes (Integer rowIndex, Integer columnIndex) {
	   if (rowIndex == 1) return Optional.of("♙");
	   if (rowIndex == 6) return Optional.of("♟");
	   return getViewModelPair(rowIndex, columnIndex)
	   .map(e -> e.getKey())
	   .findFirst();
	   };

   /*
    * возвращает необязательное значение, содержащее фигуру, которая будет использоваться в модели, или значение без значения, с учетом индекса строки и столбца.
    */
	   public static  Optional<Piece> modelPieceAtIndexes (Integer rowIndex, Integer columnIndex)  {
		   if (rowIndex == 1)    return Optional.of(new Pawn(WHITE));
		   if (rowIndex == 6)    return Optional.of(new Pawn(BLACK));
		   return getViewModelPair(rowIndex, columnIndex)
		   .map(e -> e.getValue())
		   .findFirst();
		   };

   /*
    * вспомогательный метод получает то, что нам нужно, из FenMap. Предикат фильтрует значения. Значения: 0 (обозначение FEN)
 * 1 (символ элемента в представлении), 2 (путь к изображению, используемый в DragView при перемещении элементов). Ключ
 * из FenMap - это фрагмент для модели.
    */
	   private static List<String> getValues(Predicate<Entry<Piece, List<String>>> predicate, int value){
		   return FEN_MAP
		   .entrySet()
		   .stream()
		   .filter(predicate)
		   .map(e -> e.getValue().get(value))
		   .sorted()
		   .collect(Collectors.toList());
		   }

   //вспомогательный метод
	   private static Piece getKey(Predicate<Entry<Piece, List<String>>> predicate){
		   return FEN_MAP
		   .entrySet()
		   .stream()
		   .filter(predicate)
		   .map(e -> e.getKey())
		   .findFirst()
		   .get();
		   }

   //возвращает фигуру представления, этот метод возвращает соответствующий фрагмент модели.
   public static Piece convert(String viewPiece) {
      return getKey(e -> e.getValue().get(1) == viewPiece);
   }

   //возвращает фигуру модели, возвращает соответствующий фрагмент представления.
   public static String convert(Piece piece) {
      return getValues(e -> e.getKey().getClass() == piece.getClass()
            && e.getKey().getColor() == piece.getColor(), 1).get(0);
   }

   //возвращает обозначение FEN для фрагмента, возвращает фрагмент для представления.
   public static String getViewPieceFromFEN(String fen) {
      return getValues(e -> e.getValue().get(0) == fen, 1).get(0);
   }

   //возвращает фигуру модели, возвращает его обозначение FEN
   public static String getFENFromModelPiece(Piece piece) {
      return getValues(e -> e.getKey().getClass() == piece.getClass()
            && e.getKey().getColor() == piece.getColor(), 0).get(0);
   }

   //возвращает все белые фрагменты, использованные в представлении.
   public static List<String> getAllWhiteViewPieces() {
      return getValues(e -> e.getKey().getColor() == WHITE, 1);
   }

   //возвращает все черные фигуры, используемые в представлении.
   public static List<String> getAllBlackViewPieces() {
      return getValues(e -> e.getKey().getColor() == BLACK, 1);
   }

   //возвращает путь к изображению, необходимый для обработки перетаскивания фрагментов.
   public static String getPathToImage(String viewPiece, Double labelWidth) {
      return getValues(e -> (e.getValue().get(1)) == viewPiece,
            labelWidth < SIZE_THRESHOLD ? SMALL_IMAGE : BIG_IMAGE).get(0);
   }

}
