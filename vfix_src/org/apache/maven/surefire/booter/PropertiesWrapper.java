package org.apache.maven.surefire.booter;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.surefire.util.internal.StringUtils;

public class PropertiesWrapper implements KeyValueSource {
   private final Properties properties;

   public PropertiesWrapper(Properties properties) {
      if (properties == null) {
         throw new IllegalStateException("Properties cannot be null");
      } else {
         this.properties = properties;
      }
   }

   public Properties getProperties() {
      return this.properties;
   }

   public void setAsSystemProperties() {
      Iterator i$ = this.properties.keySet().iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         String key = (String)o;
         System.setProperty(key, this.properties.getProperty(key));
      }

   }

   public String getProperty(String key) {
      return this.properties.getProperty(key);
   }

   public boolean getBooleanProperty(String propertyName) {
      return Boolean.valueOf(this.properties.getProperty(propertyName));
   }

   public Boolean getBooleanObjectProperty(String propertyName) {
      return Boolean.valueOf(this.properties.getProperty(propertyName));
   }

   public File getFileProperty(String key) {
      String property = this.getProperty(key);
      if (property == null) {
         return null;
      } else {
         TypeEncodedValue typeEncodedValue = new TypeEncodedValue(File.class.getName(), property);
         return (File)typeEncodedValue.getDecodedValue();
      }
   }

   public List<String> getStringList(String propertyPrefix) {
      List<String> result = new ArrayList();

      String value;
      for(int i = 0; (value = this.getProperty(propertyPrefix + i)) != null; ++i) {
         result.add(value);
      }

      return result;
   }

   public TypeEncodedValue getTypeEncodedValue(String key) {
      String typeEncoded = this.getProperty(key);
      if (typeEncoded == null) {
         return null;
      } else {
         int typeSep = typeEncoded.indexOf("|");
         String type = typeEncoded.substring(0, typeSep);
         String value = typeEncoded.substring(typeSep + 1);
         return new TypeEncodedValue(type, value);
      }
   }

   Classpath getClasspath(String prefix) {
      List<String> elements = this.getStringList(prefix);
      return new Classpath(elements);
   }

   public void setClasspath(String prefix, Classpath classpath) {
      List classpathElements = classpath.getClassPath();

      for(int i = 0; i < classpathElements.size(); ++i) {
         String element = (String)classpathElements.get(i);
         this.setProperty(prefix + i, element);
      }

   }

   public void setProperty(String key, String value) {
      if (value != null) {
         this.properties.setProperty(key, value);
      }

   }

   public void addList(List items, String propertyPrefix) {
      if (items != null && items.size() != 0) {
         int i = 0;
         Iterator i$ = items.iterator();

         while(i$.hasNext()) {
            Object item = i$.next();
            if (item == null) {
               throw new NullPointerException(propertyPrefix + i + " has null value");
            }

            String[] stringArray = StringUtils.split(item.toString(), ",");
            String[] arr$ = stringArray;
            int len$ = stringArray.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String aStringArray = arr$[i$];
               this.properties.setProperty(propertyPrefix + i, aStringArray);
               ++i;
            }
         }

      }
   }

   public void copyTo(Map target) {
      Iterator iter = this.properties.keySet().iterator();

      while(iter.hasNext()) {
         Object key = iter.next();
         target.put(key, this.properties.get(key));
      }

   }
}
