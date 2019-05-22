package polyglot.ast;

import polyglot.util.Enum;

public interface Special extends Expr {
   Special.Kind SUPER = new Special.Kind("super");
   Special.Kind THIS = new Special.Kind("this");

   Special.Kind kind();

   Special kind(Special.Kind var1);

   TypeNode qualifier();

   Special qualifier(TypeNode var1);

   public static class Kind extends Enum {
      public Kind(String name) {
         super(name);
      }
   }
}
