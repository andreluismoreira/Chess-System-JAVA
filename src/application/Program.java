package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessExeption;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
	
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> captured = new ArrayList();
		
		while (true) {
			try {
				Ui.clearScreen();
				Ui.printMatch(chessMatch, captured);
				System.out.println();
				System.out.print("source: ");
				ChessPosition source = Ui.readChessPosition(sc);
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				Ui.clearScreen();
				Ui.printBoard(chessMatch.getPieces(), possibleMoves);
				System.out.println();
				System.out.print("target: ");
				ChessPosition target = Ui.readChessPosition(sc);
				System.out.println();

				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				
				if(capturedPiece != null) {
					captured.add(capturedPiece);
				}
				
			} catch (ChessExeption e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		 
	}
}
