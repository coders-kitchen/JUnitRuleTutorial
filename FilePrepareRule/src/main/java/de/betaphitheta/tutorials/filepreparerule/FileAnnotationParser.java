/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.tutorials.filepreparerule;

import org.junit.runner.Description;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * This class parses the requested directory and file structure.
 * 
 * The structure is configured by the FileSetup and DirectorySetup annotations.
 * Both annotations can be used in scope of a test-description or a test-class. If the
 * scope is test-class, the provided information are used for every test-description.
 * 
 * On the other hand a test-description scoped annotation is only valid when the description is executed.
 * @author peter
 */
public class FileAnnotationParser {

    final Description description;
    final Object target;
    final List<String> generalFiles;
    final HashMap<String, HashSet<String>> directoryStructure;

    public FileAnnotationParser(final Description description, final Object classUnderTest) {
        this.description = description;
        this.target = classUnderTest;
        generalFiles = new ArrayList<String>();
        directoryStructure = new HashMap<String, HashSet<String>>();
    }

    public HashMap<String, HashSet<String>> parseAnnotationsAndCreateStructure() {
        prepareGeneralFileSetupIfRequested();
        prepareGeneralDirectorySetupIfRequested();
        createDirectoryStructureMap();
        return directoryStructure;
    }

    private void parseIfAnnotationIsNotNull(Annotation[] annotations) {
        if (annotations != null) {
            parseStructureFromAnnotations(annotations);
        }
    }

    private void parseStructureFromAnnotations(Annotation[] annotations) {
        for (Annotation ann : annotations) {
            if (ann instanceof FileSetup) {
                continue;
            }
            if (ann instanceof DirectorySetup) {
                DirectorySetup setup = (DirectorySetup) ann;
                final String currentDir = setup.directory();
                String[] files = setup.files();
                if (!directoryStructure.containsKey(currentDir)) {
                    HashSet<String> fileList = new HashSet<String>();
                    directoryStructure.put(currentDir, fileList);
                }
                HashSet<String> fileList = directoryStructure.get(currentDir);
                fileList.addAll(generalFiles);
                fileList.addAll(Arrays.asList(files));
            }
        }
    }

    private void prepareGeneralFileSetupIfRequested() {
        FileSetup fileSetup = description.getAnnotation(FileSetup.class);
        addFilesToGeneralFiles(fileSetup);
        fileSetup = target.getClass().getAnnotation(FileSetup.class);
        addFilesToGeneralFiles(fileSetup);
    }

    private void addFilesToGeneralFiles(FileSetup fileSetup) {
        if (fileSetup != null) {
            generalFiles.addAll(Arrays.asList(fileSetup.files()));
        }
    }

    private void prepareGeneralDirectorySetupIfRequested() {
        Annotation[] annotations = target.getClass().getAnnotations();
        parseIfAnnotationIsNotNull(annotations);
    }

    private void createDirectoryStructureMap() {
        Collection<Annotation> annotations;
        annotations = description.getAnnotations();
        parseIfAnnotationIsNotNull(annotations.toArray(new Annotation[annotations.size()]));
    }
}
