package org.codehaus.groovy.runtime.dgmimpl.arrays;

public abstract class ArrayGetAtMetaMethod extends ArrayMetaMethod {
   protected ArrayGetAtMetaMethod() {
      this.parameterTypes = INTEGER_CLASS_ARR;
   }

   public String getName() {
      return "getAt";
   }
}
