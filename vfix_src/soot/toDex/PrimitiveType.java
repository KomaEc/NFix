package soot.toDex;

import java.util.Locale;

public enum PrimitiveType {
   BOOLEAN,
   BYTE,
   CHAR,
   SHORT,
   INT,
   LONG,
   FLOAT,
   DOUBLE;

   public String getName() {
      return this.name().toLowerCase(Locale.ENGLISH);
   }

   public static PrimitiveType getByName(String name) {
      PrimitiveType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PrimitiveType p = var1[var3];
         if (p.getName().equals(name)) {
            return p;
         }
      }

      throw new RuntimeException("not found: " + name);
   }
}
