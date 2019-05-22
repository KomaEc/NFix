package polyglot.ast;

import polyglot.util.Enum;

public interface Branch extends Stmt {
   Branch.Kind BREAK = new Branch.Kind("break");
   Branch.Kind CONTINUE = new Branch.Kind("continue");

   Branch.Kind kind();

   Branch kind(Branch.Kind var1);

   String label();

   Branch label(String var1);

   public static class Kind extends Enum {
      public Kind(String name) {
         super(name);
      }
   }
}
