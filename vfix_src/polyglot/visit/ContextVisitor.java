package polyglot.visit;

import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.Context;
import polyglot.types.TypeSystem;

public class ContextVisitor extends ErrorHandlingVisitor {
   protected ContextVisitor outer = null;
   protected Context context = null;

   public ContextVisitor(Job job, TypeSystem ts, NodeFactory nf) {
      super(job, ts, nf);
   }

   public NodeVisitor begin() {
      this.context = this.job.context();
      if (this.context == null) {
         this.context = this.ts.createContext();
      }

      this.outer = null;
      return super.begin();
   }

   public Context context() {
      return this.context;
   }

   public ContextVisitor context(Context c) {
      ContextVisitor v = (ContextVisitor)this.copy();
      v.context = c;
      return v;
   }

   protected Context enterScope(Node parent, Node n) {
      return parent != null ? parent.del().enterScope(n, this.context) : n.del().enterScope(this.context);
   }

   protected void addDecls(Node n) {
      n.addDecls(this.context);
   }

   public NodeVisitor enter(Node parent, Node n) {
      if (Report.should_report((String)"visit", 5)) {
         Report.report(5, "enter(" + n + ")");
      }

      ContextVisitor v = this;
      Context c = this.enterScope(parent, n);
      if (c != this.context) {
         v = (ContextVisitor)this.copy();
         v.context = c;
         v.outer = this;
         v.error = false;
      }

      return v.superEnter(parent, n);
   }

   public NodeVisitor superEnter(Node parent, Node n) {
      return super.enter(parent, n);
   }

   public Node leave(Node parent, Node old, Node n, NodeVisitor v) {
      Node m = super.leave(parent, old, n, v);
      this.addDecls(m);
      return m;
   }
}
