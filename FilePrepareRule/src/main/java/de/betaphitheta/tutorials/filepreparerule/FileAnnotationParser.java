/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.betaphitheta.tutorials.filepreparerule;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.junit.runners.model.FrameworkMethod;

/**
 * This class parses the requested directory and file structure.
 * 
 * The structure is configured by the FileSetup and DirectorySetup annotations.
 * Both annotations can be used in scope of a test-method or a test-class. If the
 * scope is test-class, the provided informations are used for every test-method.
 * 
 * On the other hand a test-method scoped annotation is only valid when the method is executed.
 * @author peter
 */
public class FileAnnotationParser {

    final FrameworkMethod method;
    final Object target;
    final List<String> generalFiles;
    final HashMap<String, HashSet<String>> directoryStructure;

    public FileAnnotationParser(final FrameworkMethod method, final Object target) {
        this.method = method;
        this.target = target;
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

    private void prepareGeneralDirectorySetupIfRequested() {
        Annotation[] annotations = target.getClass().getAnnotations();
        parseIfAnnotationIsNotNull(annotations);
    }

    private void createDirectoryStructureMap() {
        Annotation[] annotations = method.getAnnotations();
        parseIfAnnotationIsNotNull(annotations);
    }
}
