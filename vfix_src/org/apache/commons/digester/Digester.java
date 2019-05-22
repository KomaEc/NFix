package org.apache.commons.digester;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Digester extends DefaultHandler {
   protected StringBuffer bodyText = new StringBuffer();
   protected ArrayStack bodyTexts = new ArrayStack();
   protected ArrayStack matches = new ArrayStack(10);
   protected ClassLoader classLoader = null;
   protected boolean configured = false;
   protected EntityResolver entityResolver;
   protected HashMap entityValidator = new HashMap();
   protected ErrorHandler errorHandler = null;
   protected SAXParserFactory factory = null;
   /** @deprecated */
   protected String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
   protected Locator locator = null;
   protected String match = "";
   protected boolean namespaceAware = false;
   protected HashMap namespaces = new HashMap();
   protected ArrayStack params = new ArrayStack();
   protected SAXParser parser = null;
   protected String publicId = null;
   protected XMLReader reader = null;
   protected Object root = null;
   protected Rules rules = null;
   protected String schemaLanguage = "http://www.w3.org/2001/XMLSchema";
   protected String schemaLocation = null;
   protected ArrayStack stack = new ArrayStack();
   protected boolean useContextClassLoader = false;
   protected boolean validating = false;
   protected Log log = LogFactory.getLog("org.apache.commons.digester.Digester");
   protected Log saxLog = LogFactory.getLog("org.apache.commons.digester.Digester.sax");
   protected static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
   protected Substitutor substitutor;
   private HashMap stacksByName = new HashMap();

   public Digester() {
   }

   public Digester(SAXParser parser) {
      this.parser = parser;
   }

   public Digester(XMLReader reader) {
      this.reader = reader;
   }

   public String findNamespaceURI(String prefix) {
      ArrayStack stack = (ArrayStack)this.namespaces.get(prefix);
      if (stack == null) {
         return null;
      } else {
         try {
            return (String)stack.peek();
         } catch (EmptyStackException var4) {
            return null;
         }
      }
   }

   public ClassLoader getClassLoader() {
      if (this.classLoader != null) {
         return this.classLoader;
      } else {
         if (this.useContextClassLoader) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null) {
               return classLoader;
            }
         }

         return this.getClass().getClassLoader();
      }
   }

   public void setClassLoader(ClassLoader classLoader) {
      this.classLoader = classLoader;
   }

   public int getCount() {
      return this.stack.size();
   }

   public String getCurrentElementName() {
      String elementName = this.match;
      int lastSlash = elementName.lastIndexOf(47);
      if (lastSlash >= 0) {
         elementName = elementName.substring(lastSlash + 1);
      }

      return elementName;
   }

   /** @deprecated */
   public int getDebug() {
      return 0;
   }

   /** @deprecated */
   public void setDebug(int debug) {
   }

   public ErrorHandler getErrorHandler() {
      return this.errorHandler;
   }

   public void setErrorHandler(ErrorHandler errorHandler) {
      this.errorHandler = errorHandler;
   }

   public SAXParserFactory getFactory() {
      if (this.factory == null) {
         this.factory = SAXParserFactory.newInstance();
         this.factory.setNamespaceAware(this.namespaceAware);
         this.factory.setValidating(this.validating);
      }

      return this.factory;
   }

   public boolean getFeature(String feature) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
      return this.getFactory().getFeature(feature);
   }

   public void setFeature(String feature, boolean value) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
      this.getFactory().setFeature(feature, value);
   }

   public Log getLogger() {
      return this.log;
   }

   public void setLogger(Log log) {
      this.log = log;
   }

   public Log getSAXLogger() {
      return this.saxLog;
   }

   public void setSAXLogger(Log saxLog) {
      this.saxLog = saxLog;
   }

   public String getMatch() {
      return this.match;
   }

   public boolean getNamespaceAware() {
      return this.namespaceAware;
   }

   public void setNamespaceAware(boolean namespaceAware) {
      this.namespaceAware = namespaceAware;
   }

   public void setPublicId(String publicId) {
      this.publicId = publicId;
   }

   public String getPublicId() {
      return this.publicId;
   }

   public String getRuleNamespaceURI() {
      return this.getRules().getNamespaceURI();
   }

   public void setRuleNamespaceURI(String ruleNamespaceURI) {
      this.getRules().setNamespaceURI(ruleNamespaceURI);
   }

   public SAXParser getParser() {
      if (this.parser != null) {
         return this.parser;
      } else {
         try {
            if (this.validating) {
               Properties properties = new Properties();
               properties.put("SAXParserFactory", this.getFactory());
               if (this.schemaLocation != null) {
                  properties.put("schemaLocation", this.schemaLocation);
                  properties.put("schemaLanguage", this.schemaLanguage);
               }

               this.parser = ParserFeatureSetterFactory.newSAXParser(properties);
            } else {
               this.parser = this.getFactory().newSAXParser();
            }
         } catch (Exception var2) {
            this.log.error("Digester.getParser: ", var2);
            return null;
         }

         return this.parser;
      }
   }

   public Object getProperty(String property) throws SAXNotRecognizedException, SAXNotSupportedException {
      return this.getParser().getProperty(property);
   }

   public void setProperty(String property, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
      this.getParser().setProperty(property, value);
   }

   /** @deprecated */
   public XMLReader getReader() {
      try {
         return this.getXMLReader();
      } catch (SAXException var2) {
         this.log.error("Cannot get XMLReader", var2);
         return null;
      }
   }

   public Rules getRules() {
      if (this.rules == null) {
         this.rules = new RulesBase();
         this.rules.setDigester(this);
      }

      return this.rules;
   }

   public void setRules(Rules rules) {
      this.rules = rules;
      this.rules.setDigester(this);
   }

   public String getSchema() {
      return this.schemaLocation;
   }

   public void setSchema(String schemaLocation) {
      this.schemaLocation = schemaLocation;
   }

   public String getSchemaLanguage() {
      return this.schemaLanguage;
   }

   public void setSchemaLanguage(String schemaLanguage) {
      this.schemaLanguage = schemaLanguage;
   }

   public boolean getUseContextClassLoader() {
      return this.useContextClassLoader;
   }

   public void setUseContextClassLoader(boolean use) {
      this.useContextClassLoader = use;
   }

   public boolean getValidating() {
      return this.validating;
   }

   public void setValidating(boolean validating) {
      this.validating = validating;
   }

   public XMLReader getXMLReader() throws SAXException {
      if (this.reader == null) {
         this.reader = this.getParser().getXMLReader();
      }

      this.reader.setDTDHandler(this);
      this.reader.setContentHandler(this);
      if (this.entityResolver == null) {
         this.reader.setEntityResolver(this);
      } else {
         this.reader.setEntityResolver(this.entityResolver);
      }

      this.reader.setErrorHandler(this);
      return this.reader;
   }

   public Substitutor getSubstitutor() {
      return this.substitutor;
   }

   public void setSubstitutor(Substitutor substitutor) {
      this.substitutor = substitutor;
   }

   public void characters(char[] buffer, int start, int length) throws SAXException {
      if (this.saxLog.isDebugEnabled()) {
         this.saxLog.debug("characters(" + new String(buffer, start, length) + ")");
      }

      this.bodyText.append(buffer, start, length);
   }

   public void endDocument() throws SAXException {
      if (this.saxLog.isDebugEnabled()) {
         if (this.getCount() > 1) {
            this.saxLog.debug("endDocument():  " + this.getCount() + " elements left");
         } else {
            this.saxLog.debug("endDocument()");
         }
      }

      while(this.getCount() > 1) {
         this.pop();
      }

      Iterator rules = this.getRules().rules().iterator();

      while(rules.hasNext()) {
         Rule rule = (Rule)rules.next();

         try {
            rule.finish();
         } catch (Exception var5) {
            this.log.error("Finish event threw exception", var5);
            throw this.createSAXException(var5);
         } catch (Error var6) {
            this.log.error("Finish event threw error", var6);
            throw var6;
         }
      }

      this.clear();
   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      boolean debug = this.log.isDebugEnabled();
      if (debug) {
         if (this.saxLog.isDebugEnabled()) {
            this.saxLog.debug("endElement(" + namespaceURI + "," + localName + "," + qName + ")");
         }

         this.log.debug("  match='" + this.match + "'");
         this.log.debug("  bodyText='" + this.bodyText + "'");
      }

      String name = localName;
      if (localName == null || localName.length() < 1) {
         name = qName;
      }

      List rules = (List)this.matches.pop();
      if (rules != null && rules.size() > 0) {
         String bodyText = this.bodyText.toString();
         Substitutor substitutor = this.getSubstitutor();
         if (substitutor != null) {
            bodyText = substitutor.substitute(bodyText);
         }

         for(int i = 0; i < rules.size(); ++i) {
            try {
               Rule rule = (Rule)rules.get(i);
               if (debug) {
                  this.log.debug("  Fire body() for " + rule);
               }

               rule.body(namespaceURI, name, bodyText);
            } catch (Exception var14) {
               this.log.error("Body event threw exception", var14);
               throw this.createSAXException(var14);
            } catch (Error var15) {
               this.log.error("Body event threw error", var15);
               throw var15;
            }
         }
      } else if (debug) {
         this.log.debug("  No rules found matching '" + this.match + "'.");
      }

      this.bodyText = (StringBuffer)this.bodyTexts.pop();
      if (debug) {
         this.log.debug("  Popping body text '" + this.bodyText.toString() + "'");
      }

      int slash;
      if (rules != null) {
         for(slash = 0; slash < rules.size(); ++slash) {
            int j = rules.size() - slash - 1;

            try {
               Rule rule = (Rule)rules.get(j);
               if (debug) {
                  this.log.debug("  Fire end() for " + rule);
               }

               rule.end(namespaceURI, name);
            } catch (Exception var12) {
               this.log.error("End event threw exception", var12);
               throw this.createSAXException(var12);
            } catch (Error var13) {
               this.log.error("End event threw error", var13);
               throw var13;
            }
         }
      }

      slash = this.match.lastIndexOf(47);
      if (slash >= 0) {
         this.match = this.match.substring(0, slash);
      } else {
         this.match = "";
      }

   }

   public void endPrefixMapping(String prefix) throws SAXException {
      if (this.saxLog.isDebugEnabled()) {
         this.saxLog.debug("endPrefixMapping(" + prefix + ")");
      }

      ArrayStack stack = (ArrayStack)this.namespaces.get(prefix);
      if (stack != null) {
         try {
            stack.pop();
            if (stack.empty()) {
               this.namespaces.remove(prefix);
            }

         } catch (EmptyStackException var4) {
            throw this.createSAXException("endPrefixMapping popped too many times");
         }
      }
   }

   public void ignorableWhitespace(char[] buffer, int start, int len) throws SAXException {
      if (this.saxLog.isDebugEnabled()) {
         this.saxLog.debug("ignorableWhitespace(" + new String(buffer, start, len) + ")");
      }

   }

   public void processingInstruction(String target, String data) throws SAXException {
      if (this.saxLog.isDebugEnabled()) {
         this.saxLog.debug("processingInstruction('" + target + "','" + data + "')");
      }

   }

   public Locator getDocumentLocator() {
      return this.locator;
   }

   public void setDocumentLocator(Locator locator) {
      if (this.saxLog.isDebugEnabled()) {
         this.saxLog.debug("setDocumentLocator(" + locator + ")");
      }

      this.locator = locator;
   }

   public void skippedEntity(String name) throws SAXException {
      if (this.saxLog.isDebugEnabled()) {
         this.saxLog.debug("skippedEntity(" + name + ")");
      }

   }

   public void startDocument() throws SAXException {
      if (this.saxLog.isDebugEnabled()) {
         this.saxLog.debug("startDocument()");
      }

      this.configure();
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes list) throws SAXException {
      boolean debug = this.log.isDebugEnabled();
      if (this.saxLog.isDebugEnabled()) {
         this.saxLog.debug("startElement(" + namespaceURI + "," + localName + "," + qName + ")");
      }

      this.bodyTexts.push(this.bodyText);
      if (debug) {
         this.log.debug("  Pushing body text '" + this.bodyText.toString() + "'");
      }

      this.bodyText = new StringBuffer();
      String name = localName;
      if (localName == null || localName.length() < 1) {
         name = qName;
      }

      StringBuffer sb = new StringBuffer(this.match);
      if (this.match.length() > 0) {
         sb.append('/');
      }

      sb.append(name);
      this.match = sb.toString();
      if (debug) {
         this.log.debug("  New match='" + this.match + "'");
      }

      List rules = this.getRules().match(namespaceURI, this.match);
      this.matches.push(rules);
      if (rules != null && rules.size() > 0) {
         Substitutor substitutor = this.getSubstitutor();
         if (substitutor != null) {
            list = substitutor.substitute(list);
         }

         for(int i = 0; i < rules.size(); ++i) {
            try {
               Rule rule = (Rule)rules.get(i);
               if (debug) {
                  this.log.debug("  Fire begin() for " + rule);
               }

               rule.begin(namespaceURI, name, list);
            } catch (Exception var13) {
               this.log.error("Begin event threw exception", var13);
               throw this.createSAXException(var13);
            } catch (Error var14) {
               this.log.error("Begin event threw error", var14);
               throw var14;
            }
         }
      } else if (debug) {
         this.log.debug("  No rules found matching '" + this.match + "'.");
      }

   }

   public void startPrefixMapping(String prefix, String namespaceURI) throws SAXException {
      if (this.saxLog.isDebugEnabled()) {
         this.saxLog.debug("startPrefixMapping(" + prefix + "," + namespaceURI + ")");
      }

      ArrayStack stack = (ArrayStack)this.namespaces.get(prefix);
      if (stack == null) {
         stack = new ArrayStack();
         this.namespaces.put(prefix, stack);
      }

      stack.push(namespaceURI);
   }

   public void notationDecl(String name, String publicId, String systemId) {
      if (this.saxLog.isDebugEnabled()) {
         this.saxLog.debug("notationDecl(" + name + "," + publicId + "," + systemId + ")");
      }

   }

   public void unparsedEntityDecl(String name, String publicId, String systemId, String notation) {
      if (this.saxLog.isDebugEnabled()) {
         this.saxLog.debug("unparsedEntityDecl(" + name + "," + publicId + "," + systemId + "," + notation + ")");
      }

   }

   public void setEntityResolver(EntityResolver entityResolver) {
      this.entityResolver = entityResolver;
   }

   public EntityResolver getEntityResolver() {
      return this.entityResolver;
   }

   public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
      if (this.saxLog.isDebugEnabled()) {
         this.saxLog.debug("resolveEntity('" + publicId + "', '" + systemId + "')");
      }

      if (publicId != null) {
         this.publicId = publicId;
      }

      String entityURL = null;
      if (publicId != null) {
         entityURL = (String)this.entityValidator.get(publicId);
      }

      if (this.schemaLocation != null && entityURL == null && systemId != null) {
         entityURL = (String)this.entityValidator.get(systemId);
      }

      if (entityURL == null) {
         if (systemId == null) {
            if (this.log.isDebugEnabled()) {
               this.log.debug(" Cannot resolve entity: '" + entityURL + "'");
            }

            return null;
         }

         if (this.log.isDebugEnabled()) {
            this.log.debug(" Trying to resolve using system ID '" + systemId + "'");
         }

         entityURL = systemId;
      }

      if (this.log.isDebugEnabled()) {
         this.log.debug(" Resolving to alternate DTD '" + entityURL + "'");
      }

      try {
         return new InputSource(entityURL);
      } catch (Exception var5) {
         throw this.createSAXException(var5);
      }
   }

   public void error(SAXParseException exception) throws SAXException {
      this.log.error("Parse Error at line " + exception.getLineNumber() + " column " + exception.getColumnNumber() + ": " + exception.getMessage(), exception);
      if (this.errorHandler != null) {
         this.errorHandler.error(exception);
      }

   }

   public void fatalError(SAXParseException exception) throws SAXException {
      this.log.error("Parse Fatal Error at line " + exception.getLineNumber() + " column " + exception.getColumnNumber() + ": " + exception.getMessage(), exception);
      if (this.errorHandler != null) {
         this.errorHandler.fatalError(exception);
      }

   }

   public void warning(SAXParseException exception) throws SAXException {
      if (this.errorHandler != null) {
         this.log.warn("Parse Warning Error at line " + exception.getLineNumber() + " column " + exception.getColumnNumber() + ": " + exception.getMessage(), exception);
         this.errorHandler.warning(exception);
      }

   }

   /** @deprecated */
   public void log(String message) {
      this.log.info(message);
   }

   /** @deprecated */
   public void log(String message, Throwable exception) {
      this.log.error(message, exception);
   }

   public Object parse(File file) throws IOException, SAXException {
      this.configure();
      InputSource input = new InputSource(new FileInputStream(file));
      input.setSystemId("file://" + file.getAbsolutePath());
      this.getXMLReader().parse(input);
      return this.root;
   }

   public Object parse(InputSource input) throws IOException, SAXException {
      this.configure();
      this.getXMLReader().parse(input);
      return this.root;
   }

   public Object parse(InputStream input) throws IOException, SAXException {
      this.configure();
      InputSource is = new InputSource(input);
      this.getXMLReader().parse(is);
      return this.root;
   }

   public Object parse(Reader reader) throws IOException, SAXException {
      this.configure();
      InputSource is = new InputSource(reader);
      this.getXMLReader().parse(is);
      return this.root;
   }

   public Object parse(String uri) throws IOException, SAXException {
      this.configure();
      InputSource is = new InputSource(uri);
      this.getXMLReader().parse(is);
      return this.root;
   }

   public void register(String publicId, String entityURL) {
      if (this.log.isDebugEnabled()) {
         this.log.debug("register('" + publicId + "', '" + entityURL + "'");
      }

      this.entityValidator.put(publicId, entityURL);
   }

   public void addRule(String pattern, Rule rule) {
      rule.setDigester(this);
      this.getRules().add(pattern, rule);
   }

   public void addRuleSet(RuleSet ruleSet) {
      String oldNamespaceURI = this.getRuleNamespaceURI();
      String newNamespaceURI = ruleSet.getNamespaceURI();
      if (this.log.isDebugEnabled()) {
         if (newNamespaceURI == null) {
            this.log.debug("addRuleSet() with no namespace URI");
         } else {
            this.log.debug("addRuleSet() with namespace URI " + newNamespaceURI);
         }
      }

      this.setRuleNamespaceURI(newNamespaceURI);
      ruleSet.addRuleInstances(this);
      this.setRuleNamespaceURI(oldNamespaceURI);
   }

   public void addBeanPropertySetter(String pattern) {
      this.addRule(pattern, new BeanPropertySetterRule());
   }

   public void addBeanPropertySetter(String pattern, String propertyName) {
      this.addRule(pattern, new BeanPropertySetterRule(propertyName));
   }

   public void addCallMethod(String pattern, String methodName) {
      this.addRule(pattern, new CallMethodRule(methodName));
   }

   public void addCallMethod(String pattern, String methodName, int paramCount) {
      this.addRule(pattern, new CallMethodRule(methodName, paramCount));
   }

   public void addCallMethod(String pattern, String methodName, int paramCount, String[] paramTypes) {
      this.addRule(pattern, new CallMethodRule(methodName, paramCount, paramTypes));
   }

   public void addCallMethod(String pattern, String methodName, int paramCount, Class[] paramTypes) {
      this.addRule(pattern, new CallMethodRule(methodName, paramCount, paramTypes));
   }

   public void addCallParam(String pattern, int paramIndex) {
      this.addRule(pattern, new CallParamRule(paramIndex));
   }

   public void addCallParam(String pattern, int paramIndex, String attributeName) {
      this.addRule(pattern, new CallParamRule(paramIndex, attributeName));
   }

   public void addCallParam(String pattern, int paramIndex, boolean fromStack) {
      this.addRule(pattern, new CallParamRule(paramIndex, fromStack));
   }

   public void addCallParam(String pattern, int paramIndex, int stackIndex) {
      this.addRule(pattern, new CallParamRule(paramIndex, stackIndex));
   }

   public void addCallParamPath(String pattern, int paramIndex) {
      this.addRule(pattern, new PathCallParamRule(paramIndex));
   }

   public void addObjectParam(String pattern, int paramIndex, Object paramObj) {
      this.addRule(pattern, new ObjectParamRule(paramIndex, paramObj));
   }

   public void addFactoryCreate(String pattern, String className) {
      this.addFactoryCreate(pattern, className, false);
   }

   public void addFactoryCreate(String pattern, Class clazz) {
      this.addFactoryCreate(pattern, clazz, false);
   }

   public void addFactoryCreate(String pattern, String className, String attributeName) {
      this.addFactoryCreate(pattern, className, attributeName, false);
   }

   public void addFactoryCreate(String pattern, Class clazz, String attributeName) {
      this.addFactoryCreate(pattern, clazz, attributeName, false);
   }

   public void addFactoryCreate(String pattern, ObjectCreationFactory creationFactory) {
      this.addFactoryCreate(pattern, creationFactory, false);
   }

   public void addFactoryCreate(String pattern, String className, boolean ignoreCreateExceptions) {
      this.addRule(pattern, new FactoryCreateRule(className, ignoreCreateExceptions));
   }

   public void addFactoryCreate(String pattern, Class clazz, boolean ignoreCreateExceptions) {
      this.addRule(pattern, new FactoryCreateRule(clazz, ignoreCreateExceptions));
   }

   public void addFactoryCreate(String pattern, String className, String attributeName, boolean ignoreCreateExceptions) {
      this.addRule(pattern, new FactoryCreateRule(className, attributeName, ignoreCreateExceptions));
   }

   public void addFactoryCreate(String pattern, Class clazz, String attributeName, boolean ignoreCreateExceptions) {
      this.addRule(pattern, new FactoryCreateRule(clazz, attributeName, ignoreCreateExceptions));
   }

   public void addFactoryCreate(String pattern, ObjectCreationFactory creationFactory, boolean ignoreCreateExceptions) {
      creationFactory.setDigester(this);
      this.addRule(pattern, new FactoryCreateRule(creationFactory, ignoreCreateExceptions));
   }

   public void addObjectCreate(String pattern, String className) {
      this.addRule(pattern, new ObjectCreateRule(className));
   }

   public void addObjectCreate(String pattern, Class clazz) {
      this.addRule(pattern, new ObjectCreateRule(clazz));
   }

   public void addObjectCreate(String pattern, String className, String attributeName) {
      this.addRule(pattern, new ObjectCreateRule(className, attributeName));
   }

   public void addObjectCreate(String pattern, String attributeName, Class clazz) {
      this.addRule(pattern, new ObjectCreateRule(attributeName, clazz));
   }

   public void addSetNestedProperties(String pattern) {
      this.addRule(pattern, new SetNestedPropertiesRule());
   }

   public void addSetNestedProperties(String pattern, String elementName, String propertyName) {
      this.addRule(pattern, new SetNestedPropertiesRule(elementName, propertyName));
   }

   public void addSetNestedProperties(String pattern, String[] elementNames, String[] propertyNames) {
      this.addRule(pattern, new SetNestedPropertiesRule(elementNames, propertyNames));
   }

   public void addSetNext(String pattern, String methodName) {
      this.addRule(pattern, new SetNextRule(methodName));
   }

   public void addSetNext(String pattern, String methodName, String paramType) {
      this.addRule(pattern, new SetNextRule(methodName, paramType));
   }

   public void addSetRoot(String pattern, String methodName) {
      this.addRule(pattern, new SetRootRule(methodName));
   }

   public void addSetRoot(String pattern, String methodName, String paramType) {
      this.addRule(pattern, new SetRootRule(methodName, paramType));
   }

   public void addSetProperties(String pattern) {
      this.addRule(pattern, new SetPropertiesRule());
   }

   public void addSetProperties(String pattern, String attributeName, String propertyName) {
      this.addRule(pattern, new SetPropertiesRule(attributeName, propertyName));
   }

   public void addSetProperties(String pattern, String[] attributeNames, String[] propertyNames) {
      this.addRule(pattern, new SetPropertiesRule(attributeNames, propertyNames));
   }

   public void addSetProperty(String pattern, String name, String value) {
      this.addRule(pattern, new SetPropertyRule(name, value));
   }

   public void addSetTop(String pattern, String methodName) {
      this.addRule(pattern, new SetTopRule(methodName));
   }

   public void addSetTop(String pattern, String methodName, String paramType) {
      this.addRule(pattern, new SetTopRule(methodName, paramType));
   }

   public void clear() {
      this.match = "";
      this.bodyTexts.clear();
      this.params.clear();
      this.publicId = null;
      this.stack.clear();
   }

   public Object peek() {
      try {
         return this.stack.peek();
      } catch (EmptyStackException var2) {
         this.log.warn("Empty stack (returning null)");
         return null;
      }
   }

   public Object peek(int n) {
      try {
         return this.stack.peek(n);
      } catch (EmptyStackException var3) {
         this.log.warn("Empty stack (returning null)");
         return null;
      }
   }

   public Object pop() {
      try {
         return this.stack.pop();
      } catch (EmptyStackException var2) {
         this.log.warn("Empty stack (returning null)");
         return null;
      }
   }

   public void push(Object object) {
      if (this.stack.size() == 0) {
         this.root = object;
      }

      this.stack.push(object);
   }

   public void push(String stackName, Object value) {
      ArrayStack namedStack = (ArrayStack)this.stacksByName.get(stackName);
      if (namedStack == null) {
         namedStack = new ArrayStack();
         this.stacksByName.put(stackName, namedStack);
      }

      namedStack.push(value);
   }

   public Object pop(String stackName) {
      Object result = null;
      ArrayStack namedStack = (ArrayStack)this.stacksByName.get(stackName);
      if (namedStack == null) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Stack '" + stackName + "' is empty");
         }

         throw new EmptyStackException();
      } else {
         result = namedStack.pop();
         return result;
      }
   }

   public Object peek(String stackName) {
      Object result = null;
      ArrayStack namedStack = (ArrayStack)this.stacksByName.get(stackName);
      if (namedStack == null) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Stack '" + stackName + "' is empty");
         }

         throw new EmptyStackException();
      } else {
         result = namedStack.peek();
         return result;
      }
   }

   public boolean isEmpty(String stackName) {
      boolean result = true;
      ArrayStack namedStack = (ArrayStack)this.stacksByName.get(stackName);
      if (namedStack != null) {
         result = namedStack.isEmpty();
      }

      return result;
   }

   public Object getRoot() {
      return this.root;
   }

   protected void configure() {
      if (!this.configured) {
         this.initialize();
         this.configured = true;
      }
   }

   protected void initialize() {
   }

   Map getRegistrations() {
      return this.entityValidator;
   }

   /** @deprecated */
   List getRules(String match) {
      return this.getRules().match(match);
   }

   public Object peekParams() {
      try {
         return this.params.peek();
      } catch (EmptyStackException var2) {
         this.log.warn("Empty stack (returning null)");
         return null;
      }
   }

   public Object peekParams(int n) {
      try {
         return this.params.peek(n);
      } catch (EmptyStackException var3) {
         this.log.warn("Empty stack (returning null)");
         return null;
      }
   }

   public Object popParams() {
      try {
         if (this.log.isTraceEnabled()) {
            this.log.trace("Popping params");
         }

         return this.params.pop();
      } catch (EmptyStackException var2) {
         this.log.warn("Empty stack (returning null)");
         return null;
      }
   }

   public void pushParams(Object object) {
      if (this.log.isTraceEnabled()) {
         this.log.trace("Pushing params");
      }

      this.params.push(object);
   }

   public SAXException createSAXException(String message, Exception e) {
      if (e != null && e instanceof InvocationTargetException) {
         Throwable t = ((InvocationTargetException)e).getTargetException();
         if (t != null && t instanceof Exception) {
            e = (Exception)t;
         }
      }

      if (this.locator != null) {
         String error = "Error at (" + this.locator.getLineNumber() + ", " + this.locator.getColumnNumber() + ": " + message;
         return e != null ? new SAXParseException(error, this.locator, e) : new SAXParseException(error, this.locator);
      } else {
         this.log.error("No Locator!");
         return e != null ? new SAXException(message, e) : new SAXException(message);
      }
   }

   public SAXException createSAXException(Exception e) {
      if (e instanceof InvocationTargetException) {
         Throwable t = ((InvocationTargetException)e).getTargetException();
         if (t != null && t instanceof Exception) {
            e = (Exception)t;
         }
      }

      return this.createSAXException(e.getMessage(), e);
   }

   public SAXException createSAXException(String message) {
      return this.createSAXException(message, (Exception)null);
   }
}
