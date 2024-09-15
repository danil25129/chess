package chess.pieces;


import static chess.Game.attackingMoves;
import static chess.Game.searchForKings;
import static chess.util.Movement.oneRowOrOneColumnOrBoth;
import chess.Game;
import chess.Move;
import javafx.scene.paint.Color;

public class King extends Piece {

	public King(Color color) {
		super(color);
	}

	//если ход допустимый
	public boolean checkMove(Game g, Move m) {
		return super.checkMove(g, m) 
				&& oneRowOrOneColumnOrBoth(m);
	}


	//возвращает true если король не под шахом
	public boolean notInCheck(Game g) {
		return attackingMoves(g, searchForKings(g).get(getColor()))
				.isEmpty();
	};
	

	@Override
	public Piece makeCopy() {
		return new King(getColor());
	}
}
