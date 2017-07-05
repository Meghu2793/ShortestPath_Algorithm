/*Megha Krishnamurthy 
  STudent ID: 800974844 */

package com.java.shortestpath;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import com.java.shortestpath.*;

public class MinHeap {
	private static final int division = 2;
	private int size;
	private int cap;
	private ArrayList<Vertex> VertexHeap;

	public MinHeap(int cap) {
		this.cap = cap;
		size = 0;
		VertexHeap = new ArrayList<>();
		for (int count = 0; count < cap; count++) {
			VertexHeap.add(new Vertex() {
				{
					dist = (float) -1.0;
				}
			});
		}
	}
	
	public void insert(Vertex x) {
		if (isFull())
			throw new NoSuchElementException("Overflow Exception");
		VertexHeap.add(size++, x);

		heapUp(size - 1);
	}

	public Vertex findMin() {
		if (isEmpty())
			throw new NoSuchElementException("Underflow Exception");
		return VertexHeap.get(0);
	}

	public Vertex deleteMin() {
		Vertex keyItem = VertexHeap.get(0);
		size--;
		VertexHeap.remove(0);
		return keyItem;
	}

	public Vertex delete(int ind) {
		if (isEmpty())
			throw new NoSuchElementException("Underflow Exception");
		Vertex keyItem = VertexHeap.get(ind);
		VertexHeap.add(ind, VertexHeap.get(size - 1));
		size--;
		heapDown(ind);
		return keyItem;
	}

	private void heapUp(int childInd) {
		Vertex tmp = VertexHeap.get(childInd);
		while (childInd > 0 && tmp.dist < VertexHeap.get(parent(childInd)).dist) {
			VertexHeap.add(childInd, VertexHeap.get(parent(childInd)));
			childInd = parent(childInd);
		}

		VertexHeap.add(childInd, tmp);
	}

	private void heapDown(int ind) {
		int child;
		Vertex tmp = VertexHeap.get(ind);
		while (kthChild(ind, 1) < size) {
			child = smallChild(ind);
			if (VertexHeap.get(child).dist < tmp.dist)
				VertexHeap.add(ind, VertexHeap.get(child));
			else
				break;
			ind = child;
		}

		VertexHeap.add(ind, tmp);
	}

	private int smallChild(int ind) {
		int bestChild = kthChild(ind, 1);
		int k = 2;
		int pos = kthChild(ind, k);
		while ((k <= division) && (pos < size)) {
			if (VertexHeap.get(pos).dist < VertexHeap.get(bestChild).dist)
				bestChild = pos;
			pos = kthChild(ind, k++);
		}
		return bestChild;
	}

	public int getIndexOf(Vertex v){
		if(VertexHeap.contains(v)){
			return VertexHeap.indexOf(v);
		}
		
		return -1;
	}
	public boolean isEmpty() {
		return size == 0;
	}

	public boolean isFull() {
		return size == VertexHeap.size();
	}
	private int kthChild(int i, int k) {
		return division * i + k;
	}
	private int parent(int i) {
		return (i - 1) / division;
	}

}

