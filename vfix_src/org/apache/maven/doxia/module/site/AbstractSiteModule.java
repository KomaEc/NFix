package org.apache.maven.doxia.module.site;

public abstract class AbstractSiteModule implements SiteModule {
   private String sourceDirectory;
   private String extension;

   public String getSourceDirectory() {
      return this.sourceDirectory;
   }

   public String getExtension() {
      return this.extension;
   }
}
