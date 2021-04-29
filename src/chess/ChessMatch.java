package chess;

import boardGame.Board;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	
	public ChessMatch() {
	board = new Board(8,8);
	initialSetup();
	}
	
	public ChessPiece[][] getPieces() {
		ChessPiece [][] mat = new ChessPiece [board.getRows()][board.getColumns()];
		for(int i = 0; i<board.getRows(); i ++) {
			for(int j = 0; j< board.getColumns(); j ++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
				}
			}
			return mat;
		}

	private void placeNewpiece (char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column,row).toPosistion());
	}
	
	private void initialSetup() {
		placeNewpiece('c', 1, new Rook(board, Color.White));
        placeNewpiece('c', 2, new Rook(board, Color.White));
        placeNewpiece('d', 2, new Rook(board, Color.White));
        placeNewpiece('e', 2, new Rook(board, Color.White));
        placeNewpiece('e', 1, new Rook(board, Color.White));
        placeNewpiece('d', 1, new King(board, Color.White));

        placeNewpiece('c', 7, new Rook(board, Color.Black));
        placeNewpiece('c', 8, new Rook(board, Color.Black));
        placeNewpiece('d', 7, new Rook(board, Color.Black));
        placeNewpiece('e', 7, new Rook(board, Color.Black));
        placeNewpiece('e', 8, new Rook(board, Color.Black));
        placeNewpiece('d', 8, new King(board, Color.Black));
	}
}
