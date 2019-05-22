package org.apache.maven.doxia.module.site;

public interface SiteModule {
   String ROLE = SiteModule.class.getName();

   String getSourceDirectory();

   String getExtension();

   String getParserId();
}
