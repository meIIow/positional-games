package cs701project;

import java.util.Scanner;

public class Setup {

	public static void main(String[] args) {
		
		int[] boardInfo = getBoardInfo();
		GameRep game = InitializeRules.go(boardInfo);
		
		if (game.dimensions.length == 2) {
			game.printBoard2D();
		} else if (game.dimensions.length == 3) {
			game.printBoard3D();
		}
		
		Scanner scan = new Scanner(System.in);
		boolean done = false;
		
		game.addPlayers(false, false, "Good", "Bad");
		
		int[] test1 = {6,6,0,0,-100000, 100000, -1000, 1000, -10, 10, 0, 0, 10,-10, 1000, -1000, 100000, -100000};
		int[] test2 = {6,6,0,0,-100000, 100000, -1, 1, -1, 1, 1, 1, 1, -1, 1, -1, 100000, -100000};
		int[] test3 = {2,2,0,0,-10000000, 10000000, -1, 1, -1, 1, -1, 1, 1, 1, 1, -1, 1, -1, 1, -1, 10000000, -10000000};
		int[] test4 = {1,1,0,0,-10000, 10000, -1000, 1000, -100, 100, -10, 10, 1, 1, 10, -10, 100, -100, 1000, -1000, 10000, -10000};
		

		//game.addMiniMaxInfo(test4);
		
		//InitializeRules.printArray(game.scoringMatrix[0]);
		//InitializeRules.printArray(game.scoringMatrix[1]);
		
		while (!done) {
			System.out.println("Play (P), Create (C), or Exit (E)");
			String myInput = scan.next();
			if (myInput.toUpperCase().equals("P")) {
				PlayGame.start(game);
			} else if (myInput.toUpperCase().equals("C")) {
				OptimizeWeights.go(game);
			} else if (myInput.toUpperCase().equals("E")) {
				done = true;
			} else {
				System.out.println("Invalid input");
			}
		}
		
		scan.close();
		// pvp, vs computer or cvc
		// if vs cmp, who first
		// if cvc, which AIs play?
	}
	
	public static int[] getBoardInfo() {
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Describe Game Rules");
		System.out.println("How many in a row to win?");
		int x = scan.nextInt();
		
		System.out.println("Enter Number of Board Dimensions");
		int numDimensions = scan.nextInt();
		int[] boardInfo = new int[numDimensions + 1];
		boardInfo[0] = x;
		
		for (int dimension = 1; dimension <= numDimensions; dimension ++) {
			System.out.println("Enter Board Dimension " + dimension);
			boardInfo[dimension] = scan.nextInt();
		}
		
		//scan.close();
		
		return boardInfo;
	}
}
