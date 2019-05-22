package org.jboss.util;

public class NullArgumentException extends IllegalArgumentException {
   private static final long serialVersionUID = -2308503249278841584L;
   protected final String name;
   protected final Object index;

   public NullArgumentException(String name) {
      super(makeMessage(name));
      this.name = name;
      this.index = null;
   }

   public NullArgumentException(String name, long index) {
      super(makeMessage(name, new Long(index)));
      this.name = name;
      this.index = new Long(index);
   }

   public NullArgumentException(String name, Object index) {
      super(makeMessage(name, index));
      this.name = name;
      this.index = index;
   }

   public NullArgumentException() {
      this.name = null;
      this.index = null;
   }

   public final String getArgumentName() {
      return this.name;
   }

   public final Object getArgumentIndex() {
      return this.index;
   }

   private static String makeMessage(String name) {
      return "'" + name + "' is null";
   }

   private static String makeMessage(String name, Object index) {
      return "'" + name + "[" + index + "]' is null";
   }
}
