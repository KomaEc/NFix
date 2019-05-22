package org.codehaus.groovy.tools.groovydoc;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class FileSystemResourceManager implements ResourceManager {
   private String basedir;
   private static final String FS = "/";

   public FileSystemResourceManager() {
      this.basedir = "";
   }

   public FileSystemResourceManager(String basedir) {
      this.basedir = basedir + "/";
   }

   public Reader getReader(String resourceName) throws IOException {
      return DefaultGroovyMethods.newReader(new File(this.basedir + resourceName));
   }
}
