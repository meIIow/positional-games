package cs701project;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class MiniMaxTemp {
	
	int maxDepth;
	int TRUE_INF = Integer.MAX_VALUE;
	int INF = 10000000;
	//int[] playerCycle;
	//int[] multiplierCycle;
	boolean[] maxCycle;
	int startTurn;
	int[] multiplier;
	
	
	public MiniMaxTemp(GameRep game) {
		startTurn = game.getTurn();
		int currentPlayer = startTurn % 2;
		maxDepth = game.getDepth(currentPlayer);
		//playerCycle = new int[maxDepth];
		//multiplierCycle = new int[maxDepth];
		maxCycle = new boolean[maxDepth+1];
		
		boolean isMax = true;
		for (int depth = 0; depth < maxDepth+1; depth ++) {
			//playerCycle[depth] = currentPlayer;
			//multiplierCycle[depth] = 
			maxCycle[depth] = isMax;
			isMax = !isMax;
		}
		
		multiplier = game.getScoringList(currentPlayer);
		
	}
	
	public  int run(GameRep game) {
		MoveInfo bestMove = MiniMaxRec(game, 0);
		return bestMove.moves[1];
	}
	
	private MoveInfo MiniMaxRec(GameRep currentGame, int depth) {
		
		if (depth >= maxDepth || currentGame.won == true || currentGame.openSpaces.isEmpty()) {
			int score = currentGame.score;
			int move = currentGame.lastMove;
			boolean won = currentGame.won;
			MoveInfo onlyMove = new MoveInfo(score, depth, won, move);
			return onlyMove;
		} else {
			boolean nowMax = maxCycle[depth];
			GameRep[] possibilities = new GameRep[currentGame.openSpaces.size()];
			int[] openMoves = currentGame.getMoves();
			for (int openMove = 0; openMove < openMoves.length; openMove ++) {
				GameRep temp = (GameRep) deepClone(currentGame);
				temp.nextMove(openMoves[openMove]);
				calculateScore(temp);
				possibilities[openMove] = temp;
			}
			Arrays.sort(possibilities);
			if (nowMax) {
				Collections.reverse(Arrays.asList(possibilities));
			}
			
			MoveInfo best = MiniMaxRec(possibilities[0], depth + 1);
			
			for (int possibility = 1; possibility < possibilities.length; possibility ++) {
				MoveInfo next = MiniMaxRec(possibilities[possibility], depth + 1);
				if (next.isBetter(best) && nowMax) {
					best = next;
				} else if (!next.isBetter(best) && !nowMax) {
					best = next;
				}
			}
			
			best.updateMoveInfo(currentGame.score, depth, currentGame.lastMove);
			return best;
		}
	}
	
	
	int calculateScore(GameRep currentGame) {
		int score = 0;
		int[] scoreState = currentGame.getScoreState();
		for (int setType = 0; setType < multiplier.length; setType ++) {
			int addMe = multiplier[setType] * scoreState[setType];
			score += addMe;
		}
		
		if (score >= INF / 2) {
			score = TRUE_INF;
		} else if (score <= - INF / 2) {
			score = - TRUE_INF;
		}
		
		currentGame.score = score;
		return score;
	}
	
	Object deepClone(Object object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ObjectOutputStream oos = new ObjectOutputStream(baos);
		    oos.writeObject(object);
		    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		    ObjectInputStream ois = new ObjectInputStream(bais);
		    return ois.readObject();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
}
