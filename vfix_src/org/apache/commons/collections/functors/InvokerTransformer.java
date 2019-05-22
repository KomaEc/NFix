package org.apache.commons.collections.functors;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.collections.FunctorException;
import org.apache.commons.collections.Transformer;

public class InvokerTransformer implements Transformer, Serializable {
   private static final long serialVersionUID = -8653385846894047688L;
   private final String iMethodName;
   private final Class[] iParamTypes;
   private final Object[] iArgs;

   public static Transformer getInstance(String methodName) {
      if (methodName == null) {
         throw new IllegalArgumentException("The method to invoke must not be null");
      } else {
         return new InvokerTransformer(methodName);
      }
   }

   public static Transformer getInstance(String methodName, Class[] paramTypes, Object[] args) {
      if (methodName == null) {
         throw new IllegalArgumentException("The method to invoke must not be null");
      } else if (paramTypes == null && args != null || paramTypes != null && args == null || paramTypes != null && args != null && paramTypes.length != args.length) {
         throw new IllegalArgumentException("The parameter types must match the arguments");
      } else if (paramTypes != null && paramTypes.length != 0) {
         paramTypes = (Class[])paramTypes.clone();
         args = (Object[])args.clone();
         return new InvokerTransformer(methodName, paramTypes, args);
      } else {
         return new InvokerTransformer(methodName);
      }
   }

   private InvokerTransformer(String methodName) {
      this.iMethodName = methodName;
      this.iParamTypes = null;
      this.iArgs = null;
   }

   public InvokerTransformer(String methodName, Class[] paramTypes, Object[] args) {
      this.iMethodName = methodName;
      this.iParamTypes = paramTypes;
      this.iArgs = args;
   }

   public Object transform(Object input) {
      if (input == null) {
         return null;
      } else {
         try {
            Class cls = input.getClass();
            Method method = cls.getMethod(this.iMethodName, this.iParamTypes);
            return method.invoke(input, this.iArgs);
         } catch (NoSuchMethodException var5) {
            throw new FunctorException("InvokerTransformer: The method '" + this.iMethodName + "' on '" + input.getClass() + "' does not exist");
         } catch (IllegalAccessException var6) {
            throw new FunctorException("InvokerTransformer: The method '" + this.iMethodName + "' on '" + input.getClass() + "' cannot be accessed");
         } catch (InvocationTargetException var7) {
            throw new FunctorException("InvokerTransformer: The method '" + this.iMethodName + "' on '" + input.getClass() + "' threw an exception", var7);
         }
      }
   }
}
