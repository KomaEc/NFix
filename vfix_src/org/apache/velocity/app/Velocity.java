package org.apache.velocity.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Properties;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapterImpl;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;

public class Velocity implements RuntimeConstants {
   public static void init() throws Exception {
      RuntimeSingleton.init();
   }

   public static void init(String propsFilename) throws Exception {
      RuntimeSingleton.init(propsFilename);
   }

   public static void init(Properties p) throws Exception {
      RuntimeSingleton.init(p);
   }

   public static void setProperty(String key, Object value) {
      RuntimeSingleton.setProperty(key, value);
   }

   public static void addProperty(String key, Object value) {
      RuntimeSingleton.addProperty(key, value);
   }

   public static void clearProperty(String key) {
      RuntimeSingleton.clearProperty(key);
   }

   public static void setExtendedProperties(ExtendedProperties configuration) {
      RuntimeSingleton.setConfiguration(configuration);
   }

   public static Object getProperty(String key) {
      return RuntimeSingleton.getProperty(key);
   }

   public static boolean evaluate(Context context, Writer out, String logTag, String instring) throws ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {
      return evaluate(context, out, logTag, (Reader)(new BufferedReader(new StringReader(instring))));
   }

   /** @deprecated */
   public static boolean evaluate(Context context, Writer writer, String logTag, InputStream instream) throws ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {
      BufferedReader br = null;
      String encoding = null;

      try {
         encoding = RuntimeSingleton.getString("input.encoding", "ISO-8859-1");
         br = new BufferedReader(new InputStreamReader(instream, encoding));
      } catch (UnsupportedEncodingException var8) {
         String msg = "Unsupported input encoding : " + encoding + " for template " + logTag;
         throw new ParseErrorException(msg);
      }

      return evaluate(context, writer, logTag, (Reader)br);
   }

   public static boolean evaluate(Context context, Writer writer, String logTag, Reader reader) throws ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {
      SimpleNode nodeTree = null;

      try {
         nodeTree = RuntimeSingleton.parse(reader, logTag);
      } catch (ParseException var18) {
         throw new ParseErrorException(var18);
      } catch (TemplateInitException var19) {
         throw new ParseErrorException(var19);
      }

      if (nodeTree != null) {
         InternalContextAdapterImpl ica = new InternalContextAdapterImpl(context);
         ica.pushCurrentTemplateName(logTag);

         try {
            try {
               nodeTree.init(ica, RuntimeSingleton.getRuntimeServices());
            } catch (TemplateInitException var14) {
               throw new ParseErrorException(var14);
            } catch (RuntimeException var15) {
               throw var15;
            } catch (Exception var16) {
               getLog().error("Velocity.evaluate() : init exception for tag = " + logTag, var16);
            }

            nodeTree.render(ica, writer);
         } finally {
            ica.popCurrentTemplateName();
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean invokeVelocimacro(String vmName, String logTag, String[] params, Context context, Writer writer) {
      if (vmName != null && params != null && context != null && writer != null && logTag != null) {
         if (!RuntimeSingleton.isVelocimacro(vmName, logTag)) {
            getLog().error("Velocity.invokeVelocimacro() : VM '" + vmName + "' not registered.");
            return false;
         } else {
            StringBuffer construct = new StringBuffer("#");
            construct.append(vmName);
            construct.append("(");

            for(int i = 0; i < params.length; ++i) {
               construct.append(" $");
               construct.append(params[i]);
            }

            construct.append(" )");

            try {
               return evaluate(context, writer, logTag, construct.toString());
            } catch (ParseErrorException var7) {
               throw var7;
            } catch (MethodInvocationException var8) {
               throw var8;
            } catch (ResourceNotFoundException var9) {
               throw var9;
            } catch (IOException var10) {
               getLog().error("Velocity.invokeVelocimacro() failed", var10);
               return false;
            } catch (RuntimeException var11) {
               throw var11;
            }
         }
      } else {
         getLog().error("Velocity.invokeVelocimacro() : invalid parameter");
         return false;
      }
   }

   /** @deprecated */
   public static boolean mergeTemplate(String templateName, Context context, Writer writer) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, Exception {
      return mergeTemplate(templateName, RuntimeSingleton.getString("input.encoding", "ISO-8859-1"), context, writer);
   }

   public static boolean mergeTemplate(String templateName, String encoding, Context context, Writer writer) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, Exception {
      Template template = RuntimeSingleton.getTemplate(templateName, encoding);
      if (template == null) {
         getLog().error("Velocity.mergeTemplate() was unable to load template '" + templateName + "'");
         return false;
      } else {
         template.merge(context, writer);
         return true;
      }
   }

   public static Template getTemplate(String name) throws ResourceNotFoundException, ParseErrorException, Exception {
      return RuntimeSingleton.getTemplate(name);
   }

   public static Template getTemplate(String name, String encoding) throws ResourceNotFoundException, ParseErrorException, Exception {
      return RuntimeSingleton.getTemplate(name, encoding);
   }

   public static boolean resourceExists(String resourceName) {
      return RuntimeSingleton.getLoaderNameForResource(resourceName) != null;
   }

   public static Log getLog() {
      return RuntimeSingleton.getLog();
   }

   /** @deprecated */
   public static void warn(Object message) {
      getLog().warn(message);
   }

   /** @deprecated */
   public static void info(Object message) {
      getLog().info(message);
   }

   /** @deprecated */
   public static void error(Object message) {
      getLog().error(message);
   }

   /** @deprecated */
   public static void debug(Object message) {
      getLog().debug(message);
   }

   public static void setApplicationAttribute(Object key, Object value) {
      RuntimeSingleton.getRuntimeInstance().setApplicationAttribute(key, value);
   }

   /** @deprecated */
   public static boolean templateExists(String resourceName) {
      return resourceExists(resourceName);
   }
}
