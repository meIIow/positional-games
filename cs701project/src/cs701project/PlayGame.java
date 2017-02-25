package cs701project;
import java.util.*;

public class PlayGame {

	public static int start(GameRep game) {
		boolean playAgain = true;
		int winner = -1;
		Scanner scan = new Scanner(System.in);
		while (playAgain) {
			while (!game.won && game.statesLeft > 0) {
				if (game.dimensions.length == 2) {
					game.printBoard2D();
				} else if (game.dimensions.length == 3) {
					game.printBoard3D();
				}
				
				int player = game.turn % 2;
				int move = -1;
				if (game.isHuman[player]) {
					boolean noMove = true;
					while (noMove) {
						System.out.println("Next Move: ");
						if (scan.hasNextInt()) {
							move = scan.nextInt();
							if (game.openSpaces.contains(move)) {
								noMove = false;
							} else {
								System.out.println("invalid move");
							}
						}
					}
				} else {
					MiniMaxTemp miniMax = new MiniMaxTemp(game);
					move = miniMax.run(game);
					
				}
				
				game.nextMove(move);
			}
			
			if (!game.won) {
				winner = -1;
				System.out.println("Cat's Game");
			} else {
				winner = game.winner;
				System.out.println(game.players[winner] + " Wins!");
			}
			
			if (game.dimensions.length == 2) {
				game.printBoard2D();
			} else if (game.dimensions.length == 3) {
				game.printBoard3D();
			}
			
			if (game.isHuman[0] || game.isHuman[1]) {
				System.out.println("play again? Y/N");
				if (!scan.next().equals("Y")) {
					playAgain = false;
				}
			} else {
				playAgain = false;
			}
			
			game.resetBoard();
		}
		
		return winner;
	}
}
