package org.codehaus.groovy.reflection.stdclasses;

import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.CachedMethod;
import org.codehaus.groovy.reflection.ClassInfo;

public class CachedClosureClass extends CachedClass {
   private final Class[] parameterTypes;
   private final int maximumNumberOfParameters;

   public CachedClosureClass(Class klazz, ClassInfo classInfo) {
      super(klazz, classInfo);
      CachedMethod[] methods = this.getMethods();
      int maximumNumberOfParameters = -1;
      Class[] parameterTypes = null;
      CachedMethod[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         CachedMethod method = arr$[i$];
         if ("doCall".equals(method.getName())) {
            Class[] pt = method.getNativeParameterTypes();
            if (pt.length > maximumNumberOfParameters) {
               parameterTypes = pt;
               maximumNumberOfParameters = pt.length;
            }
         }
      }

      maximumNumberOfParameters = Math.max(maximumNumberOfParameters, 0);
      this.maximumNumberOfParameters = maximumNumberOfParameters;
      this.parameterTypes = parameterTypes;
   }

   public Class[] getParameterTypes() {
      return this.parameterTypes;
   }

   public int getMaximumNumberOfParameters() {
      return this.maximumNumberOfParameters;
   }
}
