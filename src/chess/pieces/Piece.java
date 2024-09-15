package chess.pieces;
import chess.Game;
import chess.Move;
import javafx.scene.paint.Color;

public abstract class Piece {
	private final Color color;
	private boolean hasMoved = false;

	
	protected Piece (Color color) {
		this.color = color;
	}

	//Если клетка пуста или фигура другого цвета
	public boolean checkMove(Game g, Move m) {
		return  !m.endPiecePresent() || m.getEnd().getPiece().get().getColor() != getColor();
	}	
	
	public abstract Piece makeCopy();
	
	public Color getColor() {
		return color;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}


	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
}
