/*
 *Copyright 2012 Peter Daum 
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotations allows to specify a directory and a set of files which has to be created in this directory.
 * 
 * If this annotation is used in class scope it will be used for every testmethod. In scope of a testmethod it is only valid for this single test method
 * @author mrpaeddah
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DirectorySetup {
    String directory();
    String[] files() default {};
}
