package mazeGenerator;

import maze.Maze;

public class ModifiedPrimsGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
		//If the maze type is a standard rectangular maze
		if(maze.type == Maze.NORMAL)
		{
			generateNormalMaze(maze);
		}
		//If the maze type is a maze with hexagonal cells
		else if(maze.type == Maze.HEX)
		{
			generateHexMaze(maze);
		}
		//If the maze type is unable to be generated using this generator
		else
		{
			System.err.println("Maze type invalid for this generator");
			return;
		}

	} // end of generateMaze()
	
	//Method to generate a normal rectangular maze
	public void generateNormalMaze(Maze maze) {
		
	} //end of generateNormalMaze()
	
	//Method to generate a hexagonal maze
	public void generateHexMaze(Maze maze) {
		
	} //end of generateHexMaze()

} // end of class ModifiedPrimsGenerator
