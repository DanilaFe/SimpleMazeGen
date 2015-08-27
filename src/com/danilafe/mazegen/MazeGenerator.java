package com.danilafe.mazegen;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class to generate mazes!
 * @author danilafe
 * @version 1.1
 */
public class MazeGenerator {

	private static Random random = new Random();
	private static final int MAX_THREADS = 8;
	private static int threadCount = 0;
	
	private MazeGenerator(){
		
	}
	
	public static byte[][] newBlankWalledArray(int width, int height){
		byte[][] mazeArray = new byte[width * 2 + 1][height * 2 + 1];
		for(int i = 0; i < mazeArray.length; i++){
			for(int j = 0; j < mazeArray[0].length; j++){
				mazeArray[i][j] = ((i % 2 == 1) && (j % 2 == 1)) ? (byte) 1 : (byte) 0;
			}
		}
		return mazeArray;
	}
	
	
	/**
	 * Generates a new maze using the Recursive Backtracker algorithm. 
	 * @param width the width of the generated maze, in "corridors" - walls don't count as part of the width.
	 * @param height the height of the generated maze, in "corridors" - walls don't count as part of the width.
	 * @return the generated array of bytes. 0 means wall, 1 means unvisited and 2 means empty.
	 */
	public static byte[][] generateRecursiveBacktrackerMaze(int width, int height){
		byte[][] mazeArray = newBlankWalledArray(width, height);
		
		generateRecursiveBacktrackerMaze(mazeArray);
		
		return mazeArray;
	}
	
	/**
	 * Fills the given array with maze pathways. Existing rooms do not get connected on purpose. 
	 * @param mazeArray the array to use for generating a maze. Has to be a "walled" array, represented as follows: <br>
	 * <table>
	 * <tr><td># </td><td># </td><td># </td><td># </td><td># </td></tr>
	 * <tr><td># </td><td>  </td><td># </td><td>  </td><td># </td></tr>
	 * <tr><td># </td><td># </td><td># </td><td># </td><td># </td></tr>
	 * <tr><td># </td><td>  </td><td># </td><td>  </td><td># </td></tr>
	 * <tr><td># </td><td># </td><td># </td><td># </td><td># </td></tr>
	 * </table>
	 */
	public static void generateRecursiveBacktrackerMaze(byte[][] mazeArray){
		int width = (mazeArray.length - 1) / 2;
		int height = (mazeArray[0].length - 1) / 2;
		int[] cursorPos;
		do {
			do {
				cursorPos = new int[]{
						random.nextInt(width),
						random.nextInt(height)
					};
			} while (getArrayValue(mazeArray, cursorPos[0], cursorPos[1]) != 1);
			
			backtrack(mazeArray, cursorPos[0], cursorPos[1], cursorPos[0], cursorPos[1], width, height);
		} while(containsValue(mazeArray, (byte) 1));
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
	private static void backtrack(final byte[][] array, final int pos_x, final int pos_y, final int pos_prevx, final int pos_prevy, final int mazeWidth, final int mazeHeight){
		writeBetween(array, pos_x, pos_y, pos_prevx, pos_prevy, (byte) 2); 
		writeToMazeRarray(array, pos_x, pos_y, (byte) 2);
		while(!isSurrounded(array, pos_x, pos_y)){
			int direction = random.nextInt(4);
			final int[] newCheckPos = new int[2];
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
			if(getArrayValue(array, newCheckPos[0], newCheckPos[1]) != 2 && getArrayValue(array, newCheckPos[0], newCheckPos[1]) != 3){
				if(threadCount < MAX_THREADS){
					threadCount += 1;
					Thread backtrackThread = new Thread("Backtracking Thread " + threadCount){
						public void run(){
							backtrack(array, newCheckPos[0], newCheckPos[1], pos_x, pos_y, mazeWidth, mazeHeight);
							threadCount -= 1;
						}
					};
					backtrackThread.start();
				}
				backtrack(array, newCheckPos[0], newCheckPos[1], pos_x, pos_y, mazeWidth, mazeHeight);
			}
			
		}
	}
	
	/**
	 * Prints a maze array, replacing byte values with their respective characters.
	 * @param maze the maze array to print.
	 */
	public static void printMazeArray(byte[][] maze){
		for(int h = 0; h < maze[0].length; h++){
			for(int w = 0; w < maze.length; w++){
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
				case 3:
					System.out.print("+");
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
	private synchronized static void writeToMazeRarray(byte[][] maze, int w, int h, byte val){
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
	private synchronized static void writeBetween(byte[][] maze, int w1, int h1, int w2, int h2, byte val){
		maze[((w1 * 2 + 1) + (w2 * 2 + 1)) / 2][((h1 * 2 + 1) + (h2 * 2 + 1)) / 2] = val;
	}
	
	/**
	 * Gets a value from the array at the "corridor position" (which doesn't include walls)
	 * @param maze the array to read from
	 * @param w the x-coordinate or the w-position of where to read 
	 * @param h the y-coordinate or the h-position of where to read
	 * @return
	 */
	private synchronized static byte getArrayValue(byte[][] maze, int w, int h){
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
	private synchronized static boolean isSurrounded(byte[][] maze, int w, int h){
		return ((getArrayValue(maze, w - 1, h) == 2 || getArrayValue(maze, w - 1, h) == -1 || getArrayValue(maze, w - 1, h) == 3) 
				&& (getArrayValue(maze, w + 1, h) == 2 || getArrayValue(maze, w + 1, h) == -1 || getArrayValue(maze, w + 1, h) == 3)
				&& (getArrayValue(maze, w, h - 1) == 2 || getArrayValue(maze, w, h - 1) == -1 || getArrayValue(maze, w, h - 1) == 3)
				&& (getArrayValue(maze, w, h + 1) == 2 || getArrayValue(maze, w, h + 1) == -1 || getArrayValue(maze, w, h + 1) == 3));
	}
	
	/**
	 * Fills the area between the given two corridor points (not array indexes!) with the given value, walls included
	 * @param maze the array to process
	 * @param w1 the x-coordinate or the w-position of the first cell
	 * @param h1 the y-coordinate or the h-position of the first cell
	 * @param w2 the x-coordinate or the w-position of the second cell
	 * @param h2 the y-coordinate or the h-position of the second cell
	 * @param val the value to write
	 */
	private synchronized static void fillArea(byte[][] maze, int w1, int h1, int w2, int h2, byte value){
		boolean w2Bigger = w2 > w1;
		boolean h2Bigger = h2 > h1;
		for(int h = h1; (h2Bigger && h <= h2) || (!h2Bigger && h >= h2); h += (h2Bigger) ? 1 : -1){
			for(int w = w1; (w2Bigger && w <= w2) || (!w2Bigger && w >= w2); w += (w2Bigger) ? 1 : -1){
				if(w != w2)
				writeBetween(maze, w, h, w + ((w2Bigger) ? 1 : -1), h, (byte) 3);
				if(h != h2)
				writeBetween(maze, w, h, w, h + ((h2Bigger) ? 1 : -1), (byte) 3);
				if(h != h2 && w != w2)
				writeBetween(maze, w, h, w + ((w2Bigger) ? 1 : -1), h + ((h2Bigger) ? 1 : -1), value);
				writeToMazeRarray(maze, w, h, (byte) 3); 
			}
		}
	}
	
	/**
	 * Flood fills the given array, replacing one value with another.
	 * @param array the array to flood fill
	 * @param x the starting x-coordinate of the flood fill
	 * @param y the starting y-coordinate of the flood fill
	 * @param toReplace the value to be replaced
	 * @param value the value to replace with
	 */
	private synchronized static void floodFill(byte[][] array, int x, int y, byte toReplace, byte value){
		if (array[x][y] != toReplace) return;
		array[x][y] = value;
		if(x > 0) floodFill(array, x - 1, y, toReplace, value);
		if(x < array.length - 1) floodFill(array, x + 1, y, toReplace, value);
		if(y > 0) floodFill(array, x, y - 1, toReplace, value);
		if(y < array[0].length - 1) floodFill(array, x, y + 1, toReplace, value);
	}
	
	/**
	 * Replaces parts of the maze with room tiles. (room tile id = 3)
	 * @param array the array to perform the operation on.
	 * @param iterations how many times to try place a room
	 * @param maxDim the maximum possible width / height of a room.
	 */
	public synchronized static void fillWithRooms(byte[][] array, int iterations, int maxDim, boolean allowIntersection){
		int arrayWidthCorridors = (array.length - 1) / 2;
		int arrayHeightCorridors = (array[0].length - 1) / 2;
		ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
		for(int i = 0; i < iterations; i++){
			boolean intersects = true;
			int sourceX;
			int sourceY;
			int targetX;
			int targetY;
			Rectangle rectangle;
			do {
				sourceX = random.nextInt(arrayWidthCorridors);
				sourceY = random.nextInt(arrayHeightCorridors);
				targetX = sourceX + (random.nextInt(maxDim) - maxDim / 2);
				targetY = sourceY + (random.nextInt(maxDim) - maxDim / 2);
				if(targetX < 0) targetX = 0;
				if(targetY < 0) targetY = 0;
				if(targetX >= arrayWidthCorridors) targetX = arrayWidthCorridors - 1;
				if(targetY >= arrayHeightCorridors) targetY = arrayHeightCorridors - 1;
				
				int rectPointX = (sourceX < targetX) ? sourceX : targetX;
				int rectPointY = (sourceY < targetY) ? sourceY : targetY;
				int rectWidth = Math.abs(targetX - sourceX);
				int rectHeight = Math.abs(targetY - sourceY);
				rectangle = new Rectangle(rectPointX - 1, rectPointY - 1, rectWidth + 1, rectHeight + 1);
				intersects = false;
				for(Rectangle otherRect : rectangles){
					intersects |= (otherRect.intersects(rectangle));
				}
				if(allowIntersection) intersects = false;
			} while (intersects);
			
			rectangles.add(rectangle);
			
			fillArea(array, sourceX, sourceY, targetX, targetY, (byte) 3);
		}
	}
	
	/**
	 * Checks if the cell connects other cells of valueA and valueB
	 * @param array the array to perform the operation on
	 * @param x the x-coordinate of the cell to check
	 * @param y the y-coordinate of the cell to check
	 * @param valA the first value the cell has to touch
	 * @param valB the second value the cell has to touch
	 * @return whether the cell touches other cells of both types
	 */
	private synchronized static boolean isConnector(byte[][] array, int x, int y, byte valA, byte valB){
		boolean hasValA = false;
		boolean hasValB = false;
		if(x > 0) {
			hasValA |= array[x - 1][y] == valA;
			hasValB |= array[x - 1][y] == valB;
		}
		if(x < array.length - 1) {
			hasValA |= array[x + 1][y] == valA;
			hasValB |= array[x + 1][y] == valB;
		}
		if(y > 0) {
			hasValA |= array[x][y - 1] == valA;
			hasValB |= array[x][y - 1] == valB;
		}
		if(y < array[0].length - 1) {
			hasValA |= array[x][y + 1] == valA;
			hasValB |= array[x][y + 1] == valB;
		}
		return hasValA && hasValB;
	}

	/**
	 * Finds areas with room tiles, and if they are adjacent to empty corridors, connects them and also marks them as empty space.
	 * @param array the array to perform the operation on.
	 */
	public synchronized static void connectPassages(byte[][] array){
		connectValues(array, (byte) 2, (byte) 3);
	}
	
	/**
	 * Generic method to connect two different tile IDS
	 * @param array the array to perform the operation on
	 * @param valA the first value to connect
	 * @param valB the second value to connect
	 */
	public synchronized static void connectValues(byte[][] array, byte valA, byte valB){
		for(int h = 0; h < array[0].length; h ++){
			for(int w = 0; w < array.length; w ++){
				if(isConnector(array, w, h, valA, valB)){
					array[w][h] = valB;
					floodFill(array, w, h, valB, valA); 
				}
			}
		}
	}
	
	private synchronized static boolean containsValue(byte[][] array, byte value){
		boolean contains = false;
		for(int w = 0; w < array.length; w++){
			for(int h = 0; h < array[0].length; h++){
				contains |= array[w][h] == value;
			}
		}
		return contains;
	}
	
	public synchronized static void setSeed(long seed){
		random.setSeed(seed);
	}
	
	
	public synchronized static BufferedImage generateMazeImage(byte[][] mazeArray, int cellWidth, int cellHeight, Color wallColor, Color emptyColor){
		BufferedImage bufferedImage = new BufferedImage(mazeArray.length * cellWidth, mazeArray[0].length * cellHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bufferedImage.createGraphics();
		for(int i = 0; i < mazeArray.length; i++){
			for(int j = 0; j < mazeArray[0].length; j ++){
				Color toUse = (mazeArray[i][j] == 0) ? wallColor : emptyColor;
				graphics.setColor(toUse);
				graphics.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
			}
		}
		return bufferedImage;
	}
	
	public static void main(String[] args) {
		System.out.println("Running Tests");
		System.out.println("Generating Recursive Backtracker Maze");
		long millis = System.currentTimeMillis();
		printMazeArray(generateRecursiveBacktrackerMaze(10, 10));
		System.out.println("Operation Took " + (System.currentTimeMillis() - millis) + " millis");
		System.out.println("Generating Recursive Backtracker Maze With Rooms");
		millis = System.currentTimeMillis();
		byte[][] testMaze = newBlankWalledArray(30, 30);
		fillWithRooms(testMaze, 30, 15, true);
		generateRecursiveBacktrackerMaze(testMaze);
		connectPassages(testMaze);
		printMazeArray(testMaze);
		System.out.println("Operation Took " + (System.currentTimeMillis() - millis) + " millis");
	}
	
}
