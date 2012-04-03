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

import java.util.HashMap;
import java.util.HashSet;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * This implementation of MethodRule returns a FilePrepareStatement which 
 * creates and deletes the requested file structure for a test method.
 * @author peter
 */
public class FilePrepareRule implements MethodRule {
    public Statement apply(final Statement base, FrameworkMethod method, Object target) {
        FileAnnotationParser fileAnnotationParser = new FileAnnotationParser(method, target);
        final HashMap<String, HashSet<String>> structure = fileAnnotationParser.parseAnnotationsAndCreateStructure();
        return new FilePrepareStatement(base, structure);
    }
}
