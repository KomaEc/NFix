package org.apache.tools.ant.util;

import java.util.Enumeration;
import java.util.Hashtable;

public class LazyHashtable extends Hashtable {
   protected boolean initAllDone = false;

   protected void initAll() {
      if (!this.initAllDone) {
         this.initAllDone = true;
      }
   }

   public Enumeration elements() {
      this.initAll();
      return super.elements();
   }

   public boolean isEmpty() {
      this.initAll();
      return super.isEmpty();
   }

   public int size() {
      this.initAll();
      return super.size();
   }

   public boolean contains(Object value) {
      this.initAll();
      return super.contains(value);
   }

   public boolean containsKey(Object value) {
      this.initAll();
      return super.containsKey(value);
   }

   public boolean containsValue(Object value) {
      return this.contains(value);
   }

   public Enumeration keys() {
      this.initAll();
      return super.keys();
   }
}
