package polyglot.frontend;

import java.util.List;
import polyglot.ast.Node;
import polyglot.types.Context;
import polyglot.util.InternalCompilerError;

public class InnerJob extends Job {
   protected Job outer;
   protected Context context;
   protected Pass.ID begin;
   protected Pass.ID end;

   public InnerJob(ExtensionInfo lang, JobExt ext, Node ast, Context context, Job outer, Pass.ID begin, Pass.ID end) {
      super(lang, ext, ast);
      this.context = context;
      this.outer = outer;
      this.begin = begin;
      this.end = end;
      if (ast == null) {
         throw new InternalCompilerError("Null ast");
      } else if (outer == null) {
         throw new InternalCompilerError("Null outer job");
      }
   }

   public String toString() {
      String name = "inner-job[" + this.begin + ".." + this.end + "](code=" + this.context.currentCode() + " class=" + this.context.currentClass() + ") [" + this.status() + "]";
      return name + " (" + (this.isRunning() ? "running " : "before ") + this.nextPass() + ")" + " <<< passes = " + this.passes + " >>>";
   }

   public List getPasses() {
      List l = this.lang.passes(this, this.begin, this.end);

      for(int i = 0; i < l.size(); ++i) {
         Pass pass = (Pass)l.get(i);
         if (pass.id() == this.begin) {
            this.nextPass = i;
         }

         if (i == l.size() - 1 && pass.id() != this.end) {
            throw new InternalCompilerError("ExtensionInfo.passes returned incorrect list: " + l);
         }
      }

      return l;
   }

   public Context context() {
      return this.context;
   }

   public SourceJob sourceJob() {
      return this.outer.sourceJob();
   }
}
