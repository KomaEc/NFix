package org.codehaus.groovy.reflection;

import java.lang.reflect.Array;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.wrappers.Wrapper;

public class ParameterTypes {
   protected volatile Class[] nativeParamTypes;
   protected volatile CachedClass[] parameterTypes;
   protected boolean isVargsMethod;

   public ParameterTypes() {
   }

   public ParameterTypes(Class[] pt) {
      this.nativeParamTypes = pt;
   }

   public ParameterTypes(String[] pt) {
      this.nativeParamTypes = new Class[pt.length];

      for(int i = 0; i != pt.length; ++i) {
         try {
            this.nativeParamTypes[i] = Class.forName(pt[i]);
         } catch (ClassNotFoundException var5) {
            NoClassDefFoundError err = new NoClassDefFoundError();
            err.initCause(var5);
            throw err;
         }
      }

   }

   public ParameterTypes(CachedClass[] parameterTypes) {
      this.setParametersTypes(parameterTypes);
   }

   protected final void setParametersTypes(CachedClass[] pt) {
      this.parameterTypes = pt;
      this.isVargsMethod = pt.length > 0 && pt[pt.length - 1].isArray;
   }

   public CachedClass[] getParameterTypes() {
      if (this.parameterTypes == null) {
         this.getParametersTypes0();
      }

      return this.parameterTypes;
   }

   private synchronized void getParametersTypes0() {
      if (this.parameterTypes == null) {
         Class[] npt = this.nativeParamTypes == null ? this.getPT() : this.nativeParamTypes;
         CachedClass[] pt = new CachedClass[npt.length];

         for(int i = 0; i != npt.length; ++i) {
            pt[i] = ReflectionCache.getCachedClass(npt[i]);
         }

         this.nativeParamTypes = npt;
         this.setParametersTypes(pt);
      }
   }

   public Class[] getNativeParameterTypes() {
      if (this.nativeParamTypes == null) {
         this.getNativeParameterTypes0();
      }

      return this.nativeParamTypes;
   }

   private synchronized void getNativeParameterTypes0() {
      if (this.nativeParamTypes == null) {
         Class[] npt;
         if (this.parameterTypes != null) {
            npt = new Class[this.parameterTypes.length];

            for(int i = 0; i != this.parameterTypes.length; ++i) {
               npt[i] = this.parameterTypes[i].getTheClass();
            }
         } else {
            npt = this.getPT();
         }

         this.nativeParamTypes = npt;
      }
   }

   protected Class[] getPT() {
      throw new UnsupportedOperationException(this.getClass().getName());
   }

   public boolean isVargsMethod(Object[] arguments) {
      if (!this.isVargsMethod) {
         return false;
      } else {
         int lenMinus1 = this.parameterTypes.length - 1;
         if (lenMinus1 == arguments.length) {
            return true;
         } else if (lenMinus1 > arguments.length) {
            return false;
         } else if (arguments.length > this.parameterTypes.length) {
            return true;
         } else {
            Object last = arguments[arguments.length - 1];
            if (last == null) {
               return true;
            } else {
               Class clazz = last.getClass();
               return !clazz.equals(this.parameterTypes[lenMinus1].getTheClass());
            }
         }
      }
   }

   public final Object[] coerceArgumentsToClasses(Object[] argumentArray) {
      argumentArray = this.correctArguments(argumentArray);
      CachedClass[] pt = this.parameterTypes;
      int len = argumentArray.length;

      for(int i = 0; i < len; ++i) {
         Object argument = argumentArray[i];
         if (argument != null) {
            argumentArray[i] = pt[i].coerceArgument(argument);
         }
      }

      return argumentArray;
   }

   public Object[] correctArguments(Object[] argumentArray) {
      if (argumentArray == null) {
         return MetaClassHelper.EMPTY_ARRAY;
      } else {
         CachedClass[] pt = this.getParameterTypes();
         if (pt.length == 1 && argumentArray.length == 0) {
            return this.isVargsMethod ? new Object[]{Array.newInstance(pt[0].getTheClass().getComponentType(), 0)} : MetaClassHelper.ARRAY_WITH_NULL;
         } else {
            return this.isVargsMethod && this.isVargsMethod(argumentArray) ? fitToVargs(argumentArray, pt) : argumentArray;
         }
      }
   }

   private static Object[] fitToVargs(Object[] argumentArray, CachedClass[] paramTypes) {
      Class vargsClass = ReflectionCache.autoboxType(paramTypes[paramTypes.length - 1].getTheClass().getComponentType());
      Object[] newArgs;
      Object wrapped;
      if (argumentArray.length == paramTypes.length - 1) {
         newArgs = new Object[paramTypes.length];
         System.arraycopy(argumentArray, 0, newArgs, 0, argumentArray.length);
         wrapped = MetaClassHelper.makeArray((Object)null, vargsClass, 0);
         newArgs[newArgs.length - 1] = wrapped;
         return newArgs;
      } else if (argumentArray.length == paramTypes.length) {
         Object lastArgument = argumentArray[argumentArray.length - 1];
         if (lastArgument != null && !lastArgument.getClass().isArray()) {
            wrapped = MetaClassHelper.makeArray(lastArgument, vargsClass, 1);
            System.arraycopy(argumentArray, argumentArray.length - 1, wrapped, 0, 1);
            Object[] newArgs = new Object[paramTypes.length];
            System.arraycopy(argumentArray, 0, newArgs, 0, paramTypes.length - 1);
            newArgs[newArgs.length - 1] = wrapped;
            return newArgs;
         } else {
            return argumentArray;
         }
      } else if (argumentArray.length > paramTypes.length) {
         newArgs = new Object[paramTypes.length];
         System.arraycopy(argumentArray, 0, newArgs, 0, paramTypes.length - 1);
         int numberOfVargs = argumentArray.length - paramTypes.length;
         Object vargs = MetaClassHelper.makeCommonArray(argumentArray, paramTypes.length - 1, vargsClass);
         newArgs[newArgs.length - 1] = vargs;
         return newArgs;
      } else {
         throw new GroovyBugError("trying to call a vargs method without enough arguments");
      }
   }

   public boolean isValidMethod(Class[] arguments) {
      if (arguments == null) {
         return true;
      } else {
         int size = arguments.length;
         CachedClass[] pt = this.getParameterTypes();
         int paramMinus1 = pt.length - 1;
         if (this.isVargsMethod && size >= paramMinus1) {
            return this.isValidVarargsMethod(arguments, size, pt, paramMinus1);
         } else if (pt.length == size) {
            return this.isValidExactMethod(arguments, pt);
         } else {
            return pt.length == 1 && size == 0 && !pt[0].isPrimitive;
         }
      }
   }

   private boolean isValidExactMethod(Class[] arguments, CachedClass[] pt) {
      int size = pt.length;

      for(int i = 0; i < size; ++i) {
         if (!pt[i].isAssignableFrom(arguments[i])) {
            return false;
         }
      }

      return true;
   }

   public boolean isValidExactMethod(Object[] args) {
      this.getParametersTypes0();
      int size = args.length;
      if (size != this.parameterTypes.length) {
         return false;
      } else {
         for(int i = 0; i < size; ++i) {
            if (args[i] != null && !this.parameterTypes[i].isAssignableFrom(args[i].getClass())) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean isValidExactMethod(Class[] args) {
      this.getParametersTypes0();
      int size = args.length;
      if (size != this.parameterTypes.length) {
         return false;
      } else {
         for(int i = 0; i < size; ++i) {
            if (args[i] != null && !this.parameterTypes[i].isAssignableFrom(args[i])) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean testComponentAssignable(Class toTestAgainst, Class toTest) {
      Class component = toTest.getComponentType();
      return component == null ? false : MetaClassHelper.isAssignableFrom(toTestAgainst, component);
   }

   private boolean isValidVarargsMethod(Class[] arguments, int size, CachedClass[] pt, int paramMinus1) {
      for(int i = 0; i < paramMinus1; ++i) {
         if (!pt[i].isAssignableFrom(arguments[i])) {
            return false;
         }
      }

      CachedClass varg = pt[paramMinus1];
      Class clazz = varg.getTheClass().getComponentType();
      if (size != pt.length || !varg.isAssignableFrom(arguments[paramMinus1]) && !testComponentAssignable(clazz, arguments[paramMinus1])) {
         for(int i = paramMinus1; i < size; ++i) {
            if (!MetaClassHelper.isAssignableFrom(clazz, arguments[i])) {
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public boolean isValidMethod(Object[] arguments) {
      if (arguments == null) {
         return true;
      } else {
         int size = arguments.length;
         CachedClass[] paramTypes = this.getParameterTypes();
         int paramMinus1 = paramTypes.length - 1;
         int i;
         if (size >= paramMinus1 && paramTypes.length > 0 && paramTypes[paramMinus1].isArray) {
            for(i = 0; i < paramMinus1; ++i) {
               if (!paramTypes[i].isAssignableFrom(this.getArgClass(arguments[i]))) {
                  return false;
               }
            }

            CachedClass varg = paramTypes[paramMinus1];
            Class clazz = varg.getTheClass().getComponentType();
            if (size == paramTypes.length && (varg.isAssignableFrom(this.getArgClass(arguments[paramMinus1])) || testComponentAssignable(clazz, this.getArgClass(arguments[paramMinus1])))) {
               return true;
            } else {
               for(int i = paramMinus1; i < size; ++i) {
                  if (!MetaClassHelper.isAssignableFrom(clazz, this.getArgClass(arguments[i]))) {
                     return false;
                  }
               }

               return true;
            }
         } else if (paramTypes.length == size) {
            for(i = 0; i < size; ++i) {
               if (!paramTypes[i].isAssignableFrom(this.getArgClass(arguments[i]))) {
                  return false;
               }
            }

            return true;
         } else {
            return paramTypes.length == 1 && size == 0 && !paramTypes[0].isPrimitive;
         }
      }
   }

   private Class getArgClass(Object arg) {
      Class cls;
      if (arg == null) {
         cls = null;
      } else if (arg instanceof Wrapper) {
         cls = ((Wrapper)arg).getType();
      } else {
         cls = arg.getClass();
      }

      return cls;
   }
}
