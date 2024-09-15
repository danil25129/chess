package chess;

import static chess.util.ConverterPieceView.modelPieceAtIndexes;

import java.util.Optional;
import chess.pieces.Piece;

public class Board {

	private Field[][] playingField = new Field[8][8];

	public Board () {
		this.set();
	}
	
	/*
	 * создает поля и ставит фигуры.
	 */
	private void set() {
		for(int x=0; x<8; x++){
			for(int y=0; y<8; y++){
				playingField[x][y] = new Field (x, y);
				setPiece(x, y, modelPieceAtIndexes(x, y).orElse(null));
			}
		}
	}

	/*
	 * очищает доску
	 */
	public void clear() {
		for (Field[] fields : playingField) {
			for (Field field : fields) {
			if (field.piecePresent()) {
			field.setPiece(null);
			}
			}
			}
	}

	public Field[][] getBoardState() {
		return playingField;
	}

	public Field getField(int x, int y) {
		return playingField[x][y];
	}

	public Optional<Piece> getPiece(int x, int y) {
		return playingField[x][y].getPiece();
	}

	public void setPiece(int x, int y, Piece piece) {
		playingField[x][y].setPiece(piece);
	}

}
