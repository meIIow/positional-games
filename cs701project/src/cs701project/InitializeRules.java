package cs701project;
import java.math.*;
import java.util.*;


public class InitializeRules {

	public static GameRep go(int[] boardInfo) {
		
		GameRep game = controller(boardInfo);
		return game;
	}
	
	public static GameRep controller(int[] boardInfo) {
		
		int x = (boardInfo[0]);
		int[] dimensions = new int[boardInfo.length - 1];
		for (int dimension = 1; dimension < boardInfo.length; dimension ++) {
			dimensions[dimension - 1] = (boardInfo[dimension]);
		}
		
		int choices = 3;
		int numDimensions = dimensions.length;
		int[][] combos = getCombos(choices, numDimensions-1);
		
		printMatrix(combos);
		printArray(dimensions);
		System.out.println(x);
		//printMatrix(trimCombos(choices, combos));
		//printMatrix(trimCombos(choices, trimCombos(choices, combos)));
		//printMatrix(trimCombos(3, trimCombos(choices, trimCombos(choices, combos))));
		
		int[][][] originalWinSets = getWinSets(dimensions, x, combos, choices);
		int[][] winSets = convertWinSets(originalWinSets, dimensions);
		
		int[][] doubleWinMaps = getWinMaps(winSets, dimensions, numDimensions);
		int[][] winMaps = new int[doubleWinMaps.length / 2][doubleWinMaps[0].length];
		int[][] winPosMaps = new int[doubleWinMaps.length / 2][doubleWinMaps[0].length];
		
		for (int position = 0; position < winMaps.length; position ++) {
			winMaps[position] = copyArray(doubleWinMaps[position]);
			winPosMaps[position] = copyArray(doubleWinMaps[position + winMaps.length]);
		}
		
		/*
		 * winSets = list of all sets for which getting all values = win
		 * winMaps = list of all the winSets that a given board position is a part of
		 * winPosMaps = list of positions within each win set that a given position is part of
		 */
		
		GameRep game = new GameRep(winSets, winMaps, winPosMaps, dimensions, x);
		return game;
		
	}
	
	public static int[] getBoardInfo() {
		
		Scanner scan = new Scanner(System.in);
		
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
		
		scan.close();
		
		return boardInfo;
	}
	
	public static int[][] getCombos(int choices, int num) {
		
		int comboMax = (int)Math.pow(choices,num);
		int[][] comboSet = new int[comboMax][num + 1];
		
		for (int baseX = 0; baseX < comboMax; baseX ++) {
			int temp = baseX;
			for (int power = 0; power < num; power ++) {
				comboSet[baseX][power] = (temp % choices) - 1;
				temp = temp / choices;
			}
			comboSet[baseX][num] = 1;
		}
		
		return comboSet;
	}
	
	public static int[][][] getWinSets(int[] dimensions, int x, int[][] combos, int choices) {
		int numWinSets = 0;
		int winSetIndex = 0;
		int[][][] winSets = new int[1][1][1];
		
		int numDimensions = dimensions.length;
		//int numCombos = combos.length;
		
		int[] numFit = new int[numDimensions];
		for (int dimension = 0; dimension < numDimensions; dimension ++) {
			numFit[dimension] = (dimensions[dimension] + 1 - x);
		}
		
		int[] multMatrix;
		int[][] blankWinSet = new int[x][dimensions.length];
		
		boolean ready = false;
		
		for (int loop = 0; loop < 2; loop ++) {
			
			int[][] comboCopy = copyMatrix(combos);
			
			if (ready) {
				winSets = new int[numWinSets][x][numDimensions];
			}	
		
			for (int dimension = 0; dimension < numDimensions; dimension ++) {
				int numCombos = comboCopy.length;
				for (int combo = 0; combo < numCombos; combo ++) {
					int multiplier = 1;
					for (int comboDimension = 0; comboDimension < numDimensions; comboDimension ++) {
						if (comboCopy[combo][comboDimension] == 0) {
							multMatrix = dimensions;
						} else {
							multMatrix = numFit;
						}
						multiplier = multiplier * multMatrix[comboDimension];
					}
					if (!ready) {
						numWinSets += multiplier;
					} else {
						System.out.println(winSetIndex);
						winSetIndex = recursiveCount(dimensions, numFit, winSetIndex, winSets, comboCopy[combo], 0, blankWinSet, x);
					}
				}
				
				/*
				int dimensionsLeft = numDimensions - dimension - 1;
				int power = (int)Math.pow(choices, dimensionsLeft);
				System.out.println("power is " + power);
				int multiplier = 1;
				for (int next = dimension + 1; next < numDimensions; next ++) {
					multiplier = multiplier * (dimensions[next] - x + 1);
				}
				System.out.println("multiplier is " + multiplier);
				numWinSets += (power * multiplier);
				*/
				
				comboCopy = trimCombos(choices, comboCopy);
			}
		
		System.out.println(numWinSets);
		ready = true;
		
		}
		for (int u = 0; u < winSets.length; u ++) {
			printMatrix(winSets[u]);
			System.out.println("");
		}
		
		
		
		return winSets;
	}
	
	public static int recursiveCount(int[] dimensions, int[] numFit, int foundWinSets, int[][][] winSets, int[] combo, int startDimension, int[][] currentWinSet, int x) {
		System.out.println("dimensions is: " + startDimension);
		System.out.println("numFound is: " + foundWinSets);
		int direction = combo[startDimension];
		int itNumber = 0;
		boolean done = (startDimension == dimensions.length - 1);
		int start = 0;
		if (direction == 0) {
			itNumber = dimensions[startDimension];
		} else {
			itNumber = numFit[startDimension];
			if (direction == -1) {start = (dimensions[startDimension] - numFit[startDimension]);}
		}
		
		for (int i = 0; i < itNumber; i ++) {
			System.out.println("COUNT");
			int k = i + start;
			int[][] tempWinSet = copyMatrix(currentWinSet);
			for (int j = 0; j < x; j ++) {
				tempWinSet[j][startDimension] = k;
				k += direction;
			}
			if (done) {
				System.out.println("done");
				winSets[foundWinSets] = tempWinSet;
				foundWinSets ++;
			} else {
				System.out.println("continue");
				foundWinSets = recursiveCount(dimensions, numFit, foundWinSets, winSets, combo, (startDimension+1), tempWinSet, x);
			}	
		}
		return foundWinSets;
	}
	
	public static int[][] getWinMaps(int[][] winSet, int[] dimensions, int numDimensions) {
		System.out.println("getting win maps");
		
		int totalPositions = 1;
		for (int dimension = 0; dimension < numDimensions; dimension ++) {
			totalPositions = totalPositions * dimensions[dimension];
		}
		int[] winsPerPosition = new int[totalPositions];
		
		for (int set = 0; set < winSet.length; set ++) {
			for (int coordinate = 0; coordinate < winSet[set].length; coordinate ++) {
				winsPerPosition[winSet[set][coordinate]] ++;
			}
		}
		
		int maxSets = -1;
		for (int position = 0; position < totalPositions; position ++) {
			if (winsPerPosition[position] > maxSets) {
				maxSets = winsPerPosition[position];
			}
		}
		
		System.out.println("max is " + maxSets);
		
		int[][] winMaps = new int[totalPositions * 2][maxSets + 1];
		for (int position = 0; position < totalPositions; position ++) {
			for (int set = 1; set < maxSets; set ++) {
				winMaps[position][set] = -1;
				winMaps[position + totalPositions][set] = -1;
			}
		}
		
		for (int set = 0; set < winSet.length; set ++) {
			for (int coordinate = 0; coordinate < winSet[set].length; coordinate ++) {
				int position = winSet[set][coordinate];
				winMaps[position][0] ++;
				winMaps[position][winMaps[position][0]] = set;
				winMaps[position + totalPositions][winMaps[position][0]] = coordinate;
			}
		}
		
		return winMaps;
	}
	
	public static int[][] trimCombos(int choices, int[][] combos) {
		int numCombos = combos.length;
		int dimensions = combos[0].length;
		int stopHere = dimensions - 1;
		for (int trimMe = (dimensions - 1); trimMe > -1; trimMe --) {
			if (combos[0][trimMe] == 0) {stopHere --;}
		}
		
		int[][] trimmedCombos = new int[numCombos / choices][dimensions];
		for (int comboIndex = 0; comboIndex < (numCombos / choices); comboIndex ++) {
			int fromBack = (numCombos * 2 / choices) + comboIndex;
			for (int dimensionIndex = 0; dimensionIndex < stopHere; dimensionIndex ++) {
				trimmedCombos[comboIndex][dimensionIndex] = combos[fromBack][dimensionIndex];
			}
		}
		
		return trimmedCombos;
	}
	
	public static int[][] convertWinSets(int[][][] winSets, int[] dimensions) {
		int sets = winSets.length;
		int x = winSets[0].length;
		int[][] newWinSets = new int[sets][x];
		for (int set = 0; set < sets; set ++) {
			for (int w = 0; w < x; w ++) {
				newWinSets[set][w] = toSingle(winSets[set][w], dimensions);
			}
		}
		return newWinSets;
	}
	
	public static int[] copyArray(int[] array) {
		int[] copy = new int[array.length];
		for (int arrayIndex = 0; arrayIndex < array.length; arrayIndex ++) {
			copy[arrayIndex] = array[arrayIndex];
		}
		return copy;
	}
	
	public static int[][] copyMatrix(int[][] matrix) {
		int rows = matrix.length;
		int columns = matrix[0].length;
		
		int[][] copy = new int[rows][columns];
		for (int row = 0; row < rows; row ++) {
			for (int column = 0; column < columns; column ++) {
				copy[row][column] = matrix[row][column];
			}
		}
		return copy;
	}
	
	public static void printMatrix(int[][] matrix) {
		int rows = matrix.length;
		int columns = matrix[0].length;
		for (int row = 0; row < rows; row ++) {
			for (int column = 0; column < columns; column ++) {
				System.out.print(matrix[row][column] + " ");
			}
			System.out.println("");
		}
	}
	
	public static void printArray(int[] array) {
		for (int index = 0; index < array.length; index ++) {
			System.out.print(array[index] + " ");
		}
		System.out.println("");
	}
	
	public static int[] toMulti(int single, int[] dimensions) {
		int temp = single;
		int[] multi = new int[dimensions.length];
		for (int dimension = 0; dimension < dimensions.length; dimension ++) {
			multi[dimension] = temp % dimensions[dimension];
			temp = temp / dimensions[dimension];
		}
		return multi;
	}
	
	public static int toSingle(int[] coordinates, int[] dimensions) {
		int single = coordinates[0];
		int multiplier = dimensions[0];
		for (int dimension = 1; dimension < dimensions.length; dimension ++) {
			single += (coordinates[dimension] * multiplier);
			multiplier = multiplier * dimensions[dimension];
		}
		return single;
	}
	
}
