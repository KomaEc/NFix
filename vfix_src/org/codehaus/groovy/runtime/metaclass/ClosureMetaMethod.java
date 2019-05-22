package org.codehaus.groovy.runtime.metaclass;

import groovy.lang.Closure;
import groovy.lang.ClosureInvokingMethod;
import groovy.lang.MetaMethod;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.CachedMethod;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.MethodClosure;

public class ClosureMetaMethod extends MetaMethod implements ClosureInvokingMethod {
   private final Closure callable;
   private final CachedMethod doCall;
   private final String name;
   private final CachedClass declaringClass;

   public ClosureMetaMethod(String name, Closure c, CachedMethod doCall) {
      this(name, c.getOwner().getClass(), c, doCall);
   }

   public ClosureMetaMethod(String name, Class declaringClass, Closure c, CachedMethod doCall) {
      super(doCall.getNativeParameterTypes());
      this.name = name;
      this.callable = c;
      this.doCall = doCall;
      this.declaringClass = ReflectionCache.getCachedClass(declaringClass);
   }

   public int getModifiers() {
      return 1;
   }

   public String getName() {
      return this.name;
   }

   public Class getReturnType() {
      return Object.class;
   }

   public CachedClass getDeclaringClass() {
      return this.declaringClass;
   }

   public Object invoke(Object object, Object[] arguments) {
      Closure cloned = (Closure)this.callable.clone();
      cloned.setDelegate(object);
      arguments = this.coerceArgumentsToClasses(arguments);
      return this.doCall.invoke(cloned, arguments);
   }

   public Closure getClosure() {
      return this.callable;
   }

   public static List<MetaMethod> createMethodList(final String name, final Class declaringClass, final Closure closure) {
      List<MetaMethod> res = new ArrayList();
      if (closure instanceof MethodClosure) {
         MethodClosure methodClosure = (MethodClosure)closure;
         Object owner = closure.getOwner();
         Class ownerClass = (Class)((Class)(owner instanceof Class ? owner : owner.getClass()));
         CachedMethod[] arr$ = ReflectionCache.getCachedClass(ownerClass).getMethods();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            CachedMethod method = arr$[i$];
            if (method.getName().equals(methodClosure.getMethod())) {
               MetaMethod metaMethod = new ClosureMetaMethod.MethodClosureMetaMethod(name, declaringClass, closure, method);
               res.add(adjustParamTypesForStdMethods(metaMethod, name));
            }
         }
      } else if (closure instanceof GeneratedClosure) {
         CachedMethod[] arr$ = ReflectionCache.getCachedClass(closure.getClass()).getMethods();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            CachedMethod method = arr$[i$];
            if (method.getName().equals("doCall")) {
               MetaMethod metaMethod = new ClosureMetaMethod(name, declaringClass, closure, method);
               res.add(adjustParamTypesForStdMethods(metaMethod, name));
            }
         }
      } else {
         MetaMethod metaMethod = new MetaMethod(closure.getParameterTypes()) {
            public int getModifiers() {
               return 1;
            }

            public String getName() {
               return name;
            }

            public Class getReturnType() {
               return Object.class;
            }

            public CachedClass getDeclaringClass() {
               return ReflectionCache.getCachedClass(declaringClass);
            }

            public Object invoke(Object object, Object[] arguments) {
               Closure cloned = (Closure)closure.clone();
               cloned.setDelegate(object);
               arguments = this.coerceArgumentsToClasses(arguments);
               return InvokerHelper.invokeMethod(cloned, "call", arguments);
            }
         };
         res.add(adjustParamTypesForStdMethods(metaMethod, name));
      }

      return res;
   }

   private static MetaMethod adjustParamTypesForStdMethods(MetaMethod metaMethod, String methodName) {
      Class[] nativeParamTypes = metaMethod.getNativeParameterTypes();
      nativeParamTypes = nativeParamTypes != null ? nativeParamTypes : new Class[0];
      if ("methodMissing".equals(methodName) && nativeParamTypes.length == 2 && nativeParamTypes[0] != String.class) {
         nativeParamTypes[0] = String.class;
      }

      return metaMethod;
   }

   public CachedMethod getDoCall() {
      return this.doCall;
   }

   public static ClosureMetaMethod copy(ClosureMetaMethod closureMethod) {
      return (ClosureMetaMethod)(closureMethod instanceof ClosureMetaMethod.MethodClosureMetaMethod ? new ClosureMetaMethod.MethodClosureMetaMethod(closureMethod.getName(), closureMethod.getDeclaringClass().getTheClass(), closureMethod.getClosure(), closureMethod.getDoCall()) : new ClosureMetaMethod(closureMethod.getName(), closureMethod.getDeclaringClass().getTheClass(), closureMethod.getClosure(), closureMethod.getDoCall()));
   }

   private static class MethodClosureMetaMethod extends ClosureMetaMethod {
      public MethodClosureMetaMethod(String name, Class declaringClass, Closure closure, CachedMethod method) {
         super(name, declaringClass, closure, method);
      }

      public Object invoke(Object object, Object[] arguments) {
         return this.getDoCall().invoke(this.getClosure().getOwner(), arguments);
      }
   }
}
