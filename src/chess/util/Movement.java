package chess.util;




import chess.Move;


public class Movement {

	public static boolean noRows(Move m) {
		return m.getEndX() - m.getStartX() == 0;
	};

	public static boolean noColumns(Move m) {
		return m.getEndY() - m.getStartY() == 0;
	};

	public static boolean diagonal(Move m) {
		return m.numberOfRows() == m.numberOfColumns();
	};

	public static boolean straight(Move m) {
		return m.numberOfRows() == 0 ^ m.numberOfColumns() == 0;
	};

	public static boolean jump (Move m) {
		return m.numberOfRows() == 2 && m.numberOfColumns() == 1
				|| m.numberOfRows() == 1 && m.numberOfColumns() == 2;
	};

	public static boolean oneRow (Move m) {
		return m.numberOfRows() == 1;
	};

	public static boolean oneColumn (Move m) {
		return m.numberOfColumns() == 1;
	};

	public static boolean twoRows (Move m) {
		return m.numberOfRows() == 2;
	};

	public static boolean twoColumns (Move m) {
		return m.numberOfColumns() == 2 ;
	};

	public static boolean oneRowOrOneColumnOrBoth (Move m) {
		return m.numberOfRows() <= 1 &&  m.numberOfColumns() <= 1;
	};

	public static boolean left(Move m) {
		return m.getEndY() - m.getStartY() >= 1;
	};

	public static boolean right (Move m) {
		return m.getStartY() - m.getEndY() >= 1;
	};
	public static boolean up (Move m) {
		return m.getEndX() - m.getStartX() >= 1;
	};

	public static boolean down (Move m) {
		return m.getStartX() - m.getEndX() >= 1;
	};
}




