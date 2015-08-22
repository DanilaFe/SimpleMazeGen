import java.util.Arrays;
import java.util.Random;

public class MazeGenerator {

	private static Random random = new Random();
	
	private MazeGenerator(){
		
	}
	
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
		
		return mazeArray;
	}
	
	public static void backtrack(byte[][] array, int pos_x, int pos_y, int pos_prevx, int pos_prevy){
		writeBetween(array, pos_x, pos_y, pos_prevx, pos_prevy, (byte) 2); 
		writeToMazeRarray(array, pos_x, pos_y, (byte) 2);
		if(isSurrounded(array, pos_x, pos_y)) return;
	}
	
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

	public static void writeToMazeRarray(byte[][] maze, int w, int h, byte val){
		maze[w * 2 + 1][h * 2 + 1] = val;
	}
	
	public static void writeBetween(byte[][] maze, int w1, int h1, int w2, int h2, byte val){
		maze[((w1 * 2 + 1) + (w2 * 2 + 1)) / 2][((h1 * 2 + 1) + (h2 * 2 + 1)) / 2] = val;
	}
	
	public static byte getArrayValue(byte[][] maze, int w, int h){
		return maze[w * 2 + 1][h * 2 + 1];
	}

	public static boolean isSurrounded(byte[][] maze, int w, int h){
		if(getArrayValue(maze, w - 1, h) == 2 && getArrayValue(maze, w + 1, h) == 2 && getArrayValue(maze, w, h - 1) == 2 && getArrayValue(maze, w, h + 1) == 2) return true;
		return false;
	}
	
	public static void main(String[] args) {
		byte[][] maze =  generateRecursiveBacktrackerMaze(10, 10);
		printMazeArray(maze);
	}
	
}
