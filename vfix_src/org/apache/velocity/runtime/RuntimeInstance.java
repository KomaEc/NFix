package org.apache.velocity.runtime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.Template;
import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.app.event.EventHandler;
import org.apache.velocity.app.event.IncludeEventHandler;
import org.apache.velocity.app.event.InvalidReferenceEventHandler;
import org.apache.velocity.app.event.MethodExceptionEventHandler;
import org.apache.velocity.app.event.NullSetEventHandler;
import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.log.LogManager;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.runtime.resource.ContentResource;
import org.apache.velocity.runtime.resource.ResourceManager;
import org.apache.velocity.util.ClassUtils;
import org.apache.velocity.util.RuntimeServicesAware;
import org.apache.velocity.util.StringUtils;
import org.apache.velocity.util.introspection.Introspector;
import org.apache.velocity.util.introspection.Uberspect;
import org.apache.velocity.util.introspection.UberspectLoggable;

public class RuntimeInstance implements RuntimeConstants, RuntimeServices {
   private VelocimacroFactory vmFactory = null;
   private Log log = new Log();
   private ParserPool parserPool;
   private boolean initializing = false;
   private boolean initialized = false;
   private ExtendedProperties overridingProperties = null;
   private Hashtable runtimeDirectives;
   private ExtendedProperties configuration = new ExtendedProperties();
   private ResourceManager resourceManager = null;
   private EventCartridge eventCartridge = null;
   private Introspector introspector = null;
   private Map applicationAttributes = null;
   private Uberspect uberSpect;
   // $FF: synthetic field
   static Class class$org$apache$velocity$util$introspection$Uberspect;
   // $FF: synthetic field
   static Class class$org$apache$velocity$runtime$resource$ResourceManager;
   // $FF: synthetic field
   static Class class$org$apache$velocity$app$event$ReferenceInsertionEventHandler;
   // $FF: synthetic field
   static Class class$org$apache$velocity$app$event$NullSetEventHandler;
   // $FF: synthetic field
   static Class class$org$apache$velocity$app$event$MethodExceptionEventHandler;
   // $FF: synthetic field
   static Class class$org$apache$velocity$app$event$IncludeEventHandler;
   // $FF: synthetic field
   static Class class$org$apache$velocity$app$event$InvalidReferenceEventHandler;
   // $FF: synthetic field
   static Class class$org$apache$velocity$runtime$directive$Directive;
   // $FF: synthetic field
   static Class class$org$apache$velocity$runtime$ParserPool;

   public RuntimeInstance() {
      this.vmFactory = new VelocimacroFactory(this);
      this.introspector = new Introspector(this.getLog());
      this.applicationAttributes = new HashMap();
   }

   public synchronized void init() throws Exception {
      if (!this.initialized && !this.initializing) {
         this.initializing = true;
         this.log.trace("*******************************************************************");
         this.log.debug("Starting Apache Velocity v1.5 (compiled: 2007-02-22 08:52:29)");
         this.log.trace("RuntimeInstance initializing.");
         this.initializeProperties();
         this.initializeLog();
         this.initializeResourceManager();
         this.initializeDirectives();
         this.initializeEventHandlers();
         this.initializeParserPool();
         this.initializeIntrospection();
         this.vmFactory.initVelocimacro();
         this.log.trace("RuntimeInstance successfully initialized.");
         this.initialized = true;
         this.initializing = false;
      }

   }

   public boolean isInitialized() {
      return this.initialized;
   }

   private void initializeIntrospection() throws Exception {
      String rm = this.getString("runtime.introspector.uberspect");
      String err;
      if (rm != null && rm.length() > 0) {
         err = null;

         Object o;
         try {
            o = ClassUtils.getNewInstance(rm);
         } catch (ClassNotFoundException var5) {
            String err = "The specified class for Uberspect (" + rm + ") does not exist or is not accessible to the current classloader.";
            this.log.error(err);
            throw new Exception(err);
         }

         if (!(o instanceof Uberspect)) {
            String err = "The specified class for Uberspect (" + rm + ") does not implement " + (class$org$apache$velocity$util$introspection$Uberspect == null ? (class$org$apache$velocity$util$introspection$Uberspect = class$("org.apache.velocity.util.introspection.Uberspect")) : class$org$apache$velocity$util$introspection$Uberspect).getName() + "; Velocity is not initialized correctly.";
            this.log.error(err);
            throw new Exception(err);
         } else {
            this.uberSpect = (Uberspect)o;
            if (this.uberSpect instanceof UberspectLoggable) {
               ((UberspectLoggable)this.uberSpect).setLog(this.getLog());
            }

            if (this.uberSpect instanceof RuntimeServicesAware) {
               ((RuntimeServicesAware)this.uberSpect).setRuntimeServices(this);
            }

            this.uberSpect.init();
         }
      } else {
         err = "It appears that no class was specified as the Uberspect.  Please ensure that all configuration information is correct.";
         this.log.error(err);
         throw new Exception(err);
      }
   }

   private void setDefaultProperties() {
      InputStream inputStream = null;

      try {
         inputStream = this.getClass().getResourceAsStream("/org/apache/velocity/runtime/defaults/velocity.properties");
         this.configuration.load(inputStream);
         if (this.log.isDebugEnabled()) {
            this.log.debug("Default Properties File: " + (new File("org/apache/velocity/runtime/defaults/velocity.properties")).getPath());
         }
      } catch (IOException var12) {
         this.log.error("Cannot get Velocity Runtime default properties!", var12);
      } finally {
         try {
            if (inputStream != null) {
               inputStream.close();
            }
         } catch (IOException var11) {
            this.log.error("Cannot close Velocity Runtime default properties!", var11);
         }

      }

   }

   public void setProperty(String key, Object value) {
      if (this.overridingProperties == null) {
         this.overridingProperties = new ExtendedProperties();
      }

      this.overridingProperties.setProperty(key, value);
   }

   public void setConfiguration(ExtendedProperties configuration) {
      if (this.overridingProperties == null) {
         this.overridingProperties = configuration;
      } else if (this.overridingProperties != configuration) {
         this.overridingProperties.combine(configuration);
      }

   }

   public void addProperty(String key, Object value) {
      if (this.overridingProperties == null) {
         this.overridingProperties = new ExtendedProperties();
      }

      this.overridingProperties.addProperty(key, value);
   }

   public void clearProperty(String key) {
      if (this.overridingProperties != null) {
         this.overridingProperties.clearProperty(key);
      }

   }

   public Object getProperty(String key) {
      Object o = null;
      if (!this.initialized && !this.initializing && this.overridingProperties != null) {
         o = this.overridingProperties.get(key);
      }

      if (o == null) {
         o = this.configuration.getProperty(key);
      }

      return o instanceof String ? StringUtils.nullTrim((String)o) : o;
   }

   private void initializeProperties() {
      if (!this.configuration.isInitialized()) {
         this.setDefaultProperties();
      }

      if (this.overridingProperties != null) {
         this.configuration.combine(this.overridingProperties);
      }

   }

   public void init(Properties p) throws Exception {
      this.overridingProperties = ExtendedProperties.convertProperties(p);
      this.init();
   }

   public void init(String configurationFile) throws Exception {
      this.overridingProperties = new ExtendedProperties(configurationFile);
      this.init();
   }

   private void initializeResourceManager() throws Exception {
      String rm = this.getString("resource.manager.class");
      String err;
      if (rm != null && rm.length() > 0) {
         err = null;

         Object o;
         try {
            o = ClassUtils.getNewInstance(rm);
         } catch (ClassNotFoundException var5) {
            String err = "The specified class for ResourceManager (" + rm + ") does not exist or is not accessible to the current classloader.";
            this.log.error(err);
            throw new Exception(err);
         }

         if (!(o instanceof ResourceManager)) {
            String err = "The specified class for ResourceManager (" + rm + ") does not implement " + (class$org$apache$velocity$runtime$resource$ResourceManager == null ? (class$org$apache$velocity$runtime$resource$ResourceManager = class$("org.apache.velocity.runtime.resource.ResourceManager")) : class$org$apache$velocity$runtime$resource$ResourceManager).getName() + "; Velocity is not initialized correctly.";
            this.log.error(err);
            throw new Exception(err);
         } else {
            this.resourceManager = (ResourceManager)o;
            this.resourceManager.initialize(this);
         }
      } else {
         err = "It appears that no class was specified as the ResourceManager.  Please ensure that all configuration information is correct.";
         this.log.error(err);
         throw new Exception(err);
      }
   }

   private void initializeEventHandlers() throws Exception {
      this.eventCartridge = new EventCartridge();
      String[] referenceinsertion = this.configuration.getStringArray("eventhandler.referenceinsertion.class");
      if (referenceinsertion != null) {
         for(int i = 0; i < referenceinsertion.length; ++i) {
            EventHandler ev = this.initializeSpecificEventHandler(referenceinsertion[i], "eventhandler.referenceinsertion.class", class$org$apache$velocity$app$event$ReferenceInsertionEventHandler == null ? (class$org$apache$velocity$app$event$ReferenceInsertionEventHandler = class$("org.apache.velocity.app.event.ReferenceInsertionEventHandler")) : class$org$apache$velocity$app$event$ReferenceInsertionEventHandler);
            if (ev != null) {
               this.eventCartridge.addReferenceInsertionEventHandler((ReferenceInsertionEventHandler)ev);
            }
         }
      }

      String[] nullset = this.configuration.getStringArray("eventhandler.nullset.class");
      if (nullset != null) {
         for(int i = 0; i < nullset.length; ++i) {
            EventHandler ev = this.initializeSpecificEventHandler(nullset[i], "eventhandler.nullset.class", class$org$apache$velocity$app$event$NullSetEventHandler == null ? (class$org$apache$velocity$app$event$NullSetEventHandler = class$("org.apache.velocity.app.event.NullSetEventHandler")) : class$org$apache$velocity$app$event$NullSetEventHandler);
            if (ev != null) {
               this.eventCartridge.addNullSetEventHandler((NullSetEventHandler)ev);
            }
         }
      }

      String[] methodexception = this.configuration.getStringArray("eventhandler.methodexception.class");
      if (methodexception != null) {
         for(int i = 0; i < methodexception.length; ++i) {
            EventHandler ev = this.initializeSpecificEventHandler(methodexception[i], "eventhandler.methodexception.class", class$org$apache$velocity$app$event$MethodExceptionEventHandler == null ? (class$org$apache$velocity$app$event$MethodExceptionEventHandler = class$("org.apache.velocity.app.event.MethodExceptionEventHandler")) : class$org$apache$velocity$app$event$MethodExceptionEventHandler);
            if (ev != null) {
               this.eventCartridge.addMethodExceptionHandler((MethodExceptionEventHandler)ev);
            }
         }
      }

      String[] includeHandler = this.configuration.getStringArray("eventhandler.include.class");
      if (includeHandler != null) {
         for(int i = 0; i < includeHandler.length; ++i) {
            EventHandler ev = this.initializeSpecificEventHandler(includeHandler[i], "eventhandler.include.class", class$org$apache$velocity$app$event$IncludeEventHandler == null ? (class$org$apache$velocity$app$event$IncludeEventHandler = class$("org.apache.velocity.app.event.IncludeEventHandler")) : class$org$apache$velocity$app$event$IncludeEventHandler);
            if (ev != null) {
               this.eventCartridge.addIncludeEventHandler((IncludeEventHandler)ev);
            }
         }
      }

      String[] invalidReferenceSet = this.configuration.getStringArray("eventhandler.invalidreferences.class");
      if (invalidReferenceSet != null) {
         for(int i = 0; i < invalidReferenceSet.length; ++i) {
            EventHandler ev = this.initializeSpecificEventHandler(invalidReferenceSet[i], "eventhandler.invalidreferences.class", class$org$apache$velocity$app$event$InvalidReferenceEventHandler == null ? (class$org$apache$velocity$app$event$InvalidReferenceEventHandler = class$("org.apache.velocity.app.event.InvalidReferenceEventHandler")) : class$org$apache$velocity$app$event$InvalidReferenceEventHandler);
            if (ev != null) {
               this.eventCartridge.addInvalidReferenceEventHandler((InvalidReferenceEventHandler)ev);
            }
         }
      }

   }

   private EventHandler initializeSpecificEventHandler(String classname, String paramName, Class EventHandlerInterface) throws Exception {
      if (classname != null && classname.length() > 0) {
         Object o = null;

         try {
            o = ClassUtils.getNewInstance(classname);
         } catch (ClassNotFoundException var7) {
            String err = "The specified class for " + paramName + " (" + classname + ") does not exist or is not accessible to the current classloader.";
            this.log.error(err);
            throw new Exception(err);
         }

         if (!EventHandlerInterface.isAssignableFrom(EventHandlerInterface)) {
            String err = "The specified class for " + paramName + " (" + classname + ") does not implement " + EventHandlerInterface.getName() + "; Velocity is not initialized correctly.";
            this.log.error(err);
            throw new Exception(err);
         } else {
            EventHandler ev = (EventHandler)o;
            if (ev instanceof RuntimeServicesAware) {
               ((RuntimeServicesAware)ev).setRuntimeServices(this);
            }

            return ev;
         }
      } else {
         return null;
      }
   }

   private void initializeLog() throws Exception {
      LogManager.updateLog(this.log, this);
   }

   private void initializeDirectives() throws Exception {
      this.runtimeDirectives = new Hashtable();
      Properties directiveProperties = new Properties();
      InputStream inputStream = null;

      try {
         inputStream = this.getClass().getResourceAsStream("/org/apache/velocity/runtime/defaults/directive.properties");
         if (inputStream == null) {
            throw new Exception("Error loading directive.properties! Something is very wrong if these properties aren't being located. Either your Velocity distribution is incomplete or your Velocity jar file is corrupted!");
         }

         directiveProperties.load(inputStream);
      } catch (IOException var13) {
         this.log.error("Error while loading directive properties!", var13);
      } finally {
         try {
            if (inputStream != null) {
               inputStream.close();
            }
         } catch (IOException var12) {
            this.log.error("Cannot close directive properties!", var12);
         }

      }

      Enumeration directiveClasses = directiveProperties.elements();

      while(directiveClasses.hasMoreElements()) {
         String directiveClass = (String)directiveClasses.nextElement();
         this.loadDirective(directiveClass);
         this.log.debug("Loaded System Directive: " + directiveClass);
      }

      String[] userdirective = this.configuration.getStringArray("userdirective");

      for(int i = 0; i < userdirective.length; ++i) {
         this.loadDirective(userdirective[i]);
         if (this.log.isInfoEnabled()) {
            this.log.info("Loaded User Directive: " + userdirective[i]);
         }
      }

   }

   private void loadDirective(String directiveClass) {
      try {
         Object o = ClassUtils.getNewInstance(directiveClass);
         if (o instanceof Directive) {
            Directive directive = (Directive)o;
            this.runtimeDirectives.put(directive.getName(), directive);
         } else {
            this.log.error(directiveClass + " does not implement " + (class$org$apache$velocity$runtime$directive$Directive == null ? (class$org$apache$velocity$runtime$directive$Directive = class$("org.apache.velocity.runtime.directive.Directive")) : class$org$apache$velocity$runtime$directive$Directive).getName() + "; it cannot be loaded.");
         }
      } catch (Exception var4) {
         this.log.error("Failed to load Directive: " + directiveClass, var4);
      }

   }

   private void initializeParserPool() throws Exception {
      String pp = this.getString("parser.pool.class");
      String err;
      if (pp != null && pp.length() > 0) {
         err = null;

         Object o;
         try {
            o = ClassUtils.getNewInstance(pp);
         } catch (ClassNotFoundException var5) {
            String err = "The specified class for ParserPool (" + pp + ") does not exist (or is not accessible to the current classloader.";
            this.log.error(err);
            throw new Exception(err);
         }

         if (!(o instanceof ParserPool)) {
            String err = "The specified class for ParserPool (" + pp + ") does not implement " + (class$org$apache$velocity$runtime$ParserPool == null ? (class$org$apache$velocity$runtime$ParserPool = class$("org.apache.velocity.runtime.ParserPool")) : class$org$apache$velocity$runtime$ParserPool) + " Velocity not initialized correctly.";
            this.log.error(err);
            throw new Exception(err);
         } else {
            this.parserPool = (ParserPool)o;
            this.parserPool.initialize(this);
         }
      } else {
         err = "It appears that no class was specified as the ParserPool.  Please ensure that all configuration information is correct.";
         this.log.error(err);
         throw new Exception(err);
      }
   }

   public Parser createNewParser() {
      if (!this.initialized && !this.initializing) {
         this.log.debug("Velocity was not initialized! Calling init()...");

         try {
            this.init();
         } catch (Exception var2) {
            this.getLog().error("Could not auto-initialize Velocity", var2);
            throw new IllegalStateException("Velocity could not be initialized!");
         }
      }

      Parser parser = new Parser(this);
      parser.setDirectives(this.runtimeDirectives);
      return parser;
   }

   public SimpleNode parse(Reader reader, String templateName) throws ParseException {
      return this.parse(reader, templateName, true);
   }

   public SimpleNode parse(Reader reader, String templateName, boolean dumpNamespace) throws ParseException {
      if (!this.initialized && !this.initializing) {
         this.log.debug("Velocity was not initialized! Calling init()...");

         try {
            this.init();
         } catch (Exception var10) {
            this.getLog().error("Could not auto-initialize Velocity", var10);
            throw new IllegalStateException("Velocity could not be initialized!");
         }
      }

      SimpleNode ast = null;
      Parser parser = this.parserPool.get();
      if (parser == null) {
         if (this.log.isInfoEnabled()) {
            this.log.info("Runtime : ran out of parsers. Creating a new one.  Please increment the parser.pool.size property. The current value is too small.");
         }

         parser = this.createNewParser();
      }

      if (parser != null) {
         try {
            if (dumpNamespace) {
               this.dumpVMNamespace(templateName);
            }

            ast = parser.parse(reader, templateName);
         } finally {
            this.parserPool.put(parser);
         }
      } else {
         this.log.error("Runtime : ran out of parsers and unable to create more.");
      }

      return ast;
   }

   public Template getTemplate(String name) throws ResourceNotFoundException, ParseErrorException, Exception {
      return this.getTemplate(name, this.getString("input.encoding", "ISO-8859-1"));
   }

   public Template getTemplate(String name, String encoding) throws ResourceNotFoundException, ParseErrorException, Exception {
      if (!this.initialized && !this.initializing) {
         this.log.info("Velocity not initialized yet. Calling init()...");
         this.init();
      }

      return (Template)this.resourceManager.getResource(name, 1, encoding);
   }

   public ContentResource getContent(String name) throws ResourceNotFoundException, ParseErrorException, Exception {
      return this.getContent(name, this.getString("input.encoding", "ISO-8859-1"));
   }

   public ContentResource getContent(String name, String encoding) throws ResourceNotFoundException, ParseErrorException, Exception {
      if (!this.initialized && !this.initializing) {
         this.log.info("Velocity not initialized yet. Calling init()...");
         this.init();
      }

      return (ContentResource)this.resourceManager.getResource(name, 2, encoding);
   }

   public String getLoaderNameForResource(String resourceName) {
      if (!this.initialized && !this.initializing) {
         this.log.debug("Velocity was not initialized! Calling init()...");

         try {
            this.init();
         } catch (Exception var3) {
            this.getLog().error("Could not initialize Velocity", var3);
            throw new IllegalStateException("Velocity could not be initialized!");
         }
      }

      return this.resourceManager.getLoaderNameForResource(resourceName);
   }

   public Log getLog() {
      return this.log;
   }

   /** @deprecated */
   public void warn(Object message) {
      this.getLog().warn(message);
   }

   /** @deprecated */
   public void info(Object message) {
      this.getLog().info(message);
   }

   /** @deprecated */
   public void error(Object message) {
      this.getLog().error(message);
   }

   /** @deprecated */
   public void debug(Object message) {
      this.getLog().debug(message);
   }

   public String getString(String key, String defaultValue) {
      return this.configuration.getString(key, defaultValue);
   }

   public Directive getVelocimacro(String vmName, String templateName) {
      return this.vmFactory.getVelocimacro(vmName, templateName);
   }

   public boolean addVelocimacro(String name, String macro, String[] argArray, String sourceTemplate) {
      return this.vmFactory.addVelocimacro(name, macro, argArray, sourceTemplate);
   }

   public boolean isVelocimacro(String vmName, String templateName) {
      return this.vmFactory.isVelocimacro(vmName, templateName);
   }

   public boolean dumpVMNamespace(String namespace) {
      return this.vmFactory.dumpVMNamespace(namespace);
   }

   public String getString(String key) {
      return StringUtils.nullTrim(this.configuration.getString(key));
   }

   public int getInt(String key) {
      return this.configuration.getInt(key);
   }

   public int getInt(String key, int defaultValue) {
      return this.configuration.getInt(key, defaultValue);
   }

   public boolean getBoolean(String key, boolean def) {
      return this.configuration.getBoolean(key, def);
   }

   public ExtendedProperties getConfiguration() {
      return this.configuration;
   }

   public Introspector getIntrospector() {
      return this.introspector;
   }

   public EventCartridge getApplicationEventCartridge() {
      return this.eventCartridge;
   }

   public Object getApplicationAttribute(Object key) {
      return this.applicationAttributes.get(key);
   }

   public Object setApplicationAttribute(Object key, Object o) {
      return this.applicationAttributes.put(key, o);
   }

   public Uberspect getUberspect() {
      return this.uberSpect;
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
