package boardGame;

public abstract class Piece {

	protected Position position;
	private Board board;
	
	public Piece( Board board) {
		super();
		this.board = board;
	}

	protected Board getBoard() {
		return board;
	}

	public boolean isThereAnyPossibleMoves() {
		boolean [][] mat = possibleMoves();
		for(int i = 0; i < mat.length; i++) {
			for(int j = 0; j < mat.length; j++) {
				if (mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
	
	public abstract boolean[][] possibleMoves();
	
	//Hook method
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];
	}
	
	
}
