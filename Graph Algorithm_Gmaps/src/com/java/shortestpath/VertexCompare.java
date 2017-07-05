/*Megha Krishnamurthy 
  STudent ID: 800974844 */

package com.java.shortestpath;

import java.util.Comparator;
import com.java.shortestpath.*;

public class VertexCompare implements Comparator<Vertex> {
	
	@Override
	public int compare(Vertex o1, Vertex o2) {
		return o1.name.compareTo(o2.name);
	}
}