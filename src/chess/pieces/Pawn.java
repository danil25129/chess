package chess.pieces;


import static chess.util.Movement.down;
import static chess.util.Movement.noColumns;
import static chess.util.Movement.oneColumn;
import static chess.util.Movement.oneRow;
import static chess.util.Movement.twoRows;
import static chess.util.Movement.up;
import static javafx.scene.paint.Color.WHITE;

import chess.Game;
import chess.Move;
import javafx.scene.paint.Color;

public class Pawn extends Piece {


	public Pawn(Color color) {
		super(color);
	}


	/*
	 * проверяет законность хода пешки. Пешки могут двигаться тремя 
	 * различными способами.
	 * 1. Взятие фигуры. 
	 * 2. Обычный ход. 
	 * 3. Большой первый ход.
	 */
	public boolean checkMove(Game g, Move m) {
		return super.checkMove(g, m)
				&& (checkForTakingAPiece(g, m)
				    || checkForRegularMove(m)
				    || checkForBigFirstStep(g, m));
	}
	
	
	private boolean directionForColor(Move m) {
		boolean resultCheck = true;
		if (m.getColor() == WHITE) {
			resultCheck = up(m);
		} else {
			resultCheck = down(m);
		}
		return resultCheck;
	}
	/*
	 * возвращает значение true, если пешка делает допустимый ход, при котором противник взят.
	 * Это означает: пешка должна переключить 1 столбец и переместить 1 строку в правильном направлении
	 * (вверх для белых, вниз для черных). Конечное поле должно содержать фигуру (super проверяет, что это противники)
	 */
	private boolean checkForTakingAPiece(Game g, Move m) {
		return oneColumn(m) 
				&& oneRow(m)
				&& directionForColor(m)
				&& m.endPiecePresent();
	}

	/*
	 * returns true if the pawn makes a regular move (moving one row and no columns
	 * to an empty field).
	 */
	private boolean checkForRegularMove(Move m) {
		return oneRow(m) 
				&& noColumns(m)
				&& directionForColor(m) 
				&& !m.endPiecePresent();
	}

	/*
	 * возвращает значение true, если ход является законным "большим шагом" для пешки. Это означает, что
	пешка * перемещается на два ряда в правильном направлении. Столбцов нет. Что это первый ход
	, который делает пешка *. Что нет фигуры, преграждающей путь пешке. И конечное поле должно быть
	 * пустым.
	 */
	private boolean checkForBigFirstStep(Game g, Move m) {
		int moveOneRow = getColor() == WHITE ? 1 : -1;
			return noColumns(m)
				&& twoRows(m)
				&& directionForColor(m)
				&& !hasMoved()
				&& !g.getBoardState()[m.getStartX() + moveOneRow][m.getEndY()].piecePresent()
				&& !m.endPiecePresent();
	}



	@Override
	public Piece makeCopy() {
		return new Pawn(getColor());
	}
}
