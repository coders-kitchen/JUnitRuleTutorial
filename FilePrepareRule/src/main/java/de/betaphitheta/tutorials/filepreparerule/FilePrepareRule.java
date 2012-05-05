/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.tutorials.filepreparerule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.HashMap;
import java.util.HashSet;

/**
 * This implementation of MethodRule returns a FilePrepareStatement which 
 * creates and deletes the requested file structure for a test description.
 * @author mrpaeddah
 */
public class FilePrepareRule implements TestRule {

    private final Object classUnderTest;
    FileAnnotationParser fileAnnotationParser;
    public FilePrepareRule(Object classUnderTest) {
        this.classUnderTest = classUnderTest;
        fileAnnotationParser = new FileAnnotationParser();
    }

    @Override
    public Statement apply(Statement base, Description description) {
        fileAnnotationParser.setDescription(description);
        fileAnnotationParser.target = classUnderTest;
        final HashMap<String, HashSet<String>> structure = fileAnnotationParser.parseAnnotationsAndCreateStructure();
        return new FilePrepareStatement(base, structure) ;
    }
}
