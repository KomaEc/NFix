package org.apache.commons.digester.xmlrules;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.BeanPropertySetterRule;
import org.apache.commons.digester.CallMethodRule;
import org.apache.commons.digester.CallParamRule;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.FactoryCreateRule;
import org.apache.commons.digester.ObjectCreateRule;
import org.apache.commons.digester.ObjectCreationFactory;
import org.apache.commons.digester.ObjectParamRule;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.RuleSetBase;
import org.apache.commons.digester.Rules;
import org.apache.commons.digester.SetNextRule;
import org.apache.commons.digester.SetPropertiesRule;
import org.apache.commons.digester.SetPropertyRule;
import org.apache.commons.digester.SetRootRule;
import org.apache.commons.digester.SetTopRule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class DigesterRuleParser extends RuleSetBase {
   public static final String DIGESTER_PUBLIC_ID = "-//Jakarta Apache //DTD digester-rules XML V1.0//EN";
   private String digesterDtdUrl;
   protected Digester targetDigester;
   protected String basePath;
   protected DigesterRuleParser.PatternStack patternStack;
   private Set includedFiles;
   // $FF: synthetic field
   static Class class$org$apache$commons$digester$Rule;

   public DigesterRuleParser() {
      this.basePath = "";
      this.includedFiles = new HashSet();
      this.patternStack = new DigesterRuleParser.PatternStack();
   }

   public DigesterRuleParser(Digester targetDigester) {
      this.basePath = "";
      this.includedFiles = new HashSet();
      this.targetDigester = targetDigester;
      this.patternStack = new DigesterRuleParser.PatternStack();
   }

   private DigesterRuleParser(Digester targetDigester, DigesterRuleParser.PatternStack stack, Set includedFiles) {
      this.basePath = "";
      this.includedFiles = new HashSet();
      this.targetDigester = targetDigester;
      this.patternStack = stack;
      this.includedFiles = includedFiles;
   }

   public void setTarget(Digester d) {
      this.targetDigester = d;
   }

   public void setBasePath(String path) {
      if (path == null) {
         this.basePath = "";
      } else if (path.length() > 0 && !path.endsWith("/")) {
         this.basePath = path + "/";
      } else {
         this.basePath = path;
      }

   }

   public void setDigesterRulesDTD(String dtdURL) {
      this.digesterDtdUrl = dtdURL;
   }

   protected String getDigesterRulesDTD() {
      return this.digesterDtdUrl;
   }

   public void add(Rule rule) {
      this.targetDigester.addRule(this.basePath + this.patternStack.toString(), rule);
   }

   public void addRuleInstances(Digester digester) {
      String ruleClassName = (class$org$apache$commons$digester$Rule == null ? (class$org$apache$commons$digester$Rule = class$("org.apache.commons.digester.Rule")) : class$org$apache$commons$digester$Rule).getName();
      digester.register("-//Jakarta Apache //DTD digester-rules XML V1.0//EN", this.getDigesterRulesDTD());
      digester.addRule("*/pattern", new DigesterRuleParser.PatternRule("value"));
      digester.addRule("*/include", new DigesterRuleParser.IncludeRule());
      digester.addFactoryCreate("*/bean-property-setter-rule", (ObjectCreationFactory)(new DigesterRuleParser.BeanPropertySetterRuleFactory()));
      digester.addRule("*/bean-property-setter-rule", new DigesterRuleParser.PatternRule("pattern"));
      digester.addSetNext("*/bean-property-setter-rule", "add", ruleClassName);
      digester.addFactoryCreate("*/call-method-rule", (ObjectCreationFactory)(new DigesterRuleParser.CallMethodRuleFactory()));
      digester.addRule("*/call-method-rule", new DigesterRuleParser.PatternRule("pattern"));
      digester.addSetNext("*/call-method-rule", "add", ruleClassName);
      digester.addFactoryCreate("*/object-param-rule", (ObjectCreationFactory)(new DigesterRuleParser.ObjectParamRuleFactory()));
      digester.addRule("*/object-param-rule", new DigesterRuleParser.PatternRule("pattern"));
      digester.addSetNext("*/object-param-rule", "add", ruleClassName);
      digester.addFactoryCreate("*/call-param-rule", (ObjectCreationFactory)(new DigesterRuleParser.CallParamRuleFactory()));
      digester.addRule("*/call-param-rule", new DigesterRuleParser.PatternRule("pattern"));
      digester.addSetNext("*/call-param-rule", "add", ruleClassName);
      digester.addFactoryCreate("*/factory-create-rule", (ObjectCreationFactory)(new DigesterRuleParser.FactoryCreateRuleFactory()));
      digester.addRule("*/factory-create-rule", new DigesterRuleParser.PatternRule("pattern"));
      digester.addSetNext("*/factory-create-rule", "add", ruleClassName);
      digester.addFactoryCreate("*/object-create-rule", (ObjectCreationFactory)(new DigesterRuleParser.ObjectCreateRuleFactory()));
      digester.addRule("*/object-create-rule", new DigesterRuleParser.PatternRule("pattern"));
      digester.addSetNext("*/object-create-rule", "add", ruleClassName);
      digester.addFactoryCreate("*/set-properties-rule", (ObjectCreationFactory)(new DigesterRuleParser.SetPropertiesRuleFactory()));
      digester.addRule("*/set-properties-rule", new DigesterRuleParser.PatternRule("pattern"));
      digester.addSetNext("*/set-properties-rule", "add", ruleClassName);
      digester.addRule("*/set-properties-rule/alias", new DigesterRuleParser.SetPropertiesAliasRule());
      digester.addFactoryCreate("*/set-property-rule", (ObjectCreationFactory)(new DigesterRuleParser.SetPropertyRuleFactory()));
      digester.addRule("*/set-property-rule", new DigesterRuleParser.PatternRule("pattern"));
      digester.addSetNext("*/set-property-rule", "add", ruleClassName);
      digester.addFactoryCreate("*/set-top-rule", (ObjectCreationFactory)(new DigesterRuleParser.SetTopRuleFactory()));
      digester.addRule("*/set-top-rule", new DigesterRuleParser.PatternRule("pattern"));
      digester.addSetNext("*/set-top-rule", "add", ruleClassName);
      digester.addFactoryCreate("*/set-next-rule", (ObjectCreationFactory)(new DigesterRuleParser.SetNextRuleFactory()));
      digester.addRule("*/set-next-rule", new DigesterRuleParser.PatternRule("pattern"));
      digester.addSetNext("*/set-next-rule", "add", ruleClassName);
      digester.addFactoryCreate("*/set-root-rule", (ObjectCreationFactory)(new DigesterRuleParser.SetRootRuleFactory()));
      digester.addRule("*/set-root-rule", new DigesterRuleParser.PatternRule("pattern"));
      digester.addSetNext("*/set-root-rule", "add", ruleClassName);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   // $FF: synthetic method
   DigesterRuleParser(Digester x0, DigesterRuleParser.PatternStack x1, Set x2, Object x3) {
      this(x0, x1, x2);
   }

   protected class SetPropertiesAliasRule extends Rule {
      public SetPropertiesAliasRule() {
      }

      public void begin(Attributes attributes) {
         String attrName = attributes.getValue("attr-name");
         String propName = attributes.getValue("prop-name");
         SetPropertiesRule rule = (SetPropertiesRule)this.digester.peek();
         rule.addAlias(attrName, propName);
      }
   }

   protected class SetRootRuleFactory extends AbstractObjectCreationFactory {
      public Object createObject(Attributes attributes) {
         String methodName = attributes.getValue("methodname");
         String paramType = attributes.getValue("paramtype");
         return paramType != null && paramType.length() != 0 ? new SetRootRule(methodName, paramType) : new SetRootRule(methodName);
      }
   }

   protected class SetNextRuleFactory extends AbstractObjectCreationFactory {
      public Object createObject(Attributes attributes) {
         String methodName = attributes.getValue("methodname");
         String paramType = attributes.getValue("paramtype");
         return paramType != null && paramType.length() != 0 ? new SetNextRule(methodName, paramType) : new SetNextRule(methodName);
      }
   }

   protected class SetTopRuleFactory extends AbstractObjectCreationFactory {
      public Object createObject(Attributes attributes) {
         String methodName = attributes.getValue("methodname");
         String paramType = attributes.getValue("paramtype");
         return paramType != null && paramType.length() != 0 ? new SetTopRule(methodName, paramType) : new SetTopRule(methodName);
      }
   }

   protected class SetPropertyRuleFactory extends AbstractObjectCreationFactory {
      public Object createObject(Attributes attributes) {
         String name = attributes.getValue("name");
         String value = attributes.getValue("value");
         return new SetPropertyRule(name, value);
      }
   }

   protected class SetPropertiesRuleFactory extends AbstractObjectCreationFactory {
      public Object createObject(Attributes attributes) {
         return new SetPropertiesRule();
      }
   }

   protected class ObjectCreateRuleFactory extends AbstractObjectCreationFactory {
      public Object createObject(Attributes attributes) {
         String className = attributes.getValue("classname");
         String attrName = attributes.getValue("attrname");
         return attrName != null && attrName.length() != 0 ? new ObjectCreateRule(className, attrName) : new ObjectCreateRule(className);
      }
   }

   protected class FactoryCreateRuleFactory extends AbstractObjectCreationFactory {
      public Object createObject(Attributes attributes) {
         String className = attributes.getValue("classname");
         String attrName = attributes.getValue("attrname");
         boolean ignoreExceptions = "true".equalsIgnoreCase(attributes.getValue("ignore-exceptions"));
         return attrName != null && attrName.length() != 0 ? new FactoryCreateRule(className, attrName, ignoreExceptions) : new FactoryCreateRule(className, ignoreExceptions);
      }
   }

   protected class ObjectParamRuleFactory extends AbstractObjectCreationFactory {
      public Object createObject(Attributes attributes) throws Exception {
         int paramIndex = Integer.parseInt(attributes.getValue("paramnumber"));
         String attributeName = attributes.getValue("attrname");
         String type = attributes.getValue("type");
         String value = attributes.getValue("value");
         Rule objectParamRule = null;
         if (type == null) {
            throw new RuntimeException("Attribute 'type' is required.");
         } else {
            Object param = null;
            Class clazz = Class.forName(type);
            if (value == null) {
               param = clazz.newInstance();
            } else {
               param = ConvertUtils.convert(value, clazz);
            }

            if (attributeName == null) {
               objectParamRule = new ObjectParamRule(paramIndex, param);
            } else {
               objectParamRule = new ObjectParamRule(paramIndex, attributeName, param);
            }

            return objectParamRule;
         }
      }
   }

   protected class CallParamRuleFactory extends AbstractObjectCreationFactory {
      public Object createObject(Attributes attributes) {
         int paramIndex = Integer.parseInt(attributes.getValue("paramnumber"));
         String attributeName = attributes.getValue("attrname");
         String fromStack = attributes.getValue("from-stack");
         Rule callParamRule = null;
         if (attributeName == null) {
            if (fromStack == null) {
               callParamRule = new CallParamRule(paramIndex);
            } else {
               callParamRule = new CallParamRule(paramIndex, Boolean.valueOf(fromStack));
            }
         } else {
            if (fromStack != null) {
               throw new RuntimeException("Attributes from-stack and attrname cannot both be present.");
            }

            callParamRule = new CallParamRule(paramIndex, attributeName);
         }

         return callParamRule;
      }
   }

   protected class CallMethodRuleFactory extends AbstractObjectCreationFactory {
      public Object createObject(Attributes attributes) {
         Rule callMethodRule = null;
         String methodName = attributes.getValue("methodname");
         if (attributes.getValue("paramcount") == null) {
            callMethodRule = new CallMethodRule(methodName);
         } else {
            int paramCount = Integer.parseInt(attributes.getValue("paramcount"));
            String paramTypesAttr = attributes.getValue("paramtypes");
            if (paramTypesAttr != null && paramTypesAttr.length() != 0) {
               ArrayList paramTypes = new ArrayList();
               StringTokenizer tokens = new StringTokenizer(paramTypesAttr, " \t\n\r,");

               while(tokens.hasMoreTokens()) {
                  paramTypes.add(tokens.nextToken());
               }

               callMethodRule = new CallMethodRule(methodName, paramCount, (String[])paramTypes.toArray(new String[0]));
            } else {
               callMethodRule = new CallMethodRule(methodName, paramCount);
            }
         }

         return callMethodRule;
      }
   }

   private class BeanPropertySetterRuleFactory extends AbstractObjectCreationFactory {
      private BeanPropertySetterRuleFactory() {
      }

      public Object createObject(Attributes attributes) throws Exception {
         Rule beanPropertySetterRule = null;
         String propertyname = attributes.getValue("propertyname");
         if (propertyname == null) {
            beanPropertySetterRule = new BeanPropertySetterRule();
         } else {
            beanPropertySetterRule = new BeanPropertySetterRule(propertyname);
         }

         return beanPropertySetterRule;
      }

      // $FF: synthetic method
      BeanPropertySetterRuleFactory(Object x1) {
         this();
      }
   }

   private class RulesPrefixAdapter implements Rules {
      private Rules delegate;
      private String prefix;

      public RulesPrefixAdapter(String patternPrefix, Rules rules) {
         this.prefix = patternPrefix;
         this.delegate = rules;
      }

      public void add(String pattern, Rule rule) {
         StringBuffer buffer = new StringBuffer();
         buffer.append(this.prefix);
         if (!pattern.startsWith("/")) {
            buffer.append('/');
         }

         buffer.append(pattern);
         this.delegate.add(buffer.toString(), rule);
      }

      public void clear() {
         this.delegate.clear();
      }

      public Digester getDigester() {
         return this.delegate.getDigester();
      }

      public String getNamespaceURI() {
         return this.delegate.getNamespaceURI();
      }

      /** @deprecated */
      public List match(String pattern) {
         return this.delegate.match(pattern);
      }

      public List match(String namespaceURI, String pattern) {
         return this.delegate.match(namespaceURI, pattern);
      }

      public List rules() {
         return this.delegate.rules();
      }

      public void setDigester(Digester digester) {
         this.delegate.setDigester(digester);
      }

      public void setNamespaceURI(String namespaceURI) {
         this.delegate.setNamespaceURI(namespaceURI);
      }
   }

   private class IncludeRule extends Rule {
      public IncludeRule() {
      }

      public void begin(Attributes attributes) throws Exception {
         String fileName = attributes.getValue("path");
         if (fileName != null && fileName.length() > 0) {
            this.includeXMLRules(fileName);
         }

         String className = attributes.getValue("class");
         if (className != null && className.length() > 0) {
            this.includeProgrammaticRules(className);
         }

      }

      private void includeXMLRules(String fileName) throws IOException, SAXException, CircularIncludeException {
         ClassLoader cl = Thread.currentThread().getContextClassLoader();
         if (cl == null) {
            cl = DigesterRuleParser.this.getClass().getClassLoader();
         }

         URL fileURL = cl.getResource(fileName);
         if (fileURL == null) {
            throw new FileNotFoundException("File \"" + fileName + "\" not found.");
         } else {
            fileName = fileURL.toExternalForm();
            if (!DigesterRuleParser.this.includedFiles.add(fileName)) {
               throw new CircularIncludeException(fileName);
            } else {
               DigesterRuleParser includedSet = new DigesterRuleParser(DigesterRuleParser.this.targetDigester, DigesterRuleParser.this.patternStack, DigesterRuleParser.this.includedFiles);
               includedSet.setDigesterRulesDTD(DigesterRuleParser.this.getDigesterRulesDTD());
               Digester digester = new Digester();
               digester.addRuleSet(includedSet);
               digester.push(DigesterRuleParser.this);
               digester.parse(fileName);
               DigesterRuleParser.this.includedFiles.remove(fileName);
            }
         }
      }

      private void includeProgrammaticRules(String className) throws ClassNotFoundException, ClassCastException, InstantiationException, IllegalAccessException {
         Class cls = Class.forName(className);
         DigesterRulesSource rulesSource = (DigesterRulesSource)cls.newInstance();
         Rules digesterRules = DigesterRuleParser.this.targetDigester.getRules();
         Rules prefixWrapper = DigesterRuleParser.this.new RulesPrefixAdapter(DigesterRuleParser.this.patternStack.toString(), digesterRules);
         DigesterRuleParser.this.targetDigester.setRules(prefixWrapper);

         try {
            rulesSource.getRules(DigesterRuleParser.this.targetDigester);
         } finally {
            DigesterRuleParser.this.targetDigester.setRules(digesterRules);
         }

      }
   }

   private class PatternRule extends Rule {
      private String attrName;
      private String pattern = null;

      public PatternRule(String attrName) {
         this.attrName = attrName;
      }

      public void begin(Attributes attributes) {
         this.pattern = attributes.getValue(this.attrName);
         if (this.pattern != null) {
            DigesterRuleParser.this.patternStack.push(this.pattern);
         }

      }

      public void end() {
         if (this.pattern != null) {
            DigesterRuleParser.this.patternStack.pop();
         }

      }
   }

   protected class PatternStack extends ArrayStack {
      public String toString() {
         StringBuffer str = new StringBuffer();

         for(int i = 0; i < this.size(); ++i) {
            String elem = this.get(i).toString();
            if (elem.length() > 0) {
               if (str.length() > 0) {
                  str.append('/');
               }

               str.append(elem);
            }
         }

         return str.toString();
      }
   }
}
