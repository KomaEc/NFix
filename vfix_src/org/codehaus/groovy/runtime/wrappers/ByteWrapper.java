package org.codehaus.groovy.runtime.wrappers;

public class ByteWrapper extends PojoWrapper {
   public ByteWrapper(byte wrapped) {
      super(wrapped, Byte.TYPE);
   }
}
