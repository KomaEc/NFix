package org.codehaus.groovy.runtime;

import groovy.lang.GroovyObjectSupport;
import java.util.Collections;
import java.util.Iterator;

public class NullObject extends GroovyObjectSupport {
   private static final NullObject INSTANCE = new NullObject();

   private NullObject() {
   }

   public static NullObject getNullObject() {
      return INSTANCE;
   }

   public Object clone() {
      throw new NullPointerException("Cannot invoke method clone() on null object");
   }

   public Object getProperty(String property) {
      throw new NullPointerException("Cannot get property '" + property + "' on null object");
   }

   public void setProperty(String property, Object newValue) {
      throw new NullPointerException("Cannot set property '" + property + "' on null object");
   }

   public Object invokeMethod(String name, Object args) {
      throw new NullPointerException("Cannot invoke method " + name + "() on null object");
   }

   public boolean equals(Object to) {
      return to == null;
   }

   public Iterator iterator() {
      return Collections.EMPTY_LIST.iterator();
   }

   public Object plus(String s) {
      return this.getMetaClass().invokeMethod(this, "toString", new Object[0]) + s;
   }

   public boolean is(Object other) {
      return other == null;
   }

   public Object asType(Class c) {
      return null;
   }

   public boolean asBoolean() {
      return false;
   }

   public String toString() {
      return "null";
   }

   public int hashCode() {
      throw new NullPointerException("Cannot invoke method hashCode() on null object");
   }
}
