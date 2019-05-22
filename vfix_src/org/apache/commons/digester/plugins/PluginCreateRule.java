package org.apache.commons.digester.plugins;

import java.util.List;
import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.xml.sax.Attributes;

public class PluginCreateRule extends Rule implements InitializableRule {
   private String pluginClassAttrNs = null;
   private String pluginClassAttr = null;
   private String pluginIdAttrNs = null;
   private String pluginIdAttr = null;
   private String pattern;
   private Class baseClass = null;
   private Declaration defaultPlugin;
   private PluginConfigurationException initException;
   // $FF: synthetic field
   static Class class$java$lang$Object;

   public PluginCreateRule(Class baseClass) {
      this.baseClass = baseClass;
   }

   public PluginCreateRule(Class baseClass, Class dfltPluginClass) {
      this.baseClass = baseClass;
      if (dfltPluginClass != null) {
         this.defaultPlugin = new Declaration(dfltPluginClass);
      }

   }

   public PluginCreateRule(Class baseClass, Class dfltPluginClass, RuleLoader dfltPluginRuleLoader) {
      this.baseClass = baseClass;
      if (dfltPluginClass != null) {
         this.defaultPlugin = new Declaration(dfltPluginClass, dfltPluginRuleLoader);
      }

   }

   public void setPluginClassAttribute(String namespaceUri, String attrName) {
      this.pluginClassAttrNs = namespaceUri;
      this.pluginClassAttr = attrName;
   }

   public void setPluginIdAttribute(String namespaceUri, String attrName) {
      this.pluginIdAttrNs = namespaceUri;
      this.pluginIdAttr = attrName;
   }

   public void postRegisterInit(String matchPattern) throws PluginConfigurationException {
      Log log = LogUtils.getLogger(this.digester);
      boolean debug = log.isDebugEnabled();
      if (debug) {
         log.debug("PluginCreateRule.postRegisterInit: rule registered for pattern [" + matchPattern + "]");
      }

      if (this.digester == null) {
         this.initException = new PluginConfigurationException("Invalid invocation of postRegisterInit: digester not set.");
         throw this.initException;
      } else if (this.pattern != null) {
         this.initException = new PluginConfigurationException("A single PluginCreateRule instance has been mapped to multiple patterns; this is not supported.");
         throw this.initException;
      } else if (matchPattern.indexOf(42) != -1) {
         this.initException = new PluginConfigurationException("A PluginCreateRule instance has been mapped to pattern [" + matchPattern + "]." + " This pattern includes a wildcard character." + " This is not supported by the plugin architecture.");
         throw this.initException;
      } else {
         if (this.baseClass == null) {
            this.baseClass = class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object;
         }

         PluginRules rules = (PluginRules)this.digester.getRules();
         PluginManager pm = rules.getPluginManager();
         if (this.defaultPlugin != null) {
            if (!this.baseClass.isAssignableFrom(this.defaultPlugin.getPluginClass())) {
               this.initException = new PluginConfigurationException("Default class [" + this.defaultPlugin.getPluginClass().getName() + "] does not inherit from [" + this.baseClass.getName() + "].");
               throw this.initException;
            }

            try {
               this.defaultPlugin.init(this.digester, pm);
            } catch (PluginException var7) {
               throw new PluginConfigurationException(var7.getMessage(), var7.getCause());
            }
         }

         this.pattern = matchPattern;
         if (this.pluginClassAttr == null) {
            this.pluginClassAttrNs = rules.getPluginClassAttrNs();
            this.pluginClassAttr = rules.getPluginClassAttr();
            if (debug) {
               log.debug("init: pluginClassAttr set to per-digester values [ns=" + this.pluginClassAttrNs + ", name=" + this.pluginClassAttr + "]");
            }
         } else if (debug) {
            log.debug("init: pluginClassAttr set to rule-specific values [ns=" + this.pluginClassAttrNs + ", name=" + this.pluginClassAttr + "]");
         }

         if (this.pluginIdAttr == null) {
            this.pluginIdAttrNs = rules.getPluginIdAttrNs();
            this.pluginIdAttr = rules.getPluginIdAttr();
            if (debug) {
               log.debug("init: pluginIdAttr set to per-digester values [ns=" + this.pluginIdAttrNs + ", name=" + this.pluginIdAttr + "]");
            }
         } else if (debug) {
            log.debug("init: pluginIdAttr set to rule-specific values [ns=" + this.pluginIdAttrNs + ", name=" + this.pluginIdAttr + "]");
         }

      }
   }

   public void begin(String namespace, String name, Attributes attributes) throws Exception {
      Log log = this.digester.getLogger();
      boolean debug = log.isDebugEnabled();
      if (debug) {
         log.debug("PluginCreateRule.begin: pattern=[" + this.pattern + "]" + " match=[" + this.digester.getMatch() + "]");
      }

      if (this.initException != null) {
         throw this.initException;
      } else {
         String path = this.digester.getMatch();
         PluginRules oldRules = (PluginRules)this.digester.getRules();
         PluginRules newRules = new PluginRules(path, oldRules);
         this.digester.setRules(newRules);
         PluginManager pluginManager = newRules.getPluginManager();
         Declaration currDeclaration = null;
         if (debug) {
            log.debug("PluginCreateRule.begin: installing new plugin: oldrules=" + oldRules.toString() + ", newrules=" + newRules.toString());
         }

         String pluginClassName;
         if (this.pluginClassAttrNs == null) {
            pluginClassName = attributes.getValue(this.pluginClassAttr);
         } else {
            pluginClassName = attributes.getValue(this.pluginClassAttrNs, this.pluginClassAttr);
         }

         String pluginId;
         if (this.pluginIdAttrNs == null) {
            pluginId = attributes.getValue(this.pluginIdAttr);
         } else {
            pluginId = attributes.getValue(this.pluginIdAttrNs, this.pluginIdAttr);
         }

         if (pluginClassName != null) {
            currDeclaration = pluginManager.getDeclarationByClass(pluginClassName);
            if (currDeclaration == null) {
               currDeclaration = new Declaration(pluginClassName);

               try {
                  currDeclaration.init(this.digester, pluginManager);
               } catch (PluginException var16) {
                  throw new PluginInvalidInputException(var16.getMessage(), var16.getCause());
               }

               pluginManager.addDeclaration(currDeclaration);
            }
         } else if (pluginId != null) {
            currDeclaration = pluginManager.getDeclarationById(pluginId);
            if (currDeclaration == null) {
               throw new PluginInvalidInputException("Plugin id [" + pluginId + "] is not defined.");
            }
         } else {
            if (this.defaultPlugin == null) {
               throw new PluginInvalidInputException("No plugin class specified for element " + this.pattern);
            }

            currDeclaration = this.defaultPlugin;
         }

         currDeclaration.configure(this.digester, this.pattern);
         Class pluginClass = currDeclaration.getPluginClass();
         Object instance = pluginClass.newInstance();
         this.getDigester().push(instance);
         if (debug) {
            log.debug("PluginCreateRule.begin: pattern=[" + this.pattern + "]" + " match=[" + this.digester.getMatch() + "]" + " pushed instance of plugin [" + pluginClass.getName() + "]");
         }

         List rules = newRules.getDecoratedRules().match(namespace, path);
         this.fireBeginMethods(rules, namespace, name, attributes);
      }
   }

   public void body(String namespace, String name, String text) throws Exception {
      String path = this.digester.getMatch();
      PluginRules newRules = (PluginRules)this.digester.getRules();
      List rules = newRules.getDecoratedRules().match(namespace, path);
      this.fireBodyMethods(rules, namespace, name, text);
   }

   public void end(String namespace, String name) throws Exception {
      String path = this.digester.getMatch();
      PluginRules newRules = (PluginRules)this.digester.getRules();
      List rules = newRules.getDecoratedRules().match(namespace, path);
      this.fireEndMethods(rules, namespace, name);
      this.digester.setRules(newRules.getParent());
      this.digester.pop();
   }

   public String getPattern() {
      return this.pattern;
   }

   public void fireBeginMethods(List rules, String namespace, String name, Attributes list) throws Exception {
      if (rules != null && rules.size() > 0) {
         Log log = this.digester.getLogger();
         boolean debug = log.isDebugEnabled();

         for(int i = 0; i < rules.size(); ++i) {
            try {
               Rule rule = (Rule)rules.get(i);
               if (debug) {
                  log.debug("  Fire begin() for " + rule);
               }

               rule.begin(namespace, name, list);
            } catch (Exception var10) {
               throw this.digester.createSAXException(var10);
            } catch (Error var11) {
               throw var11;
            }
         }
      }

   }

   private void fireBodyMethods(List rules, String namespaceURI, String name, String text) throws Exception {
      if (rules != null && rules.size() > 0) {
         Log log = this.digester.getLogger();
         boolean debug = log.isDebugEnabled();

         for(int i = 0; i < rules.size(); ++i) {
            try {
               Rule rule = (Rule)rules.get(i);
               if (debug) {
                  log.debug("  Fire body() for " + rule);
               }

               rule.body(namespaceURI, name, text);
            } catch (Exception var10) {
               throw this.digester.createSAXException(var10);
            } catch (Error var11) {
               throw var11;
            }
         }
      }

   }

   public void fireEndMethods(List rules, String namespaceURI, String name) throws Exception {
      if (rules != null) {
         Log log = this.digester.getLogger();
         boolean debug = log.isDebugEnabled();

         for(int i = 0; i < rules.size(); ++i) {
            int j = rules.size() - i - 1;

            try {
               Rule rule = (Rule)rules.get(j);
               if (debug) {
                  log.debug("  Fire end() for " + rule);
               }

               rule.end(namespaceURI, name);
            } catch (Exception var10) {
               throw this.digester.createSAXException(var10);
            } catch (Error var11) {
               throw var11;
            }
         }
      }

   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
