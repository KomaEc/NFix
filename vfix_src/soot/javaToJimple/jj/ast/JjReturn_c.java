package soot.javaToJimple.jj.ast;

import polyglot.ast.Expr;
import polyglot.ext.jl.ast.Return_c;
import polyglot.types.CodeInstance;
import polyglot.types.Context;
import polyglot.types.MethodInstance;
import polyglot.types.Type;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;

public class JjReturn_c extends Return_c {
   public JjReturn_c(Position pos, Expr expr) {
      super(pos, expr);
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      if (child == this.expr) {
         Context c = av.context();
         CodeInstance ci = c.currentCode();
         if (ci instanceof MethodInstance) {
            MethodInstance mi = (MethodInstance)ci;
            return mi.returnType();
         }
      }

      return child.type();
   }
}
