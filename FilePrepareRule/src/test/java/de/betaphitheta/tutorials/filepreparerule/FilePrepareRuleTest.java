package de.betaphitheta.tutorials.filepreparerule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.Mock;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.HashSet;


/**
 * Created with IntelliJ IDEA.
 * User: mrpaeddah
 * Date: 05.05.12
 * Time: 21:50
 * To change this template use File | Settings | File Templates.
 */
public class FilePrepareRuleTest {

    FilePrepareRule underTest;

    @Mock
    FileAnnotationParser parser;

    @Mock
    Statement statement;

    @Mock
    Description description;

    HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        underTest = new FilePrepareRule(new Object());
        underTest.fileAnnotationParser = parser;
        when(parser.parseAnnotationsAndCreateStructure()).thenReturn(map);
    }

    @Test
    public void apply() throws Exception {
        Statement response = underTest.apply(statement, description);
        verify(parser, times(1)).parseAnnotationsAndCreateStructure();
        assertEquals(FilePrepareStatement.class, response.getClass());
    }
}
