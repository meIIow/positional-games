package cs701project;

public class MoveInfo {

	int score;
	int[] bestScores;
	int fromDepth;
	boolean won;
	int[] moves;
	
	public MoveInfo(int score, int depth, boolean won, int move) {
		this.score = score;
		this.won = won;
		this.moves = new int[depth+1];
		this.bestScores = new int[depth+1];
		this.fromDepth = depth;
		this.moves[depth] = move;
		this.bestScores[depth] = score;
	}
	
	public void updateMoveInfo(int score, int currentDepth, int move) {
		this.bestScores[currentDepth] = score;
		this.moves[currentDepth] = move;
	}
	
	public boolean isBetter(MoveInfo compMove) {
		
		if (compMove.score == this.score) {
			if (compMove.fromDepth != this.fromDepth) {
				return (compMove.fromDepth > this.fromDepth);
			} else {
				boolean stillTied = true;
				int index = 0;
				boolean better = true;
				while (stillTied && index < this.fromDepth) {
					if (compMove.bestScores[index] < this.bestScores[index]) {
						stillTied = false;
					} else if (compMove.bestScores[index] > this.bestScores[index]) {
						stillTied = false;
						better = false;
					}
					index ++;
				}
				return better;
			}
		} else {
			return (compMove.score < this.score);
		}
	}
}
