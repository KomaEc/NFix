package org.codehaus.groovy.reflection;

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import groovy.lang.MissingMethodException;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import org.codehaus.groovy.classgen.BytecodeHelper;
import org.codehaus.groovy.runtime.InvokerInvocationException;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteGenerator;
import org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite;
import org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite;
import org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite;
import org.codehaus.groovy.runtime.metaclass.MethodHelper;

public class CachedMethod extends MetaMethod implements Comparable {
   public final CachedClass cachedClass;
   private final Method cachedMethod;
   private int hashCode;
   private static CachedMethod.MyComparator comparator = new CachedMethod.MyComparator();
   private SoftReference<Constructor> pogoCallSiteConstructor;
   private SoftReference<Constructor> pojoCallSiteConstructor;
   private SoftReference<Constructor> staticCallSiteConstructor;

   public CachedMethod(CachedClass clazz, Method method) {
      this.cachedMethod = method;
      this.cachedClass = clazz;
   }

   public CachedMethod(Method method) {
      this(ReflectionCache.getCachedClass(method.getDeclaringClass()), method);
   }

   public static CachedMethod find(Method method) {
      CachedMethod[] methods = ReflectionCache.getCachedClass(method.getDeclaringClass()).getMethods();
      int i = Arrays.binarySearch(methods, method, comparator);
      return i < 0 ? null : methods[i];
   }

   protected Class[] getPT() {
      return this.cachedMethod.getParameterTypes();
   }

   public String getName() {
      return this.cachedMethod.getName();
   }

   public String getDescriptor() {
      return BytecodeHelper.getMethodDescriptor(this.getReturnType(), this.getNativeParameterTypes());
   }

   public CachedClass getDeclaringClass() {
      return this.cachedClass;
   }

   public final Object invoke(Object object, Object[] arguments) {
      try {
         return this.cachedMethod.invoke(object, arguments);
      } catch (IllegalArgumentException var5) {
         throw new InvokerInvocationException(var5);
      } catch (IllegalAccessException var6) {
         throw new InvokerInvocationException(var6);
      } catch (InvocationTargetException var7) {
         Throwable cause = var7.getCause();
         throw cause instanceof RuntimeException && !(cause instanceof MissingMethodException) ? (RuntimeException)cause : new InvokerInvocationException(var7);
      }
   }

   public ParameterTypes getParamTypes() {
      return null;
   }

   public Class getReturnType() {
      return this.cachedMethod.getReturnType();
   }

   public int getParamsCount() {
      return this.getParameterTypes().length;
   }

   public int getModifiers() {
      return this.cachedMethod.getModifiers();
   }

   public String getSignature() {
      return this.getName() + this.getDescriptor();
   }

   public final Method setAccessible() {
      return this.cachedMethod;
   }

   public boolean isStatic() {
      return MethodHelper.isStatic(this.cachedMethod);
   }

   public int compareTo(Object o) {
      return o instanceof CachedMethod ? this.compareToCachedMethod((CachedMethod)o) : this.compareToMethod((Method)o);
   }

   private int compareToCachedMethod(CachedMethod m) {
      if (m == null) {
         return -1;
      } else {
         int strComp = this.getName().compareTo(m.getName());
         if (strComp != 0) {
            return strComp;
         } else {
            int retComp = this.getReturnType().getName().compareTo(m.getReturnType().getName());
            if (retComp != 0) {
               return retComp;
            } else {
               CachedClass[] params = this.getParameterTypes();
               CachedClass[] mparams = m.getParameterTypes();
               int pd = params.length - mparams.length;
               if (pd != 0) {
                  return pd;
               } else {
                  for(int i = 0; i != params.length; ++i) {
                     int nameComp = params[i].getName().compareTo(mparams[i].getName());
                     if (nameComp != 0) {
                        return nameComp;
                     }
                  }

                  throw new RuntimeException("Should never happen");
               }
            }
         }
      }
   }

   private int compareToMethod(Method m) {
      if (m == null) {
         return -1;
      } else {
         int strComp = this.getName().compareTo(m.getName());
         if (strComp != 0) {
            return strComp;
         } else {
            int retComp = this.getReturnType().getName().compareTo(m.getReturnType().getName());
            if (retComp != 0) {
               return retComp;
            } else {
               CachedClass[] params = this.getParameterTypes();
               Class[] mparams = m.getParameterTypes();
               int pd = params.length - mparams.length;
               if (pd != 0) {
                  return pd;
               } else {
                  for(int i = 0; i != params.length; ++i) {
                     int nameComp = params[i].getName().compareTo(mparams[i].getName());
                     if (nameComp != 0) {
                        return nameComp;
                     }
                  }

                  return 0;
               }
            }
         }
      }
   }

   public boolean equals(Object o) {
      return o instanceof CachedMethod && this.cachedMethod.equals(((CachedMethod)o).cachedMethod) || o instanceof Method && this.cachedMethod.equals(o);
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = this.cachedMethod.hashCode();
         if (this.hashCode == 0) {
            this.hashCode = -889274690;
         }
      }

      return this.hashCode;
   }

   public String toString() {
      return this.cachedMethod.toString();
   }

   public CallSite createPogoMetaMethodSite(CallSite site, MetaClassImpl metaClass, Class[] params) {
      Constructor constructor;
      if (!this.hasPogoCallSiteConstructor()) {
         constructor = null;
         if (CallSiteGenerator.isCompilable(this)) {
            constructor = CallSiteGenerator.compilePogoMethod(this);
            if (constructor != null) {
               this.pogoCallSiteConstructor = new SoftReference(constructor);
            }
         }
      }

      if (this.hasPogoCallSiteConstructor()) {
         constructor = (Constructor)this.pogoCallSiteConstructor.get();
         if (constructor != null) {
            try {
               return (CallSite)constructor.newInstance(site, metaClass, this, params);
            } catch (Throwable var6) {
            }
         }
      }

      return new PogoMetaMethodSite.PogoCachedMethodSiteNoUnwrapNoCoerce(site, metaClass, this, params);
   }

   public CallSite createPojoMetaMethodSite(CallSite site, MetaClassImpl metaClass, Class[] params) {
      Constructor constructor;
      if (!this.hasPojoCallSiteConstructor()) {
         constructor = null;
         if (CallSiteGenerator.isCompilable(this)) {
            constructor = CallSiteGenerator.compilePojoMethod(this);
            if (constructor != null) {
               this.pojoCallSiteConstructor = new SoftReference(constructor);
            }
         }
      }

      if (this.hasPogoCallSiteConstructor()) {
         constructor = (Constructor)this.pojoCallSiteConstructor.get();
         if (constructor != null) {
            try {
               return (CallSite)constructor.newInstance(site, metaClass, this, params);
            } catch (Throwable var6) {
            }
         }
      }

      return new PojoMetaMethodSite.PojoCachedMethodSiteNoUnwrapNoCoerce(site, metaClass, this, params);
   }

   public CallSite createStaticMetaMethodSite(CallSite site, MetaClassImpl metaClass, Class[] params) {
      Constructor constructor;
      if (!this.hasStaticCallSiteConstructor()) {
         constructor = null;
         if (CallSiteGenerator.isCompilable(this)) {
            constructor = CallSiteGenerator.compileStaticMethod(this);
            if (constructor != null) {
               this.staticCallSiteConstructor = new SoftReference(constructor);
            }
         }
      }

      if (this.hasStaticCallSiteConstructor()) {
         constructor = (Constructor)this.staticCallSiteConstructor.get();
         if (constructor != null) {
            try {
               return (CallSite)constructor.newInstance(site, metaClass, this, params);
            } catch (Throwable var6) {
            }
         }
      }

      return new StaticMetaMethodSite.StaticMetaMethodSiteNoUnwrapNoCoerce(site, metaClass, this, params);
   }

   public boolean hasPogoCallSiteConstructor() {
      return this.pogoCallSiteConstructor != null && this.pogoCallSiteConstructor.get() != null;
   }

   public boolean hasPojoCallSiteConstructor() {
      return this.pojoCallSiteConstructor != null && this.pojoCallSiteConstructor.get() != null;
   }

   public boolean hasStaticCallSiteConstructor() {
      return this.staticCallSiteConstructor != null && this.staticCallSiteConstructor.get() != null;
   }

   public Method getCachedMethod() {
      return this.cachedMethod;
   }

   private static class MyComparator implements Comparator {
      private MyComparator() {
      }

      public int compare(Object o1, Object o2) {
         if (o1 instanceof CachedMethod) {
            return ((CachedMethod)o1).compareTo(o2);
         } else if (o2 instanceof CachedMethod) {
            return -((CachedMethod)o2).compareTo(o1);
         } else {
            throw new ClassCastException("One of the two comperables must be a CachedMethod");
         }
      }

      // $FF: synthetic method
      MyComparator(Object x0) {
         this();
      }
   }
}
