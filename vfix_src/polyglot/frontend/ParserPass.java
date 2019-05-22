package polyglot.frontend;

import java.io.IOException;
import java.io.Reader;
import polyglot.ast.Node;
import polyglot.main.Report;
import polyglot.util.ErrorQueue;

public class ParserPass extends AbstractPass {
   Job job;
   Compiler compiler;

   public ParserPass(Pass.ID id, Compiler compiler, Job job) {
      super(id);
      this.compiler = compiler;
      this.job = job;
   }

   public boolean run() {
      ErrorQueue eq = this.compiler.errorQueue();
      FileSource source = (FileSource)this.job.source();

      try {
         Reader reader = source.open();
         Parser p = this.compiler.sourceExtension().parser(reader, source, eq);
         if (Report.should_report((String)"frontend", 2)) {
            Report.report(2, "Using parser " + p);
         }

         Node ast = p.parse();
         source.close();
         if (ast != null) {
            this.job.ast(ast);
            return true;
         } else {
            return false;
         }
      } catch (IOException var6) {
         eq.enqueue(2, var6.getMessage());
         return false;
      }
   }

   public String toString() {
      return this.id + "(" + this.job.source() + ")";
   }
}
