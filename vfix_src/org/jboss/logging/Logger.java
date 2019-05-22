package org.jboss.logging;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Logger implements Serializable {
   private static final long serialVersionUID = 4232175575988879434L;
   protected static String PLUGIN_CLASS_PROP = "org.jboss.logging.Logger.pluginClass";
   protected static final String LOG4J_PLUGIN_CLASS_NAME = "org.jboss.logging.log4j.Log4jLoggerPlugin";
   protected static Class<?> pluginClass = null;
   protected static String pluginClassName = null;
   protected static final String LOGGER_FQCN = Logger.class.getName();
   private final String name;
   protected transient LoggerPlugin loggerDelegate = null;

   public static String getPluginClassName() {
      return pluginClassName;
   }

   public static void setPluginClassName(String pluginClassName) {
      if (!pluginClassName.equals(Logger.pluginClassName)) {
         Logger.pluginClassName = pluginClassName;
         init();
      }

   }

   protected Logger(String name) {
      this.name = name;
      this.loggerDelegate = getDelegatePlugin(name);
   }

   public String getName() {
      return this.name;
   }

   public LoggerPlugin getLoggerPlugin() {
      return this.loggerDelegate;
   }

   public boolean isTraceEnabled() {
      return this.loggerDelegate.isTraceEnabled();
   }

   public void trace(Object message) {
      this.loggerDelegate.trace(LOGGER_FQCN, message, (Throwable)null);
   }

   public void trace(Object message, Throwable t) {
      this.loggerDelegate.trace(LOGGER_FQCN, message, t);
   }

   public void trace(String loggerFqcn, Object message, Throwable t) {
      this.loggerDelegate.trace(loggerFqcn, message, t);
   }

   public void tracef(String format, Object... params) {
      if (this.isTraceEnabled()) {
         this.trace(LOGGER_FQCN, String.format(format, params), (Throwable)null);
      }

   }

   public void tracef(Throwable t, String format, Object... params) {
      if (this.isTraceEnabled()) {
         this.trace(LOGGER_FQCN, String.format(format, params), t);
      }

   }

   /** @deprecated */
   public boolean isDebugEnabled() {
      return this.loggerDelegate.isDebugEnabled();
   }

   public void debug(Object message) {
      this.loggerDelegate.debug(LOGGER_FQCN, message, (Throwable)null);
   }

   public void debug(Object message, Throwable t) {
      this.loggerDelegate.debug(LOGGER_FQCN, message, t);
   }

   public void debug(String loggerFqcn, Object message, Throwable t) {
      this.loggerDelegate.debug(loggerFqcn, message, t);
   }

   public void debugf(String format, Object... params) {
      if (this.isDebugEnabled()) {
         this.debug(LOGGER_FQCN, String.format(format, params), (Throwable)null);
      }

   }

   public void debugf(Throwable t, String format, Object... params) {
      if (this.isDebugEnabled()) {
         this.debug(LOGGER_FQCN, String.format(format, params), t);
      }

   }

   /** @deprecated */
   public boolean isInfoEnabled() {
      return this.loggerDelegate.isInfoEnabled();
   }

   public void info(Object message) {
      this.loggerDelegate.info(LOGGER_FQCN, message, (Throwable)null);
   }

   public void info(Object message, Throwable t) {
      this.loggerDelegate.info(LOGGER_FQCN, message, t);
   }

   public void info(String loggerFqcn, Object message, Throwable t) {
      this.loggerDelegate.info(loggerFqcn, message, t);
   }

   public void infof(String format, Object... params) {
      if (this.isInfoEnabled()) {
         this.info(LOGGER_FQCN, String.format(format, params), (Throwable)null);
      }

   }

   public void infof(Throwable t, String format, Object... params) {
      if (this.isInfoEnabled()) {
         this.info(LOGGER_FQCN, String.format(format, params), t);
      }

   }

   public void warn(Object message) {
      this.loggerDelegate.warn(LOGGER_FQCN, message, (Throwable)null);
   }

   public void warn(Object message, Throwable t) {
      this.loggerDelegate.warn(LOGGER_FQCN, message, t);
   }

   public void warn(String loggerFqcn, Object message, Throwable t) {
      this.loggerDelegate.warn(loggerFqcn, message, t);
   }

   public void warnf(String format, Object... params) {
      this.warn(LOGGER_FQCN, String.format(format, params), (Throwable)null);
   }

   public void warnf(Throwable t, String format, Object... params) {
      this.warn(LOGGER_FQCN, String.format(format, params), t);
   }

   public void error(Object message) {
      this.loggerDelegate.error(LOGGER_FQCN, message, (Throwable)null);
   }

   public void error(Object message, Throwable t) {
      this.loggerDelegate.error(LOGGER_FQCN, message, t);
   }

   public void error(String loggerFqcn, Object message, Throwable t) {
      this.loggerDelegate.error(loggerFqcn, message, t);
   }

   public void errorf(String format, Object... params) {
      this.error(LOGGER_FQCN, String.format(format, params), (Throwable)null);
   }

   public void errorf(Throwable t, String format, Object... params) {
      this.error(LOGGER_FQCN, String.format(format, params), t);
   }

   public void fatal(Object message) {
      this.loggerDelegate.fatal(LOGGER_FQCN, message, (Throwable)null);
   }

   public void fatal(Object message, Throwable t) {
      this.loggerDelegate.fatal(LOGGER_FQCN, message, t);
   }

   public void fatal(String loggerFqcn, Object message, Throwable t) {
      this.loggerDelegate.fatal(loggerFqcn, message, t);
   }

   public void fatalf(String format, Object... params) {
      this.fatal(LOGGER_FQCN, String.format(format, params), (Throwable)null);
   }

   public void fatalf(Throwable t, String format, Object... params) {
      this.fatal(LOGGER_FQCN, String.format(format, params), t);
   }

   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
      stream.defaultReadObject();
      if (pluginClass == null) {
         init();
      }

      this.loggerDelegate = getDelegatePlugin(this.name);
   }

   public static Logger getLogger(String name) {
      return new Logger(name);
   }

   public static Logger getLogger(String name, String suffix) {
      return new Logger(name + "." + suffix);
   }

   public static Logger getLogger(Class<?> clazz) {
      return new Logger(clazz.getName());
   }

   public static Logger getLogger(Class<?> clazz, String suffix) {
      return new Logger(clazz.getName() + "." + suffix);
   }

   protected static LoggerPlugin getDelegatePlugin(String name) {
      Object plugin = null;

      try {
         plugin = (LoggerPlugin)pluginClass.newInstance();
      } catch (Throwable var4) {
         plugin = new NullLoggerPlugin();
      }

      try {
         ((LoggerPlugin)plugin).init(name);
      } catch (Throwable var5) {
         String extraInfo = var5.getMessage();
         System.err.println("Failed to initalize plugin: " + plugin + (extraInfo != null ? ", cause: " + extraInfo : ""));
         plugin = new NullLoggerPlugin();
      }

      return (LoggerPlugin)plugin;
   }

   protected static void init() {
      try {
         if (pluginClassName == null) {
            pluginClassName = System.getProperty(PLUGIN_CLASS_PROP, "org.jboss.logging.log4j.Log4jLoggerPlugin");
         }

         ClassLoader cl = Thread.currentThread().getContextClassLoader();
         pluginClass = cl.loadClass(pluginClassName);
      } catch (Throwable var1) {
         pluginClass = NullLoggerPlugin.class;
      }

   }

   static {
      init();
   }
}
