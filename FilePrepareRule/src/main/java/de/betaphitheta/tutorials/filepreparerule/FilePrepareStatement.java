/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.tutorials.filepreparerule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.junit.runners.model.Statement;

/**
 * This Statement wraps the original statement.
 * It grants creation and deletion of a requested directory and file structure
 * to be used by the Statement base.
 * 
 * The directory and file structure is provided by an HashMap<String, ArrayList<String>>.
 * 
 * @author mrpaeddah
 */
public class FilePrepareStatement extends Statement {

    private Statement base;
    private final HashMap<String, ArrayList<String>> directoryStructure;

    public FilePrepareStatement(final Statement base, HashMap<String, ArrayList<String>> directoryStructure) {
        this.base = base;
        this.directoryStructure = directoryStructure;
    }

    @Override
    public void evaluate() throws Throwable {
        try {
            createDirectorySetup();
            base.evaluate();
        } finally {
            deleteDirectorySetup();
        }
    }

    private void createDirectorySetup() throws IOException {
        Set<String> directories = directoryStructure.keySet();
        for (String currentDir : directories) {
            File directory = new File(currentDir);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new IOException("Directory '" + directory.getAbsolutePath() + "' does not exist and creation failed!");
            }
            ArrayList<String> files = directoryStructure.get(currentDir);
            for (String currentFile : files) {
                File toCreate = new File(directory, currentFile);
                if (!toCreate.createNewFile()) {
                    throw new IOException("Touch of file '" + toCreate.getAbsolutePath() + "' failed!");
                }
            }
        }
    }

    private void deleteDirectorySetup() throws IOException {
        Set<String> directories = directoryStructure.keySet();
        for (String directory : directories) {
            recursiveDelete(new File(directory));
        }
    }

    private void recursiveDelete(File directory) throws IOException {
        File[] listed = directory.listFiles();
        for (File subFile : listed) {
            if (subFile.isDirectory()) {
                recursiveDelete(subFile);
            }
            if (!subFile.delete()) {
                throw new IOException("File / Directory '" + subFile.getAbsolutePath() + "' couldn't be deleted!");
            }
        }
    }
}
