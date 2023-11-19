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
    
    @Override public boolean remove(L vertex) {
        if ( !( vertices().contains(vertex)) ) {
            return false;
        }
        int vertexIndex = indexInVertices(vertex);
        assert vertexIndex != -1;
        final Vertex<L> removedVertex = vertices.remove(vertexIndex);
        assert removedVertex.getLabel() == vertex;
        
        for( Vertex<L> v: vertices ) {
            v.remove(vertex);
        }
        return removedVertex != null;
    }
    
    @Override public Set<L> vertices() {
        return vertices.stream()
                .map(Vertex::getLabel)
                .collect(Collectors.toSet());
    }
    
    /** Returns an immutable view of source vertices to a target */
    @Override public Map<L, Integer> sources(L target) {
        final int targetIndex = indexInVertices(target);
        if ( targetIndex < 0 ) {
            return Collections.emptyMap();
        }
        Vertex<L> targetVertex = vertices.get(targetIndex);
        
        return Collections.unmodifiableMap(targetVertex.getSources());
    }
    /** Returns an immutable view of target vertices from a target */
    @Override public Map<L, Integer> targets(L source) {
        final int sourceIndex = indexInVertices(source);
        if ( sourceIndex < 0 ) {
            return Collections.emptyMap();
        }
        Vertex<L> sourceVertex = vertices.get(sourceIndex);
        
        return Collections.unmodifiableMap(sourceVertex.getTargets());
    }
    
    // TODO toString()
    @Override public String toString(){
        return vertices.stream()
                .filter(vertex -> vertex.getTargets().size() > 0)
                .map(vertex -> vertex.getLabel().toString() + " -> " + vertex.getTargets())
                .collect(Collectors.joining("\n"));
    }
    
}

/**
 * TODO specification
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {
    
    // TODO fields
	 private final L label;
	 private final Map<L, Integer> sources = new HashMap<>();
	 private final Map<L, Integer> targets = new HashMap<>();
    
	// TODO constructor
	 public Vertex(final L label){
	        this.label = label;        
	    }
	// TODO checkRep
	 private void checkRep(){
	        final Set<L> sourceLabels = sources.keySet();
	        final Set<L> targetLabels = targets.keySet();
	        
	        assert !sourceLabels.contains(this.label);
	        assert !targetLabels.contains(this.label);
	    }
	//helper code
	private void checkInputLabel(final L inputLabel){
	        assert inputLabel != null;
	        assert inputLabel != this.label;
	    }
	public L getLabel(){
	        return this.label;
	    }
    
    // TODO methods
	public boolean addSource(final L source, final int weight){
        checkInputLabel(source);
        assert weight > 0;
        
        if ( sources.putIfAbsent(source, weight) == null ){
            checkRep();
            return true;
        }
        return false;
    }
    /**
     * Adds a target connection from this vertex
     */
    public boolean addTarget(final L target, final int weight){
        checkInputLabel(target);
        assert weight > 0;
        
        if ( targets.putIfAbsent(target, weight) == null ) {
            checkRep();
            return true;
        }
        return false;
    }

    /**
     * Removes a vertex from this vertex, if it was a source, target or both
     */
    public int remove(final L vertex) {
        checkInputLabel(vertex);
        int sourcePrevWeight = removeSource(vertex);
        int targetPrevWeight = removeTarget(vertex);
        
        if ( sourcePrevWeight > 0 && targetPrevWeight > 0 ) {
            assert sourcePrevWeight == targetPrevWeight;
        }
        return sourcePrevWeight == 0 ? targetPrevWeight : sourcePrevWeight;
    }
    /**
     * Removes a source connection to this vertex
     */
    public int removeSource(final L source){
        checkInputLabel(source);
        
        Integer previousWeight = sources.remove(source);
        
        checkRep();
        return previousWeight == null ? 0 : previousWeight;
    }
    /**
     * Removes a target connection from this vertex
     */
    public int removeTarget(final L target){
        checkInputLabel(target);
        
        Integer previousWeight = targets.remove(target);
        
        checkRep();
        return previousWeight == null ? 0 : previousWeight;
    }
    
    public int setSource(final L source, final int weight){
        checkInputLabel(source);
        assert weight >= 0;
        final int previousWeight;
        
        if ( weight == 0 ) {
            previousWeight = removeSource(source); 
        } else if ( addSource(source, weight) || sources.get(source) == (Integer)weight) {
            previousWeight = 0;
        } else {
            previousWeight = sources.replace(source, weight);
        }
        checkRep();
        return previousWeight;
    }
    public int setTarget(final L target, final int weight){
        checkInputLabel(target);
        assert weight >= 0;
        final int previousWeight;
        
        if ( weight == 0 ) {
            previousWeight = removeTarget(target);
        } else if ( addTarget(target, weight) || targets.get(target) == (Integer)weight ) {
            previousWeight = 0;
        } else {
            previousWeight = targets.replace(target, weight);
        }
        checkRep();
        return previousWeight;
    }

    /** Returns an immutable view of this vertex's sources*/
    public Map<L, Integer> getSources(){
        return Collections.unmodifiableMap(sources);
    }
    /** Returns an immutable view of this vertex's targets*/
    public Map<L, Integer> getTargets(){
        return Collections.unmodifiableMap(targets);
    }

    public boolean isTarget(final L vertex){
        return targets.containsKey(vertex);
    }

    public boolean isSource(final L vertex){
        return sources.containsKey(vertex);
    }
    
    // TODO toString()
	@Override public String toString(){
        return String.format(
                "%s -> %s \n" +
                "%s <- %s",
                this.label.toString(), this.targets,
                this.label.toString(), this.sources);
    }
    
}
