/*Megha Krishnamurthy 
  STudent ID: 800974844 */

package com.java.shortestpath;

import java.util.HashMap;
import java.util.Map;
import com.java.shortestpath.Edge;

public class Vertex {
	public String name; // Vertex name
	public Map<Vertex,Edge> adj = new HashMap<Vertex,Edge>(); // Adjacent
																// vertices &
																// weight
	public Vertex prev; // Previous vertex on shortest path
	public float dist; // Distance of path
	public boolean isUp = true;
	public Vertex(){}
	public Vertex(String nm) {
		name = nm;
		adj = new HashMap<Vertex,Edge>();
		reset();
	}

	public void reset() {
		dist = Graph.INFINITY;
		prev = null;
	}
}

