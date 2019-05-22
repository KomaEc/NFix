package org.codehaus.groovy.runtime.dgmimpl.arrays;

import groovy.lang.GString;
import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ObjectArrayPutAtMetaMethod extends ArrayPutAtMetaMethod {
   private static final CachedClass OBJECT_CLASS = ReflectionCache.getCachedClass(Object.class);
   private static final CachedClass OBJECT_ARR_CLASS;
   private static final CachedClass[] PARAM_CLASS_ARR;

   public ObjectArrayPutAtMetaMethod() {
      this.parameterTypes = PARAM_CLASS_ARR;
   }

   public final CachedClass getDeclaringClass() {
      return OBJECT_ARR_CLASS;
   }

   public Object invoke(Object object, Object[] arguments) {
      Object[] objects = (Object[])((Object[])object);
      int index = normaliseIndex((Integer)arguments[0], objects.length);
      objects[index] = adjustNewValue(objects, arguments[1]);
      return null;
   }

   private static Object adjustNewValue(Object[] objects, Object newValue) {
      Class arrayComponentClass = objects.getClass().getComponentType();
      Object adjustedNewVal = newValue;
      if (newValue instanceof Number) {
         if (!arrayComponentClass.equals(newValue.getClass())) {
            adjustedNewVal = DefaultTypeTransformation.castToType(newValue, arrayComponentClass);
         }
      } else if (Character.class.isAssignableFrom(arrayComponentClass)) {
         adjustedNewVal = DefaultTypeTransformation.getCharFromSizeOneString(newValue);
      } else if (Number.class.isAssignableFrom(arrayComponentClass) && (newValue instanceof Character || newValue instanceof String || newValue instanceof GString)) {
         Character ch = DefaultTypeTransformation.getCharFromSizeOneString(newValue);
         adjustedNewVal = DefaultTypeTransformation.castToType(ch, arrayComponentClass);
      }

      return adjustedNewVal;
   }

   public CallSite createPojoCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
      return (CallSite)(!(args[0] instanceof Integer) ? PojoMetaMethodSite.createNonAwareCallSite(site, metaClass, metaMethod, params, args) : new ObjectArrayPutAtMetaMethod.MyPojoMetaMethodSite(site, metaClass, metaMethod, params));
   }

   static {
      OBJECT_ARR_CLASS = ReflectionCache.OBJECT_ARRAY_CLASS;
      PARAM_CLASS_ARR = new CachedClass[]{INTEGER_CLASS, OBJECT_CLASS};
   }

   private static class MyPojoMetaMethodSite extends PojoMetaMethodSite {
      public MyPojoMetaMethodSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
         super(site, metaClass, metaMethod, params);
      }

      public Object call(Object receiver, Object arg1, Object arg2) throws Throwable {
         if (this.checkPojoMetaClass()) {
            try {
               Object[] objects = (Object[])((Object[])receiver);
               objects[ArrayMetaMethod.normaliseIndex((Integer)arg1, objects.length)] = ObjectArrayPutAtMetaMethod.adjustNewValue(objects, arg2);
               return null;
            } catch (ClassCastException var5) {
               if (receiver instanceof Object[] && arg1 instanceof Integer) {
                  throw var5;
               }
            }
         }

         return super.call(receiver, arg1, arg2);
      }
   }
}
