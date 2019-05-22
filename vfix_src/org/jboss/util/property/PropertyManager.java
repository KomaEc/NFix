package org.jboss.util.property;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Map;
import org.jboss.util.ThrowableHandler;

public final class PropertyManager {
   public static final String READER_PROPERTY_NAME = "org.jboss.util.property.reader";
   public static final String DEFAULT_PROPERTY_READER_TOKEN = "DEFAULT";
   private static final String[] DEFAULT_PROPERTY_READERS = new String[]{"DEFAULT"};
   private static PropertyMap props = new PropertyMap();

   private PropertyManager() {
   }

   public static PropertyMap getDefaultPropertyMap() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertiesAccess();
      }

      return props;
   }

   public static void addPropertyListener(PropertyListener listener) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertiesAccess();
      }

      props.addPropertyListener(listener);
   }

   public static void addPropertyListeners(PropertyListener[] listeners) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertiesAccess();
      }

      props.addPropertyListeners(listeners);
   }

   public static boolean removePropertyListener(PropertyListener listener) {
      return props.removePropertyListener(listener);
   }

   public static void load(String prefix, Map map) throws PropertyException {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertiesAccess();
      }

      props.load(prefix, map);
   }

   public static void load(Map map) throws PropertyException, IOException {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertiesAccess();
      }

      props.load(map);
   }

   public static void load(PropertyReader reader) throws PropertyException, IOException {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertiesAccess();
      }

      props.load(reader);
   }

   public static void load(String classname) throws PropertyException, IOException {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertiesAccess();
      }

      props.load(classname);
   }

   public static String setProperty(String name, String value) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertyAccess(name);
      }

      return (String)props.setProperty(name, value);
   }

   public static String removeProperty(String name) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertyAccess(name);
      }

      return props.removeProperty(name);
   }

   public static String getProperty(String name, String defaultValue) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertyAccess(name);
      }

      return props.getProperty(name, defaultValue);
   }

   public static String getProperty(String name) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertyAccess(name);
      }

      return props.getProperty(name);
   }

   public static String[] getArrayProperty(String base, String[] defaultValues) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertiesAccess();
      }

      return props.getArrayProperty(base, defaultValues);
   }

   public static String[] getArrayProperty(String name) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertyAccess(name);
      }

      return props.getArrayProperty(name);
   }

   public static Iterator names() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertiesAccess();
      }

      return props.names();
   }

   public static boolean containsProperty(String name) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertyAccess(name);
      }

      return props.containsProperty(name);
   }

   public static PropertyGroup getPropertyGroup(String basename) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertiesAccess();
      }

      return props.getPropertyGroup(basename);
   }

   public static PropertyGroup getPropertyGroup(String basename, int index) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPropertiesAccess();
      }

      return props.getPropertyGroup(basename, index);
   }

   static {
      PrivilegedAction action = new PrivilegedAction() {
         public Object run() {
            PropertyManager.props.putAll(System.getProperties());
            System.setProperties(PropertyManager.props);
            String[] readerNames = PropertyManager.getArrayProperty("org.jboss.util.property.reader", PropertyManager.DEFAULT_PROPERTY_READERS);

            for(int i = 0; i < readerNames.length; ++i) {
               try {
                  if (readerNames[i].equals("DEFAULT")) {
                     PropertyManager.load((PropertyReader)(new DefaultPropertyReader()));
                  } else {
                     PropertyManager.load(readerNames[i]);
                  }
               } catch (IOException var4) {
                  ThrowableHandler.add(var4);
               }
            }

            return null;
         }
      };
      AccessController.doPrivileged(action);
   }
}
