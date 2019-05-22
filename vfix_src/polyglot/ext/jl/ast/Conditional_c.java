package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Conditional;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.FlowGraph;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class Conditional_c extends Expr_c implements Conditional {
   protected Expr cond;
   protected Expr consequent;
   protected Expr alternative;

   public Conditional_c(Position pos, Expr cond, Expr consequent, Expr alternative) {
      super(pos);
      this.cond = cond;
      this.consequent = consequent;
      this.alternative = alternative;
   }

   public Precedence precedence() {
      return Precedence.CONDITIONAL;
   }

   public Expr cond() {
      return this.cond;
   }

   public Conditional cond(Expr cond) {
      Conditional_c n = (Conditional_c)this.copy();
      n.cond = cond;
      return n;
   }

   public Expr consequent() {
      return this.consequent;
   }

   public Conditional consequent(Expr consequent) {
      Conditional_c n = (Conditional_c)this.copy();
      n.consequent = consequent;
      return n;
   }

   public Expr alternative() {
      return this.alternative;
   }

   public Conditional alternative(Expr alternative) {
      Conditional_c n = (Conditional_c)this.copy();
      n.alternative = alternative;
      return n;
   }

   protected Conditional_c reconstruct(Expr cond, Expr consequent, Expr alternative) {
      if (cond == this.cond && consequent == this.consequent && alternative == this.alternative) {
         return this;
      } else {
         Conditional_c n = (Conditional_c)this.copy();
         n.cond = cond;
         n.consequent = consequent;
         n.alternative = alternative;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Expr cond = (Expr)this.visitChild(this.cond, v);
      Expr consequent = (Expr)this.visitChild(this.consequent, v);
      Expr alternative = (Expr)this.visitChild(this.alternative, v);
      return this.reconstruct(cond, consequent, alternative);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (!ts.equals(this.cond.type(), ts.Boolean())) {
         throw new SemanticException("Condition of ternary expression must be of type boolean.", this.cond.position());
      } else {
         Expr e1 = this.consequent;
         Expr e2 = this.alternative;
         Type t1 = e1.type();
         Type t2 = e2.type();
         if (ts.equals(t1, t2)) {
            return this.type(t1);
         } else if (t1.isNumeric() && t2.isNumeric()) {
            if ((!t1.isByte() || !t2.isShort()) && (!t1.isShort() || !t2.isByte())) {
               if (t1.isIntOrLess() && t2.isInt() && ts.numericConversionValid(t1, e2.constantValue())) {
                  return this.type(t1);
               } else {
                  return t2.isIntOrLess() && t1.isInt() && ts.numericConversionValid(t2, e1.constantValue()) ? this.type(t2) : this.type(ts.promote(t1, t2));
               }
            } else {
               return this.type(ts.Short());
            }
         } else if (t1.isNull() && t2.isReference()) {
            return this.type(t2);
         } else if (t2.isNull() && t1.isReference()) {
            return this.type(t1);
         } else {
            if (t1.isReference() && t2.isReference()) {
               if (ts.isImplicitCastValid(t1, t2)) {
                  return this.type(t2);
               }

               if (ts.isImplicitCastValid(t2, t1)) {
                  return this.type(t1);
               }
            }

            throw new SemanticException("Could not find a type for ternary conditional expression.", this.position());
         }
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      if (child == this.cond) {
         return ts.Boolean();
      } else {
         return child != this.consequent && child != this.alternative ? child.type() : this.type();
      }
   }

   public String toString() {
      return this.cond + " ? " + this.consequent + " : " + this.alternative;
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      this.printSubExpr(this.cond, false, w, tr);
      w.write(" ? ");
      this.printSubExpr(this.consequent, false, w, tr);
      w.write(" : ");
      this.printSubExpr(this.alternative, false, w, tr);
   }

   public Term entry() {
      return this.cond.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFG(this.cond, FlowGraph.EDGE_KEY_TRUE, this.consequent.entry(), FlowGraph.EDGE_KEY_FALSE, this.alternative.entry());
      v.visitCFG(this.consequent, (Term)this);
      v.visitCFG(this.alternative, (Term)this);
      return succs;
   }

   public boolean isConstant() {
      return this.cond.isConstant() && this.consequent.isConstant() && this.alternative.isConstant();
   }

   public Object constantValue() {
      Object cond_ = this.cond.constantValue();
      Object then_ = this.consequent.constantValue();
      Object else_ = this.alternative.constantValue();
      if (cond_ instanceof Boolean && then_ != null && else_ != null) {
         boolean c = (Boolean)cond_;
         return c ? then_ : else_;
      } else {
         return null;
      }
   }
}
