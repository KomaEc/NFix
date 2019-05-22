package org.jboss.util.propertyeditor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import org.jboss.logging.Logger;
import org.jboss.util.Classes;

public class PropertyEditors {
   private static Logger log = Logger.getLogger(PropertyEditors.class);
   private static final String NULL = "null";
   private static boolean disableIsNull = false;
   private static boolean initialized = false;

   public static synchronized void init() {
      if (!initialized) {
         AccessController.doPrivileged(PropertyEditors.Initialize.instance);
         initialized = true;
      }

   }

   public static final boolean isNull(String value) {
      return isNull(value, true, true);
   }

   public static final boolean isNull(String value, boolean trim, boolean empty) {
      if (disableIsNull) {
         return false;
      } else if (value == null) {
         return true;
      } else {
         String trimmed = trim ? value.trim() : value;
         return empty && trimmed.length() == 0 ? true : "null".equalsIgnoreCase(trimmed);
      }
   }

   public static boolean isNullHandlingEnabled() {
      return !disableIsNull;
   }

   public static PropertyEditor findEditor(Class<?> type) {
      return PropertyEditorManager.findEditor(type);
   }

   public static PropertyEditor findEditor(String typeName) throws ClassNotFoundException {
      Class<?> type = Classes.getPrimitiveTypeForName(typeName);
      if (type == null) {
         ClassLoader loader = Thread.currentThread().getContextClassLoader();
         type = loader.loadClass(typeName);
      }

      return PropertyEditorManager.findEditor(type);
   }

   public static PropertyEditor getEditor(Class<?> type) {
      PropertyEditor editor = findEditor(type);
      if (editor == null) {
         throw new RuntimeException("No property editor for type: " + type);
      } else {
         return editor;
      }
   }

   public static PropertyEditor getEditor(String typeName) throws ClassNotFoundException {
      PropertyEditor editor = findEditor(typeName);
      if (editor == null) {
         throw new RuntimeException("No property editor for type: " + typeName);
      } else {
         return editor;
      }
   }

   public static void registerEditor(Class<?> type, Class<?> editorType) {
      PropertyEditorManager.registerEditor(type, editorType);
   }

   public static void registerEditor(String typeName, String editorTypeName) throws ClassNotFoundException {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      Class<?> type = loader.loadClass(typeName);
      Class<?> editorType = loader.loadClass(editorTypeName);
      PropertyEditorManager.registerEditor(type, editorType);
   }

   public static Object convertValue(String text, String typeName) throws ClassNotFoundException, IntrospectionException {
      Class<?> typeClass = Classes.getPrimitiveTypeForName(typeName);
      if (typeClass == null) {
         ClassLoader loader = Thread.currentThread().getContextClassLoader();
         typeClass = loader.loadClass(typeName);
      }

      PropertyEditor editor = PropertyEditorManager.findEditor(typeClass);
      if (editor == null) {
         throw new IntrospectionException("No property editor for type=" + typeClass);
      } else {
         editor.setAsText(text);
         return editor.getValue();
      }
   }

   public static void mapJavaBeanProperties(Object bean, Properties beanProps) throws IntrospectionException {
      mapJavaBeanProperties(bean, beanProps, true);
   }

   public static void mapJavaBeanProperties(Object bean, Properties beanProps, boolean isStrict) throws IntrospectionException {
      HashMap<String, PropertyDescriptor> propertyMap = new HashMap();
      BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
      PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();

      for(int p = 0; p < props.length; ++p) {
         String fieldName = props[p].getName();
         propertyMap.put(fieldName, props[p]);
      }

      boolean trace = log.isTraceEnabled();
      Iterator keys = beanProps.keySet().iterator();
      if (trace) {
         log.trace("Mapping properties for bean: " + bean);
      }

      String name;
      label54:
      do {
         while(keys.hasNext()) {
            name = (String)keys.next();
            String text = beanProps.getProperty(name);
            PropertyDescriptor pd = (PropertyDescriptor)propertyMap.get(name);
            if (pd == null) {
               if (name.length() > 1) {
                  char first = name.charAt(0);
                  String exName = Character.toUpperCase(first) + name.substring(1);
                  pd = (PropertyDescriptor)propertyMap.get(exName);
                  if (pd == null) {
                     exName = Character.toLowerCase(first) + name.substring(1);
                     pd = (PropertyDescriptor)propertyMap.get(exName);
                  }
               }

               if (pd == null) {
                  continue label54;
               }
            }

            Method setter = pd.getWriteMethod();
            if (trace) {
               log.trace("Property editor found for: " + name + ", editor: " + pd + ", setter: " + setter);
            }

            if (setter != null) {
               Class<?> ptype = pd.getPropertyType();
               PropertyEditor editor = PropertyEditorManager.findEditor(ptype);
               if (editor == null && trace) {
                  log.trace("Failed to find property editor for: " + name);
               }

               try {
                  editor.setAsText(text);
                  Object[] args = new Object[]{editor.getValue()};
                  setter.invoke(bean, args);
               } catch (Exception var15) {
                  if (trace) {
                     log.trace("Failed to write property", var15);
                  }
               }
            }
         }

         return;
      } while(!isStrict);

      String msg = "No property found for: " + name + " on JavaBean: " + bean;
      throw new IntrospectionException(msg);
   }

   public String[] getEditorSearchPath() {
      return PropertyEditorManager.getEditorSearchPath();
   }

   public void setEditorSearchPath(String[] path) {
      PropertyEditorManager.setEditorSearchPath(path);
   }

   static {
      init();
   }

   private static class Initialize implements PrivilegedAction<Object> {
      static PropertyEditors.Initialize instance = new PropertyEditors.Initialize();

      public Object run() {
         String[] currentPath = PropertyEditorManager.getEditorSearchPath();
         int length = currentPath != null ? currentPath.length : 0;
         String[] newPath = new String[length + 2];
         System.arraycopy(currentPath, 0, newPath, 2, length);
         newPath[0] = "org.jboss.util.propertyeditor";
         newPath[1] = "org.jboss.mx.util.propertyeditor";
         PropertyEditorManager.setEditorSearchPath(newPath);
         Class<?> strArrayType = String[].class;
         PropertyEditorManager.registerEditor(strArrayType, StringArrayEditor.class);
         Class<?> clsArrayType = Class[].class;
         PropertyEditorManager.registerEditor(clsArrayType, ClassArrayEditor.class);
         Class<?> intArrayType = int[].class;
         PropertyEditorManager.registerEditor(intArrayType, IntArrayEditor.class);
         Class<?> byteArrayType = byte[].class;
         PropertyEditorManager.registerEditor(byteArrayType, ByteArrayEditor.class);
         PropertyEditorManager.registerEditor(Character.TYPE, CharacterEditor.class);

         try {
            if (System.getProperty("org.jboss.util.property.disablenull") != null) {
               PropertyEditors.disableIsNull = true;
            }
         } catch (Throwable var9) {
            PropertyEditors.log.trace("Error retrieving system property org.jboss.util.property.diablenull", var9);
         }

         return null;
      }
   }
}
