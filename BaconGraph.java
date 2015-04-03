import java.io.*;
import java.util.*;

import net.datastructures.Edge;
import net.datastructures.Vertex;


public class BaconGraph {
	private static Map<String, String> actorMap;
	private static Map<String, String> movieMap;
	private static Map<String, ArrayList<String>> movieActorMap;
	
	/*
	 *
	until Q is empty
  		dequeue Q to get next vertex v to process
    		for each edge e that is incident to v in G
      			let v' be the other end of the edge
      			if v' is not in T
        			add v' to T and add an edge with the same label as e from v' to v in T
        			enqueue v' in Q
	return T
	 */
	private static DirectedAdjListMap<String, String> bfsTree(String root, DirectedAdjListMap<String,String> undGraph ) {
		
		//  insert root into an empty queue Q and into a new directed graph T
		Queue<Vertex<String>> bfsQueue = new ArrayDeque<Vertex<String>>();  // ArrayDeque VS ArrayList??
		DirectedAdjListMap<String,String> newDGraph = new DirectedAdjListMap<String,String>();
		
		bfsQueue.add(undGraph.getVertex(root)); // add root to queue. Note: getVertex(value) returns the vertex associated with the vertex value
		newDGraph.insertVertex(root);	// add root to the new directed graph T
		
		while (!bfsQueue.isEmpty()){
			Vertex<String> v = bfsQueue.poll();
			for (Edge<String> edge : undGraph.incidentEdges(v) ){
				Vertex<String> oppositeV =  undGraph.opposite(v, edge);
				if (!newDGraph.vertexInGraph(oppositeV.element()) ) {		// ?
					newDGraph.insertVertex(oppositeV.element());
					newDGraph.insertDirectedEdge(newDGraph.getVertex(oppositeV.element()), newDGraph.getVertex(v.element()), edge.element());		// get oppv vertex from newDGraph and not undGraph
					bfsQueue.add(oppositeV);
				}
			}
		}	
		return newDGraph;
	}
	
	// actors hashmap mapping actor IDs to actor names
	private static Map<String, String> ActorMap(String file) throws IOException{			
		actorMap = new HashMap<String, String>();  

		BufferedReader in = new BufferedReader(new FileReader(file));
		String line;	 
		while ((line = in.readLine()) != null){ 	
			String[] token = line.split("\\|");
			actorMap.put(token[0], token[1]); 	// key is the actor ID, value is actor's name 			
		}
		in.close();
		return actorMap;	
	}
		
	// movies hashmap mapping movie IDs to movie names
	private static Map<String, String> MovieMap(String file) throws IOException{			
		movieMap = new HashMap<String, String>(); 	 

		BufferedReader in = new BufferedReader(new FileReader(file));
		String line;	 
		while ((line = in.readLine()) != null){ 	
			String[] token = line.split("\\|");
			movieMap.put(token[0], token[1]); 	// key is the actor ID, value is actor's name 			
		}
		in.close();
		return movieMap;	
	}
	// Create an arraylist for movie-actors. You can also use a map to figure out which actors appeared in each movie,   
	// and can use that information to add the appropriate edges to the graph.
	private static Map<String, ArrayList<String>> MovieActorMap (String file) throws IOException{			
		movieActorMap = new HashMap<String, ArrayList<String>>(); 	 

		BufferedReader in = new BufferedReader(new FileReader(file));
		String line;	 
		while ((line = in.readLine()) != null){ 	
			String[] token = line.split("\\|");			
			// if the movie is not yet in the arraylist, add it to the list
			if (!movieActorMap.containsKey(token[0])){
				ArrayList<String> list = new ArrayList<String>();
				list.add(token[1]);
				movieActorMap.put(token[0], list);
			}
			else {
				movieActorMap.get(token[0]).add(token[1]); 	
			}
		}
		in.close();
		return movieActorMap;	
	}
	
	// create an undirected graph from the map
	private static AdjacencyListGraphMap<String, String> createGraph() {
		 
		AdjacencyListGraphMap<String, String> baconGraph = new  AdjacencyListGraphMap<String, String>(); 
		
		Set<String> actors = actorMap.keySet();
		for (String key : actorMap.keySet()){		// Use KeySet to loop through keys (actor IDs) 
			String actorName = actorMap.get(key);
			baconGraph.insertVertex(actorName);
		}
		// loop through movie-actors test map
		Set<String> movies = movieActorMap.keySet();	
		for (String key : movieActorMap.keySet()){	// Use KeySet to loop through keys (movie IDs)
			
			String movieName = movieMap.get(key);	// get value (movie name)
			ArrayList<String> actorList = movieActorMap.get(key);  // get arraylist of actor IDs
			
			for (int i = 0; i < actorList.size() - 1; i++) {	
				String actorName1 = actorMap.get(actorList.get(i));
				String actorName2 = actorMap.get(actorList.get(i+1));
				baconGraph.insertEdge(actorName1, actorName2, movieName);
			}
			
		}
		return baconGraph;
	}
	
	public static void traverseBacon(String v, DirectedAdjListMap<String, String> graph){
		// Get the typed in actor
		Vertex<String> chooseActor = graph.getVertex(v);
		
		// If the actor is not found in the graph, then the actor's kevin bacon number is infinite.
		if(chooseActor == null){
			System.out.println(v + " has no connections with Kevin Bacon.\n");
			return;
		}
		
		int baconNumber = 0;
		String connections = "";
		String kBNResult = chooseActor + "'s Kevin Bacon number is ";
		
		// Address of Kevin Bacon  
		Vertex<String> kevinBacon = graph.getVertex("Kevin Bacon");
		
		// Loop through the graph until we reach Kevin Bacon
		while(!(chooseActor == kevinBacon)){		
			Edge<String> shareMovie = graph.incidentEdgesOut(chooseActor).iterator().next();				// Get an edge i.e. movie and follow the path.
			Vertex<String> otherActor = graph.endVertices(shareMovie)[1];									// Get the other actor in the movie
			connections += chooseActor + " appeared in " + shareMovie + " with " + otherActor + ".\n";	// Add the current relation between the current and other actors and the movie shared.
			chooseActor = otherActor;																		// Traverse through the directed graph, the other actor should become the current for the next search.
			baconNumber++;																					// Increment Kevin Bacon Number 
		}
		// Print the resulting Kevin Bacon number and connection of the chosen actor
		System.out.println(kBNResult + baconNumber + "\n" + connections);	
	}
	
	
	private static Scanner userInput = new Scanner(System.in);
		
	public static void main(String[] args) {
		try {
//			System.out.println(ActorMap("inputs/actorsTest.txt"));
//			System.out.println(MovieMap("inputs/moviesTest.txt"));
//			System.out.println(MovieActorMap("inputs/movie-actorsTest.txt"));
//			System.out.println(createGraph());
			
			Map<String, String> map1 = ActorMap("inputs/actors.txt");
			Map<String, String> map2 = MovieMap("inputs/movies.txt");
			Map<String, ArrayList<String>> map3 = MovieActorMap("inputs/movie-actors.txt");
			DirectedAdjListMap<String, String> theBaconGraph = new DirectedAdjListMap<String, String>();
			
			for (String movie:map3.keySet()){
				ArrayList<String> actors = map3.get(movie);
				for (String actor : actors){
					if (!theBaconGraph.vertexInGraph(map1.get(actor))){
						theBaconGraph.insertVertex(map1.get(actor));
					}
				}
				String movieName = map2.get(movie);
				for (int i = 0; i < actors.size(); i++){
					for (int j = i+1; j<actors.size(); j++){
						theBaconGraph.insertEdge(map1.get(actors.get(i)), map1.get(actors.get(j)), movieName);
					}
				}
			}
					
			DirectedAdjListMap<String, String> kevinBaconGameGraph = bfsTree("Kevin Bacon", theBaconGraph);
			
			String command = "";				// Hold a name of an actor to search or quit
			
			System.out.print("Enter the name of an actor or type \"Q\" to exit: ");	
			command = userInput.nextLine();													 
			
			// Loop to keep prompting user 
			while(!command.equalsIgnoreCase("Q")){	
				traverseBacon(command, kevinBaconGameGraph);		// Find the current actor's Kevin Bacon Number
				System.out.print("Enter the name of an actor: ");		
				command = userInput.nextLine();								
			}
			
			
		}catch(IOException e){
			System.err.println("Trouble reading this file!");
			e.printStackTrace(); //finding type of exception from println, printStackTrace tells the cause (line numbers + call stack)
		}
		
	}
	
}
