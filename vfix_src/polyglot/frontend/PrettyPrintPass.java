package polyglot.frontend;

import polyglot.ast.Node;
import polyglot.util.CodeWriter;
import polyglot.visit.PrettyPrinter;

public class PrettyPrintPass extends AbstractPass {
   protected Job job;
   protected PrettyPrinter pp;
   protected CodeWriter w;

   public PrettyPrintPass(Pass.ID id, Job job, CodeWriter w, PrettyPrinter pp) {
      super(id);
      this.job = job;
      this.pp = pp;
      this.w = w;
   }

   public boolean run() {
      Node ast = this.job.ast();
      if (ast == null) {
         this.w.write("<<<< null AST >>>>");
      } else {
         this.pp.printAst(ast, this.w);
      }

      return true;
   }
}
