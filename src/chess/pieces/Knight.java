package chess.pieces;

import static chess.util.Movement.jump;

import chess.Game;
import chess.Move;
import javafx.scene.paint.Color;

public class Knight extends Piece {

	
	public Knight(Color color) {
		super(color);
	}


	//конь может перепрыгнуть через одну строку и два столбца или наоборот.
	public boolean checkMove(Game g, Move m) {
		return super.checkMove(g, m) && jump(m);
	}

	@Override
	public Piece makeCopy() {
		return new Knight(getColor());
	}


}
