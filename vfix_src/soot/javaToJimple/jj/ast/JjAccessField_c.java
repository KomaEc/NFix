package soot.javaToJimple.jj.ast;

import java.util.List;
import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ext.jl.ast.Expr_c;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;

public class JjAccessField_c extends Expr_c implements Expr {
   private Call getMeth;
   private Call setMeth;
   private Field field;

   public JjAccessField_c(Position pos, Call getMeth, Call setMeth, Field field) {
      super(pos);
      this.getMeth = getMeth;
      this.setMeth = setMeth;
      this.field = field;
   }

   public Call getMeth() {
      return this.getMeth;
   }

   public Call setMeth() {
      return this.setMeth;
   }

   public Field field() {
      return this.field;
   }

   public String toString() {
      return this.field + " " + this.getMeth + " " + this.setMeth;
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      return succs;
   }

   public Term entry() {
      return this.field.entry();
   }

   public Node visitChildren(NodeVisitor v) {
      this.visitChild(this.field, v);
      this.visitChild(this.getMeth, v);
      this.visitChild(this.setMeth, v);
      return this;
   }
}
