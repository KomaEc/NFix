package polyglot.ast;

import polyglot.util.Enum;

public interface Assign extends Expr {
   Assign.Operator ASSIGN = new Assign.Operator("=");
   Assign.Operator ADD_ASSIGN = new Assign.Operator("+=");
   Assign.Operator SUB_ASSIGN = new Assign.Operator("-=");
   Assign.Operator MUL_ASSIGN = new Assign.Operator("*=");
   Assign.Operator DIV_ASSIGN = new Assign.Operator("/=");
   Assign.Operator MOD_ASSIGN = new Assign.Operator("%=");
   Assign.Operator BIT_AND_ASSIGN = new Assign.Operator("&=");
   Assign.Operator BIT_OR_ASSIGN = new Assign.Operator("|=");
   Assign.Operator BIT_XOR_ASSIGN = new Assign.Operator("^=");
   Assign.Operator SHL_ASSIGN = new Assign.Operator("<<=");
   Assign.Operator SHR_ASSIGN = new Assign.Operator(">>=");
   Assign.Operator USHR_ASSIGN = new Assign.Operator(">>>=");

   Expr left();

   Assign left(Expr var1);

   Assign.Operator operator();

   Assign operator(Assign.Operator var1);

   Expr right();

   Assign right(Expr var1);

   boolean throwsArithmeticException();

   public static class Operator extends Enum {
      public Operator(String name) {
         super(name);
      }
   }
}
