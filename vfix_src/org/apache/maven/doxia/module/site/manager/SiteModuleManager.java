package org.apache.maven.doxia.module.site.manager;

import java.util.Collection;
import org.apache.maven.doxia.module.site.SiteModule;

public interface SiteModuleManager {
   String ROLE = SiteModuleManager.class.getName();

   Collection getSiteModules();

   SiteModule getSiteModule(String var1) throws SiteModuleNotFoundException;
}
