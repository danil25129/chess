package chess.pieces;

import static chess.util.Movement.straight;

import chess.Game;
import chess.Move;
import javafx.scene.paint.Color;

public class Rook extends Piece {

	public Rook(Color color) {
		super(color);
	}

	public boolean checkMove(Game g, Move m) {
		return super.checkMove(g, m) && straight(m); 
	}

	@Override
	public Piece makeCopy() {
		return new Rook(getColor());

	}
}