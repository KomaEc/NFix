package org.codehaus.groovy.runtime.dgmimpl.arrays;

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite;

public class ShortArrayGetAtMetaMethod extends ArrayGetAtMetaMethod {
   private static final CachedClass ARR_CLASS = ReflectionCache.getCachedClass(short[].class);

   public Class getReturnType() {
      return Short.class;
   }

   public final CachedClass getDeclaringClass() {
      return ARR_CLASS;
   }

   public Object invoke(Object object, Object[] args) {
      short[] objects = (short[])((short[])object);
      return objects[normaliseIndex((Integer)args[0], objects.length)];
   }

   public CallSite createPojoCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
      return (CallSite)(!(args[0] instanceof Integer) ? PojoMetaMethodSite.createNonAwareCallSite(site, metaClass, metaMethod, params, args) : new ShortArrayGetAtMetaMethod.MyPojoMetaMethodSite(site, metaClass, metaMethod, params));
   }

   private static class MyPojoMetaMethodSite extends PojoMetaMethodSite {
      public MyPojoMetaMethodSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
         super(site, metaClass, metaMethod, params);
      }

      public Object call(Object receiver, Object arg) throws Throwable {
         if (receiver instanceof short[] && arg instanceof Integer && this.checkPojoMetaClass()) {
            short[] objects = (short[])((short[])receiver);
            return objects[ArrayMetaMethod.normaliseIndex((Integer)arg, objects.length)];
         } else {
            return super.call(receiver, arg);
         }
      }
   }
}
