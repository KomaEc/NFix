package org.apache.commons.digester.plugins;

import java.util.Properties;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;

public class Declaration {
   private Class pluginClass;
   private String pluginClassName;
   private String id;
   private Properties properties = new Properties();
   private boolean initialized = false;
   private RuleLoader ruleLoader = null;

   public Declaration(String pluginClassName) {
      this.pluginClassName = pluginClassName;
   }

   public Declaration(Class pluginClass) {
      this.pluginClass = pluginClass;
      this.pluginClassName = pluginClass.getName();
   }

   public Declaration(Class pluginClass, RuleLoader ruleLoader) {
      this.pluginClass = pluginClass;
      this.pluginClassName = pluginClass.getName();
      this.ruleLoader = ruleLoader;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getId() {
      return this.id;
   }

   public void setProperties(Properties p) {
      this.properties.putAll(p);
   }

   public Class getPluginClass() {
      return this.pluginClass;
   }

   public void init(Digester digester, PluginManager pm) throws PluginException {
      Log log = digester.getLogger();
      boolean debug = log.isDebugEnabled();
      if (debug) {
         log.debug("init being called!");
      }

      if (this.initialized) {
         throw new PluginAssertionFailure("Init called multiple times.");
      } else {
         if (this.pluginClass == null && this.pluginClassName != null) {
            try {
               this.pluginClass = digester.getClassLoader().loadClass(this.pluginClassName);
            } catch (ClassNotFoundException var6) {
               throw new PluginException("Unable to load class " + this.pluginClassName, var6);
            }
         }

         if (this.ruleLoader == null) {
            log.debug("Searching for ruleloader...");
            this.ruleLoader = pm.findLoader(digester, this.id, this.pluginClass, this.properties);
         } else {
            log.debug("This declaration has an explicit ruleLoader.");
         }

         if (debug) {
            if (this.ruleLoader == null) {
               log.debug("No ruleLoader found for plugin declaration id [" + this.id + "]" + ", class [" + this.pluginClass.getClass().getName() + "].");
            } else {
               log.debug("RuleLoader of type [" + this.ruleLoader.getClass().getName() + "] associated with plugin declaration" + " id [" + this.id + "]" + ", class [" + this.pluginClass.getClass().getName() + "].");
            }
         }

         this.initialized = true;
      }
   }

   public void configure(Digester digester, String pattern) throws PluginException {
      Log log = digester.getLogger();
      boolean debug = log.isDebugEnabled();
      if (debug) {
         log.debug("configure being called!");
      }

      if (!this.initialized) {
         throw new PluginAssertionFailure("Not initialized.");
      } else {
         if (this.ruleLoader != null) {
            this.ruleLoader.addRules(digester, pattern);
         }

      }
   }
}
