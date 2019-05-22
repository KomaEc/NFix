package org.apache.velocity.runtime.resource.loader;

import java.io.InputStream;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.resource.Resource;

public abstract class ResourceLoader {
   protected boolean isCachingOn = false;
   protected long modificationCheckInterval = 2L;
   protected String className = null;
   protected RuntimeServices rsvc = null;
   protected Log log = null;
   // $FF: synthetic field
   static Class class$org$apache$velocity$runtime$resource$ResourceCacheImpl;

   public void commonInit(RuntimeServices rs, ExtendedProperties configuration) {
      this.rsvc = rs;
      this.log = this.rsvc.getLog();

      try {
         this.isCachingOn = configuration.getBoolean("cache", false);
      } catch (Exception var6) {
         this.isCachingOn = false;
         this.log.error("Exception using default of '" + this.isCachingOn + '\'', var6);
      }

      try {
         this.modificationCheckInterval = configuration.getLong("modificationCheckInterval", 0L);
      } catch (Exception var5) {
         this.modificationCheckInterval = 0L;
         this.log.error("Exception using default of '" + this.modificationCheckInterval + '\'', var5);
      }

      this.className = (class$org$apache$velocity$runtime$resource$ResourceCacheImpl == null ? (class$org$apache$velocity$runtime$resource$ResourceCacheImpl = class$("org.apache.velocity.runtime.resource.ResourceCacheImpl")) : class$org$apache$velocity$runtime$resource$ResourceCacheImpl).getName();

      try {
         this.className = configuration.getString("class", this.className);
      } catch (Exception var4) {
         this.log.error("Exception using default of '" + this.className + '\'', var4);
      }

   }

   public abstract void init(ExtendedProperties var1);

   public abstract InputStream getResourceStream(String var1) throws ResourceNotFoundException;

   public abstract boolean isSourceModified(Resource var1);

   public abstract long getLastModified(Resource var1);

   public String getClassName() {
      return this.className;
   }

   public void setCachingOn(boolean value) {
      this.isCachingOn = value;
   }

   public boolean isCachingOn() {
      return this.isCachingOn;
   }

   public void setModificationCheckInterval(long modificationCheckInterval) {
      this.modificationCheckInterval = modificationCheckInterval;
   }

   public long getModificationCheckInterval() {
      return this.modificationCheckInterval;
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
