/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.tutorials.filepreparerule;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class FilePrepareStatementTest {
    
    FilePrepareStatement statement;
    
    public FilePrepareStatementTest() {
    }

    @Before
    public void setUp() throws Exception {
        statement = new FilePrepareStatement(statement, null, this);
                
    }


    @Test
    public void testEvaluate() throws Exception {
        try {
            System.out.println("evaluate");
            statement.evaluate();
            fail("The test case is a prototype.");
        } catch (Throwable ex) {
            Logger.getLogger(FilePrepareStatementTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
