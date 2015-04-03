import java.util.ArrayList;
import java.util.List;

import net.datastructures.*;

/**
 * Creates an adjacency representation of a graph that can have mixed edges
 * (either directed or undirected). Uses decorator pattern to mark edges as
 * directed.
 * 
 * @author Scot Drysdale
 *
 * @param <V> the type of the vertex label. (All must be unique.)
 * @param <E> the type of the edge label.
 */
public class DirectedAdjListMap<V, E> extends AdjacencyListGraphMap<V, E> {

    private final static Object EDGE_TYPE = new Object();		
    private final static Object DIRECTED = new Object();

    /**
     * Is the given edge directed?
     * 
     * @param e the edge to test
     * @return true if e is directed
     */
    public boolean isDirected(Edge<E> e){
    	return e.get(EDGE_TYPE) == DIRECTED;	// == compares object references, it checks to see if the two operands 
    											// point to the same object (not equivalent objects, the same object).
    }
    
    /**
     * Insert a directed edge into this graph
     * 
     * @param v - the source vertex
     * @param w - the destination vertex
     * @param label  - the edge label
     * @return the new edge
     */
    public Edge<E> insertDirectedEdge(Vertex<V> v, Vertex<V> w, E label){
    	Edge<E> insertDEdge = insertEdge(v, w, label);	//make an edge
    	insertDEdge.put(EDGE_TYPE, DIRECTED); 		// mark this edge as directed
    	return insertDEdge;
    }
    	
    /**
     * Insert a directed edge into this graph
     * 
     * @param v - the source vertex identifier
     * @param w - the destination vertex identifier
     * @param label - the edge label
     * @return the new edge
     */
    public Edge<E> insertDirectedEdge(V v, V w, E label){
        return insertDirectedEdge(getVertex(v), getVertex(w), label);
    }

    /**
     * Get all incident edges with v as destination
     * 
     * @param v the destination vertex
     * @return collection of incident edges with v as destination
     */
    public Iterable<Edge<E>> incidentEdgesIn(Vertex<V> v){

    	ArrayList<Edge<E>> incomingDirected = new ArrayList<Edge<E>>();  // make a list of directed edges coming to me
    	
    	Iterable<Edge<E>> incomingIncidentEdges = super.incidentEdges(v); 		
    	for (Edge<E> edge: incomingIncidentEdges) {
        	if (!this.isDirected(edge)) {		// if undirected    i.e. if edge.get(EDGE_TYPE) != DIRECTED
        		incomingDirected.add(edge);
        	}
        	else{	// for those directed ones
        		if (v == endVertices(edge)[1]){	// array[1] is the destination in endVertices method
        			incomingDirected.add(edge); // equal sign --> vertex v and the vertex in the directed graph are the same object, i.e. same pointers between references
        		}	
        	}
    	}
    	return incomingDirected;
    }
    /**
     * Get all incident edges with v as destination
     * 
     * @param v the destination vertex identifier
     * @return collection of incident edges with v as destination
     */
    public Iterable<Edge<E>> incidentEdgesIn(V v){
    	return incidentEdgesIn(getVertex(v));
    }
     
    /**
     * Get all incident edges with v as source
     * 
     * @param v the source vertex
     * @return collection of incident edges with v as source
     */
    public Iterable<Edge<E>> incidentEdgesOut(Vertex<V> v){
    	
    	ArrayList<Edge<E>> outgoingDirected = new ArrayList<Edge<E>>();  // make a list of directed edges coming to me
	
    	Iterable<Edge<E>> outgoingIncidentEdges = super.incidentEdges(v); 		 
    	for (Edge<E> edge: outgoingIncidentEdges) {	
    		if (!this.isDirected(edge)) {		// if undirected, we want to add it for sure since it's both ways i.e will be incident
    		outgoingDirected.add(edge);
    	}
    		else{	// for those directed ones
    			if (v == endVertices(edge)[0]){ // 0th element of the array is the source
    			outgoingDirected.add(edge); // equal sign --> vertex v and the vertex in the directed graph are the same object, i.e. same pointers between references
    			}	
    		}
    	}
	return outgoingDirected;
    }

    /**
     * Get all incident edges with v as source
     * 
     * @param v the source vertex identifier
     * @return collection of incident edges with v as source
     */
    public Iterable<Edge<E>> incidentEdgesOut(V v){
    	return incidentEdgesOut(getVertex(v));
    }
    
    /**
     * Get the in degree of a vertex
     * 
     * @param v the vertex
     * @return the in degree of v
     */
    public int inDegree(Vertex<V> v){
    	return ((ArrayList<Edge<E>>) incidentEdgesIn(v)) .size();
    }

    /**
     * Get the in degree of a vertex
     * 
     * @param v the vertex identifier
     * @return the in degree of v
     */
    public int inDegree(V v){
		return inDegree(getVertex(v));
    }
  
    /**
     * Get the out degree of a vertex
     * 
     * @param v the vertex
     * @return the out degree of v
     */
    public int outDegree(Vertex<V> v){
    	return ((ArrayList<Edge<E>>) incidentEdgesOut(v)) .size();
    }
    
    /**
     * Get the out degree of a vertex
     * 
     * @param v the vertex identifier
     * @return the out degree of v
     */
    public int outDegree(V v){
    	return outDegree(getVertex(v));
    }
    
    


    /**
     * Test program
     */
    public static void main(String[] args) {
       
    	DirectedAdjListMap<String, String> baconGraph = new DirectedAdjListMap<String, String>();

        baconGraph.insertVertex("Kevin Bacon");
        baconGraph.insertVertex("Laura Linney");
        baconGraph.insertVertex("Tom Hanks");
        baconGraph.insertVertex("Liam Neeson");
        baconGraph.insertDirectedEdge("Laura Linney", "Kevin Bacon", "Mystic River");
        baconGraph.insertEdge("Liam Neeson", "Laura Linney", "Kinsey");
        baconGraph.insertDirectedEdge("Tom Hanks", "Kevin Bacon", "Apollo 13");

        System.out.println("\nDegree of Laura Linney = "
                + baconGraph.degree("Laura Linney"));

        System.out.println("\nInDegree of Laura Linney = "
                + baconGraph.inDegree("Laura Linney"));

        System.out.println("\nOutDegree of Laura Linney = "
                + baconGraph.outDegree("Laura Linney"));

        System.out.println("\nEdges into to Laura Linney:");
        for (Edge<String> edge : baconGraph.incidentEdgesIn("Laura Linney"))
            System.out.println(edge);

        System.out.println("\nEdges out of to Laura Linney:");
        for (Edge<String> edge : baconGraph.incidentEdgesOut("Laura Linney"))
            System.out.println(edge);

        System.out.println("The entire graph:");
        for (Vertex<String> vertex : baconGraph.vertices()) {
            System.out.println("\nEdges adjacent to " + vertex + ":");
            for (Edge<String> edge : baconGraph.incidentEdges(vertex))
                System.out.println(edge);

            System.out.println("\nEdges into " + vertex + ":");
            for (Edge<String> edge : baconGraph.incidentEdgesIn(vertex))
                System.out.println(edge);

            System.out.println("\nEdges out of " + vertex + ":");
            for (Edge<String> edge : baconGraph.incidentEdgesOut(vertex))
                System.out.println(edge);
        }

        System.out.println("\nRenaming Laura Linney to L. Linney");
        baconGraph.replace("Laura Linney", "L. Linney");
        System.out.println("\nGetting Laura Linney: "
                + baconGraph.getVertex("Laura Linney"));

        for (Vertex<String> vertex : baconGraph.vertices()) {
            System.out.println("\nEdges adjacent to " + vertex + ":");
            for (Edge<String> edge : baconGraph.incidentEdges(vertex))
                System.out.println(edge);
        }
        System.out.println("\nRemoving L. Linney");
        baconGraph.removeVertex("L. Linney");

        System.out.println("\nThe entire graph:");
        for (Vertex<String> vertex : baconGraph.vertices()) {
            System.out.println("\nEdges adjacent to " + vertex + ":");
            for (Edge<String> edge : baconGraph.incidentEdges(vertex))
                System.out.println(edge);

            System.out.println("\nEdges into " + vertex + ":");
            for (Edge<String> edge : baconGraph.incidentEdgesIn(vertex))
                System.out.println(edge);

            System.out.println("\nEdges out of " + vertex + ":");
            for (Edge<String> edge : baconGraph.incidentEdgesOut(vertex))
                System.out.println(edge);
        }
    }
}
