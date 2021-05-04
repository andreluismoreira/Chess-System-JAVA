package chess;

import boardGame.Position;

public class ChessPosition {

	private char column;
	private int row;
	
	public ChessPosition(char column, int row) {
		if (column < 'a' || column > 'h' && row < 1 || row > 8) {
			throw new ChessExeption("Error instatiating ChessPosition. valid values are from a1 to h8");
		}
		this.row = row;
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}

	public char getColumn() {
		return column;
	}

	protected Position toPosistion() {
		return new Position( 8 - row, column - 'a');	
	}
	
	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char)('a' + position.getColumn()),8 - position.getRow());
	}
	
	@Override
	public String toString() {
		return "" + column + row;
	}
}
