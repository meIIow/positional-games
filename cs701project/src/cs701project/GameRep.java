package cs701project;
import java.io.Serializable;
import java.util.*;

public class GameRep implements Comparable<GameRep>, Serializable{

	int x;
	int[] dimensions;
	
	int[][] winSets; // list of win sets
	int[][] winMaps; // list of win set indices for each position
	int[][] winPosMaps; // list of index within win set for each position
	
	int[] winStates;
	int statesLeft;
	
	int[] board;
	int[] scoreState;
	int turn = 0;
	int score;
	ArrayList<Integer> openSpaces;
	
	int lastMove;
	boolean won;
	int winner;
	
	
	boolean[] isHuman = new boolean[2];
	String[] players = new String[2];
	
	int[][] scoringMatrix; // 2 by scoring matrix, first and last scratch, middle open
	int[] depth = new int[2]; // 2 values
	int[] fuzziness = new int[2]; // 2 values
	
	public GameRep(int[][] winSetsIn, int[][]winMapsIn, int[][] winPosMapsIn, int[] dimensionsIn, int xIn) {
		winSets = winSetsIn;
		winMaps = winMapsIn;
		winPosMaps = winPosMapsIn;
		
		dimensions = dimensionsIn;
		x = xIn;
		
		board = new int[winMaps.length];
		
		this.winStates = new int[winSets.length];
		this.statesLeft = winSets.length;

		scoreState = new int[(2 * x) + 1];
		scoreState[x] = winSets.length;
		openSpaces = new ArrayList<Integer>();
		for (int space = 0; space < board.length; space ++) {
			openSpaces.add(space);
		}
		
		this.lastMove = -1;
		this.won = false;
		this.winner = -1;
	}
	
	@Override
	public int compareTo(GameRep compGame) {
		int compScore = compGame.getScore();
		return (this.score - compScore);
	}
	
	public void addMiniMaxInfo(int[] MiniMaxInfo) {
		depth[0] = MiniMaxInfo[0];
		depth[1] = MiniMaxInfo[1];
		fuzziness[0] = MiniMaxInfo[2];
		fuzziness[1] = MiniMaxInfo[3];
		scoringMatrix = new int[2][scoreState.length];
		for (int weight = 0; weight < scoreState.length; weight ++) {
			scoringMatrix[0][weight] = MiniMaxInfo[4 + (weight * 2)];
			scoringMatrix[1][weight] = MiniMaxInfo[5 + (weight * 2)];
		}
	}
	
	// {3,3,0,0,-100000, , 100000, -1000, 1000, -10, 10, 1, 1, 10,-10 1000, -1000, 100000, -100000}
	
	public void addPlayers(boolean p0, boolean p1, String name0, String name1) {
		isHuman[0] = p0;
		isHuman[1] = p1;
		players[0] = name0;
		players[1] = name1;
	}
	
	public int getScore() {
		return this.score;
	}
	
	
	public int CalculateScore(int move) {
		return 0;
	}
	
	public int nextMove(int move) {
		//System.out.println("turn is " + turn);
		int player = this.turn % 2;
		//System.out.println("player is " + player);
		int playerMult = playerToMult(player);
		//System.out.println(playerMult + " is player mult");
		board[move] = playerMult;
		int turnWon = updateGame(move, player, playerMult);
		this.lastMove = move;
		this.openSpaces.remove(openSpaces.indexOf(move));
		
		//System.out.println("TUrn won = " + turnWon);
		
		if (turnWon < 0) {
			this.turn ++;
		} else {
			this.won = true;
			this.winner = player;
		}
		return turnWon;
	}
	
	private int playerToMult(int player) {
		if (player == 0) {
			return 1;
		} else {
			return -1;
		}
	}
	
	private int updateGame(int move, int player, int mult) {
		int updates = winMaps[move][0];
		int[] sets = winMaps[move];
		int[] setPos = winPosMaps[move];
		int turnWins = -1;
		for (int update = 1; update <= updates; update ++) {
			int state = winStates[sets[update]];
			if (state > this.x || state < -this.x) {
				// do nothing
			}  else if (state * mult < 0) {
				statesLeft --;
				winStates[sets[update]] = this.x + 1;
				scoreState[state + this.x] --;
			} else {
				scoreState[state + this.x] --;
				scoreState[state + this.x + mult] ++;
				winStates[sets[update]] += mult;
				if (winStates[sets[update]] * mult == x) {
					turnWins = sets[update];
				}
			}
		}
		
		return turnWins;
	}
	
	public void resetBoard() {
		this.turn = 0;
		this.board = new int[this.winMaps.length];
		this.scoreState = new int[(2 * this.x) + 1];
		this.scoreState[this.x] = this.winSets.length;
		this.openSpaces = new ArrayList<Integer>();
		for (int space = 0; space < this.board.length; space ++) {
			this.openSpaces.add(space);
		}
		
		this.lastMove = -1;
		this.won = false;
		this.winner = -1;
		this.winStates = new int[winSets.length];
		this.statesLeft = winSets.length;
		
	}
	
	public int getDepth(int player) {
		return depth[player];
	}
	
	public int[] getScoringList(int player) {
		return scoringMatrix[player];
	}
	
	public  int[] getMoves()
	{
	    int[] possMoves = new int[openSpaces.size()];
	    for (int i=0; i < possMoves.length; i++)
	    {
	        possMoves[i] = openSpaces.get(i).intValue();
	    }
	    return possMoves;
	}
	
	public int[] getScoreState() {
		return scoreState;
	}
	
	public int getTurn() {
		return turn;
	}
	
	private char getChar(int space) {
		if (space < 0) {
			return 'X';
		} else if (space > 0) {
			return 'O';
		} else {
			return '*';
		}
	}
	
	public void printBoard3D() {
		int index = 0;
		for (int i = 0; i < dimensions[0]; i ++) {
			for (int j = 0; j < dimensions[1]; j ++) {
				for (int k = 0; k < dimensions[2]; k ++) {
					char now = getChar(board[index]);
					System.out.print(now + "  ");
					index ++;
				}
				
				System.out.print("       ");
				for (int k = 0; k < dimensions[2]; k ++) {
					int now = index - dimensions[2] + k;
					System.out.print(now + "  ");
				}
				
				System.out.println("");
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	public void printBoard2D() {
		int index = 0;
		for (int j = 0; j < dimensions[0]; j ++) {
			for (int k = 0; k < dimensions[1]; k ++) {
				char now = getChar(board[index]);
				System.out.print(now + "  ");
				index ++;
			}
			
			System.out.print("       ");
			for (int k = 0; k < dimensions[1]; k ++) {
				int now = index - dimensions[1] + k;
				System.out.print(now + "  ");
			}
			
			System.out.println("");
		}
		System.out.println("");
	}
	
}
