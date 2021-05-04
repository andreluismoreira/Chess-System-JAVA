package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;

	private List<Piece> piecesOnTheBoard = new ArrayList();
	private List<Piece> capturedPieces = new ArrayList();

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.Red;
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosistion();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosistion();
		Position target = targetPosition.toPosistion();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);

		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessExeption("You can't put yourself in check !");
		}

		check = (testCheck(opponent(currentPlayer))) ? true : false;

		if (testeCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		} else {
			nextTurn();
		}

		return (ChessPiece) capturedPiece;
	}

	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece) board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);

		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		// roque lado do rei
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		// roque lado da rainha
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		return capturedPiece;
	}

	public void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		// roque lado do rei
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		// roque lado da rainha
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
	}

	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessExeption("There is no piece on source position !");
		}
		if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
			throw new ChessExeption("The chosen piece is not yours");
		}
		if (!board.piece(position).isThereAnyPossibleMoves()) {
			throw new ChessExeption("There is no possible moves for the chosen piece ");
		}
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessExeption("the chosen piece can't move to target position");
		}
	}

	private Color opponent(Color color) {
		return (color == Color.Red) ? Color.Blue : Color.Red;
	}

	private ChessPiece King(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board !");
	}

	private boolean testCheck(Color color) {
		Position kingPosition = King(color).getChessPosition().toPosistion();
		List<Piece> opponentPieces = piecesOnTheBoard.stream()
				.filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testeCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece) p).getChessPosition().toPosistion();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void placeNewpiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosistion());
		piecesOnTheBoard.add(piece);
	}

	private void initialSetup() {

		placeNewpiece('h', 1, new Rook(board, Color.Red));
		placeNewpiece('a', 1, new Rook(board, Color.Red));
		placeNewpiece('c', 1, new Bishop(board, Color.Red));
		placeNewpiece('f', 1, new Bishop(board, Color.Red));
		placeNewpiece('b', 1, new Knight(board, Color.Red));
		placeNewpiece('g', 1, new Knight(board, Color.Red));
		placeNewpiece('d', 1, new Queen(board, Color.Red));
		placeNewpiece('e', 1, new King(board, Color.Red, this));
		placeNewpiece('a', 2, new Pawn(board, Color.Red));
		placeNewpiece('b', 2, new Pawn(board, Color.Red));
		placeNewpiece('c', 2, new Pawn(board, Color.Red));
		placeNewpiece('d', 2, new Pawn(board, Color.Red));
		placeNewpiece('e', 2, new Pawn(board, Color.Red));
		placeNewpiece('f', 2, new Pawn(board, Color.Red));
		placeNewpiece('g', 2, new Pawn(board, Color.Red));
		placeNewpiece('h', 2, new Pawn(board, Color.Red));

		placeNewpiece('h', 8, new Rook(board, Color.Blue));
		placeNewpiece('a', 8, new Rook(board, Color.Blue));
		placeNewpiece('c', 8, new Bishop(board, Color.Blue));
		placeNewpiece('f', 8, new Bishop(board, Color.Blue));
		placeNewpiece('b', 8, new Knight(board, Color.Blue));
		placeNewpiece('g', 8, new Knight(board, Color.Blue));
		placeNewpiece('d', 8, new Queen(board, Color.Blue));
		placeNewpiece('e', 8, new King(board, Color.Blue, this));
		placeNewpiece('a', 7, new Pawn(board, Color.Blue));
		placeNewpiece('b', 7, new Pawn(board, Color.Blue));
		placeNewpiece('c', 7, new Pawn(board, Color.Blue));
		placeNewpiece('d', 7, new Pawn(board, Color.Blue));
		placeNewpiece('e', 7, new Pawn(board, Color.Blue));
		placeNewpiece('f', 7, new Pawn(board, Color.Blue));
		placeNewpiece('g', 7, new Pawn(board, Color.Blue));
		placeNewpiece('h', 7, new Pawn(board, Color.Blue));
	}

	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.Red) ? Color.Blue : Color.Red;
	}
}
