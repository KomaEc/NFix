package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

public class PropertiesfileCache implements Cache {
   private File cachefile = null;
   private Properties cache = new Properties();
   private boolean cacheLoaded = false;
   private boolean cacheDirty = true;

   public PropertiesfileCache() {
   }

   public PropertiesfileCache(File cachefile) {
      this.cachefile = cachefile;
   }

   public void setCachefile(File file) {
      this.cachefile = file;
   }

   public File getCachefile() {
      return this.cachefile;
   }

   public boolean isValid() {
      return this.cachefile != null;
   }

   public void load() {
      if (this.cachefile != null && this.cachefile.isFile() && this.cachefile.canRead()) {
         try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(this.cachefile));
            this.cache.load(bis);
            bis.close();
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

      this.cacheLoaded = true;
      this.cacheDirty = false;
   }

   public void save() {
      if (this.cacheDirty) {
         if (this.cachefile != null && this.cache.propertyNames().hasMoreElements()) {
            try {
               BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(this.cachefile));
               this.cache.store(bos, (String)null);
               bos.flush();
               bos.close();
            } catch (Exception var2) {
               var2.printStackTrace();
            }
         }

         this.cacheDirty = false;
      }
   }

   public void delete() {
      this.cache = new Properties();
      this.cachefile.delete();
      this.cacheLoaded = true;
      this.cacheDirty = false;
   }

   public Object get(Object key) {
      if (!this.cacheLoaded) {
         this.load();
      }

      try {
         return this.cache.getProperty(String.valueOf(key));
      } catch (ClassCastException var3) {
         return null;
      }
   }

   public void put(Object key, Object value) {
      this.cache.put(String.valueOf(key), String.valueOf(value));
      this.cacheDirty = true;
   }

   public Iterator iterator() {
      Vector v = new Vector();
      Enumeration en = this.cache.propertyNames();

      while(en.hasMoreElements()) {
         v.add(en.nextElement());
      }

      return v.iterator();
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("<PropertiesfileCache:");
      buf.append("cachefile=").append(this.cachefile);
      buf.append(";noOfEntries=").append(this.cache.size());
      buf.append(">");
      return buf.toString();
   }
}
