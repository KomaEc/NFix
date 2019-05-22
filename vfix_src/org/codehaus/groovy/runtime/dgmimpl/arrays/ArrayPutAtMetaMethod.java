package org.codehaus.groovy.runtime.dgmimpl.arrays;

public abstract class ArrayPutAtMetaMethod extends ArrayMetaMethod {
   public String getName() {
      return "putAt";
   }

   public Class getReturnType() {
      return Void.class;
   }
}
