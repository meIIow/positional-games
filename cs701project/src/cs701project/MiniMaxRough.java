package cs701project;

public class MiniMaxRough {

	int[] scoringMatrix;
	int[][] winSets;
	int[][] winMaps;
	int[][] winPosMaps;
	int maxRecs;
	int branchFactor;
	int maxBranch;
	int x;
	int test;
	
	public MiniMaxRough(int[] weights, int[][] winSetsIn, int[][]winMapsIn, int[][] winPosMapsIn) {
		winSets = winSetsIn;
		winMaps = winMapsIn;
		winPosMaps = winPosMapsIn;
		maxRecs = weights[0];
		branchFactor = weights[1];
		maxBranch = weights[2];
		x = weights.length - 3;
		
		for (int weight = 0; weight < x; weight ++) {
			scoringMatrix[weight] = weights[weight + 3];
		}
		
	}
	
	public int findMove(int[][] board, int[][] mySets, int[][] opSets, int[][] sets, int movesLeft) {
		
		
		
		
		return 1;
	}
	
	
}
