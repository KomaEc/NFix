package org.codehaus.groovy.runtime.wrappers;

public class IntWrapper extends PojoWrapper {
   public IntWrapper(int wrapped) {
      super(wrapped, Integer.TYPE);
   }
}
