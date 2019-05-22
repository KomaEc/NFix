package polyglot.ast;

import polyglot.util.Enum;

public interface FloatLit extends Lit {
   FloatLit.Kind FLOAT = new FloatLit.Kind("float");
   FloatLit.Kind DOUBLE = new FloatLit.Kind("double");

   FloatLit.Kind kind();

   FloatLit kind(FloatLit.Kind var1);

   double value();

   FloatLit value(double var1);

   public static class Kind extends Enum {
      public Kind(String name) {
         super(name);
      }
   }
}
