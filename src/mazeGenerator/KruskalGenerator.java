package mazeGenerator;

import java.util.ArrayList;

import maze.Cell;
import maze.Maze;

public class KruskalGenerator implements MazeGenerator {

	//Create an array to hold the directions of a rectangular maze
	int directions[] = new int[4];
	
	@Override
	public void generateMaze(Maze maze) {
		//Set of all edges possible in the maze
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		//Maintains list of what set each cell belongs to
		Set sets[][] = new Set[maze.sizeR][maze.sizeC];
		
		//Initialise sets array elements to null
		for(int i = 0; i < sets[0].length; i++)
			for(int j = 0; j < sets[1].length; j++)
			{
				sets[i][j] = null;
			}
		
		//Fill directions array with direction values for rectangular cell
		directions[0] = Maze.EAST;
		directions[1] = Maze.NORTH;
		directions[2] = Maze.WEST;
		directions[3] = Maze.SOUTH;
		
		//Add all possible edges to the edges set.
		initEdges(maze, edges);
		
		//While there are still edges in the set, keep processing.
		while(edges.size() > 0)
		{
			//Select a random edge from set
			Edge edge = getRandomEdge();
			
			//Check which set each cell in the edge belongs to
			
			//If either of the cells don't belong to a set, join them
			
			//If the cells belong to different sets, join the two sets together
			
			//If both cells belong to the same set, discard the edge			
			
			//Remove walls
			
			//Update maze
		}
		
		
	} // end of generateMaze()
	
	//Object to describe an edge. Requires 2 cells to represent the edge
	public class Edge
	{
		public Cell cell_1 = null;
		public Cell cell_2 = null;
		
		//Constructor for edge, creates an edge between 2 cells
		public Edge(Cell cell_1, Cell cell_2)
		{
			this.cell_1 = cell_1;
			this.cell_2 = cell_2;
		}		
	}
	
	//Object to describe a list of cells 
	public class Set
	{
		public Cell root = null;
		
		//List of all cells belonging to the set
		public ArrayList<Cell> cells = new ArrayList<Cell>();
		
		//Constructor for Set, requires no parameters
		public Set()
		{
			
		}
	}


} // end of class KruskalGenerator
