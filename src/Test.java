import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Driver Class
 * @author Denizhan Soydas
 * @version 1.0
 * @since 2022-03-27
 */
public class Test {
	//our map
	static char[][] myMap;
	//our map's shadow to keep track of the places we visited before.
	static boolean[][] visited;
	//our paths to reach the treasures.
	static ArrayList<String> paths;
	
	public static void main(String[] args) {
		//Scanners for console and file
		Scanner scanConsole = new Scanner(System.in);
		String fileName = scanConsole.nextLine();
		
		//ArrayList of rows
		ArrayList<String> rows = new ArrayList<String>();

		//Read the map line by line
		Scanner scan = null;
		File f = null;
		f = new File(fileName);
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			rows.add(line);
		}
		//Read the dimensions
		int n = rows.get(0).length();
		int m = rows.size();

		//Transfer the map to a more understandable structure
		myMap = new char[m][n];
		visited = new boolean[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				myMap[i][j] = rows.get(i).charAt(j);
			}
		}
		// to initialize visited map as unvisited
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				visited[i][j] = false;
			}
		}
		// to see our map
//		for(int i = 0; i < m; i++) {
//			for(int j = 0; j < n; j++) {
//				System.out.print(myMap[i][j]);
//			}
//			System.out.println();
//		}
		// to see which indexes are visited before
//		for (int i = 0; i < m; i++) {
//			for (int j = 0; j < n; j++) {
//				System.out.print(visited[i][j] + " ");
//			}
//			System.out.println();
//		}

		//ArrayList of paths
		paths = new ArrayList<String>();

		//run the maze tracer method 
		traceMaze(2, 1, 0, null);
		
		//sort the paths we obtained
		ArrayList<String> paths_sorted = new ArrayList<String>();
		while(!paths.isEmpty()) {
			int min_index = 0;
			for(int j = 0; j < paths.size(); j++) {
				if(paths.get(j).length() < paths.get(min_index).length())
					min_index = j;
			}
			paths_sorted.add(paths.get(min_index));
			paths.remove(min_index);
		}
		
		//print the sorted paths
		System.out.println(paths_sorted.size() + " treasures are found.");
		if(paths_sorted.size() > 0) {
			System.out.println("Paths are:");
			for (int i = 0; i < paths_sorted.size(); i++)
				System.out.println((i + 1) + ")" + paths_sorted.get(i));
		}
	}
	//recursive function that traces all the maze and obtains the possible paths
	static void traceMaze(int resDirection, int row, int column, Node source) {
		//in order to not go back, we point the place we come from. i.e. we prevent infinite loops like down,up,down,up,down,up,........
		int dontGo = -1;
		if (resDirection == 1) {
			dontGo = 3;
		} 
		else if (resDirection == 2) {
			dontGo = 4;
		} 
		else if (resDirection == 3) {
			dontGo = 1;
		} 
		else if (resDirection == 4) {
			dontGo = 2;
		}
		//if we come before, we do not do anything because there may be cycles in the maze.
		if (visited[row][column])
			return;
		//we point the current index in order not to visit again.
		visited[row][column] = true;
		//if we reached to a treasure, we add the path we came from to our paths array list.
		if (myMap[row][column] == 'E') {
			String str = "E";
			Node n = source;
			for (; n.source != null; n = n.source)
				str += n.data;
			str += n.data;

			StringBuffer sb = new StringBuffer(str);
			sb.reverse();
			paths.add(sb.toString());
		} 
		//if we are in a regular way, we recurse our function for our neighbors.
		else if ((int) myMap[row][column] >= 97 && myMap[row][column] <= 122) {
			//we create a node that points to the previous node. Therefore we will not lose the path that we came from.
			Node n = new Node(myMap[row][column], source);
			//we go to our neighbors except the one we came from.
			if (dontGo == 1) {
				traceMaze(2, row, column + 1, n);
				traceMaze(3, row + 1, column, n);
				traceMaze(4, row, column - 1, n);
			} 
			else if (dontGo == 2) {
				traceMaze(1, row - 1, column, n);
				traceMaze(3, row + 1, column, n);
				traceMaze(4, row, column - 1, n);
			} 
			else if (dontGo == 3) {
				traceMaze(1, row - 1, column, n);
				traceMaze(2, row, column + 1, n);
				traceMaze(4, row, column - 1, n);
			} 
			else if (dontGo == 4) {
				traceMaze(1, row - 1, column, n);
				traceMaze(2, row, column + 1, n);
				traceMaze(3, row + 1, column, n);
			} 
			else {
				System.out.println("error: wrong direction");
			}
		}
		//if we are in a wall, we cut the way there and do not go further.
		else if (myMap[row][column] == '|' || myMap[row][column] == '+' || myMap[row][column] == '-') {
			return;
		} 
		else {
			System.out.println("error: map character");
		}
		

	}

}
