package polyglot.frontend;

import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;
import polyglot.visit.NodeVisitor;

public class VisitorPass extends AbstractPass {
   Job job;
   NodeVisitor v;

   public VisitorPass(Pass.ID id, Job job) {
      this(id, job, (NodeVisitor)null);
   }

   public VisitorPass(Pass.ID id, Job job, NodeVisitor v) {
      super(id);
      this.job = job;
      this.v = v;
   }

   public void visitor(NodeVisitor v) {
      this.v = v;
   }

   public NodeVisitor visitor() {
      return this.v;
   }

   public boolean run() {
      Node ast = this.job.ast();
      if (ast == null) {
         throw new InternalCompilerError("Null AST: did the parser run?");
      } else {
         NodeVisitor v_ = this.v.begin();
         if (v_ != null) {
            ErrorQueue q = this.job.compiler().errorQueue();
            int nErrsBefore = q.errorCount();
            if (Report.should_report((String)"frontend", 3)) {
               Report.report(3, "Running " + v_ + " on " + ast);
            }

            ast = ast.visit(v_);
            v_.finish(ast);
            int nErrsAfter = q.errorCount();
            this.job.ast(ast);
            return nErrsBefore == nErrsAfter;
         } else {
            return false;
         }
      }
   }

   public String toString() {
      return this.id.toString();
   }
}
