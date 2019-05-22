package org.apache.maven.doxia.siterenderer;

import java.io.File;

class ModuleReference {
   private final String parserId;
   private final File basedir;

   ModuleReference(String parserId, File basedir) {
      this.parserId = parserId;
      this.basedir = basedir;
   }

   public String getParserId() {
      return this.parserId;
   }

   public File getBasedir() {
      return this.basedir;
   }
}
