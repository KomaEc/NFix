package soot.javaToJimple.jj.ast;

import polyglot.ast.Expr;
import polyglot.ast.TypeNode;
import polyglot.ext.jl.ast.Cast_c;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;

public class JjCast_c extends Cast_c {
   public JjCast_c(Position pos, TypeNode castType, Expr expr) {
      super(pos, castType, expr);
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      if (child == this.expr) {
         if (this.castType.type().isReference()) {
            return ts.Object();
         }

         if (this.castType.type().isNumeric()) {
            return this.castType.type();
         }

         if (this.castType.type().isBoolean()) {
            return ts.Boolean();
         }
      }

      return child.type();
   }
}
