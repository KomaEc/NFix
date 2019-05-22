package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import com.gzoltar.shaded.org.pitest.reloc.xstream.XStreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.Caching;
import com.gzoltar.shaded.org.pitest.reloc.xstream.security.ForbiddenClassException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CachingMapper extends MapperWrapper implements Caching {
   private transient Map realClassCache;

   public CachingMapper(Mapper wrapped) {
      super(wrapped);
      this.readResolve();
   }

   public Class realClass(String elementName) {
      Object cached = this.realClassCache.get(elementName);
      if (cached != null) {
         if (cached instanceof Class) {
            return (Class)cached;
         } else {
            throw (XStreamException)cached;
         }
      } else {
         try {
            Class result = super.realClass(elementName);
            this.realClassCache.put(elementName, result);
            return result;
         } catch (ForbiddenClassException var4) {
            this.realClassCache.put(elementName, var4);
            throw var4;
         } catch (CannotResolveClassException var5) {
            this.realClassCache.put(elementName, var5);
            throw var5;
         }
      }
   }

   public void flushCache() {
      this.realClassCache.clear();
   }

   private Object readResolve() {
      this.realClassCache = Collections.synchronizedMap(new HashMap(128));
      return this;
   }
}
