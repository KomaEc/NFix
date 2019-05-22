package org.apache.maven.doxia.module.xdoc;

import org.apache.maven.doxia.module.site.AbstractSiteModule;

public class XdocSiteModule extends AbstractSiteModule {
   public String getSourceDirectory() {
      return "xdoc";
   }

   public String getExtension() {
      return "xml";
   }

   public String getParserId() {
      return "xdoc";
   }
}
