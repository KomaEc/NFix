package polyglot.frontend;

import polyglot.util.InternalCompilerError;

public class SpawnPass extends AbstractPass {
   Job job;
   Pass.ID begin;
   Pass.ID end;

   public SpawnPass(Pass.ID id, Job job, Pass.ID begin, Pass.ID end) {
      super(id);
      this.job = job;
      this.begin = begin;
      this.end = end;
   }

   public boolean run() {
      if (this.job.ast() == null) {
         throw new InternalCompilerError("Null AST.");
      } else {
         Job j = this.job.spawn(this.job.context(), this.job.ast(), this.begin, this.end);
         return j.status();
      }
   }
}
