package org.codehaus.groovy.runtime.dgmimpl.arrays;

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite;

public class ObjectArrayGetAtMetaMethod extends ArrayGetAtMetaMethod {
   private static final CachedClass OBJECT_ARR_CLASS;

   public Class getReturnType() {
      return Object.class;
   }

   public final CachedClass getDeclaringClass() {
      return OBJECT_ARR_CLASS;
   }

   public Object invoke(Object object, Object[] arguments) {
      Object[] objects = (Object[])((Object[])object);
      return objects[normaliseIndex((Integer)arguments[0], objects.length)];
   }

   public CallSite createPojoCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
      return (CallSite)(!(args[0] instanceof Integer) ? PojoMetaMethodSite.createNonAwareCallSite(site, metaClass, metaMethod, params, args) : new ObjectArrayGetAtMetaMethod.MyPojoMetaMethodSite(site, metaClass, metaMethod, params));
   }

   static {
      OBJECT_ARR_CLASS = ReflectionCache.OBJECT_ARRAY_CLASS;
   }

   private static class MyPojoMetaMethodSite extends PojoMetaMethodSite {
      public MyPojoMetaMethodSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
         super(site, metaClass, metaMethod, params);
      }

      public Object call(Object receiver, Object arg) throws Throwable {
         if (this.checkPojoMetaClass()) {
            try {
               Object[] objects = (Object[])((Object[])receiver);
               return objects[ArrayMetaMethod.normaliseIndex((Integer)arg, objects.length)];
            } catch (ClassCastException var4) {
               if (receiver instanceof Object[] && arg instanceof Integer) {
                  throw var4;
               }
            }
         }

         return super.call(receiver, arg);
      }
   }
}
