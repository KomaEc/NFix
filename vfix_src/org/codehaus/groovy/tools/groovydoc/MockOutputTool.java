package org.codehaus.groovy.tools.groovydoc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MockOutputTool implements OutputTool {
   Set outputAreas = new HashSet();
   Map output = new HashMap();

   public void makeOutputArea(String filename) {
      this.outputAreas.add(filename);
   }

   public void writeToOutput(String fileName, String text) throws Exception {
      this.output.put(fileName, text);
   }

   public boolean isValidOutputArea(String fileName) {
      return this.outputAreas.contains(fileName);
   }

   public String getText(String fileName) {
      return (String)this.output.get(fileName);
   }

   public String toString() {
      return "dirs:" + this.outputAreas + ", files:" + this.output.keySet();
   }
}
