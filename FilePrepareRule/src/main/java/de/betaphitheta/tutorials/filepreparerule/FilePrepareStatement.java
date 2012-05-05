/*
 *Copyright 2012 Peter Daum aka mrpaeddah
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package de.betaphitheta.tutorials.filepreparerule;

import org.junit.runners.model.Statement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This Statement wraps the original statement.
 * It grants creation and deletion of a requested directory and file structure
 * to be used by the Statement base.
 * 
 * The directory and file structure is provided by an HashMap<String, ArrayList<String>>.
 * 
 * @author peter
 */
public class FilePrepareStatement extends Statement {

    private Statement base;
    private final HashMap<String, HashSet<String>> directoryStructure;

    public FilePrepareStatement(final Statement base, HashMap<String, HashSet<String>> directoryStructure) {
        this.base = base;
        this.directoryStructure = directoryStructure;
    }

    @Override
    public void evaluate() throws Throwable {
        try {
            setupStructure();
            base.evaluate();
        } finally {
                tearDownStructure();
        }
    }

    private void setupStructure() throws IOException {
        Set<String> directories = directoryStructure.keySet();
        for (String currentDir : directories) {
            File directory = new File(currentDir);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new IOException("Directory '" + directory.getAbsolutePath() + "' does not exist and creation failed!");
            }
            HashSet<String> files = directoryStructure.get(currentDir);
            for (String currentFile : files) {
                File toCreate = new File(directory, currentFile);
                try {
                    if (!toCreate.createNewFile()) {
                        throw new IOException("Touch of file '" + toCreate.getAbsolutePath() + "' failed!");
                    }
                } catch (Throwable t) {
                    IOException ex = new IOException("Touch of file '" + toCreate.getAbsolutePath() + "' failed!");
                    ex.setStackTrace(t.getStackTrace());
                    throw ex;
                }
            }
        }
    }

    private void tearDownStructure() throws IOException {
        Set<String> directories = directoryStructure.keySet();
        for (String directory : directories) {
            recursiveDelete(new File(directory));
        }
    }

    private void recursiveDelete(File directory) throws IOException {
        File[] listed = directory.listFiles();
        for (File subFile : listed) {
            if(!subFile.exists())
                continue;
            if (subFile.isDirectory()) {
                recursiveDelete(subFile);
            }
            if (!subFile.delete()) {
                throw new IOException("File / Directory '" + subFile.getAbsolutePath() + "' couldn't be deleted!");
            }
        }
    }
}
