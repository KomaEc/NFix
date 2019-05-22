package org.codehaus.groovy.tools.groovydoc;

import java.io.File;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class FileOutputTool implements OutputTool {
   public void makeOutputArea(String filename) {
      File dir = new File(filename);
      dir.mkdirs();
   }

   public void writeToOutput(String fileName, String text) throws Exception {
      File file = new File(fileName);
      file.getParentFile().mkdirs();
      DefaultGroovyMethods.write(file, text);
   }
}
