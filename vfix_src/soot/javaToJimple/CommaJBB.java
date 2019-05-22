package soot.javaToJimple;

import polyglot.ast.Expr;
import soot.Value;
import soot.javaToJimple.jj.ast.JjComma_c;

public class CommaJBB extends AbstractJimpleBodyBuilder {
   protected Value createAggressiveExpr(Expr expr, boolean redAggr, boolean revIfNec) {
      return expr instanceof JjComma_c ? this.getCommaLocal((JjComma_c)expr) : this.ext().createAggressiveExpr(expr, redAggr, revIfNec);
   }

   private Value getCommaLocal(JjComma_c comma) {
      this.base().createAggressiveExpr(comma.first(), false, false);
      Value val = this.base().createAggressiveExpr(comma.second(), false, false);
      return val;
   }
}
