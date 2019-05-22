package org.codehaus.groovy.runtime.wrappers;

public class FloatWrapper extends PojoWrapper {
   public FloatWrapper(float wrapped) {
      super(wrapped, Float.TYPE);
   }
}
