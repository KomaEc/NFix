package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.ast.While;
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

public class While_c extends Loop_c implements While {
   protected Expr cond;
   protected Stmt body;

   public While_c(Position pos, Expr cond, Stmt body) {
      super(pos);
      this.cond = cond;
      this.body = body;
   }

   public Expr cond() {
      return this.cond;
   }

   public While cond(Expr cond) {
      While_c n = (While_c)this.copy();
      n.cond = cond;
      return n;
   }

   public Stmt body() {
      return this.body;
   }

   public While body(Stmt body) {
      While_c n = (While_c)this.copy();
      n.body = body;
      return n;
   }

   protected While_c reconstruct(Expr cond, Stmt body) {
      if (cond == this.cond && body == this.body) {
         return this;
      } else {
         While_c n = (While_c)this.copy();
         n.cond = cond;
         n.body = body;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Expr cond = (Expr)this.visitChild(this.cond, v);
      Stmt body = (Stmt)this.visitChild(this.body, v);
      return this.reconstruct(cond, body);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (!ts.equals(this.cond.type(), ts.Boolean())) {
         throw new SemanticException("Condition of while statement must have boolean type.", this.cond.position());
      } else {
         return this;
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      return (Type)(child == this.cond ? ts.Boolean() : child.type());
   }

   public String toString() {
      return "while (" + this.cond + ") ...";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("while (");
      this.printBlock(this.cond, w, tr);
      w.write(")");
      this.printSubStmt(this.body, w, tr);
   }

   public Term entry() {
      return this.cond.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.condIsConstantTrue()) {
         v.visitCFG(this.cond, (Term)this.body.entry());
      } else {
         v.visitCFG(this.cond, FlowGraph.EDGE_KEY_TRUE, this.body.entry(), FlowGraph.EDGE_KEY_FALSE, this);
      }

      v.push(this).visitCFG(this.body, (Term)this.cond.entry());
      return succs;
   }

   public Term continueTarget() {
      return this.cond.entry();
   }
}
