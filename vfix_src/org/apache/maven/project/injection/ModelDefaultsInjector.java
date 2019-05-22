package org.apache.maven.project.injection;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;

public interface ModelDefaultsInjector {
   String ROLE = ModelDefaultsInjector.class.getName();

   void injectDefaults(Model var1);

   void mergePluginWithDefaults(Plugin var1, Plugin var2);
}
