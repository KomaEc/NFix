package org.codehaus.groovy.binding;

class DeadEndObject {
   public Object getProperty(String property) {
      throw new DeadEndException("Cannot bind to a property on the return value of a method call");
   }

   public Object invokeMethod(String name, Object args) {
      return this;
   }
}
