package org.codehaus.groovy.runtime.dgmimpl.arrays;

import groovy.lang.GString;
import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class DoubleArrayPutAtMetaMethod extends ArrayPutAtMetaMethod {
   private static final CachedClass OBJECT_CLASS;
   private static final CachedClass ARR_CLASS;
   private static final CachedClass[] PARAM_CLASS_ARR;

   public DoubleArrayPutAtMetaMethod() {
      this.parameterTypes = PARAM_CLASS_ARR;
   }

   public final CachedClass getDeclaringClass() {
      return ARR_CLASS;
   }

   public Object invoke(Object object, Object[] args) {
      double[] objects = (double[])((double[])object);
      int index = normaliseIndex((Integer)args[0], objects.length);
      Object newValue = args[1];
      if (!(newValue instanceof Double)) {
         if (!(newValue instanceof Character) && !(newValue instanceof String) && !(newValue instanceof GString)) {
            objects[index] = ((Number)newValue).doubleValue();
         } else {
            Character ch = DefaultTypeTransformation.getCharFromSizeOneString(newValue);
            objects[index] = (Double)DefaultTypeTransformation.castToType(ch, Double.class);
         }
      } else {
         objects[index] = (Double)args[1];
      }

      return null;
   }

   public CallSite createPojoCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
      return (CallSite)(args[0] instanceof Integer && args[1] instanceof Double ? new DoubleArrayPutAtMetaMethod.MyPojoMetaMethodSite(site, metaClass, metaMethod, params) : PojoMetaMethodSite.createNonAwareCallSite(site, metaClass, metaMethod, params, args));
   }

   static {
      OBJECT_CLASS = ReflectionCache.OBJECT_CLASS;
      ARR_CLASS = ReflectionCache.getCachedClass(double[].class);
      PARAM_CLASS_ARR = new CachedClass[]{INTEGER_CLASS, OBJECT_CLASS};
   }

   private static class MyPojoMetaMethodSite extends PojoMetaMethodSite {
      public MyPojoMetaMethodSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
         super(site, metaClass, metaMethod, params);
      }

      public Object call(Object receiver, Object[] args) throws Throwable {
         if (receiver instanceof double[] && args[0] instanceof Integer && args[1] instanceof Double && this.checkPojoMetaClass()) {
            double[] objects = (double[])((double[])receiver);
            objects[ArrayMetaMethod.normaliseIndex((Integer)args[0], objects.length)] = (Double)args[1];
            return null;
         } else {
            return super.call(receiver, args);
         }
      }

      public Object call(Object receiver, Object arg1, Object arg2) throws Throwable {
         if (this.checkPojoMetaClass()) {
            try {
               double[] objects = (double[])((double[])receiver);
               objects[ArrayMetaMethod.normaliseIndex((Integer)arg1, objects.length)] = (Double)arg2;
               return null;
            } catch (ClassCastException var5) {
               if (receiver instanceof double[] && arg1 instanceof Integer) {
                  throw var5;
               }
            }
         }

         return super.call(receiver, arg1, arg2);
      }
   }
}
