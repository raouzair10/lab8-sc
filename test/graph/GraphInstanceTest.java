/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    //   TODO
    //   Partition for graph.add(label)
    //      graph: empty, contains multiple vertices
    //      label: exists in graph, doesn't exist in graph
    //      output: if add() returns true, graph is modified
    //              ie number of vertices increases by 1
    //              else graph unmodified
    //      observe with vertices()
    //
    //   Partition for graph.remove(label)
    //      graph: empty, contains multiple vertices
    //      label: exists in graph, doesn't exist in graph,
    //             exists in edges, doesnt exist in an edge
    //      output: if remove() returns true, graph is modified
    //              ie number of vertices decreases by 1
    //              else graph unmodified
    //      observe with vertices(), sources(), targets()
    //   
    //   Partition for graph.set(source,target,weight) -> previousWeight
    //      graph: empty, contains multiple vertices    
    //      source: exists in graph, doesn't exist in graph
    //      target: exists in graph, doesn't exist in graph
    //      No edge exists from source to target,
    //      An edge exists from source to target,
    //      weight: 0, > 0
    //      observe with sources(), targets(), vertices()
    //    
    //   Partition for graph.vertices() -> allVertices
    //      graph: empty, contains multiple vertices
    //   
    //   Partition for graph.sources(target) -> targetSources
    //      graph: empty, contains multiple vertices
    //      target: doesn't exist in graph, exists in graph,
    //              has no sources, has multiple sources
    //      targetSources contains all source vertices to target
    //      
    //   Partition for graph.targets(source) -> sourceTargets
    //      graph: empty, contains multiple vertices
    //      source: doesn't exist in graph, exists in graph,
    //             has no targets, has multiple targets
    //      sourceTargets contains all target vertices from source
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    // TODO other tests for instance methods of Graph
    
}
