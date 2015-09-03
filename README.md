# SimpleMazeGen

A tiny maze generator i threw together for myself. Probably not the most efficient or special.
## Sample Output

When you run the code using the main() function provided, here is what the output could look like:
```
Running Tests
Generating Recursive Backtracker Maze
# # # # # # # # # # # # # # # # # # # # # 
#           #                       #   # 
#   # # #   # # #   # # # # # # #   #   # 
#   #               #           #       # 
#   # # # # # # # # #   # # #   # # #   # 
#       #               #           #   # 
# # #   #   # # #   # # #   # # # # #   # 
#   #   #       #   #           #       # 
#   #   # # # # #   # # # # # # #   # # # 
#   #   #           #               #   # 
#   # # #   # # # # #   # # # # # # #   # 
#           #       #       #           # 
# # # # #   #   #   # # #   # # #   #   # 
#           #   #       #   #       #   # 
#   # # #   #   # # #   #   #   # # #   # 
#   #       #   #   #       #   #   #   # 
#   # # # # #   #   # # # # #   #   #   # 
#           #       #           #       # 
#   # # #   # # #   # # # # #   #   # # # 
#       #                       #       # 
# # # # # # # # # # # # # # # # # # # # # 
Operation Took 13 millis
```
## Current Algorithms
This generator currently only uses the recursive backtracker algorithm but I'm planning on adding more.

## Sample Usage
### Basics
To get started with the maze generator, you have to first create the array of bytes that represents the maze. These methods in this generator are designed to be used with a special kind of array value structure. To clarify, the generator wants to have an array passed down to it that matches the following format:
```
W W W W W W W
W E W E W E W
W W W W W W W
W E W E W E W
W W W W W W W
W E W E W E W
W W W W W W W

W = Wall byte ID
E = Empty, unvisited space byte ID
```

In all the tests that ship with the program (the main method contains them), I use 0 as the ID for walls, and 1 as an ID for blank, unvisted space. Please note that I use another ID for cells that have already been processed by the generator - two (2). 

The reason behind the data structure is fairly simple. As seen in many images of mazes, walls are treated as simply lines between maze cells. However, for the sake of simplicity, they are treated by this program as their own cells, hence we need each cell to be originally surrounded by four (4) walls. The walls diagonally between the cells don't really do much.

If you understood what I said before, you'll probably understand why the first function we use,
```
byte[][] testMaze = MazeGenerator.newBlankWalledArray(5, 5, (byte) 0, (byte) 1);
```

Produces the following result:
```
0 0 0 0 0 0 0 0 0 0 0
0 1 0 1 0 1 0 1 0 1 0
0 0 0 0 0 0 0 0 0 0 0
0 1 0 1 0 1 0 1 0 1 0
0 0 0 0 0 0 0 0 0 0 0
0 1 0 1 0 1 0 1 0 1 0
0 0 0 0 0 0 0 0 0 0 0
0 1 0 1 0 1 0 1 0 1 0
0 0 0 0 0 0 0 0 0 0 0
0 1 0 1 0 1 0 1 0 1 0
0 0 0 0 0 0 0 0 0 0 0
```

Let's start from the beginning. The first two arguments, the two fives, are the dimensions of the array, the width and height. The next parameter we pass down is the ID of the wall tile, 0 in this case, and the last one is the ID of unvisted space, 1 in this case.

But why, you may ask, does the generator create an array that's 11 by 11?
Well, the __newBlankWalledArray__ method counts the width and height of the array in cells, excluding walls. So while the hypothetical cell at (0, 0) is unvisited space, 
```Java
testMaze[0][0]
```
is actually 0, not 1, because it's a wall, not a cell.
To prevent the confusion to do with the convertion between cell coordinates and actual array coordinates, I included two functions:
```Java
MazeGenerator.writeToMazeRarray(byte[][] maze, int w, int h, byte val)
MazeGenerator.getArrayValue(byte[][] maze, int w, int h)
```

The first argument is the maze array to use with the operation, the next two are the coordinates. 

So now we have our blank array. And it's, well, pretty blank. How do we fix that?
Well, the easiest way to make the array fun is to simply add the following line to our code:
```Java
MazeGenerator.generateRecursiveBacktrackerMaze(testMaze, (byte) 0, (byte) 1, (byte) 2, MazeGenerator.getByteWrapper(new byte[]{
	2
})); 
```

Okay. This is quite overwhelming. Let's take a look at what's going on:
We pass the testMaze array to the function, along with three ID's: the wall id, which has to be the same ID as we used for generating a blank array, the unvisted cell ID, also the same as the one we passed to the creation method before. Then we pass another ID: the visited cell ID. This is basically used by the generator to determine which cells it had already tweaked. And finally, the strangest part: 
```Java
MazeGenerator.getByteWrapper(new byte[]{
	2
})
```
Well, the getByteWrapper is used to convert a simple byte array, which is much faster to type, to an array of wrapper classes with the same value, used by the __generateRecursiveBacktrackerMaze__ function. What we're converting is an array of bytes that the generator should not visit when it's busy generating a maze. We use the ID 2 so that it doesn't try add more and more passages to a cell that is already connected to the rest of the maze. That's enough explanation. Let's get some results!

Adding the 
```
MazeGenerator.printMazeArray(byte[][] maze);
```

function to the end of our code, we get:
```Java
byte[][] testMaze = MazeGenerator.newBlankWalledArray(5, 5, (byte) 0, (byte) 1);
MazeGenerator.generateRecursiveBacktrackerMaze(testMaze, (byte) 0, (byte) 1, (byte) 2, MazeGenerator.getByteWrapper(new byte[]{
	2
}));
MazeGenerator.printMazeArray(testMaze)
```

Running this code produces something like:
```
# # # # # # # # # # # 
#           #       # 
# # #   #   # # #   # 
#       #           # 
#   # # # # # # #   # 
#   #   #           # 
#   #   #   # # # # # 
#   #   #   #       # 
# # #   #   #   #   # 
#               #   # 
# # # # # # # # # # # 
```
Hooray! We made a maze!

### Rooms
Although fairly basic, I have included the functionality for rooms in this generator. The two functions that help with this are:
```Java
fillWithRooms(byte[][] array, int iterations, int maxDim, boolean allowIntersection, byte roomid, ArrayList<Rectangle> existingRects)
fillWithRooms(byte[][] array, int iterations, int maxDim, boolean allowIntersection, byte roomid)
```
Alright, let's take a look from the beginning. We provide the array for the function to work with, the number of times to "attempt" to place the rooms, the maximum size of a room, whether to allow rooms to be placed over each other, the "id" of the tiles in room to be placed, and finally, if applicable, an ArrayList of Rectangles representing the rooms that were already placed into the array, to prevent overlapping if overlapping is disabled. This function also returns an ArrayList of all the rooms that were placed into the array, including the rectangles already provided to the function. Please note that the rectangles produced are actually 2 units / cells wider in both direections, and that is because of a 1-unit border being included with each rectangle. Calling the function without the last argument simply means there were no other rooms placed in it.

Let's test it:
```Java
// Get a blank Array
byte[][] testMaze = MazeGenerator.newBlankWalledArray(20, 20, (byte) 0, (byte) 1);
// Fill it with the first set of rooms, with the ID 3, not allowing them to interestct
ArrayList<Rectangle> rooms = MazeGenerator.fillWithRooms(testMaze, 10, 10, false, (byte) 3);
// Fill it with the second set of rooms, with the ID 4, still not allowing intersection.
// We also provide the previously returned set of rooms to prevent the new rooms from overlapping the old ones.
MazeGenerator.fillWithRooms(testMaze, 10, 5, false, (byte) 4, rooms);
// We use the IDs 3 and 4 because they're hardcoded into the printMazeArray function
MazeGenerator.printMazeArray(testMaze);
```

Here is the result of the above code being run:
```
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # 
# . # . # . # . # . # . # . # . # . # . # . # . # . # . # . # . # + # . # . # . # 
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # + # # # # # # # 
# . # . # . # . # . # . # . # . # . # . # . # . # . # + + + # . # + # . # . # . # 
# # # # # # # # # # # # # # # # # # # # # # # # # # # + + + # # # + # # # # # # # 
# . # . # . # . # - - - - - # . # . # . # . # . # . # + + + # . # + # . # . # . # 
# # # # # # # # # # # # # # # # # # # # # # # # # # # + + + # # # + # # # # # # # 
# . # + + + + + + + # . # + + + + + + + # . # . # . # + + + # . # + # . # . # . # 
# # # + + + + + + + # # # + + + + + + + # # # # # # # # # # # # # # # # # # # # # 
# . # + + + + + + + # . # + + + + + + + # . # . # . # . # . # . # + + + + + + + # 
# # # + + + + + + + # # # + + + + + + + # # # # # # # # # # # # # + + + + + + + # 
# . # + + + + + + + # . # + + + + + + + # . # . # . # - - - - - # + + + + + + + # 
# # # # # # # # # # # # # + + + + + + + # # # # # # # - - - - - # + + + + + + + # 
# . # . # . # . # . # . # + + + + + + + # . # . # . # - - - - - # + + + + + + + # 
# # # # # # # # # # # # # + + + + + + + # # # # # # # # # # # # # + + + + + + + # 
# . # . # . # . # . # . # + + + + + + + # . # . # . # . # . # . # + + + + + + + # 
# # # # # # # # # # # # # + + + + + + + # # # # # # # # # # # # # + + + + + + + # 
# . # . # . # . # . # . # + + + + + + + # . # - - - # . # . # . # + + + + + + + # 
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # 
# . # . # - - - # . # . # . # . # . # . # . # . # . # . # . # . # . # - - - # . # 
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # 
# . # . # . # . # . # . # . # . # - - - # . # . # . # + + + # . # . # . # . # . # 
# # # # # # # # # # # # # # # # # - - - # # # # # # # + + + # # # # # # # # # # # 
# . # . # . # . # . # . # . # . # - - - # - # . # . # + + + # . # . # . # . # . # 
# # # # # # # # # # # # # # # # # - - - # - # # # # # + + + # # # # # # # # # # # 
# . # . # . # . # . # . # . # . # - - - # - # . # . # + + + # . # . # . # . # . # 
# # # # # # # # # # # # # # # # # # # # # - # # # # # # # # # # # # # # # # # # # 
# . # . # . # . # . # . # . # . # . # . # - # - # . # + + + # . # . # . # . # . # 
# # # # # # # # # # # # # # # # # # # # # # # - # # # + + + # # # # # # # # # # # 
# . # . # . # . # + + + + + + + # . # . # . # - # . # + + + # . # . # . # . # . # 
# # # # # # # # # + + + + + + + # # # # # # # - # # # + + + # # # # # # # # # # # 
# . # . # . # . # + + + + + + + # . # . # . # - # . # + + + # . # . # . # . # . # 
# # # # # # # # # # # # # # # # # # # # # # # # # # # + + + # # # # # # # # # # # 
# . # . # . # . # . # . # . # . # . # . # . # - - - # + + + # . # . # . # . # . # 
# # # # # # # # # # # # # # # # # # # # # # # - - - # # # # # # # # # # # # # # # 
# . # . # . # . # . # . # . # . # . # . # . # - - - # . # . # + + + + + # . # . # 
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # + + + + + # # # # # 
# . # . # . # . # . # . # . # . # - # . # . # . # . # . # . # + + + + + # . # . # 
# # # # # # # # # # # # # # # # # - # # # # # # # # # # # # # + + + + + # # # # # 
# . # . # . # . # . # . # . # . # - # . # . # . # . # . # . # + + + + + # + + + # 
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # 
```

There you have it. Two separate sets of different rooms. 
We can throw in a maze now:
```Java
MazeGenerator.generateRecursiveBacktrackerMaze(testMaze, (byte) 0, (byte) 1, (byte) 2, MazeGenerator.getByteWrapper(new byte[]{
	2, 3, 4
}));
```

Notice how we added the ID's 3 and 4 to the array of bytes: we don't want the generator trying to generate mazes inside the rooms. They don't even have walls any more!
Here's what could be produced:
```
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # 
#       #           #           #           #                   #               # 
#   #   #   # # #   #   #   # # #   # # #   #   #   # # # # #   # # # # #   #   # 
#   #   #       #       #           #   #       #           #           #   #   # 
#   # # # # #   # # # # # # #   # # #   # # # # # # # # #   # # # # #   #   # # # 
#               # + + + + + #       #       #       #       #       #   #       # 
# # # # # # # # # + + + + + # # #   #   # # #   #   #   # # #   #   #   # # #   # 
# + + + + + + + # + + + + + #           #       #   #   #       #   #           # 
# + + + + + + + # + + + + + #   # # # # #   # # # # #   # # # # #   # # #   # # # 
# + + + + + + + # + + + + + #   # + + + #           #           #           # - # 
# + + + + + + + # # # # # # #   # + + + # # # # #   # # # # #   # # # # # # # - # 
# + + + + + + + #       #       # + + + #           #       #   # - - - - - # - # 
# + + + + + + + #   #   #   # # # # # # # # # # #   #   #   #   # - - - - - # - # 
# + + + + + + + #   #   #   #   # + + + + + + + #   #   #       # - - - - - # - # 
# # # # # # # # #   #   #   #   # # # # # # # # #   #   # # # # # # # # # # # # # 
#                   #   #       #               #   #           # + + + + + + + # 
#   # # # # # # # # #   # # #   #   #   # # # # #   #   # # #   # + + + + + + + # 
#       # + + + + + #       #       #   #           #   #       # + + + + + + + # 
# # #   # + + + + + #   # # #   # # # # #   # # # # # # #   # # # + + + + + + + # 
#       # + + + + + #           # - - - #               #       # + + + + + + + # 
#   # # # # # # # # # # # # #   # - - - # # # # # # #   # # #   # + + + + + + + # 
#   # + + + + + + + + + + + #   # - - - # + #           #       # + + + + + + + # 
#   # + + + + + + + + + + + # # # # # # # + # # # # #   #   #   # + + + + + + + # 
#   # + + + + + + + + + + + # - - - #   # + # - - - #       #   # + + + + + + + # 
#   # + + + + + + + + + + + # - - - #   # + # - - - # # # # #   # # # # # # # # # 
#   # + + + + + + + + + + + # - - - #   # + # - - - #       #       #           # 
#   # # # # # # # # # # # # # # # # # # # # # # # # #   # # # # #   # # # # #   # 
#   #                       # + + + + + + + #                   #               # 
#   # # # # #   # # # # #   # + + + + + + + #   # # # # # # #   #   # # # # # # # 
#           #       #       # + + + + + + + #   #           #       # - - - - - # 
# # # # #   # # # # #   # # # + + + + + + + #   # # # # #   # # # # # - - - - - # 
# + + + #           #       # + + + + + + + #   #                   # - - - - - # 
# + + + # # # # #   #   #   # + + + + + + + #   #   # # # # # # #   # - - - - - # 
# + + + #       #   #   #   # + + + + + + + #       #   #       #   # - - - - - # 
# + + + #   # # #   # # #   # + + + + + + + # # # # #   #   #   #   # # # # # # # 
# + + + #   # - #   #       # + + + + + + + # - # - #   #   #   #           #   # 
# + + + #   # # #   #   #   # + + + + + + + # - # - #   #   #   # # # # #   #   # 
# + + + #       #       #   # + + + + + + + # - # - #       #           #       # 
# + + + #   # # # # # # #   # # # # # # # # # # # - #   # # # # # # #   # # #   # 
# + + + #                       # - - - #       # - #               #           # 
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # 
```

Alright! But the rooms so far aren't connected to the maze...That's kind of useless. Here's where we use another function:
```Java
connectValues(byte[][] array, byte valA, byte valB)
```

Not so many arguments in this one. The first is obviously the array we're modifying, the next two are the two types of cells to attempt to unite. The function will see if there are cells of type A and B separated by a wall, remove the wall, and convert the B value into the A value, essentially fusing the two chunks together.
Let's connect the two types of room to the maze. To do so, we run
```Java
MazeGenerator.connectValues(testMaze, (byte) 2, (byte) 3); 
MazeGenerator.connectValues(testMaze, (byte) 2, (byte) 4); 
```

Here's the result when executing all the code we wrote in this section:
```
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # 
#                       #                   #       #                           # 
#   # # #   # # # # # # # # # # #   #   #   #   # # #   # # # # # # #   #   # # # 
#       #                           #   #   #           #               #       # 
#   # # #   #   # # # # #   #   # # # # # # #   # # # # #   # # # # # # # # # # # 
#       #   #       #       #           #       #       #   #           #       # 
#       #   # # # # #   # # # # # # # # # # # # #   #   #   #   # # #   #   #   # 
#       #           #       #       #               #   #           #       #   # 
# # # # # # #   #   #       #   #   #   # # # # # # #   #   # # # # # # #   #   # 
#           #   #   #       #   #       #               #               #   #   # 
# # #   #   # # #   #       #   #   # # #   # # # # # # #               # # #   # 
#       #   #       #       #   #       #   #           #               #       # 
#   # # #   #   # # # # # # #   #       #   #   # # # # #               #   # # # 
#   #   #   #                   #       #   #           #               #       # 
#   #   #   # # # # #   #   #   #       #   # # #   #   #               #   #   # 
#   #       #       #   #   #   #       #       #   #   #               #   #   # 
#   # # #   #   #   #   #   #   #       # # #   # # #   #               # # #   # 
#       #       #   #   #   #   #       #   #           #               #       # 
#   #   # # # # #   #   #   #   #       #   # # # # #   # # # # # # # # #   #   # 
#   #       #       #   #   #   #       #       #       #   #               #   # 
#   # # #   # # #   #   #   #   # # # # #   # # #   # # #   #   # # # # #   #   # 
#       #       #       #   #   #           #       #           #           #   # 
# # #   # # #   # # # # #   #   #   #   # # #   #   # # # # # # #   # # #   # # # 
#           #           #   #   #   #       #   #               #   #   #       # 
#   # # # # #   # # #   #   #   # # # # #   #   #               #   #   #   #   # 
#           #       #   #   #           #   #   #               #   #       #   # 
#           #       #   #   #           #   #   # # # # # # # # #   #   # # #   # 
#           #       #   #   #           #       #                   #       #   # 
#           # # # # #   # # # # # # # # #   #   #   # # # # # # #   # # #   #   # 
#           #       #           #       #   #   #               #       #   #   # 
#           #   #   #   # # #   #   #   #   #   #               #   #   #   # # # 
#           #   #   #       #   #   #   #   #   #               #   #   #       # 
#           # # #   #       #   #   #   # # #   #               #   #   # # #   # 
#           #       #       #   #   #   #       #               #   #       #   # 
# # # # # # #   #   # # # # #   # # #   #   # # #               #   # # #   #   # 
#   #           #   #       #           #   #   #               #   #   #   #   # 
#   #   #   # # #   # # #   # # # # # # #   #   # # # # # # # # #   #   #   #   # 
#       #       #                       #       #               #       #   #   # 
#   # # # # #   # # # # # # # # # # # # # # #   #   # # # # #   # # # # #   # # # 
#           #                                       #                           # 
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # 
```

That's it! You've made a maze with rooms!
