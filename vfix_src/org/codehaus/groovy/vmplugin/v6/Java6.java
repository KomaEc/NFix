package org.codehaus.groovy.vmplugin.v6;

import org.codehaus.groovy.vmplugin.v5.Java5;

public class Java6 extends Java5 {
   private static final Class[] PLUGIN_DGM = new Class[]{PluginDefaultGroovyMethods.class, org.codehaus.groovy.vmplugin.v5.PluginDefaultGroovyMethods.class};

   public Class[] getPluginDefaultGroovyMethods() {
      return PLUGIN_DGM;
   }
}
