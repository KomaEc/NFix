package org.codehaus.groovy.runtime.wrappers;

public class CharWrapper extends PojoWrapper {
   public CharWrapper(char wrapped) {
      super(wrapped, Character.TYPE);
   }
}
