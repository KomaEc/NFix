package org.apache.commons.collections.functors;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.collections.FunctorException;
import org.apache.commons.collections.Transformer;

public class InstantiateTransformer implements Transformer, Serializable {
   private static final long serialVersionUID = 3786388740793356347L;
   public static final Transformer NO_ARG_INSTANCE = new InstantiateTransformer();
   private final Class[] iParamTypes;
   private final Object[] iArgs;

   public static Transformer getInstance(Class[] paramTypes, Object[] args) {
      if (paramTypes == null && args != null || paramTypes != null && args == null || paramTypes != null && args != null && paramTypes.length != args.length) {
         throw new IllegalArgumentException("Parameter types must match the arguments");
      } else if (paramTypes != null && paramTypes.length != 0) {
         paramTypes = (Class[])paramTypes.clone();
         args = (Object[])args.clone();
         return new InstantiateTransformer(paramTypes, args);
      } else {
         return NO_ARG_INSTANCE;
      }
   }

   private InstantiateTransformer() {
      this.iParamTypes = null;
      this.iArgs = null;
   }

   public InstantiateTransformer(Class[] paramTypes, Object[] args) {
      this.iParamTypes = paramTypes;
      this.iArgs = args;
   }

   public Object transform(Object input) {
      try {
         if (!(input instanceof Class)) {
            throw new FunctorException("InstantiateTransformer: Input object was not an instanceof Class, it was a " + (input == null ? "null object" : input.getClass().getName()));
         } else {
            Constructor con = ((Class)input).getConstructor(this.iParamTypes);
            return con.newInstance(this.iArgs);
         }
      } catch (NoSuchMethodException var6) {
         throw new FunctorException("InstantiateTransformer: The constructor must exist and be public ");
      } catch (InstantiationException var7) {
         throw new FunctorException("InstantiateTransformer: InstantiationException", var7);
      } catch (IllegalAccessException var8) {
         throw new FunctorException("InstantiateTransformer: Constructor must be public", var8);
      } catch (InvocationTargetException var9) {
         throw new FunctorException("InstantiateTransformer: Constructor threw an exception", var9);
      }
   }
}
