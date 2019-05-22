package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.Instanceof;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class Instanceof_c extends Expr_c implements Instanceof {
   protected Expr expr;
   protected TypeNode compareType;

   public Instanceof_c(Position pos, Expr expr, TypeNode compareType) {
      super(pos);
      this.expr = expr;
      this.compareType = compareType;
   }

   public Precedence precedence() {
      return Precedence.INSTANCEOF;
   }

   public Expr expr() {
      return this.expr;
   }

   public Instanceof expr(Expr expr) {
      Instanceof_c n = (Instanceof_c)this.copy();
      n.expr = expr;
      return n;
   }

   public TypeNode compareType() {
      return this.compareType;
   }

   public Instanceof compareType(TypeNode compareType) {
      Instanceof_c n = (Instanceof_c)this.copy();
      n.compareType = compareType;
      return n;
   }

   protected Instanceof_c reconstruct(Expr expr, TypeNode compareType) {
      if (expr == this.expr && compareType == this.compareType) {
         return this;
      } else {
         Instanceof_c n = (Instanceof_c)this.copy();
         n.expr = expr;
         n.compareType = compareType;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Expr expr = (Expr)this.visitChild(this.expr, v);
      TypeNode compareType = (TypeNode)this.visitChild(this.compareType, v);
      return this.reconstruct(expr, compareType);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (!this.compareType.type().isReference()) {
         throw new SemanticException("Type operand of \"instanceof\" must be a reference type.", this.compareType.position());
      } else if (!ts.isCastValid(this.expr.type(), this.compareType.type())) {
         throw new SemanticException("Expression operand incompatible with type in \"instanceof\".", this.expr.position());
      } else {
         return this.type(ts.Boolean());
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      return (Type)(child == this.expr ? ts.Object() : child.type());
   }

   public String toString() {
      return this.expr + " instanceof " + this.compareType;
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      this.printSubExpr(this.expr, w, tr);
      w.write(" instanceof ");
      this.print(this.compareType, w, tr);
   }

   public Term entry() {
      return this.expr.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFG(this.expr, (Term)this);
      return succs;
   }
}
