package org.apache.commons.collections.functors;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.collections.Factory;
import org.apache.commons.collections.FunctorException;

public class InstantiateFactory implements Factory, Serializable {
   private static final long serialVersionUID = -7732226881069447957L;
   private final Class iClassToInstantiate;
   private final Class[] iParamTypes;
   private final Object[] iArgs;
   private transient Constructor iConstructor = null;

   public static Factory getInstance(Class classToInstantiate, Class[] paramTypes, Object[] args) {
      if (classToInstantiate == null) {
         throw new IllegalArgumentException("Class to instantiate must not be null");
      } else if (paramTypes == null && args != null || paramTypes != null && args == null || paramTypes != null && args != null && paramTypes.length != args.length) {
         throw new IllegalArgumentException("Parameter types must match the arguments");
      } else if (paramTypes != null && paramTypes.length != 0) {
         paramTypes = (Class[])paramTypes.clone();
         args = (Object[])args.clone();
         return new InstantiateFactory(classToInstantiate, paramTypes, args);
      } else {
         return new InstantiateFactory(classToInstantiate);
      }
   }

   public InstantiateFactory(Class classToInstantiate) {
      this.iClassToInstantiate = classToInstantiate;
      this.iParamTypes = null;
      this.iArgs = null;
      this.findConstructor();
   }

   public InstantiateFactory(Class classToInstantiate, Class[] paramTypes, Object[] args) {
      this.iClassToInstantiate = classToInstantiate;
      this.iParamTypes = paramTypes;
      this.iArgs = args;
      this.findConstructor();
   }

   private void findConstructor() {
      try {
         this.iConstructor = this.iClassToInstantiate.getConstructor(this.iParamTypes);
      } catch (NoSuchMethodException var2) {
         throw new IllegalArgumentException("InstantiateFactory: The constructor must exist and be public ");
      }
   }

   public Object create() {
      if (this.iConstructor == null) {
         this.findConstructor();
      }

      try {
         return this.iConstructor.newInstance(this.iArgs);
      } catch (InstantiationException var4) {
         throw new FunctorException("InstantiateFactory: InstantiationException", var4);
      } catch (IllegalAccessException var5) {
         throw new FunctorException("InstantiateFactory: Constructor must be public", var5);
      } catch (InvocationTargetException var6) {
         throw new FunctorException("InstantiateFactory: Constructor threw an exception", var6);
      }
   }
}
