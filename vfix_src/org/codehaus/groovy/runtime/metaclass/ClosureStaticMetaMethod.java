package org.codehaus.groovy.runtime.metaclass;

import groovy.lang.Closure;
import groovy.lang.ClosureInvokingMethod;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ReflectionCache;

public class ClosureStaticMetaMethod extends MetaMethod implements ClosureInvokingMethod {
   private final Closure callable;
   private final CachedClass declaringClass;
   private final String name;

   public ClosureStaticMetaMethod(String name, Class declaringClass, Closure c) {
      this(name, declaringClass, c, c.getParameterTypes());
   }

   public ClosureStaticMetaMethod(String name, Class declaringClass, Closure c, Class[] paramTypes) {
      super(paramTypes);
      this.callable = c;
      this.declaringClass = ReflectionCache.getCachedClass(declaringClass);
      this.name = name;
   }

   public Object invoke(Object object, Object[] arguments) {
      Closure cloned = (Closure)this.callable.clone();
      cloned.setDelegate(object);
      return cloned.call(arguments);
   }

   public int getModifiers() {
      return 9;
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

   public Closure getClosure() {
      return this.callable;
   }
}
