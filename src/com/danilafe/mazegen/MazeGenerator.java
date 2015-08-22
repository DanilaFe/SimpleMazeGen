package com.danilafe.mazegen;
import java.util.Random;

/**
 * A class to generate mazes!
 * @author danilafe
 * @version 1.0
 */
public class MazeGenerator {

	private static Random random = new Random();
	
	private MazeGenerator(){
		
	}
	
	/**
	 * Generates a new maze using the Recursive Backtracker algorithm. 
	 * @param width the width of the generated maze, in "corridors" - walls don't count as part of the width.
	 * @param height the height of the generated maze, in "corridors" - walls don't count as part of the width.
	 * @return the generated array of bytes. 0 means wall, 1 means unvisited and 2 means empty.
	 */
	public static byte[][] generateRecursiveBacktrackerMaze(int width, int height){
		// Initialize variables
		byte[][] mazeArray = new byte[width * 2 + 1][height * 2 + 1];
		int[] cursorPos = new int[]{
			random.nextInt(width),
			random.nextInt(height)
		};
		
		// Fill Array
		for(int i = 0; i < mazeArray.length; i ++){
			for(int j = 0; j < mazeArray[0].length; j ++){
				mazeArray[i][j] = ((i % 2 == 1) && (j % 2 == 1)) ? (byte) 1 : (byte) 0;
			}
		}
		
		backtrack(mazeArray, cursorPos[0], cursorPos[1], cursorPos[0], cursorPos[1], width, height);
		
		return mazeArray;
	}
	
	/**
	 * Recursive function responsible for generating a maze using the Recursive Backtracker algorithm.
	 * @param array the array being modified recursively. Should be a "walled" array, represented as follows: <br>
	 * <table>
	 * <tr><td># </td><td># </td><td># </td><td># </td><td># </td></tr>
	 * <tr><td># </td><td>  </td><td># </td><td>  </td><td># </td></tr>
	 * <tr><td># </td><td># </td><td># </td><td># </td><td># </td></tr>
	 * <tr><td># </td><td>  </td><td># </td><td>  </td><td># </td></tr>
	 * <tr><td># </td><td># </td><td># </td><td># </td><td># </td></tr>
	 * </table>
	 * @param pos_x the x-coordinate of the cell that's being processed
	 * @param pos_y the y-coordinate of the cell that's being processed
	 * @param pos_prevx the x-coordinate of the cell that moved to this one
	 * @param pos_prevy the y-coordinate of the cell that moved to this one
	 * @param mazeWidth the width of the maze being generated.
	 * @param mazeHeight the height of the maze being generated.
	 */
	private static void backtrack(byte[][] array, int pos_x, int pos_y, int pos_prevx, int pos_prevy, int mazeWidth, int mazeHeight){
		writeBetween(array, pos_x, pos_y, pos_prevx, pos_prevy, (byte) 2); 
		writeToMazeRarray(array, pos_x, pos_y, (byte) 2);
		while(!isSurrounded(array, pos_x, pos_y)){
			int direction = random.nextInt(4);
			int[] newCheckPos = new int[2];
			switch(direction){
			case 0:
				newCheckPos[0] = pos_x - 1;
				newCheckPos[1] = pos_y;
				break;
			case 1: 
				newCheckPos[0] = pos_x + 1;
				newCheckPos[1] = pos_y;
				break;
			case 2:
				newCheckPos[0] = pos_x;
				newCheckPos[1] = pos_y - 1;
				break;
			case 3:
				newCheckPos[0] = pos_x;
				newCheckPos[1] = pos_y + 1;
				break;
			}
			if((newCheckPos[0] < 0) || (newCheckPos[0] > mazeWidth - 1) || (newCheckPos[1] < 0) || (newCheckPos[1] > mazeHeight - 1)) continue;
			if(getArrayValue(array, newCheckPos[0], newCheckPos[1]) != 2){
				backtrack(array, newCheckPos[0], newCheckPos[1], pos_x, pos_y, mazeWidth, mazeHeight);
			}
			
		}
	}
	
	/**
	 * Prints a maze array, replacing byte values with their respective characters.
	 * @param maze the maze array to print.
	 */
	public static void printMazeArray(byte[][] maze){
		for(int h = 0; h < maze[0].length; h ++){
			for(int w = 0; w < maze.length; w ++){
				switch (maze[w][h]) {
				case 0:
					System.out.print("#");
					break;
				case 1:
					System.out.print(".");
					break;
				case 2:
					System.out.print(" ");
					break;
				}
				System.out.print(" ");
			}
			System.out.println();
		}	
	}

	/**
	 * Writes a value to a given "corridor position" (which doesn't include walls)
	 * @param maze the array to write to
	 * @param w the x-coordinate or the w-position of where to write
	 * @param h the y-coordinate or the h-position of where to write
	 * @param val the value to write
	 */
	private static void writeToMazeRarray(byte[][] maze, int w, int h, byte val){
		maze[w * 2 + 1][h * 2 + 1] = val;
	}
	
	/**
	 * Writes a value to the wall between the given cells.
	 * @param maze the array to write to
	 * @param w1 the x-coordinate or the w-position of the first cell
	 * @param h1 the y-coordinate or the h-position of the first cell
	 * @param w2 the x-coordinate or the w-position of the second cell
	 * @param h2 the y-coordinate or the h-position of the second cell
	 * @param val the value to write
	 */
	private static void writeBetween(byte[][] maze, int w1, int h1, int w2, int h2, byte val){
		maze[((w1 * 2 + 1) + (w2 * 2 + 1)) / 2][((h1 * 2 + 1) + (h2 * 2 + 1)) / 2] = val;
	}
	
	/**
	 * Gets a value from the array at the "corridor position" (which doesn't include walls)
	 * @param maze the array to read from
	 * @param w the x-coordinate or the w-position of where to read 
	 * @param h the y-coordinate or the h-position of where to read
	 * @return
	 */
	private static byte getArrayValue(byte[][] maze, int w, int h){
		if((w < 0) || (w > ((maze.length - 1) / 2) - 1) || (h < 0) || (h > ((maze[0].length - 1) / 2) - 1)) return - 1;
		return maze[w * 2 + 1][h * 2 + 1];
	}

	/**
	 * Checks if all corridor cells (not walls!) around this one (not diagonally) are also "visited"
	 * @param maze the maze to read from 
	 * @param w the x-coordinate or the w-position of the potentially surrounded cell
	 * @param h the y-coordinate or the h-position of the potentially surround cell
	 * @return
	 */
	private static boolean isSurrounded(byte[][] maze, int w, int h){
		if((getArrayValue(maze, w - 1, h) == 2 || getArrayValue(maze, w - 1, h) == -1) && (getArrayValue(maze, w + 1, h) == 2 || getArrayValue(maze, w + 1, h) == -1) && (getArrayValue(maze, w, h - 1) == 2 || getArrayValue(maze, w, h - 1) == -1) && (getArrayValue(maze, w, h + 1) == 2 || getArrayValue(maze, w, h + 1) == -1)) return true;
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println("Running Tests");
		System.out.println("Generating Recursive Backtracker Maze");
		long millis = System.currentTimeMillis();
		printMazeArray(generateRecursiveBacktrackerMaze(10, 10));
		System.out.println("Operation Took " + (System.currentTimeMillis() - millis) + " millis");
	}
	
}
