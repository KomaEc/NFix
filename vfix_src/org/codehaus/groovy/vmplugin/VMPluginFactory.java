package org.codehaus.groovy.vmplugin;

import org.codehaus.groovy.vmplugin.v4.Java4;

public class VMPluginFactory {
   private static final String JDK5_CLASSNAME_CHECK = "java.lang.annotation.Annotation";
   private static final String JDK5_PLUGIN_NAME = "org.codehaus.groovy.vmplugin.v5.Java5";
   private static final String JDK6_CLASSNAME_CHECK = "javax.script.ScriptEngine";
   private static final String JDK6_PLUGIN_NAME = "org.codehaus.groovy.vmplugin.v6.Java6";
   private static VMPlugin plugin;

   public static VMPlugin getPlugin() {
      return plugin;
   }

   static {
      ClassLoader loader;
      try {
         loader = VMPluginFactory.class.getClassLoader();
         loader.loadClass("javax.script.ScriptEngine");
         plugin = (VMPlugin)loader.loadClass("org.codehaus.groovy.vmplugin.v6.Java6").newInstance();
      } catch (Exception var2) {
      }

      if (plugin == null) {
         try {
            loader = VMPluginFactory.class.getClassLoader();
            loader.loadClass("java.lang.annotation.Annotation");
            plugin = (VMPlugin)loader.loadClass("org.codehaus.groovy.vmplugin.v5.Java5").newInstance();
         } catch (Exception var1) {
            plugin = new Java4();
         }
      }

   }
}
