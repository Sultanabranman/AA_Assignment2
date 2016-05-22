package mazeGenerator;

import java.util.Random;
import java.util.Stack;

import maze.Cell;
import maze.Maze;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

	@Override
	public void generateMaze(Maze maze) {
		//Two dimensional array to store visited status for each cell
		boolean visited[][] = new boolean[maze.sizeR][maze.sizeC];			
		//Variable to hold current cell
		Cell currCell = null;
		//Variable to hold selected neighbouring cell
		Cell nc = null;		
		// Variable to hold randomly generated int
		int r = 0;		
		//Stack to store maze cells as maze is generated
		Stack<Cell> st = new Stack<Cell>();
		
		//Initialise visited array elements to false
		for(int i = 0; i < visited[0].length; i++)
			for(int j = 0; j < visited[1].length; j++)
			{
				visited[i][j] = false;
			}
		
		//Add maze entrance to the storage structure.
		st.push(maze.entrance);
		
		//Add mark entrance cell as visitied
		setVisitedStatus(maze, maze.entrance, visited);
		
		//While there are still cells with unvisited neighbours on the stack, 
		//search through the maze
		while(st.empty() == false)
		{
			//Get the current cell from the stack, without removing it
			currCell = st.peek();
			
			//Check if the current cell is an entrance to a tunnel
			if(currCell.tunnelTo != null)
			{
				//Set current cell to the other end of the tunnel
				currCell = currCell.tunnelTo;
				
				//If the cell is a tunnel, immediately go to other end of the 
				//tunnel
				st.push(currCell);
				
				//Set the cell at end of the tunnel to visited
				setVisitedStatus(maze, currCell, visited);
			}
			
			//Check if cell on top of the stack has any unvisitied neighbours
			if(check_for_neighbours(currCell, maze, visited))
			{	
				//Get a random direction that has a cell that hasn't already 
				//been visited
				r = get_rand_direction(maze, currCell, visited);	
				
				//Set nc to the randomly selected neighbour cell
				nc = currCell.neigh[r];
				
				//Remove wall in direction of randomly selected neighbour cell
				currCell.wall[r].present = false;
				
				//Update the current cell in the maze
				maze.map[currCell.r][currCell.c] = currCell;			
				
				//Push neighbour onto the stack
				st.push(nc);
				
				//Mark neighbour as visited
				setVisitedStatus(maze, nc, visited);
			}
			//If no unvisited neighbours, pop current cell from stack to 
			//backtack in maze
			else
			{
				st.pop();
				
				//If the stack is not currently empty
				if(st.isEmpty() == false)
				{
					//If the next element is a tunnel, remove it from stack
					if(st.peek().tunnelTo != null)
					{
						st.pop();
					}
				}
			}			
		}
		
		return;
	} // end of generateMaze()
	
		
	//Method to generate a normal rectangular maze with tunnels
	public void generateTunnelMaze(Maze maze) {
			
	} //end of generateTunnelMaze()
		
	//Sets the visited status of the cell based on the type of maze
	public boolean[][] setVisitedStatus(Maze maze, Cell cell, boolean[][] visited)
	{
		//If the maze is hexagonal
		if(maze.type == Maze.HEX)
		{
			visited[cell.r][cell.c - (cell.r + 1) / 2] = true;
		}
		//If the maze is rectangular
		else
		{
			visited[cell.r][cell.c] = true;
		}
		
		return visited;
	} //end of setVisitedStatus()
	
	//Returns a random direction to move in based on maze type, and if the 
	//randomly selected cell has already been visited or not
	public int get_rand_direction(Maze maze, Cell cell, boolean[][] visited)
	{
		//Variable to store randomly chosen direction
		int dir = 0;		
		//Variable to store neighbour cell selected
		Cell nc = null;
		//Flag to indicate if the neighbouring cell is visited or not
		boolean unvisited = false;
		
		//While the randomly selected cell has been marked as visited, 
		//keep randomly picking cells
		while(!unvisited)
		{
			//generate a random int to pick a random cell
			dir = get_rand_int(maze);
			
			//Pick a random neighbour cell
			nc = cell.neigh[dir];
			
			//If the neighbouring cell isn't present
			if(nc == null)
			{
				continue;
			}
			
			//If maze is a hexagonal maze
			if(maze.type == Maze.HEX)
			{
				if(visited[nc.r][nc.c - (nc.r + 1) / 2] == false)
				{
					unvisited = true;
				}
			}
			
			//If the maze is a rectangular maze
			else
			{
				//If the selected cell has not been visited, move to the 
				//cell	
				if(visited[nc.r][nc.c] == false)
				{
					unvisited = true;
				}
			}								
		}
		
		return dir;
	}
	
	public boolean check_for_neighbours(Cell cell, Maze maze, boolean[][] visited)
	{
		//Neighbouring cell
		Cell nc = null;
		
		//If the maze is a hex maze
		if(maze.type == Maze.HEX)
		{
			for(int i = 0; i < cell.neigh.length; i++)
			{
				nc = cell.neigh[i];
				
				//If the currently selected neighbouring cell is marked as null, 
				//continue checking the other directions
				if(nc == null)
				{
					continue;
				}
				
				if(visited[nc.r][nc.c - (nc.r + 1) / 2] == false)
				{
					return true;
				}
			}
		}
		//If the maze is a rectangular maze
		else
		{
			//Create an array to hold the directions of a rectangular maze
			int directions[] = new int[4]; 
			
			//Fill directions array with direction values
			directions[0] = Maze.EAST;
			directions[1] = Maze.NORTH;
			directions[2] = Maze.WEST;
			directions[3] = Maze.SOUTH;
			
			//Check all directions to see if the neighbouring cell has been visited
			for(int i = 0; i < directions.length; i++)
			{
				nc = cell.neigh[i];
				
				//If the currently selected neighbouring cell is marked as null, 
				//continue checking the other directions
				if(nc == null)
				{
					continue;
				}			
				
				//If the neighbouring cell has been unvisited, return true
				if(visited[nc.r][nc.c] == false)
				{
					return true;
				}
							
			}			
		}
		//If all neighbouring cells have been visited, return false
		return false;
	} //end of check_for_neighbours()
	
	//Method to get a random direction to move to a neighbouring cell
	public int get_rand_int(Maze maze)
	{
		//Variable to store random int
		int r = 0;
		//Create random number generator
		Random randomGenerator = new Random();
		
		//Create an array to hold the directions of a rectangular maze
		int directions[] = new int[4]; 
		
		//Fill directions array with direction values
		directions[0] = Maze.EAST;
		directions[1] = Maze.NORTH;
		directions[2] = Maze.WEST;
		directions[3] = Maze.SOUTH;
		
		//If the maze is a hexagonal maze
		if(maze.type == Maze.HEX)
		{
			//Randomly generate int in range 0-5 matching possible directions
			r = randomGenerator.nextInt(6);
		}
		//If the maze is a rectangular maze
		else
		{
			//Generate a random int in range 0 - directions length matching 
			//possible rectangular maze directions
			r = randomGenerator.nextInt(directions.length);
			//Assign r the direction matching the generated int
			r = directions[r];
		}
		
		return r;
	} //end of get_rand_int()

} // end of class RecursiveBacktrackerGenerator
