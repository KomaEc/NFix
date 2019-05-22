package org.apache.maven.plugin.surefire;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.maven.surefire.booter.Classpath;
import org.apache.maven.surefire.booter.KeyValueSource;
import org.apache.maven.surefire.util.internal.StringUtils;

public class SurefireProperties extends Properties implements KeyValueSource {
   private final LinkedHashSet<Object> items = new LinkedHashSet();
   private static final Set<String> keysThatCannotBeUsedAsSystemProperties = new HashSet<String>() {
      {
         this.add("java.library.path");
         this.add("file.encoding");
         this.add("jdk.map.althashing.threshold");
      }
   };

   public SurefireProperties() {
   }

   public SurefireProperties(Properties source) {
      if (source != null) {
         this.putAll(source);
      }

   }

   public SurefireProperties(KeyValueSource source) {
      if (source != null) {
         source.copyTo(this);
      }

   }

   public synchronized Object put(Object key, Object value) {
      this.items.add(key);
      return super.put(key, value);
   }

   public synchronized Object remove(Object key) {
      this.items.remove(key);
      return super.remove(key);
   }

   public synchronized void clear() {
      this.items.clear();
      super.clear();
   }

   public synchronized Enumeration<Object> keys() {
      return Collections.enumeration(this.items);
   }

   public void copyPropertiesFrom(Properties source) {
      if (source != null) {
         Iterator i$ = source.keySet().iterator();

         while(i$.hasNext()) {
            Object key = i$.next();
            Object value = source.get(key);
            this.put(key, value);
         }
      }

   }

   public Iterable<Object> getStringKeySet() {
      return this.keySet();
   }

   public Set<Object> propertiesThatCannotBeSetASystemProperties() {
      Set<Object> result = new HashSet();
      Iterator i$ = this.getStringKeySet().iterator();

      while(i$.hasNext()) {
         Object key = i$.next();
         if (keysThatCannotBeUsedAsSystemProperties.contains(key)) {
            result.add(key);
         }
      }

      return result;
   }

   public void copyToSystemProperties() {
      Iterator i$ = this.items.iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         String key = (String)o;
         String value = this.getProperty(key);
         System.setProperty(key, value);
      }

   }

   static SurefireProperties calculateEffectiveProperties(Properties systemProperties, Map<String, String> systemPropertyVariables, Properties userProperties, SurefireProperties props) {
      SurefireProperties result = new SurefireProperties();
      result.copyPropertiesFrom(systemProperties);
      result.copyPropertiesFrom(props);
      copyProperties(result, systemPropertyVariables);
      copyProperties(result, systemPropertyVariables);
      result.copyPropertiesFrom(userProperties);
      return result;
   }

   public static void copyProperties(Properties target, Map<String, String> source) {
      if (source != null) {
         Iterator i$ = source.keySet().iterator();

         while(i$.hasNext()) {
            String key = (String)i$.next();
            String value = (String)source.get(key);
            if (value != null) {
               target.setProperty(key, value);
            }
         }
      }

   }

   public void copyTo(Map target) {
      Iterator iter = this.keySet().iterator();

      while(iter.hasNext()) {
         Object key = iter.next();
         target.put(key, this.get(key));
      }

   }

   public void setProperty(String key, File file) {
      if (file != null) {
         this.setProperty(key, (String)file.toString());
      }

   }

   public void setProperty(String key, Boolean aBoolean) {
      if (aBoolean != null) {
         this.setProperty(key, (String)aBoolean.toString());
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
               this.setProperty(propertyPrefix + i, (String)aStringArray);
               ++i;
            }
         }

      }
   }

   public void setClasspath(String prefix, Classpath classpath) {
      List classpathElements = classpath.getClassPath();

      for(int i = 0; i < classpathElements.size(); ++i) {
         String element = (String)classpathElements.get(i);
         this.setProperty(prefix + i, (String)element);
      }

   }

   private static SurefireProperties loadProperties(InputStream inStream) throws IOException {
      Properties p = new Properties();

      try {
         p.load(inStream);
      } finally {
         close(inStream);
      }

      return new SurefireProperties(p);
   }

   public static SurefireProperties loadProperties(File file) throws IOException {
      return file != null ? loadProperties((InputStream)(new FileInputStream(file))) : new SurefireProperties();
   }

   private static void close(InputStream inputStream) {
      if (inputStream != null) {
         try {
            inputStream.close();
         } catch (IOException var2) {
         }

      }
   }

   public void setNullableProperty(String key, String value) {
      if (value != null) {
         super.setProperty(key, value);
      }

   }
}
