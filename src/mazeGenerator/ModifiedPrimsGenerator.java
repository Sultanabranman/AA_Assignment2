package mazeGenerator;

import java.util.ArrayList;
import java.util.Random;

import maze.Cell;
import maze.Maze;

public class ModifiedPrimsGenerator implements MazeGenerator {

	//Create an array to hold the directions of a rectangular maze
	int directions[] = new int[4]; 	
	
	@Override
	public void generateMaze(Maze maze) {
		//Set to store all starting cells
		ArrayList<Cell> Z = new ArrayList<Cell>();		
		//Frontier set to store all available neighbours.
		ArrayList<Cell> F = new ArrayList<Cell>();		
		//Two dimensional array to store visited status for each cell
		boolean visited[][] = new boolean[maze.sizeR][maze.sizeC];
		
		//If the passed in maze isn't a normal or hex maze
		if(maze.type != Maze.NORMAL && maze.type != Maze.HEX)
		{
			System.err.println("Error: Invalid maze type for selected generation method");
			return;
		}
		
		//Fill directions array with direction values for rectangular cell
		directions[0] = Maze.EAST;
		directions[1] = Maze.NORTH;
		directions[2] = Maze.WEST;
		directions[3] = Maze.SOUTH;
		
		//Initialise visited array elements to false
		for(int i = 0; i < visited[0].length; i++)
			for(int j = 0; j < visited[1].length; j++)
			{
				visited[i][j] = false;
			}
		
		//Add maze starting cell to Set Z
		Z.add(maze.entrance);
		
		//Mark starting cell as visited
		setVisitedStatus(maze, maze.entrance, visited);	
		
		//Add all neighbours of Set Z to frontier set (F)
		addNeighbours(maze, maze.entrance, Z, F, visited);
		
		//While there are unexplored cells in the frontier set, keep generating maze
		while(F.size() > 0)
		{
			//Randomly select cell from frontier set
			Cell fc = selectFrontierCell(F);
			
			//Remove randomly selected cell from frontier set
			F.remove(fc);
			
			//Get all neighbours of selected frontier set cell that are currently
			//in set Z
			ArrayList<Cell> neighbours = getCellNeighbours(fc, Z);
			
			//Randomly select a retrieved neighbour cell from Z
			Cell nc = selectNeighbour(neighbours);
			
			//Remove walls between cell selected from z and cell selected from F
			remove_walls(fc, nc);
			
			//Add cell selected from frontier set to set Z
			Z.add(fc);
			
			//Mark selected cell as visited
			setVisitedStatus(maze, fc, visited);
			
			//Add neighbour cells of selected cell to set F
			addNeighbours(maze, fc, Z, F, visited);
			
			//Update maze
			update_maze(maze, fc, nc);
		}
		
		
	} // end of generateMaze()
	
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
	
	//Add all possible neighbours of passed in cell to the Frontier set if they 
	//have not been visited
	public void addNeighbours(Maze maze, Cell cell, ArrayList<Cell> Z, 
			ArrayList<Cell> F, boolean[][] visited)
	{
		//Current neighbouring cell
		Cell nc = null;
		
		//If maze is a hexagonal maze
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
				
				//Check to see if the neighbouring cell has already been visited
				//and if it is already in the frontier set
				if(visited[nc.r][nc.c - (nc.r + 1) / 2] == false && 
						F.contains(nc) == false)
				{
					//If the cell isn't visited and isn't already in the 
					//frontier set, add it to the frontier set
					F.add(nc);
				}
			}
		}
		else
		{
			//Check the neighbouring cells in all directions for the passed in 
			//cell. Add the neighbour if it isn't already in the frontier set and 
			//hasn't been visited
			for(int i = 0; i < directions.length; i++)
			{
				nc = cell.neigh[i];
				
				//If there is no neighbouring cell in the direction, check next 
				//direction
				if(nc == null)
				{
					continue;
				}
				//Check to see if the neighbouring cell has already been visited
				//and if it is already in the frontier set
				if(visited[nc.r][nc.c] == false && F.contains(nc) == false)
				{
					//If the cell isn't visited and isn't already in the 
					//frontier set, add it to the frontier set
					F.add(nc);
				}
			}			
		}
		
		return;
	} //end of addNeighbours()
	
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
	}
	
	//Gets a random int to select a cell from the frontier set at the randomly 
	//selected index
	public Cell selectFrontierCell(ArrayList<Cell> F)
	{
		Cell cell = null;
		
		//Get a random int int range 0-F.size() and get the cell in that index
		cell = F.get(getRandInt(F.size()));
		
		//Return the randomly selected cell
		return cell;
	} //end if selectFrontierCell()
	
	//Gets all neighbours of passed in cell that are stored in set Z and returns 
	//the result as an array list
	public ArrayList<Cell> getCellNeighbours(Cell cell, ArrayList<Cell> Z)
	{
		//List of all neighbours of passed in cell that are in set Z
		ArrayList<Cell> neighbours = new ArrayList<Cell>();
		
		//Check all neighbours of the cell to see if it is in set Z. If it is, 
		//add it to neighbours list
		for(int i = 0; i < cell.neigh.length; i++)
		{
			//Get the neighbour cell in direction i
			Cell nc = cell.neigh[i];
			
			//If the neighbour cell doesn't exist, check the next direction
			if(nc == null)
			{
				continue;
			}
			
			//If set Z contains the neighbour cell, add neighbour cell to list
			if(Z.contains(nc))
			{
				neighbours.add(nc);
			}
		}
		
		//Return the list of all neighbours
		return neighbours;
	} //end of getCellNeighbours()
	
	//Get a randomly generated int in range 0 - neighbours.size() and return 
	//cell at index of random int
	public Cell selectNeighbour(ArrayList<Cell >neighbours)
	{
		//Cell to be returned
		Cell cell = null;
			
		//Get a random int int range 0-neighbours.size() and get the cell in 
		//that index
		cell = neighbours.get(getRandInt(neighbours.size()));
		
		//Return randomly selected cell
		return cell;
	} //end of selectNeighbours()
	
	//Remove the walls separating fc and nc
	public void remove_walls(Cell fc, Cell nc)
	{
		//Remove wall in direction of nc for cell fc. 
		for(int i = 0; i < fc.neigh.length; i++)
		{
			//If the neighbour is not present, don't check that direction
			if(fc.neigh[i] == null)
			{
				continue;
			}
			//If the neighbour is in direction i is nc, remove wall in that direction
			if(fc.neigh[i] == nc)
			{
				//remove the wall in direction i
				fc.wall[i].present = false;
			}
		}
		
		//Remove wall in direction of fc for cell nc. 
		for(int i = 0; i < nc.neigh.length; i++)
		{
			//If the neighbour is not present, don't check that direction
			if(nc.neigh[i] == null)
			{
				continue;
			}
			//If the neighbour in direction i is nc, remove wall in that direction
			if(nc.neigh[i] == fc)
			{
				//remove the wall in direction i
				nc.wall[i].present = false;
			}
		}
		
		return;
	} //end of remove_walls()
	
	//Update maze with updated information on cells fc and nc
	public void update_maze(Maze maze, Cell fc, Cell nc)
	{
		//Update cells in positions fc and nc
		maze.map[fc.r][fc.c] = fc;
		maze.map[nc.r][nc.c] = nc;
		
		return;
	}
	
	
	
} // end of class ModifiedPrimsGenerator
