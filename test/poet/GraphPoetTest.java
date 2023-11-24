/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy
          // Partitions for GraphPoet(corpus)
    //   corpus contains: one word, one line, multiple lines
    //   include words whose adjacency count > 1
    // 
    // Partitions for poem(input) -> poeticOutput
    //   input: one word, multiple words
    //        : word pairs with adjacency count > 1
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // TODO tests
    private static final GraphPoet instanceGraph(String source) {
        try {
            final File corpus = new File(source);
            GraphPoet graphPoet = new GraphPoet(corpus);
            return graphPoet;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    final GraphPoet graphOneWord = instanceGraph("test/poet/OneWord.txt");
    final GraphPoet graphOneLine = instanceGraph("test/poet/OneLine.txt");
    final GraphPoet graphMultipleLines = instanceGraph("test/poet/MultipleLines.txt");

}
