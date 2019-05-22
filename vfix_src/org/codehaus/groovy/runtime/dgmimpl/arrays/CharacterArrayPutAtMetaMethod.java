package org.codehaus.groovy.runtime.dgmimpl.arrays;

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class CharacterArrayPutAtMetaMethod extends ArrayPutAtMetaMethod {
   private static final CachedClass OBJECT_CLASS;
   private static final CachedClass ARR_CLASS;
   private static final CachedClass[] PARAM_CLASS_ARR;

   public CharacterArrayPutAtMetaMethod() {
      this.parameterTypes = PARAM_CLASS_ARR;
   }

   public final CachedClass getDeclaringClass() {
      return ARR_CLASS;
   }

   public Object invoke(Object object, Object[] args) {
      char[] objects = (char[])((char[])object);
      int index = normaliseIndex((Integer)args[0], objects.length);
      objects[index] = DefaultTypeTransformation.getCharFromSizeOneString(args[1]);
      return null;
   }

   public CallSite createPojoCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {
      return (CallSite)(args[0] instanceof Integer && args[1] instanceof Character ? new CharacterArrayPutAtMetaMethod.MyPojoMetaMethodSite(site, metaClass, metaMethod, params) : PojoMetaMethodSite.createNonAwareCallSite(site, metaClass, metaMethod, params, args));
   }

   static {
      OBJECT_CLASS = ReflectionCache.OBJECT_CLASS;
      ARR_CLASS = ReflectionCache.getCachedClass(char[].class);
      PARAM_CLASS_ARR = new CachedClass[]{INTEGER_CLASS, OBJECT_CLASS};
   }

   private static class MyPojoMetaMethodSite extends PojoMetaMethodSite {
      public MyPojoMetaMethodSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params) {
         super(site, metaClass, metaMethod, params);
      }

      public Object call(Object receiver, Object[] args) throws Throwable {
         if (receiver instanceof char[] && args[0] instanceof Integer && args[1] instanceof Character && this.checkPojoMetaClass()) {
            char[] objects = (char[])((char[])receiver);
            objects[ArrayMetaMethod.normaliseIndex((Integer)args[0], objects.length)] = (Character)args[1];
            return null;
         } else {
            return super.call(receiver, args);
         }
      }

      public Object call(Object receiver, Object arg1, Object arg2) throws Throwable {
         if (this.checkPojoMetaClass()) {
            try {
               char[] objects = (char[])((char[])receiver);
               objects[ArrayMetaMethod.normaliseIndex((Integer)arg1, objects.length)] = (Character)arg2;
               return null;
            } catch (ClassCastException var5) {
               if (receiver instanceof char[] && arg1 instanceof Integer) {
                  throw var5;
               }
            }
         }

         return super.call(receiver, arg1, arg2);
      }
   }
}
