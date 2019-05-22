package soot.javaToJimple.jj.ast;

import polyglot.ast.Expr;
import polyglot.ast.TypeNode;
import polyglot.ext.jl.ast.LocalDecl_c;
import polyglot.types.Flags;
import polyglot.types.Type;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;

public class JjLocalDecl_c extends LocalDecl_c {
   public JjLocalDecl_c(Position pos, Flags flags, TypeNode type, String name, Expr init) {
      super(pos, flags, type, name, init);
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      return this.type().type();
   }
}
