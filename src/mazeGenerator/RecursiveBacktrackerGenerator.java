package mazeGenerator;

import maze.Maze;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
		//If the maze type is a standard rectangular maze
		if(maze.type == Maze.NORMAL)
		{
			generateNormalMaze(maze);
		}
		//If the maze type is a rectangular maze with tunnels
		else if(maze.type == Maze.TUNNEL)
		{
			generateTunnelMaze(maze);
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
		
	//Method to generate a normal rectangular maze with tunnels
	public void generateTunnelMaze(Maze maze) {
			
	} //end of generateTunnelMaze()

} // end of class RecursiveBacktrackerGenerator
