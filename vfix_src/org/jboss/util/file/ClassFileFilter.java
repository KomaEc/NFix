package org.jboss.util.file;

public class ClassFileFilter implements ArchiveBrowser.Filter {
   public boolean accept(String filename) {
      return filename.endsWith(".class");
   }
}
