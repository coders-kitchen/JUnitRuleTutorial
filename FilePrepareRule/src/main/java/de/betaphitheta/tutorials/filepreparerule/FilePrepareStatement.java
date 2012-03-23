/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.tutorials.filepreparerule;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * This Statement wraps the original statement.
 * It creates and deletes the requested file / directory structure for an single test method or a whole testclass.
 * 
 * It uses DirectorySetup and FileSetup annotations to fulfil this task. Both annotations
 * are allowed to be used on method and class scope.
 * 
 * If the testclass is annoted the request structure is created for every testcase.
 * On the other hand if a special structure is onyl required for a single testmethod, this can be done by using the 
 * annotations directly on the testmethod.
 * 
 * If the testclass is annotated with FileSetup, this leads to a default setup of files for every annotated testmethod.
 * @author mrpaeddah
 */
public class FilePrepareStatement extends Statement {

    private final Statement base;
    private final FrameworkMethod method;
    private final Object target;
    private List<String> generalFiles;
    private List<String> directories;

    public FilePrepareStatement(final Statement base, 
            final FrameworkMethod method, 
            final Object target) {
        this.base = base;
        this.method = method;
        this.target = target;
        directories = new ArrayList<String>();
    }

    @Override
    public void evaluate() throws Throwable {
        try {
            prepareGeneralFileSetupIfRequested();
            prepareDirectorySetup();
            base.evaluate();
        } finally {
            deleteDirectorySetup();
        }
    }

    private void prepareGeneralFileSetupIfRequested() {
        FileSetup fileSetup = method.getAnnotation(FileSetup.class);
        addFilesToGeneralFiles(fileSetup);
        fileSetup = target.getClass().getAnnotation(FileSetup.class);
        addFilesToGeneralFiles(fileSetup);
    }

    private void addFilesToGeneralFiles(FileSetup fileSetup) {
        if (fileSetup != null) {
            generalFiles.addAll(Arrays.asList(fileSetup.files()));
        }
    }

    private void prepareDirectorySetup() throws IOException {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation ann : annotations) {
            if (ann instanceof FileSetup) {
                continue;
            }
            if (ann instanceof DirectorySetup) {
                createDirectorySetup((DirectorySetup) ann);
            }
        }
    }

    private void createDirectorySetup(DirectorySetup ann) throws IOException {
        final String currentDir = ann.directory();
        String[] files = ann.files();
        directories.add(currentDir);
        File directory = new File(currentDir);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Directory '" + directory.getAbsolutePath() + "' does not exist and creation failed!");
        }
        
        if (files != null) {
            for (String currentFile : files) {
                File toCreate = new File(directory, currentFile);
                if(!toCreate.createNewFile())
                    throw new IOException("Touch of file '" + toCreate.getAbsolutePath() + "' failed!");
            }
        }
    }

    private void deleteDirectorySetup() throws IOException {
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
