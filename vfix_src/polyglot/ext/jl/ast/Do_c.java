package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Do;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
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

public class Do_c extends Loop_c implements Do {
   protected Stmt body;
   protected Expr cond;

   public Do_c(Position pos, Stmt body, Expr cond) {
      super(pos);
      this.body = body;
      this.cond = cond;
   }

   public Stmt body() {
      return this.body;
   }

   public Do body(Stmt body) {
      Do_c n = (Do_c)this.copy();
      n.body = body;
      return n;
   }

   public Expr cond() {
      return this.cond;
   }

   public Do cond(Expr cond) {
      Do_c n = (Do_c)this.copy();
      n.cond = cond;
      return n;
   }

   protected Do_c reconstruct(Stmt body, Expr cond) {
      if (body == this.body && cond == this.cond) {
         return this;
      } else {
         Do_c n = (Do_c)this.copy();
         n.body = body;
         n.cond = cond;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Stmt body = (Stmt)this.visitChild(this.body, v);
      Expr cond = (Expr)this.visitChild(this.cond, v);
      return this.reconstruct(body, cond);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (!ts.equals(this.cond.type(), ts.Boolean())) {
         throw new SemanticException("Condition of do statement must have boolean type.", this.cond.position());
      } else {
         return this;
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      return (Type)(child == this.cond ? ts.Boolean() : child.type());
   }

   public String toString() {
      return "do { ... } while (" + this.cond + ")";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("do ");
      this.printSubStmt(this.body, w, tr);
      w.write("while(");
      this.printBlock(this.cond, w, tr);
      w.write("); ");
   }

   public Term entry() {
      return this.body.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.push(this).visitCFG(this.body, (Term)this.cond.entry());
      if (this.condIsConstantTrue()) {
         v.visitCFG(this.cond, (Term)this.body.entry());
      } else {
         v.visitCFG(this.cond, FlowGraph.EDGE_KEY_TRUE, this.body.entry(), FlowGraph.EDGE_KEY_FALSE, this);
      }

      return succs;
   }

   public Term continueTarget() {
      return this.cond.entry();
   }
}
