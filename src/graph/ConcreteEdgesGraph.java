/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
	private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //   TODO
    // Representation invariant:
    //   TODO
    // Safety from rep exposure:
    //   TODO
    
    // TODO constructor
    
    // TODO checkRep
    
    /*constructor*/
    public ConcreteEdgesGraph(){
    }
    /*checkRep*/
    private void checkRep(){
        final int sizeOfEdges = edges.size();
        final int sizeOfVertices = vertices.size();
        int minNumberOfVertices = 
                sizeOfEdges == 0 ? 0 : (int)Math.ceil(Math.sqrt(2 * sizeOfEdges) + 0.5);
        
        assert sizeOfVertices >= minNumberOfVertices;  
    }
    
    /*returns true if the new vertex is added*/
    @Override public boolean add(L vertex) {
        return vertices.add(vertex);
    }    
    
    /*@param takes the source, target and weight to be set
     *@return previous Weight*/
    @Override public int set(L source, L target, int weight) {
        assert weight >= 0;
        
        int indexOfEdge = indexOfEdgeInEdges(source, target);
        int previousWeight = 0;
        final Edge<L> previousEdge;
        
        if (weight > 0) {
            Edge<L> newEdge = new Edge<>(source, target, weight);
            if ( indexOfEdge < 0 ) {
                add(source);
                add(target);
                edges.add(newEdge);
            } else {
                previousEdge = edges.set(indexOfEdge, newEdge);
                previousWeight = previousEdge.getWeight();
            }
        } else if ( weight == 0 && indexOfEdge >= 0) {
            previousEdge = edges.remove(indexOfEdge);
            previousWeight = previousEdge.getWeight();
        }
        checkRep();
        return previousWeight;
    }
    /*helper method*/
    private int indexOfEdgeInEdges(L source, L target){        
        for(int i = 0;  i < edges.size(); i++){
            Edge<L> edge = edges.get(i);
            if (edge.getSource().equals(source) &&
                    edge.getTarget().equals(target)){
                return i;
            }
        }
        return -1;
    }
    
    @Override public boolean remove(String vertex) {
        throw new RuntimeException("not implemented");
    }
    
    @Override public Set<String> vertices() {
        throw new RuntimeException("not implemented");
    }
    
    @Override public Map<String, Integer> sources(String target) {
        throw new RuntimeException("not implemented");
    }
    
    @Override public Map<String, Integer> targets(String source) {
        throw new RuntimeException("not implemented");
    }
    
    // TODO toString()
    
}

/**
 * TODO specification
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge {
    
    // TODO fields
    
    // Abstraction function:
    //   TODO
    // Representation invariant:
    //   TODO
    // Safety from rep exposure:
    //   TODO
    
    // TODO constructor
    
    // TODO checkRep
    
    // TODO methods
    
    // TODO toString()
    
}
