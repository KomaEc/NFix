package org.codehaus.groovy.runtime;

import groovy.lang.GString;

public class GStringImpl extends GString {
   private String[] strings;

   public GStringImpl(Object[] values, String[] strings) {
      super(values);
      this.strings = strings;
   }

   public String[] getStrings() {
      return this.strings;
   }
}
