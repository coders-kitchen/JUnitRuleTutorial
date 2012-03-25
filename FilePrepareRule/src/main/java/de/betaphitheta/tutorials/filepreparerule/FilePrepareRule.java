/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.tutorials.filepreparerule;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * This implementation of MethodRule returns a FilePrepareStatement which 
 * creates and deletes the requested file structure for a test method.
 * @author mrpaeddah
 */
public class FilePrepareRule implements MethodRule {
    public Statement apply(final Statement base, FrameworkMethod method, Object target) {
        return new FilePrepareStatement(base, method, target);
    }
}
