package org.codehaus.groovy.tools.groovydoc;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class ClasspathResourceManager implements ResourceManager {
   ClassLoader classLoader;

   public ClasspathResourceManager() {
      this.classLoader = this.getClass().getClassLoader();
   }

   public ClasspathResourceManager(ClassLoader classLoader) {
      this.classLoader = classLoader;
   }

   public InputStream getInputStream(String resourceName) throws IOException {
      return this.classLoader.getResourceAsStream(resourceName);
   }

   public Reader getReader(String resourceName) throws IOException {
      return DefaultGroovyMethods.newReader(this.getInputStream(resourceName));
   }
}
