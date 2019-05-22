package soot.javaToJimple.jj.ast;

import polyglot.ast.Expr;
import polyglot.ast.TypeNode;
import polyglot.ext.jl.ast.FieldDecl_c;
import polyglot.types.Flags;
import polyglot.types.Type;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;

public class JjFieldDecl_c extends FieldDecl_c {
   public JjFieldDecl_c(Position pos, Flags flags, TypeNode type, String name, Expr init) {
      super(pos, flags, type, name, init);
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      return this.type().type();
   }
}
