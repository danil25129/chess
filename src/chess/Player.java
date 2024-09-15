package chess;
import javafx.scene.paint.Color;

public class Player {
	private String name;
	private Color  color;


	public Player(String name, Color color) {
		this.name  = name;
		this.color = color;
	}

	//возвращает true если игрок играет его собственным цветом
	public Boolean checkMove(Move m) {
		return m.getPiece().getColor() == color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
