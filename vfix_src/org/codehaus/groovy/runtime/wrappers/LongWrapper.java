package org.codehaus.groovy.runtime.wrappers;

public class LongWrapper extends PojoWrapper {
   public LongWrapper(long wrapped) {
      super(wrapped, Long.TYPE);
   }
}
