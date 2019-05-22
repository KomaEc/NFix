package org.codehaus.groovy.runtime.dgmimpl.arrays;

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite;

public class DoubleArrayGetAtMetaMethod extends ArrayGetAtMetaMethod {
   private static final CachedClass ARR_CLASS = ReflectionCache.getCachedClass(double[].class);

   public Class getReturnType() {
      return Double.class;
   }

   public final CachedClass getDeclaringClass() {
      return ARR_CLASS;
   }

   public Object invoke(Object object, Object[] args) {
      double[] objects = (double[])((double[])object);
      return objects[normaliseIndex((Integer)args[0], objects.length)];
   }

   public CallSite createPojoCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
      return (CallSite)(!(args[0] instanceof Integer) ? PojoMetaMethodSite.createNonAwareCallSite(site, metaClass, metaMethod, params, args) : new DoubleArrayGetAtMetaMethod.MyPojoMetaMethodSite(site, metaClass, metaMethod, params));
   }

   private static class MyPojoMetaMethodSite extends PojoMetaMethodSite {
      public MyPojoMetaMethodSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
         super(site, metaClass, metaMethod, params);
      }

      public Object call(Object receiver, Object arg) throws Throwable {
         if (receiver instanceof double[] && arg instanceof Integer && this.checkPojoMetaClass()) {
            double[] objects = (double[])((double[])receiver);
            return objects[ArrayMetaMethod.normaliseIndex((Integer)arg, objects.length)];
         } else {
            return super.call(receiver, arg);
         }
      }
   }
}
