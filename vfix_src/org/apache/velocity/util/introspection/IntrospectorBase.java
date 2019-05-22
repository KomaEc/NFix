package org.apache.velocity.util.introspection;

import java.lang.reflect.Method;
import org.apache.velocity.runtime.log.Log;

public abstract class IntrospectorBase implements IntrospectorCacheListener {
   protected final Log log;
   private final IntrospectorCache introspectorCache;

   protected IntrospectorBase(Log log) {
      this.log = log;
      this.introspectorCache = new IntrospectorCacheImpl(log);
      this.introspectorCache.addListener(this);
   }

   public Method getMethod(Class c, String name, Object[] params) throws IllegalArgumentException, MethodMap.AmbiguousException {
      if (c == null) {
         throw new IllegalArgumentException("class object is null!");
      } else if (params == null) {
         throw new IllegalArgumentException("params object is null!");
      } else {
         ClassMap classMap = null;
         IntrospectorCache ic = this.getIntrospectorCache();
         synchronized(ic) {
            classMap = ic.get(c);
            if (classMap == null) {
               classMap = ic.put(c);
            }
         }

         return classMap.findMethod(name, params);
      }
   }

   protected IntrospectorCache getIntrospectorCache() {
      return this.introspectorCache;
   }

   /** @deprecated */
   protected void clearCache() {
      this.getIntrospectorCache().clear();
   }

   /** @deprecated */
   protected ClassMap createClassMap(Class c) {
      return this.getIntrospectorCache().put(c);
   }

   /** @deprecated */
   protected ClassMap lookupClassMap(Class c) {
      return this.getIntrospectorCache().get(c);
   }

   public void triggerClear() {
   }

   public void triggerGet(Class c, ClassMap classMap) {
   }

   public void triggerPut(Class c, ClassMap classMap) {
   }
}
