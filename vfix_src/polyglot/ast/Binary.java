package polyglot.ast;

import polyglot.util.Enum;

public interface Binary extends Expr {
   Binary.Operator GT = new Binary.Operator(">", Precedence.RELATIONAL);
   Binary.Operator LT = new Binary.Operator("<", Precedence.RELATIONAL);
   Binary.Operator EQ = new Binary.Operator("==", Precedence.EQUAL);
   Binary.Operator LE = new Binary.Operator("<=", Precedence.RELATIONAL);
   Binary.Operator GE = new Binary.Operator(">=", Precedence.RELATIONAL);
   Binary.Operator NE = new Binary.Operator("!=", Precedence.EQUAL);
   Binary.Operator COND_OR = new Binary.Operator("||", Precedence.COND_OR);
   Binary.Operator COND_AND = new Binary.Operator("&&", Precedence.COND_AND);
   Binary.Operator ADD = new Binary.Operator("+", Precedence.ADD);
   Binary.Operator SUB = new Binary.Operator("-", Precedence.ADD);
   Binary.Operator MUL = new Binary.Operator("*", Precedence.MUL);
   Binary.Operator DIV = new Binary.Operator("/", Precedence.MUL);
   Binary.Operator MOD = new Binary.Operator("%", Precedence.MUL);
   Binary.Operator BIT_OR = new Binary.Operator("|", Precedence.BIT_OR);
   Binary.Operator BIT_AND = new Binary.Operator("&", Precedence.BIT_AND);
   Binary.Operator BIT_XOR = new Binary.Operator("^", Precedence.BIT_XOR);
   Binary.Operator SHL = new Binary.Operator("<<", Precedence.SHIFT);
   Binary.Operator SHR = new Binary.Operator(">>", Precedence.SHIFT);
   Binary.Operator USHR = new Binary.Operator(">>>", Precedence.SHIFT);

   Expr left();

   Binary left(Expr var1);

   Binary.Operator operator();

   Binary operator(Binary.Operator var1);

   Expr right();

   Binary right(Expr var1);

   boolean throwsArithmeticException();

   Binary precedence(Precedence var1);

   public static class Operator extends Enum {
      Precedence prec;

      public Operator(String name, Precedence prec) {
         super(name);
         this.prec = prec;
      }

      public Precedence precedence() {
         return this.prec;
      }
   }
}
