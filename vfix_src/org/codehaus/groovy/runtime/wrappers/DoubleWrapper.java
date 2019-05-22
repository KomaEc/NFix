package org.codehaus.groovy.runtime.wrappers;

public class DoubleWrapper extends PojoWrapper {
   public DoubleWrapper(double wrapped) {
      super(wrapped, Double.TYPE);
   }
}
