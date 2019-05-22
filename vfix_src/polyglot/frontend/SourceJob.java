package polyglot.frontend;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import polyglot.ast.Node;

public class SourceJob extends Job {
   protected Source source;
   protected Set dependencies;
   protected Set dependents;

   public SourceJob(ExtensionInfo lang, JobExt ext, Source source, Node ast) {
      super(lang, ext, ast);
      this.source = source;
      this.dependencies = new HashSet();
      this.dependents = new HashSet();
   }

   public Set dependencies() {
      return this.dependencies;
   }

   public Set dependents() {
      return this.dependents;
   }

   public void addDependent(Source s) {
      if (s != this.source()) {
         this.dependents.add(s);
      }

   }

   public void addDependency(Source s) {
      if (s != this.source()) {
         this.dependencies.add(s);
      }

   }

   public List getPasses() {
      return this.lang.passes(this);
   }

   public Source source() {
      return this.source;
   }

   public SourceJob sourceJob() {
      return this;
   }

   public String toString() {
      return this.source.toString() + " (" + (this.completed() ? "done" : (this.isRunning() ? "running " : "before ") + this.nextPass()) + ")";
   }
}
