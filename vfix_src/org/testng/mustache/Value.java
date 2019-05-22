package org.testng.mustache;

public class Value {
   private Object m_object;

   public Value(Object object) {
      this.m_object = object;
   }

   public Object get() {
      return this.m_object;
   }
}
