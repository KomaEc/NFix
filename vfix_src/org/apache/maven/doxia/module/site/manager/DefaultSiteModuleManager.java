package org.apache.maven.doxia.module.site.manager;

import java.util.Collection;
import java.util.Map;
import org.apache.maven.doxia.module.site.SiteModule;

public class DefaultSiteModuleManager implements SiteModuleManager {
   private Map siteModules;

   public Collection getSiteModules() {
      return this.siteModules.values();
   }

   public SiteModule getSiteModule(String id) throws SiteModuleNotFoundException {
      SiteModule siteModule = (SiteModule)this.siteModules.get(id);
      if (siteModule == null) {
         throw new SiteModuleNotFoundException("Cannot find site module id = " + id);
      } else {
         return siteModule;
      }
   }
}
