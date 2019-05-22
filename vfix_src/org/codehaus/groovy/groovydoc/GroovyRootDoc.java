package org.codehaus.groovy.groovydoc;

import java.util.List;
import java.util.Map;

public interface GroovyRootDoc extends GroovyDoc, GroovyDocErrorReporter {
   GroovyClassDoc classNamed(String var1);

   GroovyClassDoc[] classes();

   String[][] options();

   GroovyPackageDoc packageNamed(String var1);

   GroovyClassDoc[] specifiedClasses();

   GroovyPackageDoc[] specifiedPackages();

   Map<String, GroovyClassDoc> getVisibleClasses(List var1);
}
