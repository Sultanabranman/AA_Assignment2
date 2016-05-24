package mazeGenerator;

import java.util.ArrayList;
import java.util.Random;

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
		Cell cells[][] = new Cell[maze.sizeR][maze.sizeC];
		
		//List of all current sets
		ArrayList<Set> sets = new ArrayList<Set>();
		
		//Initialise sets array elements to null
		for(int i = 0; i < maze.sizeR; i++)
		{
			for(int j = 0; j < maze.sizeC; j++)
			{
				cells[i][j] = null;
			}
		}
		
		//Add all possible edges to the edges set.
		initEdges(maze, edges);
		
		//While there are still edges in the set, keep processing.
		while(edges.size() > 0)
		{
			//Select a random edge from set
			Edge edge = getRandomEdge(edges);
			
			//Remove the edge from set of edges
			edges.remove(edge);
			
			//Get the cells in the edge
			Cell c1 = edge.cell_1;
			Cell c2 = edge.cell_2;
			
			//If both cells don't belong to a set, join them and create a set
			if((cells[c1.r][c1.c] == null) && (cells[c2.r][c2.c] == null))
			{
				//Create a new set, and add it to the list of sets
				Set set = new Set();
				
				//Set the first cell as the root of the set
				set.root = c1;
				
				//Add both cells in the edge to the set
				set.cells.add(c1);
				set.cells.add(c2);
				
				//Mark both cells as belonging to the created set
				cells[c1.r][c1.c] = set.root;
				cells[c2.r][c2.c] = set.root;					
				
				//Add created set to the list of sets
				sets.add(set);
			}
			
			//If both cells belong to the same set, discard the edge	
			else if(cells[c1.r][c1.c] == cells[c2.r][c2.c])
			{
				continue;
			}
			
			//If cell 1 doesn't belong to a set and cell 2 does, add cell 1 to cell 2 set
			else if(cells[c1.r][c1.c] == null)
			{
				int i = -1;				
				
				//Assign c1 the same root as c2
				cells[c1.r][c1.c] = cells[c2.r][c2.c];				
			
				//Get the index of the set c2 belongs to
				i = getSet(sets, c2, cells);
				
				//Get the set that cell 2 belongs to 
				Set set = sets.get(i);
				
				//Add cell 1 of the edge to the set
				set.cells.add(c1);
				
				//Update sets with changed set
				sets.set(i, set);
			}
			
			//If cell 2 doesn't belong to a set and cell 1 does, add cell 2 to cell 1 set
			else if(cells[c2.r][c2.c] == null)
			{	
				int i = -1;
				
				//Assign unassigned the same root as assigned
				cells[c2.r][c2.c] = cells[c1.r][c1.c];				
				
				//Get the index of the set c2 belongs to
				i = getSet(sets, c1, cells);
				
				//Get the set that cell 2 belongs to 
				Set set = sets.get(i);
				
				//Add cell 1 of the edge to the set
				set.cells.add(c2);
				
				//Update sets with changed set
				sets.set(i, set);
			}
			
			//If the cells belong to different sets, join the two sets together
			else if(cells[c1.r][c1.c] != cells[c2.r][c2.c])
			{				
				//Get the set of cell 1
				Set set1 = sets.get(getSet(sets, c1, cells));
				Set set2 = sets.get(getSet(sets, c2, cells));
				
				//For each cell stored in set 2, add the cell to set 1 and 
				//update cells array
				for(int i = 0; i < set2.cells.size(); i++)
				{
					//Get the cell at index i in set 2
					Cell cell = set2.cells.get(i);
					
					//Add set 2 cells to set 1
					set1.cells.add(cell);					
					
					//Update the cell in cells to set1 root
					cells[cell.r][cell.c] = set1.root;					
				}
				
				//Remove set 2 from the list of sets
				sets.remove(set2);
			}				
			
			//Remove walls
			remove_walls(c1, c2);			
			
			//Update maze
			update_maze(maze, c1, c2);
		}
		
		return;
		
	} // end of generateMaze()
	
	//Initialise the set of edges 
	public void initEdges(Maze maze, ArrayList<Edge> edges)
	{
		for(int i = 0; i < maze.sizeC; i++)
		{
			for(int j = 0; j < maze.sizeR; j++)
			{
				Cell cell = maze.map[i][j];
								
				//If the cell is a tunnel entrance/exit, create an edge with the
				//other end of the tunnel and move on to the next tunnel
				if(cell.tunnelTo != null)
				{
					edges.add(new Edge(cell, cell.tunnelTo));
					continue;
				}
				
				//For each of the neighbours of the current cell
				for(int k = 0; k < cell.neigh.length; k++)
				{
					//If the cell deosn't have a neighbour in direction i, 
					//continue to next neighbour
					if(cell.neigh[k] == null)
					{
						continue;
					}
					//Create a new edge between cell and cell neighbour[i] and 
					//add it to the set of edges
					edges.add(new Edge(cell, cell.neigh[k]));
				}
			}
		}
		
		return;
	} //end of initEdges()
	
	//Select a random edge from the edges left in the set and return it
	public Edge getRandomEdge(ArrayList<Edge> edges)
	{
		Edge edge = null;
		
		//Get a random edges from edges by generating a int in range 
		//0 - edges.size() and retrieving the edge from edges at that int
		edge = edges.get(getRandInt(edges.size()));
		
		return edge;
	} //end of getRandomEdge()
	
	//Generates a random int in range 0 - max
	public int getRandInt(int max)
	{
		//Variable to hold random int
		int r = 0;
		//Create random number generator
		Random randomGenerator = new Random();
		
		//Generate a random int in range 0 - max 
		r = randomGenerator.nextInt(max);
		
		return r;
	}//end of getRandInt()
	
	//Searches through the list of sets for the set cell belongs to
	public int getSet(ArrayList<Set> sets, Cell cell, Cell[][] cells)
	{
		//Variable to store set while it is checked
		Set set = null;
		
		//Stores index of set cell belongs to
		int index = -1;
		
		//Search through each set for the set cell belongs to
		for(index = 0; index < sets.size(); index++)
		{
			//Get the set at index
			set = sets.get(index);			
			
			//If the root matches the root stored in the position of cell 
			if(set.root == cells[cell.r][cell.c])
			{
				//Break from loop
				break;
			}
			else
			{
				//If the root doesn't match, continue searching
				continue;
			}		
		}
		
		if(index == sets.size())
		{
			System.out.println("missed");
		}
		
		return index;
	} //end of getSet()
	
	//Remove the walls separating cell 1 and cell 2
	public void remove_walls(Cell c1, Cell c2)
	{
		//Remove wall in direction of c1 for c2. 
		for(int i = 0; i < c1.neigh.length; i++)
		{
			//If the neighbour is not present, don't check that direction
			if(c1.neigh[i] == null)
			{
				continue;
			}
			//If the neighbour is in direction i is c2, remove wall in that direction
			if(c1.neigh[i] == c2)
			{
				//remove the wall in direction i
				c1.wall[i].present = false;
			}
		}
		
		//Remove wall in direction of fc for cell nc. 
		for(int i = 0; i < c2.neigh.length; i++)
		{
			//If the neighbour is not present, don't check that direction
			if(c2.neigh[i] == null)
			{
				continue;
			}
			//If the neighbour in direction i is nc, remove wall in that direction
			if(c2.neigh[i] == c1)
			{
				//remove the wall in direction i
				c2.wall[i].present = false;
			}
		}
		
		return;
	} //end of remove_walls()
	
	//Update maze with updated information on cells c1 and c2
	public void update_maze(Maze maze, Cell c1, Cell c2)
	{
		//Update cells in positions fc and nc
		maze.map[c1.r][c1.c] = c1;
		maze.map[c2.r][c2.c] = c2;
		
		return;
	}
	
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
