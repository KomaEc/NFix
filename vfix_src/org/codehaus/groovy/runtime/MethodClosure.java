package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import groovy.lang.MetaMethod;
import java.util.Iterator;
import java.util.List;

public class MethodClosure extends Closure {
   private String method;

   public MethodClosure(Object owner, String method) {
      super(owner);
      this.method = method;
      Class clazz = owner.getClass() == Class.class ? (Class)owner : owner.getClass();
      this.maximumNumberOfParameters = 0;
      this.parameterTypes = new Class[0];
      List<MetaMethod> methods = InvokerHelper.getMetaClass(clazz).respondsTo(owner, method);
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         MetaMethod m = (MetaMethod)i$.next();
         if (m.getParameterTypes().length > this.maximumNumberOfParameters) {
            Class[] pt = m.getNativeParameterTypes();
            this.maximumNumberOfParameters = pt.length;
            this.parameterTypes = pt;
         }
      }

   }

   public String getMethod() {
      return this.method;
   }

   protected Object doCall(Object arguments) {
      return InvokerHelper.invokeMethod(this.getOwner(), this.method, arguments);
   }

   public Object getProperty(String property) {
      return "method".equals(property) ? this.getMethod() : super.getProperty(property);
   }
}
