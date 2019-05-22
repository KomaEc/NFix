package org.jboss.util.property;

import java.security.AccessController;
import java.security.PrivilegedAction;

public final class Property {
   public static String LINE_SEPARATOR;
   public static String FILE_SEPARATOR;
   public static String PATH_SEPARATOR;

   public static void addListener(PropertyListener listener) {
      PropertyManager.addPropertyListener(listener);
   }

   public static void addListeners(PropertyListener[] listeners) {
      PropertyManager.addPropertyListeners(listeners);
   }

   public static boolean removeListener(PropertyListener listener) {
      return PropertyManager.removePropertyListener(listener);
   }

   public static String set(String name, String value) {
      return PropertyManager.setProperty(name, value);
   }

   public static String remove(String name) {
      return PropertyManager.getProperty(name);
   }

   public static String get(String name, String defaultValue) {
      return PropertyManager.getProperty(name, defaultValue);
   }

   public static String get(String name) {
      return PropertyManager.getProperty(name);
   }

   public static String[] getArray(String base, String[] defaultValues) {
      return PropertyManager.getArrayProperty(base, defaultValues);
   }

   public static String[] getArray(String name) {
      return PropertyManager.getArrayProperty(name);
   }

   public static boolean exists(String name) {
      return PropertyManager.containsProperty(name);
   }

   public static PropertyGroup getGroup(String basename) {
      return PropertyManager.getPropertyGroup(basename);
   }

   public static PropertyGroup getGroup(String basename, int index) {
      return PropertyManager.getPropertyGroup(basename, index);
   }

   static {
      PrivilegedAction action = new PrivilegedAction() {
         public Object run() {
            Property.LINE_SEPARATOR = Property.get("line.separator");
            Property.FILE_SEPARATOR = Property.get("file.separator");
            Property.PATH_SEPARATOR = Property.get("path.separator");
            return null;
         }
      };
      AccessController.doPrivileged(action);
   }
}
