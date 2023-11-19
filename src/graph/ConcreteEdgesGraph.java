/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    
    /*@param vertex to be removed
     *@return boolean true if removed*/
    @Override public boolean remove(L vertex) {
        final int initialSizeEdges = edges.size();
        final int initialSizeVertices = vertices.size();
        
        Predicate<Edge<L>> vertexInEdge = (Edge<L> edge) -> ( ( edge.getSource().equals(vertex) )||( edge.getTarget().equals(vertex) ) ) ;
        Predicate<L> vertexInVertices = v -> v.equals(vertex);
        
        boolean removedEdge = edges.removeIf(vertexInEdge);
        boolean removedVertice = vertices.removeIf(vertexInVertices);
        
        //NB a vertex can exist without being in an edge
        //if removedEdge, then removedVertice
        if(removedVertice){
            assert initialSizeVertices != vertices.size();
            assert initialSizeVertices - 1 == vertices.size();
        }
        if(removedEdge){
            assert initialSizeEdges != edges.size();
            assert removedVertice;
        }
        checkRep();
        return initialSizeVertices - 1 == vertices.size();
    }
    
    /** Returns an read-only view of ConcreteEdgesGraph's vertices */
    @Override public Set<L> vertices() {
        return Collections.unmodifiableSet(vertices);
    }
    
    /** Returns a map of a target's sources */
    @Override public Map<L, Integer> sources(L target) {
        return edges.stream()
                .filter(edge -> edge.getTarget().equals(target))
                .collect(Collectors.toMap(Edge::getSource, Edge::getWeight));
    }
    /** Returns a map of a source's targets */
    @Override public Map<L, Integer> targets(L source) {
        return edges.stream()
                .filter(edge -> edge.getSource().equals(source))
                .collect(Collectors.toMap(Edge::getTarget, Edge::getWeight));
    }
    
    // TODO toString()
    @Override public String toString(){
        if ( edges.isEmpty() ) {
            return "Empty Graph";
        }
        return edges.stream()
                .map(edge -> edge.toString())
                .collect(Collectors.joining("\n"));
    }
    
}

/**
 * TODO specification
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {
	// TODO fields
	private final L source;
    private final L target;
    private final int weight;
   
    // TODO constructor
    public Edge(final L source, final L target, final int weight){
        assert weight > 0;
        
        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }
    // TODO checkRep
    private void checkRep(){
        assert source != null;
        assert target != null;
        assert weight > 0;
    }
 // TODO methods
    //observers (Getters and Setters)
    /** Returns this Edge's source*/   
    public L getSource(){
        return source;
    }
    /**Returns this Edge's target*/
    public L getTarget(){
        return target;
    }
    /**Returns this Edge's weight*/
    public int getWeight(){
        return weight;
    }
    public Edge<L> setWeight(int newWeight){
        checkRep();
        return new Edge<>(source, target, newWeight);
    }
 // TODO toString()
    @Override public String toString(){
        return getSource().toString() + 
                " -> " + 
                getTarget().toString() + 
                ": " + 
                getWeight();
    }
    @Override public boolean equals(Object that){
        if (! (that instanceof Edge)) {
            return false;
        }
        Edge<?> thatEdge = (Edge<?>)that;
        return this.getSource().equals(thatEdge.getSource()) &&
               this.getTarget().equals(thatEdge.getTarget()) &&
               this.getWeight() == thatEdge.getWeight();
    }
    @Override public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + getSource().hashCode();
        result = prime * result + getTarget().hashCode();
        result = prime * result + (int) getWeight();
        return result;
    }
    
}
