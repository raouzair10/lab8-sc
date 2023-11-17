/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//TODO: improve overall performance
/**
 * An implementation of Graph.
 * 
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
   /**
    * 
    * <p>The implementation involves a lot of checking and defensive copies
    * which is costly in terms of both performance and memory. This is 
    * because we are storing a mutable type to a list, so methods such
    * as contains() and get() can't be used to access the vertices. 
    * It is a requirement that we not add new fields and we stick to the
    * rep provided. 
    * I tried using a lookup table without violating the conditions, which
    * would store the vertices' labels in a list that matches the positions 
    * of their relative vertices to have constant time access, found that
    * challenging.
    * If allowed to alter the rep, I'd use a map:
    *       Map<String, Vertex>
    *  this would be performance friendly, constant time access and mutating
    *  any vertex would not affect the rep.
    *  
    */
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   represents a directed weighted graph as multiple vertices 
    //   connecting as source to target pairs with each pair having a
    //   weight.
    //   
    // Representation invariant:
    //   only one instance of a vertex can exist in vertices
    // Safety from rep exposure:
    //   vertices is a mutable list, so operation make defensive
    //   copies and use immutable views to avoid sharing the rep
    //   A Vertex is a mutable type, operations use defensive copies 
    //   to avoid sharing the rep
    
  
    public ConcreteVerticesGraph(){
    }
    private void checkRep(){        
        assert vertices().size() == vertices.size();
    }
    //helper method
    /**
     * Returns the index of a vertex in list of vertices
     * @param label the label of the vertex being searched
     * @return the index, i, of the vertex having label such that
     *         vertices.get(i).getLabel() == label, or -1 if
     *         no vertex was found
     */
    private int indexInVertices(L label){
        for(int i = 0; i < vertices.size(); i++){
            if ( vertices.get(i).getLabel().equals(label) ) {
                return i;
            }
        }
        return -1;
    }
    //end of helper method
    
    @Override public boolean add(L vertex) {        
        if ( vertices().contains(vertex) ) {
            return false;
        }
        Vertex<L> vertexObj = new Vertex<>(vertex);    
        final boolean vertexAdded = vertices.add(vertexObj);
        checkRep();
        return vertexAdded;
    }
    
    @Override public int set(L source, L target, int weight) {
        assert source != target;
        assert weight >= 0;
        
        final Vertex<L> sourceVertex;
        final Vertex<L> targetVertex;
        
        Set<L> verticeLabels = vertices();
        if ( verticeLabels.contains(source) ) {
            int sourceIndex = indexInVertices(source);
            sourceVertex = vertices.get(sourceIndex);
        } else {
            sourceVertex = new Vertex<>(source);
            vertices.add(sourceVertex);
        }
        
        if ( verticeLabels.contains(target) ) {
            int targetIndex = indexInVertices(target);
            targetVertex = vertices.get(targetIndex);
        } else {
            targetVertex = new Vertex<>(target);
            vertices.add(targetVertex);
        }
        
        int sourcePrevWeight = sourceVertex.setTarget(target, weight);
        int targetPrevWeight = targetVertex.setSource(source, weight);
        assert sourcePrevWeight == targetPrevWeight;
        
        checkRep();
        return sourcePrevWeight;
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
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex {
    
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
