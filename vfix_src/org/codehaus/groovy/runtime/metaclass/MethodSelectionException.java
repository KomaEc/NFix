package org.codehaus.groovy.runtime.metaclass;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaMethod;
import java.lang.reflect.Modifier;
import org.codehaus.groovy.reflection.CachedConstructor;
import org.codehaus.groovy.util.FastArray;

public class MethodSelectionException extends GroovyRuntimeException {
   private final String methodName;
   private final FastArray methods;
   private final Class[] arguments;

   public MethodSelectionException(String methodName, FastArray methods, Class[] arguments) {
      super(methodName);
      this.methodName = methodName;
      this.arguments = arguments;
      this.methods = methods;
   }

   public String getMessage() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("Could not find which method ").append(this.methodName);
      this.appendClassNames(buffer, this.arguments);
      buffer.append(" to invoke from this list:");
      this.appendMethods(buffer);
      return buffer.toString();
   }

   private void appendClassNames(StringBuffer argBuf, Class[] classes) {
      argBuf.append("(");

      for(int i = 0; i < classes.length; ++i) {
         if (i > 0) {
            argBuf.append(", ");
         }

         Class clazz = classes[i];
         String name = clazz == null ? "null" : clazz.getName();
         argBuf.append(name);
      }

      argBuf.append(")");
   }

   private void appendMethods(StringBuffer buffer) {
      for(int i = 0; i < this.methods.size; ++i) {
         buffer.append("\n  ");
         Object methodOrConstructor = this.methods.get(i);
         if (methodOrConstructor instanceof MetaMethod) {
            MetaMethod method = (MetaMethod)methodOrConstructor;
            buffer.append(Modifier.toString(method.getModifiers()));
            buffer.append(" ").append(method.getReturnType().getName());
            buffer.append(" ").append(method.getDeclaringClass().getName());
            buffer.append("#");
            buffer.append(method.getName());
            this.appendClassNames(buffer, method.getNativeParameterTypes());
         } else {
            CachedConstructor method = (CachedConstructor)methodOrConstructor;
            buffer.append(Modifier.toString(method.cachedConstructor.getModifiers()));
            buffer.append(" ").append(method.cachedConstructor.getDeclaringClass().getName());
            buffer.append("#<init>");
            this.appendClassNames(buffer, method.getNativeParameterTypes());
         }
      }

   }
}
