package polyglot.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Enum implements Serializable {
   private String name;
   private static Map cache = new HashMap();

   protected Enum(String name) {
      this.name = name;
      if (this.intern() != this) {
         throw new InternalCompilerError("Duplicate enum \"" + name + "\"");
      }
   }

   private Enum() {
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public boolean equals(Object o) {
      return this == o;
   }

   public String toString() {
      return this.name;
   }

   public Enum intern() {
      Enum.EnumKey k = new Enum.EnumKey(this);
      Enum e = (Enum)cache.get(k);
      if (e == null) {
         cache.put(k, this);
         return this;
      } else {
         return e;
      }
   }

   private static class EnumKey {
      Enum e;

      EnumKey(Enum e) {
         this.e = e;
      }

      public boolean equals(Object o) {
         return o instanceof Enum.EnumKey && this.e.name.equals(((Enum.EnumKey)o).e.name) && this.e.getClass() == ((Enum.EnumKey)o).e.getClass();
      }

      public int hashCode() {
         return this.e.getClass().hashCode() ^ this.e.name.hashCode();
      }

      public String toString() {
         return this.e.toString();
      }
   }
}
