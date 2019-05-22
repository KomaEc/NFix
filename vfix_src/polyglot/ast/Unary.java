package polyglot.ast;

import polyglot.util.Enum;

public interface Unary extends Expr {
   Unary.Operator BIT_NOT = new Unary.Operator("~", true);
   Unary.Operator NEG = new Unary.Operator("-", true);
   Unary.Operator POST_INC = new Unary.Operator("++", false);
   Unary.Operator POST_DEC = new Unary.Operator("--", false);
   Unary.Operator PRE_INC = new Unary.Operator("++", true);
   Unary.Operator PRE_DEC = new Unary.Operator("--", true);
   Unary.Operator POS = new Unary.Operator("+", true);
   Unary.Operator NOT = new Unary.Operator("!", true);

   Expr expr();

   Unary expr(Expr var1);

   Unary.Operator operator();

   Unary operator(Unary.Operator var1);

   public static class Operator extends Enum {
      boolean prefix;
      String name;

      public Operator(String name, boolean prefix) {
         super(name + (prefix ? "" : "post"));
         this.name = name;
         this.prefix = prefix;
      }

      public boolean isPrefix() {
         return this.prefix;
      }

      public String toString() {
         return this.name;
      }
   }
}
