package org.codehaus.groovy.groovydoc;

public interface GroovyPackageDoc extends GroovyDoc {
   GroovyClassDoc[] allClasses();

   GroovyClassDoc[] allClasses(boolean var1);

   GroovyClassDoc[] enums();

   GroovyClassDoc[] errors();

   GroovyClassDoc[] exceptions();

   GroovyClassDoc findClass(String var1);

   GroovyClassDoc[] interfaces();

   GroovyClassDoc[] ordinaryClasses();

   String summary();

   String description();

   String nameWithDots();

   String getRelativeRootPath();
}
