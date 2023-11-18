
package graph;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.Test;


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
    //
    
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
    
    // TODO other tests for instance methods of Graph
    @Test
    //covers empty graph
    //       label doesn't exist in graph
    public void testAddEmptyGraph(){
        Graph<String> graph = emptyInstance();
        
        final int InitialNumOfVertices = graph.vertices().size();
        final String vertex = "vertex";
        final boolean vertexAdded = graph.add(vertex);
        final int CurrentNumOfVertices = graph.vertices().size();
        
        assertTrue("Expected vertex to be added", vertexAdded);
        assertEquals("Expected vertices to increase by one",InitialNumOfVertices + 1,CurrentNumOfVertices);
    }
    
    @Test
    //covers graph contains multiple vertices
    //       label exists in graph
    public void testAddExistsInGraph(){
        Graph<String> graph = emptyInstance();
        
        final String vertex1 = "vertex1";
        final String vertex2 = "vertex2";
        
        final boolean vertex1Added = graph.add(vertex1);
        final boolean vertex2Added = graph.add(vertex2);
        
        final int InitialNumOfVertices = graph.vertices().size();
        
        final boolean vertex1AddedAgain = graph.add(vertex1);
        
        final int CurrentNumOfVertices = graph.vertices().size();
        
        assertTrue("Expected vertex1 to be added", vertex1Added);
        assertTrue("Expected vertex2 to be added", vertex2Added);
        assertFalse("Expected vertex1 not to be added", vertex1AddedAgain);
        assertEquals("Expected same number of vertices",InitialNumOfVertices,CurrentNumOfVertices);
    }
    
    //Tests for graph.remove(vertex)
    @Test
    //covers empty graph
    //       label doesn't exist in graph
    public void testRemoveEmptyGraph(){
        Graph<String> graph = emptyInstance();
        
        final int InitialNumVertices = graph.vertices().size();
        final String vertex = "vertex";
        final boolean vertexRemoved = graph.remove(vertex);
        final int CurrentNumVertices = graph.vertices().size();
        
        assertFalse("Expected no effect on graph after remove", vertexRemoved);
        assertEquals("Expected same number of vertices", InitialNumVertices, CurrentNumVertices);
    } 
    
    @Test
    //covers graph contains multiple vertices
    //       label doesn't exist in graph
    //       label doesn't exist in an edge
    public void testRemoveNotExistInGraph(){
        Graph<String> graph = emptyInstance();
        
        final String vertex1 = "vertex1";
        final String vertex2 = "vertex2";
        final String vertex3 = "vertex3";
        
        graph.add(vertex1);
        graph.add(vertex2);
        
        final int InitialNumOfVertices = graph.vertices().size();
        
        final boolean vertex3Removed = graph.remove(vertex3);
        
        final int CurrentNumOfVertices = graph.vertices().size();
        
        assertFalse("Expected no effect on graph after remove", vertex3Removed);
        assertEquals("Expected same number of vertices", InitialNumOfVertices, CurrentNumOfVertices);
    }
    
    @Test
    //covers graph contains multiple vertices
    //       label exists in graph
    //       label doesn't exist in an edge
    public void testRemoveExistsInGraph(){
        Graph<String> graph = emptyInstance();
        Set<String> vertices = graph.vertices();
        
        final String vertex1 = "vertex1";
        final String vertex2 = "vertex2";
        
        graph.add(vertex1);
        graph.add(vertex2);
        
        final int InitialNumOfVertices = graph.vertices().size();
        
        final boolean vertex1Removed = graph.remove(vertex1);
        
        final int CurrentNumOfVertices = graph.vertices().size();
        
        assertTrue("Expected vertex removed", vertex1Removed);
        assertEquals("Expected number of vertices reduced by 1", InitialNumOfVertices - 1, CurrentNumOfVertices);
        assertFalse("Expected correct vertex removed", vertices.contains(vertex1));
    }
    @Test
    //covers graph contains multiple vertices
    //       label exists in graph
    //       label exists in edges
    public void testRemoveExistInGraphAndEdge(){
        Graph<String> graph = emptyInstance();
        final String source1 = "vertex1";
        final String source2 = "vertex2";
        final String source3 = "vertex3";
        final String target1 = "vertex4";
        final int weight = 1;
        
        //create 3 edges
        graph.set(source1, target1, weight);
        graph.set(source2, target1, weight);
        graph.set(source3, source1, weight);
        
        int initialNumVertices = graph.vertices().size();
        final boolean vertexRemoved = graph.remove(source1); 
        int currentNumVertices = graph.vertices().size();
        
        int expectedNumSources = 1;
        int expectedNumTargets = 1;
        int currentNumSources = graph.sources(target1).size() +
                                graph.sources(source1).size();
        int currentNumTargets = graph.targets(source1).size() +
                                graph.targets(source2).size() +
                                graph.targets(source3).size();
        
        assertTrue("Expected vertex removed from graph", vertexRemoved);
        assertEquals("Expected number of vertices to reduce by 1", initialNumVertices - 1, currentNumVertices);
        assertEquals("Expected vertex removed from sources", expectedNumSources, currentNumSources);
        assertEquals("Expected vertex removed from targets", expectedNumTargets, currentNumTargets);
        assertFalse("Expected correct vertex removed", graph.vertices().contains(source1));
    }
    //Tests for graph.set()
    @Test
    //covers empty graph
    //       target doesn't exist in graph,
    //       source doesn't exist in graph,
    //       No edge exists from source to target
    //       weight > 0
    //expects set() to add a new edge to graph
    public void testSetEmptyGraph(){
        Graph<String> graph = emptyInstance();
        final int InitialNumVertices = graph.vertices().size();
        
        final String source = "vertex1";
        final String target = "vertex2";
        final int weight = 1;
        
        final int previousWeight = graph.set(source, target, weight);
        
        final int CurrentNumVertices = graph.vertices().size();
        Map<String, Integer> targets = graph.targets(source);
        Map<String, Integer> sources = graph.sources(target);
        
        assertEquals("Expected no previous weight", 0, previousWeight);
        assertNotEquals("Expected graph to be modified", InitialNumVertices, CurrentNumVertices);
        assertEquals("Expected number of vertices increase by 2", InitialNumVertices + 2, CurrentNumVertices);
        assertTrue("Expected source to have target", targets.containsKey(target));
        assertTrue("Expected target to have source", sources.containsKey(source));
        assertEquals("Expected source to have correct weight", (Integer)weight, sources.get(source));
        assertEquals("Expected target to have correct weight", (Integer)weight, targets.get(target));
    }
    
    @Test
    //covers graph contains multiple vertices,
    //       source doesn't exist in graph,
    //       target exists in graph,
    //       No edge from source to target,
    //       weight > 0
    public void testSetSourceNotExist(){
        Graph<String> graph = emptyInstance();
        
        final String source = "vertex1";
        final String target = "vertex2";
        final int weight = 1;
        
        graph.add(target);
        final int InitialNumVertices = graph.vertices().size();
        final int previousWeight = graph.set(source, target, weight);
        final int CurrentNumVertices = graph.vertices().size();
        Map<String, Integer> targets = graph.targets(source);
        Map<String, Integer> sources = graph.sources(target);
        
        assertEquals("Expected no previous weight", 0, previousWeight);
        assertNotEquals("Expected graph to be modified", InitialNumVertices, CurrentNumVertices);
        assertEquals("Expected number of vertices to increase by 1", InitialNumVertices + 1, CurrentNumVertices);
        assertTrue("Expected source to have target", targets.containsKey(target));
        assertTrue("Expected target to have source", sources.containsKey(source));
        assertEquals("Expected source to have correct weight", (Integer)weight, sources.get(source));
        assertEquals("Expected target to have correct weight", (Integer)weight, targets.get(target));
    }
    
    @Test
    //covers graph contains multiple vertices,
    //       target doesn't exist in graph,
    //       source exists in graph,
    //       No edge from source to target,
    //       weight > 0
    public void testSetTargetNotExist(){
        Graph<String> graph = emptyInstance();
        
        final String source = "vertex1";
        final String target = "vertex2";
        final int weight = 1;
        
        graph.add(source);
        final int InitialNumVertices = graph.vertices().size();
        final int previousWeight = graph.set(source, target, weight);
        final int CurrentNumVertices = graph.vertices().size();
        Map<String, Integer> targets = graph.targets(source);
        Map<String, Integer> sources = graph.sources(target);
        
        assertEquals("Expected no previous weight", 0, previousWeight);
        assertNotEquals("Expected graph to be modified", InitialNumVertices, CurrentNumVertices);
        assertEquals("Expected number of vertices to increase by 1", InitialNumVertices + 1, CurrentNumVertices);
        assertTrue("Expected source to have target", targets.containsKey(target));
        assertTrue("Expected target to have source", sources.containsKey(source));
        assertEquals("Expected source to have correct weight", (Integer)weight, sources.get(source));
        assertEquals("Expected target to have correct weight", (Integer)weight, targets.get(target));
    }
    
    @Test
    //covers graph contains multiple vertices,
    //       source and target exist in graph,
    //       No edge from source to target,
    //       weight > 0
    public void testSetNoEdgeExists(){
        Graph<String> graph = emptyInstance();
        
        final String source = "vertex1";
        final String target = "vertex2";
        final int weight = 1;
        
        graph.add(source);
        graph.add(target);
        final int InitialNumVertices = graph.vertices().size();
        final int previousWeight = graph.set(source, target, weight);
        final int CurrentNumVertices = graph.vertices().size();
        Map<String, Integer> targets = graph.targets(source);
        Map<String, Integer> sources = graph.sources(target);
        
        assertEquals("Expected no previous weight", 0, previousWeight);
        assertEquals("Expected graph not to be modified", InitialNumVertices, CurrentNumVertices);
        assertTrue("Expected source to have target", targets.containsKey(target));
        assertTrue("Expected target to have source", sources.containsKey(source));
        assertEquals("Expected source to have correct weight", (Integer)weight, sources.get(source));
        assertEquals("Expected target to have correct weight", (Integer)weight, targets.get(target));
    }
    
    @Test
    //covers graph contains multiple vertices,
    //       source and target exist in graph,
    //       an edge exists from source to target
    //       weight > 0
    public void testSetUpdateWeight(){
        Graph<String> graph = emptyInstance();
        
        //create an edge to test set() on
        final String source = "vertex1";
        final String target = "vertex2";
        final int initialWeight = 1;
        graph.add(source);
        graph.add(target);
        graph.set(source, target, initialWeight);
        
        final int newWeight = 2;
        final int InitialNumVertices = graph.vertices().size();
        final int previousWeight = graph.set(source, target, newWeight);
        final int CurrentNumVertices = graph.vertices().size();
        Map<String, Integer> targets = graph.targets(source);
        Map<String, Integer> sources = graph.sources(target);
        
        assertEquals("Expected previous weight to be initial weight", initialWeight, previousWeight);
        assertEquals("Expected edge to change weight", (Integer)newWeight, targets.get(target));
        assertEquals("Expected edge to change weight", (Integer)newWeight, sources.get(source));
        assertEquals("Expected same number of vertices", InitialNumVertices, CurrentNumVertices);
        
    }
    
    @Test
    //covers graph contains multiple vertices,
    //       source and target exist in graph,
    //       an edge exists from source to target
    //       weight = 0
    public void testSetRemoveEdge(){
        Graph<String> graph = emptyInstance();
        
        //create an edge to test set() on
        final String source = "vertex1";
        final String target = "vertex2";
        final int initialWeight = 1;
        graph.add(source);
        graph.add(target);
        graph.set(source, target, initialWeight);
        
        final int newWeight = 0;
        final int InitialNumVertices = graph.vertices().size();
        final int previousWeight = graph.set(source, target, newWeight);
        final int CurrentNumVertices = graph.vertices().size();
        Map<String, Integer> targets = graph.targets(source);
        Map<String, Integer> sources = graph.sources(target);
        
        assertEquals("Expected previous weight to be initial weight", initialWeight, previousWeight);
        assertEquals("Expected same number of vertices", InitialNumVertices, CurrentNumVertices);
        assertFalse("Expected edge removed from graph", sources.containsKey(source));
        assertFalse("Expected edge removed from graph", targets.containsKey(target));
        
    }
    
    @Test
    //covers graph contains multiple vertices,
    //       source and target exist in graph,
    //       No edge exists from source to target
    //       weight = 0
    public void testSetEdgeNotExistRemoveEdge(){
        Graph<String> graph = emptyInstance();

        final String source = "vertex1";
        final String target = "vertex2";
        final int weight = 0;
        
        graph.add(source);
        graph.add(target);
        final int InitialNumVertices = graph.vertices().size();
        final int previousWeight = graph.set(source, target, weight);
        final int CurrentNumVertices = graph.vertices().size();
        Map<String, Integer> targets = graph.targets(source);
        Map<String, Integer> sources = graph.sources(target);
        
        assertEquals("Expected previous weight to be initial weight", weight, previousWeight);
        assertEquals("Expected same number of vertices", InitialNumVertices, CurrentNumVertices);
        assertEquals("Expected no new edge in graph", Collections.emptyMap(), targets);
        assertEquals("Expected no new edge in graph", Collections.emptyMap(), sources);
    }
    
    @Test
    //covers enpty graph
    public void testVerticesEmptyGraph() {
        Graph<String> graph = emptyInstance();
        assertEquals("Expected new graph to have no vertices", Collections.emptySet(), graph.vertices());
    }
    
    @Test
    //covers graph contains multiple vertices
    public void testVertices() {
        Graph<String> graph = emptyInstance();
        
        graph.add("vertex1");
        graph.add("vertex2");
        graph.add("vertex3");
        
        Set<String> vertices = graph.vertices();
        
        assertNotEquals("Expected non-empty set", Collections.emptySet(), vertices);
        assertEquals("Expected 3 vertices", 3, vertices.size());
    }
    
    @Test
    //covers empty graph
    public void testSourcesEmptyGraph(){
        Graph<String> graph = emptyInstance();
        final String target = "vertex1";
        Map<String, Integer> sources = graph.sources(target);
        
        assertEquals("Expected new graph to have no sources", Collections.emptyMap(), sources);
    }
    
    @Test
    //covers graph contains multiple vertices
    //       target doesn't exist in graph
    public void testSourcesNoTarget(){
        Graph<String> graph = emptyInstance();
        
        graph.add("vertex1");
        graph.add("vertex2");
        final String target = "vertex3";
        Map<String, Integer> sources = graph.sources(target);
        
        assertEquals("Expected graph to have no sources", Collections.emptyMap(), sources);
        
    }
    
    @Test
    //covers graph contains multiple vertices
    //       target exists in graph
    //       target has no sources
    public void testSourcesNoSourcesToTarget(){
        Graph<String> graph = emptyInstance();
        final String target = "vertex1";
        graph.add(target);
        graph.add("vertex2");
        Map<String, Integer> sources = graph.sources(target);
        
        assertEquals("Expected graph to have no sources", Collections.emptyMap(), sources);
    }
    
    @Test
    //covers graph contains multiple vertices,
    //       target exists in graph,
    //       target has multiple sources
    public void testSourcesMultipleSources(){
        Graph<String> graph = emptyInstance();
        
        //create edges to test sources
        final String target = "vertex";
        final String vertex1 = "vertex1";
        final String vertex2 = "vertex2";
        final String vertex3 = "vertex3";
        final int weight = 1;
        
        graph.set(vertex1, target, weight);
        graph.set(vertex2, target, weight);
        graph.add(vertex3);
        
        Map<String, Integer> sources = graph.sources(target);
        
        assertNotEquals("Expected graph to have sources", Collections.emptyMap(), sources);
        assertEquals("Expected 2 sources to target", 2, sources.keySet().size());
        assertTrue("Expected sources to contain vertex1, vertex2", 
                Arrays.asList(vertex1, vertex2).containsAll(sources.keySet()));
        }
    
    @Test
    //covers empty graph
    public void testTargetsEmptyGraph(){
        Graph<String> graph = emptyInstance();
        Map<String, Integer> targets = graph.targets("vertex");
        
        assertEquals("Expected empty graph to have no targets", Collections.emptyMap(), targets);   
    }
    
    @Test
    //covers graph has multiple vertices
    //       source not in graph
    public void testTargetsNoTarget(){
        Graph<String> graph = emptyInstance();
        
        graph.add("vertex1");
        graph.add("vertex2");
        
        final String source = "vertex3";
        
        Map<String, Integer> targets = graph.targets(source);
        
        assertEquals("Expected graph to have no targets", Collections.emptyMap(), targets);
    }
    
    @Test
    //covers graph contains multiple vertices,
    //       source in graph,
    //       source has no targets
    public void testTargetsNoTargetsFromSource(){
        Graph<String> graph = emptyInstance();
        final String source = "vertex1";
        
        graph.add(source);
        graph.add("vertex2");
        
        Map<String, Integer> targets = graph.targets(source);
        
        assertEquals("Expected graph to have no targets", Collections.emptyMap(), targets);
    }
    
    @Test
    //covers graph contains multiple vertices,
    //       source in graph,
    //       source has multiple targets
    public void testTargetsMultipleTargets(){
        Graph<String> graph = emptyInstance();
        final String source = "vertex";
        final String vertex1 = "vertex1";
        final String vertex2 = "vertex2";
        final String vertex3 = "vertex3";
        final int weight = 1;
        
        graph.set(source, vertex1, weight);
        graph.set(source, vertex2, weight);
        graph.add(vertex3);
        
        Map<String, Integer> targets = graph.targets(source);
        
        assertNotEquals("Expected graph to have targets", Collections.emptyMap(), targets);
        assertEquals("Expected 2 targets from source", 2, targets.keySet().size());
        assertTrue("Expected targets to containe vertex1, vertex2", 
                Arrays.asList(vertex1,vertex2).containsAll(targets.keySet()));
    }
}