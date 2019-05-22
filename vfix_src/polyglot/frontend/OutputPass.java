package polyglot.frontend;

import polyglot.ast.Node;
import polyglot.util.InternalCompilerError;
import polyglot.visit.Translator;

public class OutputPass extends AbstractPass {
   protected Job job;
   protected Translator translator;

   public OutputPass(Pass.ID id, Job job, Translator translator) {
      super(id);
      this.job = job;
      this.translator = translator;
   }

   public boolean run() {
      Node ast = this.job.ast();
      if (ast == null) {
         throw new InternalCompilerError("AST is null");
      } else {
         return this.translator.translate(ast);
      }
   }
}
