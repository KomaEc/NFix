package org.apache.maven.doxia.module.site.manager;

public class SiteModuleNotFoundException extends Exception {
   public SiteModuleNotFoundException(String message) {
      super(message);
   }

   public SiteModuleNotFoundException(Throwable cause) {
      super(cause);
   }

   public SiteModuleNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }
}
