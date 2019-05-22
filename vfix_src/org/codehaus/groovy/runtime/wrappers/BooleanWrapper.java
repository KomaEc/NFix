package org.codehaus.groovy.runtime.wrappers;

public class BooleanWrapper extends PojoWrapper {
   public BooleanWrapper(boolean wrapped) {
      super(wrapped ? Boolean.TRUE : Boolean.FALSE, Boolean.TYPE);
   }
}
