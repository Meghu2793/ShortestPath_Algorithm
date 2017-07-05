/*Megha Krishnamurthy 
  STudent ID: 800974844 */
package com.java.shortestpath;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.List;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import com.java.shortestpath.*;

public class Graph{
	public static final int INFINITY = Integer.MAX_VALUE;
	private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
	StringBuilder fw = new StringBuilder();
	static MinHeap heap;
	VertexCompare vertexcomparator = new VertexCompare();
	
	public void addEdge(String sourceName, String destName, String edgeWeight) {
		Vertex v = getVertex(sourceName);
		Vertex w = getVertex(destName);
		Edge e = new Edge(Float.parseFloat(edgeWeight));
		v.adj.put(w, e);
	}

	public void printPath(String destName) {
		Vertex w = vertexMap.get(destName);
		if (w == null)
			throw new NoSuchElementException("Destination vertex not found");
		else if (w.dist == INFINITY)
			System.out.println(destName + " is unreachable");
		else {
			System.out.print("(Distance is: " + w.dist + ") ");
			fw.append("(Distance is: " + w.dist + ") ");
			printPath(w);
			System.out.println();
		}
	}

	private void vertexup(String nextToken) {
		Vertex v = getVertex(nextToken);
		if(!v.isUp){
			v.isUp = true;
		}
	}
	private Vertex getVertex(String vertexName) {
		Vertex v = vertexMap.get(vertexName);
		if (v == null) {
			v = new Vertex(vertexName);
			vertexMap.put(vertexName, v);
		}
		return v;
	}

	private void printPath(Vertex dest) {
		if (dest.prev != null) {
			printPath(dest.prev);
			System.out.print(" ");
			fw.append(" ");
			
		}

		System.out.print(dest.name);
		fw.append(dest.name);
		fw.append(" ");
	}

	private void clearAll() {
		for (Vertex v : vertexMap.values())
			v.reset();
	}
	public List<Vertex> unweighted(String startName) {
		clearAll();

		Vertex start = vertexMap.get(startName);
		if (start == null)
			throw new NoSuchElementException("Start vertex not found");

		Queue<Vertex> q = new LinkedList<Vertex>();
		List<Vertex> discoveredVertices = new ArrayList<Vertex>();
		q.add(start);
		start.dist = 0;

		while (!q.isEmpty()) {
			Vertex v = q.remove();
			for (Map.Entry<Vertex, Edge> w : v.adj.entrySet()) {
				if (w.getKey().dist == INFINITY) {
					w.getKey().dist = v.dist + 1;
					w.getKey().prev = v;
					discoveredVertices.add(w.getKey());
					q.add(w.getKey());
				}
			}
		}

		return discoveredVertices;
	}

	public void alterEdge(StringTokenizer st, boolean isUp) {
		String source = st.nextToken(), dest = st.nextToken();
		if (getVertex((source)) != null && getVertex(dest) != null) {
			if (getVertex((source)).adj.keySet().contains(getVertex(dest))) {
				getVertex((source)).adj.get(getVertex(dest)).isUp = isUp;
			} else {
				System.out.println("The provided destination vertex is not an adjacent vertex of the source vertex");
			}

		} else {
			System.out.println("The entered source or destination vertex name is not valid, kindly check again!");
		}
	}

	private void vertexdown(String nextToken) {
		Vertex v = getVertex(nextToken);
		if(v.isUp){
			v.isUp = false;
		}
	}

	private void deleteedge(String nextToken, String nextToken2) {
		if (getVertex(nextToken) != null && getVertex(nextToken2) != null) {
		Vertex v = getVertex(nextToken);
		Vertex w = getVertex(nextToken2);
		if(v.adj.keySet().contains(w)){
		v.adj.remove(w);
		w.prev = null;
		}
	}
		else {
			System.out.println("The entered source/destination vertex name is invalid");
			fw.append("The entered source/destination vertex name is invalid"+ "\n");
		}
	}

	public List<Vertex> shortestPath(String startName, String destName) {
		clearAll();
		List<Vertex> outputvertices = new ArrayList();
		Vertex source = vertexMap.get(startName);
		if (source == null)
			throw new NoSuchElementException("source vertex not found");
		else if (!source.isUp)
			throw new NoSuchElementException("source vertex is down");
		else {
			source.dist = 0;
			heap.insert(source);
			while (!heap.isEmpty()) {
				Vertex evaluationNode = heap.deleteMin();
				outputvertices.add(evaluationNode);
				for (Map.Entry<Vertex, Edge> adjvertex : evaluationNode.adj.entrySet()) {
					if (!adjvertex.getKey().isUp || !adjvertex.getValue().isUp) {
						continue;
					} else {
						if (adjvertex.getKey().dist > evaluationNode.dist + adjvertex.getValue().weight) {
							int indexValue;
							if ((indexValue = heap.getIndexOf(adjvertex.getKey())) > -1) {
								heap.delete(indexValue);
							}

							adjvertex.getKey().dist = evaluationNode.dist + adjvertex.getValue().weight;
							adjvertex.getKey().prev = evaluationNode;
							heap.insert(adjvertex.getKey());
						}
					}
				}
			}

			return outputvertices;
		}
	}
	
	public static void main(String[] args) {
		Graph g = new Graph();
		try {
			System.out.println(args[0]);
			FileReader fin = new FileReader(args[0]);
			Scanner graphFile = new Scanner(fin);
			String line;
			while (graphFile.hasNextLine()) {
				line = graphFile.nextLine();
				StringTokenizer st = new StringTokenizer(line);
				try {
					if (st.countTokens() != 3) {
						System.err.println("Skipping ill-formatted line " + line);
						continue;
					}
					String source = st.nextToken();
					String dest = st.nextToken();
					String edgeWeight = st.nextToken();
					g.addEdge(source, dest, edgeWeight);
					g.addEdge(dest, source, edgeWeight);
				} catch (NumberFormatException e) {
					System.err.println("Skipping ill-formatted line " + line);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		System.out.println("File read...");
		System.out.println(g.vertexMap.size() + " vertices");
		try{
		FileReader query = new FileReader(args[1]);
		Scanner in = new Scanner(query);
		String line;
		FileWriter output = new FileWriter(args[2]);
		loop: while (in.hasNextLine()) {
			line = in.nextLine();
			StringTokenizer st = new StringTokenizer(line);
			switch (st.nextToken()) {
				case "quit":
					break loop;
				case "print":

					Map<String, Vertex> graphSorted = new TreeMap<String, Vertex>(g.vertexMap);
					for (Map.Entry<String, Vertex> values : graphSorted.entrySet()) {
						System.out.print(values.getKey());
						g.fw.append(values.getKey());
						if (!values.getValue().isUp) {
							System.out.print(" Down");
							g.fw.append(" DOWN");
						}
						System.out.println();
						g.fw.append("\n");
						Map<Vertex,Edge> sortedVertices = new TreeMap<Vertex, Edge>(g.vertexcomparator);
						for (Map.Entry<Vertex, Edge> map : values.getValue().adj.entrySet()) {
							sortedVertices.put(map.getKey(), map.getValue());
						}
						for (Map.Entry<Vertex, Edge> adjVertex : sortedVertices.entrySet()) {
							System.out.print("\t" + adjVertex.getKey().name);
							g.fw.append("\t" + adjVertex.getKey().name);
							System.out.print(" " + adjVertex.getValue().weight);
							g.fw.append(" " + adjVertex.getValue().weight);
							if (!adjVertex.getValue().isUp) {
								System.out.print(" Down");
								g.fw.append(" DOWN");
							}
							System.out.println("");
							g.fw.append("\n");
						}
					}
					
					g.fw.append("\n");
					break;
				case "reachable":

					Map<String, Vertex> sortedMap = new TreeMap<String, Vertex>(g.vertexMap);

					for (Map.Entry<String, Vertex> value : sortedMap.entrySet()) {
						if (value.getValue().isUp) {
						System.out.println(value.getKey());
						g.fw.append(value.getKey() + "\n");
						List<Vertex> reachableVertices = g.unweighted(value.getKey());
						for (Vertex vertex : reachableVertices) {
							System.out.println("\t" + vertex.name);
							g.fw.append("\t" + vertex.name + "\n");
						}
						System.out.println();
					}
					}
					g.fw.append("\n");

					break;
				case "vertexdown":g.vertexdown(st.nextToken());
					break;
				case "vertexup":g.vertexup(st.nextToken());
					break;
				case "deleteedge":g.deleteedge(st.nextToken(), st.nextToken());
					break;
				case "edgedown":
					g.alterEdge(st, false);
					break;
				case "edgeup":
					g.alterEdge(st, true);
					break;
				case "path":
					String source = st.nextToken(), dest = st.nextToken();
					if (g.getVertex((source)) != null && g.getVertex(dest) != null) {
						heap = new MinHeap(g.vertexMap.size());
						List<Vertex> value = g.shortestPath(source, dest);
						Vertex v = null;
						for (Vertex vertex : value) {
							v = vertex;
							if (vertex.name.equals(dest)) {
								break;
							}
						}
						if (v != null) {
							g.printPath(v);
							System.out.println(" " + Math.round(v.dist * 100.0) / 100.0);
							g.fw.append(" " + Math.round(v.dist * 100.0) / 100.0 + "\n\n");
						} else {
							System.out.println("No path from the given source to destination");
							g.fw.append("No path from the given source to destination" + "\n");
						}

					} else {
						System.out.println(
								"The entered source/destination vertex name is invalid");
						g.fw.append("The entered source/destination vertex name is invalid"
										+ "\n");
					}
					break;
				case "addedge": g.addEdge(st.nextToken(), st.nextToken(), st.nextToken());
					break;
				default:
					System.out.println("Invalid input, Please try with valid input");
					break;

		}
	}
		PrintWriter pw = new PrintWriter(output);
		pw.print(g.fw.toString());
		pw.flush();
		pw.close();
		in.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

}
