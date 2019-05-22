package org.apache.maven.doxia.macro;

import java.io.File;
import java.util.Map;

public class MacroRequest {
   private File basedir;
   private Map parameters;

   public MacroRequest(Map param, File base) {
      this.parameters = param;
      this.basedir = base;
   }

   public File getBasedir() {
      return this.basedir;
   }

   public void setBasedir(File base) {
      this.basedir = base;
   }

   public Map getParameters() {
      return this.parameters;
   }

   public Object getParameter(String key) {
      return this.parameters.get(key);
   }
}
