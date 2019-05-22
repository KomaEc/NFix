package org.codehaus.groovy.runtime.dgmimpl;

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteAwareMetaMethod;
import org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite;
import org.codehaus.groovy.runtime.typehandling.NumberMath;

public abstract class NumberNumberMetaMethod extends CallSiteAwareMetaMethod {
   private static final CachedClass NUMBER_CLASS = ReflectionCache.getCachedClass(Number.class);
   private static final CachedClass[] NUMBER_CLASS_ARR;

   protected NumberNumberMetaMethod() {
      this.parameterTypes = NUMBER_CLASS_ARR;
   }

   public int getModifiers() {
      return 1;
   }

   public Class getReturnType() {
      return NUMBER_CLASS.getTheClass();
   }

   public final CachedClass getDeclaringClass() {
      return NUMBER_CLASS;
   }

   static {
      NUMBER_CLASS_ARR = new CachedClass[]{NUMBER_CLASS};
   }

   public abstract static class NumberNumberCallSite extends PojoMetaMethodSite {
      final NumberMath math;

      public NumberNumberCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Number receiver, Number arg) {
         super(site, metaClass, metaMethod, params);
         this.math = NumberMath.getMath(receiver, arg);
      }
   }
}
