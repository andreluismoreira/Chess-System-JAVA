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

//se por coluna dentro do tabuleiro e numero fora da erro(o contrario tmb) resolver	metodo chessposition
	
	public static void main(String[] args) {
	
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> captured = new ArrayList();
		
		while (!chessMatch.getCheckMate()) {
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
				
				if (chessMatch.getPromoted() != null) {
					System.out.println("Enter Piece for promotion [Q/N/R/B]: ");
					String type = sc.nextLine().toUpperCase();
					while(!type.equals("B") && !type.equals("N") && !type.equals("Q") && !type.equals("R")){
						System.out.println("Invalid Value! Enter Piece for promotion [Q/N/R/B]: ");
						type = sc.nextLine().toUpperCase();
					}
					chessMatch.replacePromotedPiece(type);
				}
				
			} catch (ChessExeption e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		Ui.clearScreen();
		Ui.printMatch(chessMatch, captured);
		 
	}
}
