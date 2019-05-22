package org.apache.maven.doxia.module.apt;

import org.apache.maven.doxia.module.site.AbstractSiteModule;

public class AptSiteModule extends AbstractSiteModule {
   public String getSourceDirectory() {
      return "apt";
   }

   public String getExtension() {
      return "apt";
   }

   public String getParserId() {
      return "apt";
   }
}
