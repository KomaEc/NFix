package polyglot.ext.jl.ast;

import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Assign;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.ast.Variable;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public abstract class Assign_c extends Expr_c implements Assign {
   protected Expr left;
   protected Assign.Operator op;
   protected Expr right;

   public Assign_c(Position pos, Expr left, Assign.Operator op, Expr right) {
      super(pos);
      this.left = left;
      this.op = op;
      this.right = right;
   }

   public Precedence precedence() {
      return Precedence.ASSIGN;
   }

   public Expr left() {
      return this.left;
   }

   public Assign left(Expr left) {
      Assign_c n = (Assign_c)this.copy();
      n.left = left;
      return n;
   }

   public Assign.Operator operator() {
      return this.op;
   }

   public Assign operator(Assign.Operator op) {
      Assign_c n = (Assign_c)this.copy();
      n.op = op;
      return n;
   }

   public Expr right() {
      return this.right;
   }

   public Assign right(Expr right) {
      Assign_c n = (Assign_c)this.copy();
      n.right = right;
      return n;
   }

   protected Assign_c reconstruct(Expr left, Expr right) {
      if (left == this.left && right == this.right) {
         return this;
      } else {
         Assign_c n = (Assign_c)this.copy();
         n.left = left;
         n.right = right;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Expr left = (Expr)this.visitChild(this.left, v);
      Expr right = (Expr)this.visitChild(this.right, v);
      return this.reconstruct(left, right);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      Type t = this.left.type();
      Type s = this.right.type();
      TypeSystem ts = tc.typeSystem();
      if (!(this.left instanceof Variable)) {
         throw new SemanticException("Target of assignment must be a variable.", this.position());
      } else if (this.op == Assign.ASSIGN) {
         if (!ts.isImplicitCastValid(s, t) && !ts.equals(s, t) && !ts.numericConversionValid(t, this.right.constantValue())) {
            throw new SemanticException("Cannot assign " + s + " to " + t + ".", this.position());
         } else {
            return this.type(t);
         }
      } else if (this.op == Assign.ADD_ASSIGN) {
         if (ts.equals(t, ts.String()) && ts.canCoerceToString(s, tc.context())) {
            return this.type(ts.String());
         } else if (t.isNumeric() && s.isNumeric()) {
            return this.type(ts.promote(t, s));
         } else {
            throw new SemanticException("The " + this.op + " operator must have " + "numeric or String operands.", this.position());
         }
      } else if (this.op != Assign.SUB_ASSIGN && this.op != Assign.MUL_ASSIGN && this.op != Assign.DIV_ASSIGN && this.op != Assign.MOD_ASSIGN) {
         if (this.op != Assign.BIT_AND_ASSIGN && this.op != Assign.BIT_OR_ASSIGN && this.op != Assign.BIT_XOR_ASSIGN) {
            if (this.op != Assign.SHL_ASSIGN && this.op != Assign.SHR_ASSIGN && this.op != Assign.USHR_ASSIGN) {
               throw new InternalCompilerError("Unrecognized assignment operator " + this.op + ".");
            } else if (ts.isImplicitCastValid(t, ts.Long()) && ts.isImplicitCastValid(s, ts.Long())) {
               return this.type(ts.promote(t));
            } else {
               throw new SemanticException("The " + this.op + " operator must have " + "integral operands.", this.position());
            }
         } else if (t.isBoolean() && s.isBoolean()) {
            return this.type(ts.Boolean());
         } else if (ts.isImplicitCastValid(t, ts.Long()) && ts.isImplicitCastValid(s, ts.Long())) {
            return this.type(ts.promote(t, s));
         } else {
            throw new SemanticException("The " + this.op + " operator must have " + "integral or boolean operands.", this.position());
         }
      } else if (t.isNumeric() && s.isNumeric()) {
         return this.type(ts.promote(t, s));
      } else {
         throw new SemanticException("The " + this.op + " operator must have " + "numeric operands.", this.position());
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      if (child == this.right) {
         TypeSystem ts = av.typeSystem();
         return ts.numericConversionValid(this.left.type(), child.constantValue()) ? child.type() : this.left.type();
      } else {
         return child.type();
      }
   }

   public boolean throwsArithmeticException() {
      return this.op == Assign.DIV_ASSIGN || this.op == Assign.MOD_ASSIGN;
   }

   public String toString() {
      return this.left + " " + this.op + " " + this.right;
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      this.printSubExpr(this.left, true, w, tr);
      w.write(" ");
      w.write(this.op.toString());
      w.allowBreak(2, " ");
      this.printSubExpr(this.right, false, w, tr);
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(operator " + this.op + ")");
      w.end();
   }

   public abstract Term entry();

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.operator() == Assign.ASSIGN) {
         this.acceptCFGAssign(v);
      } else {
         this.acceptCFGOpAssign(v);
      }

      return succs;
   }

   protected abstract void acceptCFGAssign(CFGBuilder var1);

   protected abstract void acceptCFGOpAssign(CFGBuilder var1);

   public List throwTypes(TypeSystem ts) {
      List l = new LinkedList();
      if (this.throwsArithmeticException()) {
         l.add(ts.ArithmeticException());
      }

      return l;
   }
}
