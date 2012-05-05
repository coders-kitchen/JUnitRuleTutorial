/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.tutorials.filepreparerule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 *
 * @author mrpaeddah
 */
public class FilePrepareStatementTest extends Statement {

    FilePrepareStatement statement;

    public FilePrepareStatementTest() {
    }

    @Before
    public void setUp() throws Exception {
        HashMap<String, HashSet<String>> structure = new HashMap<String, HashSet<String>>();
        final HashSet<String> fileSet = new HashSet<String>();
        fileSet.add("test.jar");
        structure.put("./tmp", fileSet);
        statement = new FilePrepareStatement(this, structure);
    }

    @Test
    public void successfulSetup() throws Exception {
        try {
            statement.evaluate();
        } catch (Throwable ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }

    @Test
    public void directorySetupFailed() throws Exception {
        try {
            File f = new File("./tmp");
            f.mkdirs();
            f.setWritable(false);

            HashMap<String, HashSet<String>> structure = new HashMap<String, HashSet<String>>();
            final HashSet<String> fileSet = new HashSet<String>();
            fileSet.add("test.jar");
            structure.put("./tmp/tmp", fileSet);
            statement = new FilePrepareStatement(this, structure);
            statement.evaluate();
        } catch (Throwable ex) {
            ex.printStackTrace();
            assertEquals("Directory '/home/mrpaeddah/Development/tutorials/JUnitRuleTutorial/FilePrepareRule/./tmp/tmp' does not exist and creation failed!", ex.getMessage());
        } finally {
            File f = new File("./tmp");
            f.setWritable(true);
            f.delete();
        }
    }

    @Test
    public void fileSetupFailed() throws Exception {
        try {
            File f = new File("./tmp");
            f.mkdirs();
            f.setWritable(false);

            HashMap<String, HashSet<String>> structure = new HashMap<String, HashSet<String>>();
            final HashSet<String> fileSet = new HashSet<String>();
            fileSet.add("test.jar");
            structure.put("./tmp", fileSet);
            statement = new FilePrepareStatement(this, structure);
            statement.evaluate();
        } catch (Throwable ex) {
            assertEquals("Touch of file '/home/mrpaeddah/Development/tutorials/JUnitRuleTutorial/FilePrepareRule/./tmp/test.jar' failed!", ex.getMessage());
        } finally {
            File f = new File("./tmp");
            f.setWritable(true);
            f.delete();
        }
    }

    @Test
    public void tearDown() throws Exception {
        try {
            statement.evaluate();
        } catch (Throwable ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }

    @Override
    public void evaluate() throws Throwable {
        File base = new File("./");
        File[] tmpDir = base.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.equals("tmp");
            }
        });
        assertNotNull(tmpDir);
        assertEquals(1, tmpDir.length);
        String[] files = tmpDir[0].list();

        assertNotNull(files);
        assertEquals(1, tmpDir.length);
        assertArrayEquals(new String[]{"test.jar"}, files);
    }
}
