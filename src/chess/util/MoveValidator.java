package chess.util;

import static chess.util.MoveValidator.ValidationResult.ILLEGAL_MOVE_FOR_PIECE;
import static chess.util.MoveValidator.ValidationResult.CHECK_ON_KING;
import static chess.util.MoveValidator.ValidationResult.LEGAL_MOVE;
import static chess.util.MoveValidator.ValidationResult.MOVE_ILLEGAL_MOVE;
import static chess.util.MoveValidator.ValidationResult.PATH_IS_OBSTRUCTED;
import static chess.util.MoveValidator.ValidationResult.PLAYING_THE_WRONG_COLOR;

import chess.Game;
import chess.Move;


public class MoveValidator {

	public enum ValidationResult {
		LEGAL_MOVE,
		PATH_IS_OBSTRUCTED,
		CHECK_ON_KING,
		ILLEGAL_MOVE_FOR_PIECE,
		MOVE_ILLEGAL_MOVE,
		PLAYING_THE_WRONG_COLOR,
		KING_MOVES_OVER_FIELD_IN_CHECK;

		@Override
		public String toString() {
			return name().substring(0, 1) + name().substring(1).replace('_', ' ').toLowerCase()+".";
		}
	}

	public static ValidationResult legalForPiece(Game g, Move m) {
		return m.getPiece().checkMove(g, m) ?
				LEGAL_MOVE : ILLEGAL_MOVE_FOR_PIECE;
	}

	public static ValidationResult pathUnobstructed(Game g, Move m) {
		return Game.pathUnobstructed(g, m) ?
				LEGAL_MOVE : PATH_IS_OBSTRUCTED;
	}

	public static ValidationResult legalForPlayer(Game g, Move m) {
		return g.getActivePlayer().checkMove(m) ?
				LEGAL_MOVE : PLAYING_THE_WRONG_COLOR;
	}

	public static ValidationResult legalForMove(Game g, Move m) {
		return m.checkMove() ?
				LEGAL_MOVE : MOVE_ILLEGAL_MOVE;
	}

	public static ValidationResult kingSave(Game g, Move m) {
		return Game.kingSave(g, m) ?
				LEGAL_MOVE : CHECK_ON_KING;
	}
	
	
	
	public static ValidationResult finalResult(Game g, Move m) {
			
			ValidationResult result = legalForPiece(g, m);
			
			if (result == LEGAL_MOVE) {
				
				result = pathUnobstructed(g, m);
				if (result == LEGAL_MOVE) {
					
					result = legalForPlayer(g, m);
					if (result == LEGAL_MOVE) {
						result = legalForMove(g, m);
						if (result == LEGAL_MOVE) {
							result = kingSave(g, m);
								if (result == LEGAL_MOVE) {
									return result;
								}
						} else {
							return result;
						}
					} else {
						return result;
					}
					
				} else {
					return result;
				}
				
			} else {
				return result;
			}
		return result;
	}

}
