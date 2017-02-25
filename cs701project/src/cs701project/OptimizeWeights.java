package cs701project;

public class OptimizeWeights {

	public static void go(GameRep game) {
		int INF = 1000000;
		int[] extend = new int[game.x+1];
		int[] random = new int[game.x+1];
		int[] branch = new int[game.x+1];
		//int[] balanced = new int[game.x+1];
		int[] linear = new int[game.x+1];
		int[] exponential = new int[game.x+1];
		
		extend[game.x] = INF;
		random[game.x] = INF;
		branch[game.x] = INF;
		linear[game.x] = INF;
		exponential[game.x] = INF;
		
		
		int initialExtend = 1;
		int initialBranch = 1;
		int initialLinear = 1;
		int initialExponential = 1;
		for (int i = 0; i < game.x; i ++) {
			extend[i] = initialExtend;
			branch[i] = initialBranch;
			linear[i] = initialLinear;
			exponential[i] = initialExponential;
			
			initialExtend *= 10;
			initialLinear += 1;
			initialExponential *= 2;
		}
		
		int[][] originals = new int[5][game.x+1];
		originals[0] = InitializeRules.copyArray(random);
		originals[1] = InitializeRules.copyArray(branch);
		originals[2] = InitializeRules.copyArray(linear);
		originals[3] = InitializeRules.copyArray(exponential);
		originals[4] = InitializeRules.copyArray(extend);
		
		InitializeRules.printMatrix(originals);
		
		game.addPlayers(false, false, "First", "Second");
		
		
		int[][] record = new int[5][5];
		
		for (int i = 0; i < 5; i ++) {
			for (int j = 0; j < 5; j ++) {
				int size = game.x * 2 + 1;
				int[] weights1 = new int[size];
				int[] weights2 = new int[size];
				int[] weights3 = new int[size * 2 + 4];
				for (int k = 0; k < game.x+1; k++) {
					weights1[(size/2) - k] = originals[i][k] * -1;
					weights1[(size/2) + k] = originals[i][k];
					weights2[(size/2) - k] = originals[j][k];
					weights2[(size/2) + k] = originals[j][k] * -1;
				}
				
				weights3[0] = 4;
				weights3[1] = 4;
				for (int k = 0; k < size; k ++) {
					weights3[2 * k + 4] = weights1[k];
					weights3[2 * k + 5] = weights2[k];
				}
				
				//InitializeRules.printArray(weights3);
				
				game.addMiniMaxInfo(weights3);
				//InitializeRules.printMatrix(game.scoringMatrix);
				
				int winner = PlayGame.start(game);
				if (winner == -1) {
					record[i][2] ++;
					record[j][2] ++;
				} else if (winner == 0) {
					record[i][0] ++;
					record[j][4] ++;
				} else {
					record[j][1] ++;
					record[i][3] ++;
				}
				
				game.resetBoard();
			}
		}
		
		InitializeRules.printMatrix(record);
		
	}
	
	public static void store() {
		
	}
}
