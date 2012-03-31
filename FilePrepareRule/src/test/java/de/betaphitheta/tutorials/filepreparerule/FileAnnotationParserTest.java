/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.tutorials.filepreparerule;

import java.lang.annotation.Annotation;
import java.util.Arrays;
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
    DirectorySetup directorySetup;
    FileSetup fileSetup;
    FileAnnotationParser parser;
    private String directorySetupDirectory;
    private String[] directorySetupFiles;
    private String[] fileSetupFiles;

    public FileAnnotationParserTest() {
        directorySetupDirectory = "./testTmp";
        directorySetupFiles = new String[]{"testTmp.jar"};
        fileSetupFiles = new String[]{"testTmp.war"};
    }

    @Before
    public void setUpClass() throws Exception {
        method = mock(FrameworkMethod.class);

        directorySetup = mock(DirectorySetup.class);
        when(directorySetup.directory()).thenReturn(directorySetupDirectory);
        when(directorySetup.files()).thenReturn(directorySetupFiles);

        fileSetup = mock(FileSetup.class);
        when(fileSetup.files()).thenReturn(fileSetupFiles);
    }

    @Test
    public void parseOnlyClassDefaultDirectoryStructure() {
        parser = new FileAnnotationParser(method, new DefaultDirectoryClass());
        final String expectedDirectory = "./tmp";
        final int expectedDirectoryCount = 1;
        final String[] expectedFiles = new String[]{"test.jar", "test.war"};
        runParseTest(expectedDirectoryCount, expectedDirectory, expectedFiles);
    }

    @Test
    public void parseDefaultDirectoryWithAdditionalDefaultFiles() {
        parser = new FileAnnotationParser(method, new DefaultDirectoryAndDefaultFilesClass());
        final String expectedDirectory = "./tmp";
        final int expectedDirectoryCount = 1;
        final String[] expectedFiles = new String[]{"test.jar", "test.war", "test.wab"};
        runParseTest(expectedDirectoryCount, expectedDirectory, expectedFiles);
    }

    @Test
    public void parseDirectorySetupOnlyAtTestMethod() {
        when(method.getAnnotations()).thenReturn(new Annotation[]{directorySetup});
        parser = new FileAnnotationParser(method, new EmptyTestClass());
        runParseTest(1, directorySetupDirectory, directorySetupFiles);
    }
    
    @Test
    public void parseDirectorySetupAndFileSetupAtTestMethod() {
        when(method.getAnnotations()).thenReturn(new Annotation[]{directorySetup, fileSetup});
        when(method.getAnnotation(FileSetup.class)).thenReturn(fileSetup);
        parser = new FileAnnotationParser(method, new EmptyTestClass());
        String[] files = {directorySetupFiles[0], fileSetupFiles[0]};
        runParseTest(1, directorySetupDirectory, files);
    }

    @Test
    public void parseFileSetupOnlyAtTestMethod() {
        when(method.getAnnotations()).thenReturn(new Annotation[]{fileSetup});
        when(method.getAnnotation(FileSetup.class)).thenReturn(fileSetup);
        parser = new FileAnnotationParser(method, new EmptyTestClass());
        HashMap<String, HashSet<String>> parsed = parser.parseAnnotationsAndCreateStructure();
        Set<String> keys = parsed.keySet();
        assertNotNull(keys);

        assertEquals(0, keys.size());
    }
    
    private void runParseTest(final int expectedDirectoryCount, final String expectedDirectory, final String[] expectedFiles) {
        HashMap<String, HashSet<String>> parsed = parser.parseAnnotationsAndCreateStructure();
        Set<String> keys = parsed.keySet();
        assertNotNull(keys);

        assertEquals(expectedDirectoryCount, keys.size());
        String firstKey = keys.iterator().next();

        assertEquals(expectedDirectory, firstKey);

        HashSet<String> files = parsed.get(firstKey);
        String[] toArray = files.toArray(new String[files.size()]);
        Arrays.sort(toArray);
        Arrays.sort(expectedFiles);
        assertArrayEquals(expectedFiles, toArray);
    }
}
