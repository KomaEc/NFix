package groovy.util;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.MetaClass;
import groovy.lang.MissingMethodException;
import org.codehaus.groovy.runtime.InvokerHelper;

class FactoryInterceptorMetaClass extends DelegatingMetaClass {
   FactoryBuilderSupport factory;

   public FactoryInterceptorMetaClass(MetaClass delegate, FactoryBuilderSupport factory) {
      super(delegate);
      this.factory = factory;
   }

   public Object invokeMethod(Object object, String methodName, Object arguments) {
      try {
         return this.delegate.invokeMethod(object, methodName, arguments);
      } catch (MissingMethodException var8) {
         try {
            return this.factory.getMetaClass().respondsTo(this.factory, methodName).isEmpty() ? this.factory.invokeMethod(methodName, arguments) : InvokerHelper.invokeMethod(this.factory, methodName, arguments);
         } catch (MissingMethodException var7) {
            Object root;
            for(root = var8; ((Throwable)root).getCause() != null; root = ((Throwable)root).getCause()) {
            }

            ((Throwable)root).initCause(var7);
            throw var8;
         }
      }
   }

   public Object invokeMethod(Object object, String methodName, Object[] arguments) {
      try {
         return this.delegate.invokeMethod(object, methodName, arguments);
      } catch (MissingMethodException var8) {
         try {
            return this.factory.getMetaClass().respondsTo(this.factory, methodName).isEmpty() ? this.factory.invokeMethod(methodName, arguments) : InvokerHelper.invokeMethod(this.factory, methodName, arguments);
         } catch (MissingMethodException var7) {
            Object root;
            for(root = var8; ((Throwable)root).getCause() != null; root = ((Throwable)root).getCause()) {
            }

            ((Throwable)root).initCause(var7);
            throw var8;
         }
      }
   }
}
