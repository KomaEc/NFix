package org.apache.commons.digester;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.xml.sax.Attributes;

public class SetNestedPropertiesRule extends Rule {
   private static final String PROP_IGNORE = "ignore-me";
   private Log log = null;
   private SetNestedPropertiesRule.AnyChildRule anyChildRule = new SetNestedPropertiesRule.AnyChildRule();
   private SetNestedPropertiesRule.AnyChildRules newRules;
   private Rules oldRules;
   private boolean trimData;
   private boolean allowUnknownChildElements;
   private HashMap elementNames;

   public SetNestedPropertiesRule() {
      this.newRules = new SetNestedPropertiesRule.AnyChildRules(this.anyChildRule);
      this.oldRules = null;
      this.trimData = true;
      this.allowUnknownChildElements = false;
      this.elementNames = new HashMap();
   }

   public SetNestedPropertiesRule(String elementName, String propertyName) {
      this.newRules = new SetNestedPropertiesRule.AnyChildRules(this.anyChildRule);
      this.oldRules = null;
      this.trimData = true;
      this.allowUnknownChildElements = false;
      this.elementNames = new HashMap();
      this.elementNames.put(elementName, propertyName);
   }

   public SetNestedPropertiesRule(String[] elementNames, String[] propertyNames) {
      this.newRules = new SetNestedPropertiesRule.AnyChildRules(this.anyChildRule);
      this.oldRules = null;
      this.trimData = true;
      this.allowUnknownChildElements = false;
      this.elementNames = new HashMap();
      int i = 0;

      for(int size = elementNames.length; i < size; ++i) {
         String propName = null;
         if (i < propertyNames.length) {
            propName = propertyNames[i];
         }

         if (propName == null) {
            this.elementNames.put(elementNames[i], "ignore-me");
         } else {
            this.elementNames.put(elementNames[i], propName);
         }
      }

   }

   public void setDigester(Digester digester) {
      super.setDigester(digester);
      this.log = digester.getLogger();
      this.anyChildRule.setDigester(digester);
   }

   public void setTrimData(boolean trimData) {
      this.trimData = trimData;
   }

   public boolean getTrimData() {
      return this.trimData;
   }

   public void setAllowUnknownChildElements(boolean allowUnknownChildElements) {
      this.allowUnknownChildElements = allowUnknownChildElements;
   }

   public boolean getAllowUnknownChildElements() {
      return this.allowUnknownChildElements;
   }

   public void begin(String namespace, String name, Attributes attributes) throws Exception {
      this.oldRules = this.digester.getRules();
      this.newRules.init(this.digester.getMatch() + "/", this.oldRules);
      this.digester.setRules(this.newRules);
   }

   public void body(String bodyText) throws Exception {
      this.digester.setRules(this.oldRules);
   }

   public void addAlias(String elementName, String propertyName) {
      if (propertyName == null) {
         this.elementNames.put(elementName, "ignore-me");
      } else {
         this.elementNames.put(elementName, propertyName);
      }

   }

   public String toString() {
      return "SetNestedPropertiesRule";
   }

   private class AnyChildRule extends Rule {
      private String currChildNamespaceURI;
      private String currChildElementName;

      private AnyChildRule() {
         this.currChildNamespaceURI = null;
         this.currChildElementName = null;
      }

      public void begin(String namespaceURI, String name, Attributes attributes) throws Exception {
         this.currChildNamespaceURI = namespaceURI;
         this.currChildElementName = name;
      }

      public void body(String value) throws Exception {
         boolean debug = SetNestedPropertiesRule.this.log.isDebugEnabled();
         String propName = (String)SetNestedPropertiesRule.this.elementNames.get(this.currChildElementName);
         if (propName != "ignore-me") {
            if (propName == null) {
               propName = this.currChildElementName;
            }

            if (this.digester.log.isDebugEnabled()) {
               this.digester.log.debug("[SetNestedPropertiesRule]{" + this.digester.match + "} Setting property '" + propName + "' to '" + value + "'");
            }

            Object top = this.digester.peek();
            if (this.digester.log.isDebugEnabled()) {
               if (top != null) {
                  this.digester.log.debug("[SetNestedPropertiesRule]{" + this.digester.match + "} Set " + top.getClass().getName() + " properties");
               } else {
                  this.digester.log.debug("[SetPropertiesRule]{" + this.digester.match + "} Set NULL properties");
               }
            }

            if (SetNestedPropertiesRule.this.trimData) {
               value = value.trim();
            }

            if (!SetNestedPropertiesRule.this.allowUnknownChildElements) {
               if (top instanceof DynaBean) {
                  DynaProperty descx = ((DynaBean)top).getDynaClass().getDynaProperty(propName);
                  if (descx == null) {
                     throw new NoSuchMethodException("Bean has no property named " + propName);
                  }
               } else {
                  PropertyDescriptor desc = PropertyUtils.getPropertyDescriptor(top, propName);
                  if (desc == null) {
                     throw new NoSuchMethodException("Bean has no property named " + propName);
                  }
               }
            }

            BeanUtils.setProperty(top, propName, value);
         }
      }

      public void end(String namespace, String name) throws Exception {
         this.currChildElementName = null;
      }

      // $FF: synthetic method
      AnyChildRule(Object x1) {
         this();
      }
   }

   private class AnyChildRules implements Rules {
      private String matchPrefix = null;
      private Rules decoratedRules = null;
      private ArrayList rules = new ArrayList(1);
      private SetNestedPropertiesRule.AnyChildRule rule;

      public AnyChildRules(SetNestedPropertiesRule.AnyChildRule rule) {
         this.rule = rule;
         this.rules.add(rule);
      }

      public Digester getDigester() {
         return null;
      }

      public void setDigester(Digester digester) {
      }

      public String getNamespaceURI() {
         return null;
      }

      public void setNamespaceURI(String namespaceURI) {
      }

      public void add(String pattern, Rule rule) {
      }

      public void clear() {
      }

      public List match(String matchPath) {
         return this.match((String)null, matchPath);
      }

      public List match(String namespaceURI, String matchPath) {
         List match = this.decoratedRules.match(namespaceURI, matchPath);
         if (matchPath.startsWith(this.matchPrefix) && matchPath.indexOf(47, this.matchPrefix.length()) == -1) {
            if (match != null && match.size() != 0) {
               LinkedList newMatch = new LinkedList(match);
               newMatch.addLast(this.rule);
               return newMatch;
            } else {
               return this.rules;
            }
         } else {
            return match;
         }
      }

      public List rules() {
         throw new RuntimeException("AnyChildRules.rules not implemented.");
      }

      public void init(String prefix, Rules rules) {
         this.matchPrefix = prefix;
         this.decoratedRules = rules;
      }
   }
}
