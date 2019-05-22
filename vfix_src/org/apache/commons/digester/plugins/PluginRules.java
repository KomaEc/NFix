package org.apache.commons.digester.plugins;

import java.util.List;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.Rules;
import org.apache.commons.digester.RulesBase;
import org.apache.commons.logging.Log;

public class PluginRules implements Rules {
   protected Digester digester;
   private Rules decoratedRules;
   private PluginManager pluginManager;
   private String mountPoint;
   private PluginRules parent;
   private PluginContext pluginContext;

   public PluginRules() {
      this(new RulesBase());
   }

   public PluginRules(Rules decoratedRules) {
      this.digester = null;
      this.mountPoint = null;
      this.parent = null;
      this.pluginContext = null;
      this.decoratedRules = decoratedRules;
      this.pluginContext = new PluginContext();
      this.pluginManager = new PluginManager(this.pluginContext);
   }

   PluginRules(String mountPoint, PluginRules parent) {
      this.digester = null;
      this.mountPoint = null;
      this.parent = null;
      this.pluginContext = null;
      this.decoratedRules = new RulesBase();
      this.pluginContext = parent.pluginContext;
      this.pluginManager = new PluginManager(parent.pluginManager);
      this.mountPoint = mountPoint;
      this.parent = parent;
   }

   public Rules getParent() {
      return this.parent;
   }

   public Digester getDigester() {
      return this.digester;
   }

   public void setDigester(Digester digester) {
      this.digester = digester;
      this.decoratedRules.setDigester(digester);
   }

   public String getNamespaceURI() {
      return this.decoratedRules.getNamespaceURI();
   }

   public void setNamespaceURI(String namespaceURI) {
      this.decoratedRules.setNamespaceURI(namespaceURI);
   }

   public PluginManager getPluginManager() {
      return this.pluginManager;
   }

   public List getRuleFinders() {
      return this.pluginContext.getRuleFinders();
   }

   public void setRuleFinders(List ruleFinders) {
      this.pluginContext.setRuleFinders(ruleFinders);
   }

   Rules getDecoratedRules() {
      return this.decoratedRules;
   }

   public List rules() {
      return this.decoratedRules.rules();
   }

   public void add(String pattern, Rule rule) {
      Log log = LogUtils.getLogger(this.digester);
      boolean debug = log.isDebugEnabled();
      if (debug) {
         log.debug("add entry: mapping pattern [" + pattern + "]" + " to rule of type [" + rule.getClass().getName() + "]");
      }

      if (pattern.startsWith("/")) {
         pattern = pattern.substring(1);
      }

      if (this.mountPoint != null && !pattern.equals(this.mountPoint) && !pattern.startsWith(this.mountPoint + "/")) {
         log.warn("An attempt was made to add a rule with a pattern thatis not at or below the mountpoint of the current PluginRules object. Rule pattern: " + pattern + ", mountpoint: " + this.mountPoint + ", rule type: " + rule.getClass().getName());
      } else {
         this.decoratedRules.add(pattern, rule);
         if (rule instanceof InitializableRule) {
            try {
               ((InitializableRule)rule).postRegisterInit(pattern);
            } catch (PluginConfigurationException var6) {
               if (debug) {
                  log.debug("Rule initialisation failed", var6);
               }

               return;
            }
         }

         if (debug) {
            log.debug("add exit: mapped pattern [" + pattern + "]" + " to rule of type [" + rule.getClass().getName() + "]");
         }

      }
   }

   public void clear() {
      this.decoratedRules.clear();
   }

   /** @deprecated */
   public List match(String path) {
      return this.match((String)null, path);
   }

   public List match(String namespaceURI, String path) {
      Log log = LogUtils.getLogger(this.digester);
      boolean debug = log.isDebugEnabled();
      if (debug) {
         log.debug("Matching path [" + path + "] on rules object " + this.toString());
      }

      List matches;
      if (this.mountPoint != null && path.length() <= this.mountPoint.length()) {
         if (debug) {
            log.debug("Path [" + path + "] delegated to parent.");
         }

         matches = this.parent.match(namespaceURI, path);
      } else {
         matches = this.decoratedRules.match(namespaceURI, path);
      }

      return matches;
   }

   public void setPluginClassAttribute(String namespaceUri, String attrName) {
      this.pluginContext.setPluginClassAttribute(namespaceUri, attrName);
   }

   public void setPluginIdAttribute(String namespaceUri, String attrName) {
      this.pluginContext.setPluginIdAttribute(namespaceUri, attrName);
   }

   public String getPluginClassAttrNs() {
      return this.pluginContext.getPluginClassAttrNs();
   }

   public String getPluginClassAttr() {
      return this.pluginContext.getPluginClassAttr();
   }

   public String getPluginIdAttrNs() {
      return this.pluginContext.getPluginIdAttrNs();
   }

   public String getPluginIdAttr() {
      return this.pluginContext.getPluginIdAttr();
   }
}
