package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Labeled;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;

public class Labeled_c extends Stmt_c implements Labeled {
   protected String label;
   protected Stmt statement;

   public Labeled_c(Position pos, String label, Stmt statement) {
      super(pos);
      this.label = label;
      this.statement = statement;
   }

   public String label() {
      return this.label;
   }

   public Labeled label(String label) {
      Labeled_c n = (Labeled_c)this.copy();
      n.label = label;
      return n;
   }

   public Stmt statement() {
      return this.statement;
   }

   public Labeled statement(Stmt statement) {
      Labeled_c n = (Labeled_c)this.copy();
      n.statement = statement;
      return n;
   }

   protected Labeled_c reconstruct(Stmt statement) {
      if (statement != this.statement) {
         Labeled_c n = (Labeled_c)this.copy();
         n.statement = statement;
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Stmt statement = (Stmt)this.visitChild(this.statement, v);
      return this.reconstruct(statement);
   }

   public String toString() {
      return this.label + ": " + this.statement;
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write(this.label + ": ");
      this.print(this.statement, w, tr);
   }

   public Term entry() {
      return this.statement.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.push(this).visitCFG(this.statement, (Term)this);
      return succs;
   }
}
