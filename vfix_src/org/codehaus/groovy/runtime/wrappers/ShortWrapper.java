package org.codehaus.groovy.runtime.wrappers;

public class ShortWrapper extends PojoWrapper {
   public ShortWrapper(short wrapped) {
      super(wrapped, Short.TYPE);
   }
}
