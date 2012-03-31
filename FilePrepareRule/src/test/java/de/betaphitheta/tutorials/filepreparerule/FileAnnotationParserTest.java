/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.tutorials.filepreparerule;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author mrpaeddah
 */
public class FileAnnotationParserTest {

    FrameworkMethod method;
    FileAnnotationParser parser;

    public FileAnnotationParserTest() {
    }

    @Before
    public void setUpClass() throws Exception {
        method = mock(FrameworkMethod.class);
        
    }

    @Test
    public void parseOnlyClassDefaultDirectoryStructure() {
        parser = new FileAnnotationParser(method, new DefaultDirectoryClass());
        HashMap<String, HashSet<String>> parsed = parser.parseAnnotationsAndCreateStructure();
        Set<String> keys = parsed.keySet();
        assertNotNull(keys);
        
        assertEquals(1, keys.size());      
        String firstKey = keys.iterator().next();
        
        assertEquals("./tmp", firstKey);
        
        HashSet<String> files = parsed.get(firstKey);
        String[] toArray = files.toArray(new String[2]);
        assertArrayEquals(new String[] {"test.jar", "test.war"}, toArray);
    }
    
        @Test
    public void parseDefaultDirectoryWithAdditionalDefaultFiles() {
        parser = new FileAnnotationParser(method, new DefaultDirectoryClass());
        HashMap<String, HashSet<String>> parsed = parser.parseAnnotationsAndCreateStructure();
        Set<String> keys = parsed.keySet();
        assertNotNull(keys);
        
        assertEquals(1, keys.size());      
        String firstKey = keys.iterator().next();
        
        assertEquals("./tmp", firstKey);
        HashSet<String> files = parsed.get(firstKey);
        String[] toArray = files.toArray(new String[2]);
        assertArrayEquals(new String[] {"test.jar", "test.war"}, toArray);
    }
}
